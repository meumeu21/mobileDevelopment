package com.example.rumireaborlykovacalendarapp.domain.repository;

public interface UserRepository {
    interface AuthCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    void login(String email, String password, AuthCallback callback);
    void register(String email, String password, AuthCallback callback);
    void logout();
}
