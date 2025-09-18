package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.User;

public interface UserRepository {
    User login(String email, String password);
    User register(String name, String email, String password);
    User getUserProfile();
    void logout();
}
