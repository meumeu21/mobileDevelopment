package com.example.rumireaborlykovacalendarapp.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.rumireaborlykovacalendarapp.data.local.AppDatabase;
import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;
import static com.example.rumireaborlykovacalendarapp.data.local.mappers.EventMapper.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private final AppDatabase db;
    private final int userId;

    public EventRepositoryImpl(Context context, int userId) {
        this.userId = userId;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    public List<Event> getEventsForDate(Date date) {
        Date normalized = normalizeDate(date);

        long start = normalized.getTime();
        long end = start + 24 * 60 * 60 * 1000 - 1;

        System.out.println("DEBUG: Поиск событий в диапазоне: " + new Date(start) + " - " + new Date(end));

        List<EventEntity> entities = db.eventDao().getEventsOn(start, end, userId);

        System.out.println("DEBUG: Найдено событий: " + entities.size());

        return toDomainList(entities);
    }


    @Override
    public List<Event> getEvents() {
        System.out.println("DEBUG: Запрос всех событий пользователя: " + userId);
        List<EventEntity> entities = db.eventDao().getAllEvents(userId);
        System.out.println("DEBUG: Всего событий в БД: " + entities.size());
        return toDomainList(entities);
    }

    @Override
    public Event getSpecificEvent(String id) {
        return toDomain(db.eventDao().getEvent(Integer.parseInt(id), userId));
    }

    @Override
    public Event createEvent(String title, Date date, int startHour, int startMinute,
                             int endHour, int endMinute, boolean notify, int groupId,
                             String description) {
        date = normalizeDate(date);
        EventEntity e = new EventEntity();
        e.title = title;
        e.date = date;
        e.startHour = startHour;
        e.startMinute = startMinute;
        e.endHour = endHour;
        e.endMinute = endMinute;
        e.notify = notify;
        e.groupId = groupId;
        e.description = description;
        e.userId = userId;

        long newId = db.eventDao().insert(e);
        e.id = (int) newId;
        return toDomain(e);
    }

    public Event updateEvent(Event event) {
        db.eventDao().update(
                event.getId(),
                event.getTitle(),
                event.getDate(),
                event.getStartHour(),
                event.getStartMinute(),
                event.getEndHour(),
                event.getEndMinute(),
                event.isNotify(),
                event.getGroupId(),
                event.getDescription(),
                userId
        );

        return event;
    }

    @Override
    public void deleteEvent(String id) {
        db.eventDao().delete(Integer.parseInt(id), userId);
    }

    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date normalized = cal.getTime();
        System.out.println("DEBUG: Нормализация даты: " + date + " -> " + normalized);

        return normalized;
    }
}
