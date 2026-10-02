package com.pocketplan.service;

import com.pocketplan.dao.UserDAO;
import com.pocketplan.model.User;
import com.pocketplan.util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(String name, String email, String password) {

        if (name == null || name.isBlank()) {
            return false;
        }

        if (email == null || email.isBlank()) {
            return false;
        }

        if (password == null || password.length() < 6) {
            return false;
        }

        email = email.trim().toLowerCase();

        if (userDAO.emailExists(email)) {
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User(
                name.trim(),
                email,
                hashedPassword
        );

        return userDAO.register(user);
    }

    public User login(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        email = email.trim().toLowerCase();

        User user = userDAO.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
