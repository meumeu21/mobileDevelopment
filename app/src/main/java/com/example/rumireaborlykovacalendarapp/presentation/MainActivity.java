package com.example.rumireaborlykovacalendarapp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.data.FirebaseUserRepository;
import com.example.rumireaborlykovacalendarapp.data.repository.EventRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.usecases.Logout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private Logout logoutUseCase;
    private RecyclerView rvEvents;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        try {
            EventRepositoryImpl repository = new EventRepositoryImpl(this);
            List<Event> events = repository.getEvents();
            Log.d("MainActivity", "Loaded events: " + events.size());

            adapter = new EventAdapter(events);
            rvEvents.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("MainActivity", "Error loading events", e);
        }
    }
}
