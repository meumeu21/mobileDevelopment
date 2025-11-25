package com.example.rumireaborlykovacalendarapp.presentation.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.presentation.fragments.AccountFragment;
import com.example.rumireaborlykovacalendarapp.presentation.fragments.AddEventFragment;
import com.example.rumireaborlykovacalendarapp.presentation.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private String userId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null || userId.isEmpty()) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getUid();
            }
        }

        initViews();
        setupNavigation();

        loadHomeFragment();
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadHomeFragment();
                return true;
            } else if (itemId == R.id.nav_add_event) {
                loadAddEventFragment();
                return true;
            } else if (itemId == R.id.nav_account) {
                loadAccountFragment();
                return true;
            }
            return false;
        });
    }

    private void loadHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("USER_ID", userId);
        fragment.setArguments(args);

        replaceFragment(fragment);
    }

    private void loadAddEventFragment() {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Войдите в аккаунт чтобы добавить событие", Toast.LENGTH_SHORT).show();
            bottomNavigation.setSelectedItemId(R.id.nav_home);
            return;
        }

        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString("USER_ID", userId);
        fragment.setArguments(args);

        replaceFragment(fragment);
    }

    private void loadAccountFragment() {
        AccountFragment fragment = new AccountFragment();
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void updateFragmentsForGuest() {
        userId = null;

        if (bottomNavigation.getSelectedItemId() == R.id.nav_add_event) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment) {
            loadHomeFragment();
        } else if (currentFragment instanceof AccountFragment) {
            loadAccountFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && (userId == null || userId.isEmpty())) {
            userId = currentUser.getUid();
            updateFragmentsForAuth();
        }
    }

    private void updateFragmentsForAuth() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment) {
            loadHomeFragment();
        } else if (currentFragment instanceof AddEventFragment) {
            loadAddEventFragment();
        } else if (currentFragment instanceof AccountFragment) {
            loadAccountFragment();
        }
    }
}