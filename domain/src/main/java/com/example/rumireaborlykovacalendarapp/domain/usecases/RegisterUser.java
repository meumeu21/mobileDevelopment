package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class RegisterUser {
    private final UserRepository repository;
    public RegisterUser(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(String email, String password, UserRepository.AuthCallback callback) {
        repository.register(email, password, callback);
    }
}
