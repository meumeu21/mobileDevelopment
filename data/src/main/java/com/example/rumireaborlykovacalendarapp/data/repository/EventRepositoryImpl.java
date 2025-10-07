package com.example.rumireaborlykovacalendarapp.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.rumireaborlykovacalendarapp.data.local.AppDatabase;
import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;
import com.example.rumireaborlykovacalendarapp.data.remote.NetworkApi;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private final AppDatabase database;
    private final NetworkApi api;

    public EventRepositoryImpl(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "calendar_db")
                .allowMainThreadQueries()
                .build();

        api = new NetworkApi();
    }

    @Override
    public List<Event> getEventsForDate(Date date) {
        return Collections.emptyList();
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return Collections.emptyList();
    }

    @Override
    public Event getSpecificEvent(String id) {
        return null;
    }

    @Override
    public Event createEvent(String title, Date date) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        return null;
    }

    @Override
    public void deleteEvent(String id) {

    }

    @Override
    public List<Event> getEvents() {
        List<EventEntity> local = database.eventDao().getAllEvents();
        if (!local.isEmpty()) {
            List<Event> events = new ArrayList<>();
            for (EventEntity e : local) {
                events.add(new Event(e.id, e.title, e.date, e.description));
            }
            return events;
        }

        List<Event> remote = api.fetchEventsFromServer();
        for (Event event : remote) {
            database.eventDao().insert(new EventEntity(event.getTitle(), event.getDate(), event.getDescription()));
        }

        return remote;
    }

    @Override
    public void addEvent(Event event) {
        database.eventDao().insert(new EventEntity(event.getTitle(), event.getDate(), event.getDescription()));
    }

    @Override
    public void clearEvents() {
        database.eventDao().clearAll();
    }
}
