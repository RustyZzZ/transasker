package com.transquiz.transasker.service;

import com.transquiz.transasker.model.security.User;

public interface UserService {
    User getCurrentUser();

    User registerUser(String username, String password);

    User registerUser(String username, String password, String role);
}
