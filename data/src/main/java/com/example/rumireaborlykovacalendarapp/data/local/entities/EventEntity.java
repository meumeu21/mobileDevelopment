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

    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;

    public boolean notify;

    public int groupId;
    public int userId;
    public String description;
}
