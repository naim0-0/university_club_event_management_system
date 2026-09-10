package com.eventmgmt.ui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoginView {
    private final StackPane root = new StackPane();
    public final TextField email = new TextField("admin@campus.edu");
    public final PasswordField password = new PasswordField();
    public final Button login = new Button("Sign in");
    public final Label error = new Label();

    public LoginView() {
        root.getStyleClass().add("login-page");
        Label mark = new Label("CE"); mark.getStyleClass().add("brand-mark");
        Label title = new Label("Welcome to Campus Events"); title.getStyleClass().add("login-title");
        Label subtitle = new Label("Sign in with your university account"); subtitle.getStyleClass().add("muted");
        email.setPromptText("Email address"); password.setPromptText("Password");
        password.setText("admin123"); login.setMaxWidth(Double.MAX_VALUE); login.setDefaultButton(true);
        error.getStyleClass().add("error-label"); error.setWrapText(true);
        Label samples = new Label("Demo accounts\nAdmin: admin@campus.edu / admin123\nPresident: rahim@campus.edu / president123\nStudents: karim, ayesha, nusrat, tanvir, or mehedi @campus.edu\nPassword: student123");
        samples.getStyleClass().add("demo-accounts");
        VBox card = new VBox(13, mark, title, subtitle, new Separator(),
                new Label("Email"), email, new Label("Password"), password, error, login, samples);
        card.setAlignment(Pos.CENTER_LEFT); card.setMaxWidth(400); card.getStyleClass().add("login-card");
        root.getChildren().add(card);
    }

    public Parent getRoot() { return root; }
}
