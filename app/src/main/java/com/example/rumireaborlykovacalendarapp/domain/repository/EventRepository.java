package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository {
    List<Event> getEventsForDate(Date date);
    List<Event> getUpcomingEvents();
    Event getSpecificEvent(String id);
    Event createEvent(String title, Date date);
    Event updateEvent(Event event);
    void deleteEvent(String id);
}
