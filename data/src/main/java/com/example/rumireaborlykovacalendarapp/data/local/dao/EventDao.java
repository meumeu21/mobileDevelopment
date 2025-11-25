package com.example.rumireaborlykovacalendarapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM events WHERE userId=:userId")
    List<EventEntity> getAllEvents(int userId);

    @Query("SELECT * FROM events WHERE id=:id AND userId=:userId LIMIT 1")
    EventEntity getEvent(int id, int userId);

    @Query("SELECT * FROM events WHERE date BETWEEN :start AND :end AND userId=:userId")
    List<EventEntity> getEventsOn(long start, long end, int userId);

    @Insert
    long insert(EventEntity event);

    @Query("UPDATE events SET title=:title, date=:date, startHour=:startHour, startMinute=:startMinute, endHour=:endHour, endMinute=:endMinute, notify=:notify, groupId=:groupId, description=:description WHERE id=:id AND userId=:userId")
    void update(int id, String title, Date date, int startHour, int startMinute, int endHour, int endMinute, boolean notify, int groupId, String description, int userId);

    @Query("DELETE FROM events WHERE id=:id AND userId=:userId")
    void delete(int id, int userId);

    @Query("DELETE FROM events WHERE userId=:userId")
    void clearAll(int userId);
}

