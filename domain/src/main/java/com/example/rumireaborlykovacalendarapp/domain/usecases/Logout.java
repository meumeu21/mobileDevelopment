package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class Logout {
    private final UserRepository repository;

    public Logout(UserRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.logout();
    }
}
