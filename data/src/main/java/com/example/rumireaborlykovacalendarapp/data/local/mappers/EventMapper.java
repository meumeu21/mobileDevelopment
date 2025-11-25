package com.example.rumireaborlykovacalendarapp.data.local.mappers;

import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public static Event toDomain(EventEntity entity) {
        if (entity == null) return null;

        System.out.println("DEBUG: Конвертация EventEntity в Event: " + entity.title +
                " id: " + entity.id + " userId: " + entity.userId);

        return new Event(
                entity.id,
                entity.title,
                entity.date,
                entity.startHour,
                entity.startMinute,
                entity.endHour,
                entity.endMinute,
                entity.notify,
                entity.groupId,
                entity.description,
                entity.userId
        );
    }

    public static EventEntity toEntity(Event domain) {
        if (domain == null) return null;

        EventEntity entity = new EventEntity();
        entity.id = domain.getId();
        entity.title = domain.getTitle();
        entity.date = domain.getDate();
        entity.startHour = domain.getStartHour();
        entity.startMinute = domain.getStartMinute();
        entity.endHour = domain.getEndHour();
        entity.endMinute = domain.getEndMinute();
        entity.notify = domain.isNotify();
        entity.groupId = domain.getGroupId();
        entity.description = domain.getDescription();
        entity.userId = domain.getUserId();

        return entity;
    }

    public static List<Event> toDomainList(List<EventEntity> entities) {
        List<Event> list = new ArrayList<>();
        if (entities != null) {
            System.out.println("DEBUG: Конвертация " + entities.size() + " событий");
            for (EventEntity entity : entities) {
                Event domain = toDomain(entity);
                if (domain != null) {
                    list.add(domain);
                }
            }
        }
        return list;
    }
}