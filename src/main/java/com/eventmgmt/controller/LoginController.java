package com.eventmgmt.controller;

import com.eventmgmt.models.User;
import com.eventmgmt.service.AuthService;
import com.eventmgmt.service.BusinessException;
import com.eventmgmt.ui.LoginView;
import java.util.function.Consumer;

public class LoginController {
    public LoginController(LoginView view, AuthService auth, Consumer<User> onSuccess) {
        view.login.setOnAction(e -> {
            try {
                view.error.setText("");
                onSuccess.accept(auth.login(view.email.getText(), view.password.getText()));
            } catch (BusinessException ex) { view.error.setText(ex.getMessage()); }
        });
    }
}
