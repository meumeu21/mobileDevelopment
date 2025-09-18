package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.Date;
import java.util.List;

public class GetEventsForDate {
    private final EventRepository repository;

    public GetEventsForDate(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> execute(Date date) {
        return repository.getEventsForDate(date);
    }
}
