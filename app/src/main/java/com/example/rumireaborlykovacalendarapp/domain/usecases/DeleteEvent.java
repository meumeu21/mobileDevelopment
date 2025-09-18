package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

public class DeleteEvent {
    private final EventRepository repository;

    public DeleteEvent(EventRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteEvent(id);
    }
}
