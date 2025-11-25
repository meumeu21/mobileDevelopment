package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository {
    List<Event> getEventsForDate(Date date);
    Event getSpecificEvent(String id);
    Event createEvent(String title, Date date, int startHour, int startMinute,
                      int endHour, int endMinute, boolean notify, int groupId,
                      String description);
    Event updateEvent(Event event);
    void deleteEvent(String id);
    List<Event> getEvents();
}
