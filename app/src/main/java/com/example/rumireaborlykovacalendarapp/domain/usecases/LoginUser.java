package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class LoginUser {
    private final UserRepository userRepository;

    public LoginUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String email, String password) {
        return userRepository.login(email, password);
    }
}
