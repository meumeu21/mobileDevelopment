package com.example.rumireaborlykovacalendarapp.data.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FakeEventRepository implements EventRepository {

    private List<Event> events = new ArrayList<>();

    public FakeEventRepository() {
        events.add(new Event("1", "Лекция по Android", new Date()));
        events.add(new Event("2", "Собрание команды", addDays(new Date(), 1)));
        events.add(new Event("3", "Тренировка", addDays(new Date(), 2)));
    }

    @Override
    public List<Event> getEventsForDate(Date date) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (isSameDay(e.getDate(), date)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return new ArrayList<>(events);
    }

    @Override
    public Event getSpecificEvent(String id) {
        for (Event e : events) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Event createEvent(String title, Date date) {
        Event newEvent = new Event(String.valueOf(events.size() + 1), title, date);
        events.add(newEvent);
        return newEvent;
    }

    @Override
    public Event updateEvent(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                events.set(i, event);
                return event;
            }
        }
        return null;
    }

    @Override
    public void deleteEvent(String id) {
        events.removeIf(e -> e.getId().equals(id));
    }

    private boolean isSameDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}