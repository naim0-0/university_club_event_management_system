package com.eventmgmt.ui;

import com.eventmgmt.models.Club;
import com.eventmgmt.models.Event;
import com.eventmgmt.models.Registration;
import com.eventmgmt.models.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {
    private final BorderPane root = new BorderPane();
    public final Label eventCount = metric("0");
    public final Label approvedCount = metric("0");
    public final Label registrationCount = metric("0");
    public final TableView<Event> eventTable = new TableView<>();
    public final TableView<Registration> registrationTable = new TableView<>();
    public final ComboBox<Club> clubInput = new ComboBox<>();
    public final TextField titleInput = new TextField();
    public final TextField budgetInput = new TextField();
    public final Button createEvent = new Button("Create event");
    public final Button submitEvent = new Button("Submit");
    public final Button approveEvent = new Button("Approve");
    public final Button rejectEvent = new Button("Reject");
    public final ComboBox<User> userInput = new ComboBox<>();
    public final ComboBox<Event> registrationEventInput = new ComboBox<>();
    public final TextField feeInput = new TextField("500");
    public final CheckBox earlyBirdInput = new CheckBox("Early-bird discount");
    public final Button register = new Button("Register attendee");
    public final ListView<String> notificationList = new ListView<>();
    public final Label status = new Label("Ready");
    public final Label currentUser = new Label();
    public final Button logout = new Button("Sign out");

    public MainView() {
        root.getStyleClass().add("app");
        root.setTop(header());
        root.setCenter(content());
        root.setBottom(statusBar());
    }

    public Parent getRoot() { return root; }

    private HBox header() {
        Label mark = new Label("CE"); mark.getStyleClass().add("brand-mark");
        Label title = new Label("Campus Events"); title.getStyleClass().add("brand-title");
        Label subtitle = new Label("Plan, approve and manage university activities");
        subtitle.getStyleClass().add("muted");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        currentUser.getStyleClass().add("user-badge");
        HBox box = new HBox(14, mark, new VBox(2, title, subtitle), spacer, currentUser, logout);
        box.setAlignment(Pos.CENTER_LEFT); box.getStyleClass().add("header");
        return box;
    }

    private VBox content() {
        HBox metrics = new HBox(16, card("Total events", eventCount),
                card("Approved", approvedCount), card("Registrations", registrationCount));
        metrics.getChildren().forEach(node -> HBox.setHgrow(node, Priority.ALWAYS));
        TabPane tabs = new TabPane(eventsTab(), registrationsTab(), notificationsTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        VBox content = new VBox(18, metrics, tabs);
        content.setPadding(new Insets(22, 28, 16, 28));
        return content;
    }

    private VBox card(String caption, Label number) {
        Label label = new Label(caption); label.getStyleClass().add("muted");
        VBox box = new VBox(5, label, number); box.getStyleClass().add("metric-card");
        box.setMaxWidth(Double.MAX_VALUE); return box;
    }

    private Tab eventsTab() {
        configureEventTable();
        titleInput.setPromptText("e.g. Programming Contest"); budgetInput.setPromptText("Budget");
        clubInput.setPromptText("Organizing club"); titleInput.setPrefWidth(240); clubInput.setPrefWidth(190);
        HBox create = new HBox(10, titleInput, budgetInput, clubInput, createEvent);
        create.setAlignment(Pos.CENTER_LEFT); create.getStyleClass().add("toolbar");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actions = new HBox(9, new Label("Selected event:"), spacer, submitEvent, approveEvent, rejectEvent);
        actions.setAlignment(Pos.CENTER_LEFT);
        VBox body = new VBox(12, create, eventTable, actions); VBox.setVgrow(eventTable, Priority.ALWAYS);
        body.setPadding(new Insets(16)); return new Tab("Events", body);
    }

    private Tab registrationsTab() {
        configureRegistrationTable();
        userInput.setPromptText("Attendee"); registrationEventInput.setPromptText("Approved event");
        feeInput.setPromptText("Base fee"); userInput.setPrefWidth(220); registrationEventInput.setPrefWidth(250);
        HBox form = new HBox(10, userInput, registrationEventInput, feeInput, earlyBirdInput, register);
        form.setAlignment(Pos.CENTER_LEFT); form.getStyleClass().add("toolbar");
        VBox body = new VBox(12, form, registrationTable); VBox.setVgrow(registrationTable, Priority.ALWAYS);
        body.setPadding(new Insets(16)); return new Tab("Registrations", body);
    }

    private Tab notificationsTab() {
        notificationList.setPlaceholder(new Label("Workflow updates will appear here."));
        VBox body = new VBox(notificationList); VBox.setVgrow(notificationList, Priority.ALWAYS);
        body.setPadding(new Insets(16)); return new Tab("Activity", body);
    }

    private void configureEventTable() {
        eventTable.getColumns().add(column("ID", 70, Event::getId));
        eventTable.getColumns().add(column("Event", 310, Event::getTitle));
        eventTable.getColumns().add(column("Budget", 150, e -> String.format("৳%,.2f", e.getBudget())));
        eventTable.getColumns().add(column("Status", 145, Event::getStatus));
        eventTable.getColumns().add(column("Club ID", 100, Event::getClubId));
        eventTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        eventTable.setPlaceholder(new Label("No events yet. Create the first one above."));
    }

    private void configureRegistrationTable() {
        registrationTable.getColumns().add(column("Registration", 130, Registration::getId));
        registrationTable.getColumns().add(column("User ID", 130, Registration::getUserId));
        registrationTable.getColumns().add(column("Event ID", 130, Registration::getEventId));
        registrationTable.getColumns().add(column("Fee paid", 180, r -> String.format("৳%,.2f", r.getFee())));
        registrationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private <T> TableColumn<T, Object> column(String name, double width, java.util.function.Function<T, Object> value) {
        TableColumn<T, Object> column = new TableColumn<>(name); column.setPrefWidth(width);
        column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(value.apply(cell.getValue()))); return column;
    }

    private HBox statusBar() {
        status.getStyleClass().add("status-label"); HBox bar = new HBox(status);
        bar.getStyleClass().add("status-bar"); return bar;
    }

    private static Label metric(String value) {
        Label label = new Label(value); label.getStyleClass().add("metric-number"); return label;
    }
}
