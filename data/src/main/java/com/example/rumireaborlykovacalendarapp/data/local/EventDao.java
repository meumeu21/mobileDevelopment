package com.example.rumireaborlykovacalendarapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    List<EventEntity> getAllEvents();

    @Insert
    void insert(EventEntity event);

    @Query("DELETE FROM events")
    void clearAll();
}
