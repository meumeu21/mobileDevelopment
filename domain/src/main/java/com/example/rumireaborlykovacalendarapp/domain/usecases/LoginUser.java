package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class LoginUser {
    private final UserRepository repository;

    public LoginUser(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(String email, String password, UserRepository.AuthCallback callback) {
        repository.login(email, password, callback);
    }
}
