package com.example.rumireaborlykovacalendarapp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.data.FirebaseUserRepository;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.usecases.Logout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private Logout logoutUseCase;
    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "MainActivity created");

        btnLogout = findViewById(R.id.btnLogout);
        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        logoutUseCase = new Logout(new FirebaseUserRepository());
        btnLogout.setOnClickListener(v -> {
            logoutUseCase.execute();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        viewModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        viewModel.getCombinedEvents().observe(this, this::updateUI);
    }

    private void updateUI(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            Log.d("MainActivity", "Displaying " + events.size() + " events");
            adapter = new EventAdapter(events);
            rvEvents.setAdapter(adapter);
        } else {
            Log.d("MainActivity", "No events to display");
        }
    }
}
