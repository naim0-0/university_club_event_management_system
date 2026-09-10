package com.eventmgmt.service;

import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.models.User;
import com.eventmgmt.security.PasswordHasher;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO users;
    public AuthService(UserDAO users) { this.users = users; }

    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank())
            throw new BusinessException("Enter both email and password.");
        try {
            User user = users.findByEmail(email.trim());
            if (user == null || !user.isActive() || !PasswordHasher.matches(password, user.getPasswordHash()))
                throw new BusinessException("Invalid email or password.");
            return user;
        } catch (SQLException e) { throw new BusinessException("Could not sign in.", e); }
    }
}
