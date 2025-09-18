package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.List;

public class GetUpcomingEvents {
    private final EventRepository repository;

    public GetUpcomingEvents(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> execute() {
        return repository.getUpcomingEvents();
    }
}
