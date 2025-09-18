package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

public class UpdateEvent {
    private final EventRepository repository;

    public UpdateEvent(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Event event) {
        return repository.updateEvent(event);
    }
}
