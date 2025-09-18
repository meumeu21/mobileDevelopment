package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

public class GetSpecificEvent {
    private final EventRepository repository;

    public GetSpecificEvent(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(String id) {
        return repository.getSpecificEvent(id);
    }
}
