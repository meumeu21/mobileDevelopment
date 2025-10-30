package com.example.rumireaborlykovacalendarapp.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.rumireaborlykovacalendarapp.data.local.AppDatabase;
import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;
import com.example.rumireaborlykovacalendarapp.data.remote.NetworkApi;
import com.example.rumireaborlykovacalendarapp.data.stub.StubDataRepository;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private final AppDatabase database;
    private final StubDataRepository stubDataRepository;

    public EventRepositoryImpl(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "calendar_db")
                .allowMainThreadQueries()
                .build();

        stubDataRepository = StubDataRepository.getInstance();
    }

    @Override
    public List<Event> getEventsForDate(Date date) {
        List<Event> allEvents = getEvents();
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : allEvents) {
            if (isSameDay(event.getDate(), date)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        List<Event> allEvents = getEvents();
        List<Event> upcomingEvents = new ArrayList<>();
        Date now = new Date();

        for (Event event : allEvents) {
            if (event.getDate().after(now)) {
                upcomingEvents.add(event);
            }
        }
        return upcomingEvents;
    }

    @Override
    public Event getSpecificEvent(String id) {
        try {
            int eventId = Integer.parseInt(id);
            return stubDataRepository.getEventById(eventId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Event createEvent(String title, Date date) {
        List<Event> existingEvents = getEvents();
        int maxId = 0;
        for (Event event : existingEvents) {
            if (event.getId() != null && event.getId() > maxId) {
                maxId = event.getId();
            }
        }

        Event newEvent = new Event(maxId + 1, title, date, "Новое событие");
        addEvent(newEvent);
        return newEvent;
    }

    @Override
    public Event updateEvent(Event event) {
        return event;
    }

    @Override
    public void deleteEvent(String id) {
    }

    @Override
    public List<Event> getEvents() {
         return stubDataRepository.getStubEvents();

//        List<EventEntity> local = database.eventDao().getAllEvents();
//        if (!local.isEmpty()) {
//            List<Event> events = new ArrayList<>();
//            for (EventEntity e : local) {
//                events.add(new Event(e.id, e.title, e.date, e.description));
//            }
//            return events;
//        }
//
//        List<Event> stubEvents = stubDataRepository.getStubEvents();
//
//        for (Event event : stubEvents) {
//            database.eventDao().insert(new EventEntity(
//                event.getTitle(),
//                event.getDate(),
//                event.getDescription()
//            ));
//        }
//
//        return stubEvents;
    }

    @Override
    public void addEvent(Event event) {
        database.eventDao().insert(new EventEntity(
                event.getTitle(),
                event.getDate(),
                event.getDescription()
        ));
    }

    @Override
    public void clearEvents() {
        database.eventDao().clearAll();
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) return false;

        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }
}