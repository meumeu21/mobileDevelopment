package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.Date;

public class CreateEvent {

    private final EventRepository repository;

    public CreateEvent(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(String title, Date date, int startHour, int startMinute,
                         int endHour, int endMinute, boolean notify, int groupId,
                         String description) {
        return repository.createEvent(title, date, startHour, startMinute,
                endHour, endMinute, notify, groupId, description);
    }
}
