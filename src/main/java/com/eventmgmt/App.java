package com.eventmgmt;

import com.eventmgmt.controller.MainController;
import com.eventmgmt.dao.ClubDAO;
import com.eventmgmt.dao.EventDAO;
import com.eventmgmt.dao.RegistrationDAO;
import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.patterns.behavioral.observer.EventNotifier;
import com.eventmgmt.patterns.structural.RegistrationFacade;
import com.eventmgmt.service.DirectoryService;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.AuthService;
import com.eventmgmt.ui.MainView;
import com.eventmgmt.ui.LoginView;
import com.eventmgmt.controller.LoginController;
import com.eventmgmt.models.User;
import com.eventmgmt.security.AuthorizationService;
import com.eventmgmt.utils.DatabaseInitializer;
import com.eventmgmt.utils.DatabaseSeeder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private Stage stage;
    private UserDAO users;
    private EventDAO events;
    private ClubDAO clubs;
    private RegistrationDAO registrations;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        DatabaseInitializer.initialize();
        DatabaseSeeder.seed();
        users = new UserDAO(); events = new EventDAO(); clubs = new ClubDAO();
        registrations = new RegistrationDAO();
        stage.setTitle("Campus Events"); stage.setMinWidth(940); stage.setMinHeight(620);
        showLogin();
        stage.show();
    }

    private void showLogin() {
        LoginView login = new LoginView();
        new LoginController(login, new AuthService(users), this::showApplication);
        stage.setScene(styledScene(login.getRoot(), 1120, 720));
    }

    private void showApplication(User actor) {
        EventNotifier notifier = new EventNotifier();
        MainView view = new MainView();
        AuthorizationService access = new AuthorizationService();
        new MainController(view, new EventService(events, clubs, notifier, access, actor),
                new DirectoryService(users, clubs),
                new RegistrationFacade(users, events, registrations, access, actor),
                notifier, actor, this::showLogin);
        stage.setScene(styledScene(view.getRoot(), 1120, 720));
    }

    private Scene styledScene(javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/com/eventmgmt/styles.css").toExternalForm());
        return scene;
    }

    public static void main(String[] args) { launch(args); }
}
