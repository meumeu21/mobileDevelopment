package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class GetUserProfile {
    private final UserRepository repository;

    public GetUserProfile(UserRepository repository) {
        this.repository = repository;
    }

    public User execute() {
        return repository.getUserProfile();
    }
}
