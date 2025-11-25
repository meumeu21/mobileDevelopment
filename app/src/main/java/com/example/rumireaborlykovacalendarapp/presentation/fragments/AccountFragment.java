package com.example.rumireaborlykovacalendarapp.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.presentation.activities.AuthActivity;
import com.example.rumireaborlykovacalendarapp.presentation.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    private TextView tvUserEmail, tvAuthStatus;
    private Button btnLogin, btnRegister, btnLogout;
    private ViewGroup authContainer, userContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);
        checkAuthStatus();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvAuthStatus = view.findViewById(R.id.tvAuthStatus);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnLogout = view.findViewById(R.id.btnLogout);
        authContainer = view.findViewById(R.id.authContainer);
        userContainer = view.findViewById(R.id.userContainer);
    }

    private void checkAuthStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            showUserInfo(currentUser);
        } else {
            showAuthForm();
        }
    }

    private void showUserInfo(FirebaseUser user) {
        authContainer.setVisibility(View.GONE);
        userContainer.setVisibility(View.VISIBLE);

        tvUserEmail.setText(user.getEmail());
        tvAuthStatus.setText("Вы авторизованы");
    }

    private void showAuthForm() {
        authContainer.setVisibility(View.VISIBLE);
        userContainer.setVisibility(View.GONE);

        tvAuthStatus.setText("Вы не авторизованы");
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
            checkAuthStatus();

            if (getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateFragmentsForGuest();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAuthStatus();
    }
}