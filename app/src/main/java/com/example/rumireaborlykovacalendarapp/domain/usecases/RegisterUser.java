package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class RegisterUser {
    private final UserRepository repository;

    public RegisterUser(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(String name, String email, String password) {
        return repository.register(name, email, password);
    }
}
