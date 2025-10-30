package com.example.rumireaborlykovacalendarapp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.data.FirebaseUserRepository;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.usecases.Logout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnRefresh;
    private Logout logoutUseCase;
    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "MainActivity created");

        initViews();
        setupLogoutButton();
        setupRefreshButton();
        setupRecyclerView();
        setupViewModel();
    }

    private void initViews() {
        btnLogout = findViewById(R.id.btnLogout);
        btnRefresh = findViewById(R.id.btnRefresh);
        rvEvents = findViewById(R.id.rvEvents);
    }

    private void setupLogoutButton() {
        logoutUseCase = new Logout(new FirebaseUserRepository());
        btnLogout.setOnClickListener(v -> {
            logoutUseCase.execute();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupRefreshButton() {
        btnRefresh.setOnClickListener(v -> {
            viewModel.refreshEvents();
            Toast.makeText(this, "Обновление данных...", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupRecyclerView() {
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(new ArrayList<>());
        rvEvents.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        viewModel.getEvents().observe(this, this::updateEvents);

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                Toast.makeText(this, "Загрузка данных...", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateEvents(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            Log.d("MainActivity", "Displaying " + events.size() + " events");
            adapter.updateEvents(events);
            Toast.makeText(this, "Загружено событий: " + events.size(), Toast.LENGTH_SHORT).show();
        } else {
            Log.d("MainActivity", "No events to display");
            adapter.updateEvents(new ArrayList<>());
            Toast.makeText(this, "Нет событий для отображения", Toast.LENGTH_SHORT).show();
        }
    }
}