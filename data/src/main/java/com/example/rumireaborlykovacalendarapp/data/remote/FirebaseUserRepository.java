package com.example.rumireaborlykovacalendarapp.data.remote;

import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUserRepository implements UserRepository {

    private final FirebaseAuth mAuth;

    public FirebaseUserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException().getMessage());
                });
    }

    @Override
    public void register(String email, String password, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure(task.getException().getMessage());
                });
    }

    @Override
    public void logout() {
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
    }
}
