package com.example.rumireaborlykovacalendarapp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;

@Database(entities = {EventEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}
