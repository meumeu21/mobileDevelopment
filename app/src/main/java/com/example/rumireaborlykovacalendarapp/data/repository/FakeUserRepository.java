package com.example.rumireaborlykovacalendarapp.data.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class FakeUserRepository implements UserRepository {
    private User testUser = new User("1", "Иван Иванов", "test@example.com");

    @Override
    public User login(String email, String password) {
        return testUser; // всегда возвращает тестового пользователя
    }

    @Override
    public User register(String name, String email, String password) {
        return new User("2", name, email);
    }

    @Override
    public User getUserProfile() {
        return testUser;
    }

    @Override
    public void logout() {
        testUser = null;
    }
}

