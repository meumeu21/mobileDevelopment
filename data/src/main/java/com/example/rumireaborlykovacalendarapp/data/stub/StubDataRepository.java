package com.example.rumireaborlykovacalendarapp.data.stub;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StubDataRepository {

    private static StubDataRepository instance;
    private final List<Event> stubEvents;

    private StubDataRepository() {
        stubEvents = generateStubEvents();
    }

    public static StubDataRepository getInstance() {
        if (instance == null) {
            instance = new StubDataRepository();
        }
        return instance;
    }

    private List<Event> generateStubEvents() {
        List<Event> events = new ArrayList<>();

        events.add(new Event(1, "Встреча с командой",
                new Date(System.currentTimeMillis() + 86400000), "Еженедельный стендап"));
        events.add(new Event(2, "Презентация проекта",
                new Date(System.currentTimeMillis() + 172800000), "Демонстрация MVP клиенту"));
        events.add(new Event(3, "Код-ревью",
                new Date(System.currentTimeMillis() + 259200000), "Обсуждение новой функциональности"));
        events.add(new Event(4, "Обучение новым технологиям",
                new Date(System.currentTimeMillis() + 345600000), "Изучение Kotlin Coroutines"));
        events.add(new Event(5, "Планирование спринта",
                new Date(System.currentTimeMillis() + 432000000), "Определение задач на следующий спринт"));

        return events;
    }

    public List<Event> getStubEvents() {
        return new ArrayList<>(stubEvents);
    }

    public Event getEventById(int id) {
        for (Event event : stubEvents) {
            if (event.getId() != null && event.getId() == id) {
                return event;
            }
        }
        return null;
    }
}