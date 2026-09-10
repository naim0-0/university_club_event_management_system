package com.eventmgmt.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.eventmgmt.models.Event;
import com.eventmgmt.models.Registration;
import com.eventmgmt.patterns.behavioral.observer.EventNotifier;
import com.eventmgmt.patterns.structural.RegistrationFacade;
import com.eventmgmt.service.BusinessException;
import com.eventmgmt.service.DirectoryService;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.EventService.Action;
import com.eventmgmt.ui.MainView;
import com.eventmgmt.models.User;
import com.eventmgmt.security.AuthorizationService;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

/** UI coordination only; business rules remain in services and the facade. */
public class MainController {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private final MainView view;
    private final EventService eventService;
    private final DirectoryService directoryService;
    private final RegistrationFacade registrationFacade;
    private User actor;

    public MainController(MainView view, EventService eventService, DirectoryService directoryService,
                          RegistrationFacade registrationFacade, EventNotifier notifier) {
        this(view, eventService, directoryService, registrationFacade, notifier, null, () -> {});
    }

    public MainController(MainView view, EventService eventService, DirectoryService directoryService,
                          RegistrationFacade registrationFacade, EventNotifier notifier,
                          User actor, Runnable onLogout) {
        this.view = view;
        this.eventService = eventService;
        this.directoryService = directoryService;
        this.registrationFacade = registrationFacade;
        this.actor = actor;
        notifier.subscribe(message -> view.notificationList.getItems().add(0,
                LocalTime.now().format(TIME) + "  " + message));
        if (actor != null) configureRole(actor);
        view.logout.setOnAction(e -> onLogout.run());
        bindActions();
        refresh();
    }

    private void configureRole(User actor) {
        AuthorizationService access = new AuthorizationService();
        view.currentUser.setText(actor.getName() + " · " + actor.getRole());
        boolean admin = access.isAdmin(actor);
        boolean president = access.isPresident(actor);
        view.createEvent.setDisable(!admin && !president);
        view.titleInput.setDisable(!admin && !president);
        view.budgetInput.setDisable(!admin && !president);
        view.clubInput.setDisable(!admin && !president);
        view.submitEvent.setDisable(!admin && !president);
        view.approveEvent.setDisable(!admin);
        view.rejectEvent.setDisable(!admin);
    }

    private void bindActions() {
        view.createEvent.setOnAction(e -> run("Event created as a draft.", this::createEvent));
        view.submitEvent.setOnAction(e -> transition(Action.SUBMIT));
        view.approveEvent.setOnAction(e -> transition(Action.APPROVE));
        view.rejectEvent.setOnAction(e -> transition(Action.REJECT));
        view.register.setOnAction(e -> run("Attendee registered successfully.", this::register));
    }

    private void createEvent() {
        double budget = number(view.budgetInput.getText(), "Enter a valid budget.");
        if (view.clubInput.getValue() == null) throw new BusinessException("Please select an organizing club.");
        eventService.create(view.titleInput.getText(), budget, view.clubInput.getValue().getId());
        view.titleInput.clear(); view.budgetInput.clear();
    }

    private void transition(Action action) {
        run("Event status updated.", () -> eventService.transition(
                view.eventTable.getSelectionModel().getSelectedItem(), action));
    }

    private void register() {
        if (view.userInput.getValue() == null || view.registrationEventInput.getValue() == null)
            throw new BusinessException("Please select both an attendee and an approved event.");
        double fee = number(view.feeInput.getText(), "Enter a valid base fee.");
        registrationFacade.register(view.userInput.getValue().getId(),
                view.registrationEventInput.getValue().getId(), fee, view.earlyBirdInput.isSelected());
    }

    private double number(String text, String message) {
        try { return Double.parseDouble(text.trim()); }
        catch (RuntimeException e) { throw new BusinessException(message); }
    }

    private void run(String success, Runnable task) {
        try {
            task.run(); refresh(); view.status.setText(success);
        } catch (BusinessException e) {
            view.status.setText(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.setHeaderText("Unable to complete the action"); alert.showAndWait();
        }
    }

    private void refresh() {
        List<Event> events = eventService.findAll();
        List<Registration> registrations = registrationFacade.registrations();
        view.eventTable.setItems(FXCollections.observableArrayList(events));
        view.registrationTable.setItems(FXCollections.observableArrayList(registrations));
        var visibleClubs = directoryService.clubs();
        if (actor != null && "PRESIDENT".equalsIgnoreCase(actor.getRole()))
            visibleClubs = visibleClubs.stream().filter(c -> c.getPresidentId() == actor.getId()).toList();
        view.clubInput.setItems(FXCollections.observableArrayList(visibleClubs));
        List<User> visibleUsers = directoryService.users();
        if (actor != null && !"ADMIN".equalsIgnoreCase(actor.getRole()))
            visibleUsers = visibleUsers.stream().filter(u -> u.getId() == actor.getId()).toList();
        view.userInput.setItems(FXCollections.observableArrayList(visibleUsers));
        if (visibleUsers.size() == 1) view.userInput.setValue(visibleUsers.get(0));
        view.registrationEventInput.setItems(FXCollections.observableArrayList(
                events.stream().filter(e -> "APPROVED".equals(e.getStatus())).toList()));
        view.eventCount.setText(String.valueOf(events.size()));
        view.approvedCount.setText(String.valueOf(events.stream().filter(e -> "APPROVED".equals(e.getStatus())).count()));
        view.registrationCount.setText(String.valueOf(registrations.size()));
    }
}
