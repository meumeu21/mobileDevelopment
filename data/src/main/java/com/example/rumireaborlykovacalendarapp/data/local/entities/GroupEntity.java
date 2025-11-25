package com.example.rumireaborlykovacalendarapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class GroupEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String color;

    public int userId;
}

