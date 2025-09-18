package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class UpdateUserProfile {
    private final UserRepository repository;

    public UpdateUserProfile(UserRepository repository) {
        this.repository = repository;
    }

    public User execute(User user) {
        return user;
    }
}
