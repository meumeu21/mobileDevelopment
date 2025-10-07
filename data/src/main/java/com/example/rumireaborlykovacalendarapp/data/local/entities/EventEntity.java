package com.example.rumireaborlykovacalendarapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "events")
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public Date date;
    public String description;

    public EventEntity(String title, Date date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }
}
