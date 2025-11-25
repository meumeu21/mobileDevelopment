package com.example.rumireaborlykovacalendarapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.rumireaborlykovacalendarapp.data.local.dao.EventDao;
import com.example.rumireaborlykovacalendarapp.data.local.dao.GroupDao;
import com.example.rumireaborlykovacalendarapp.data.local.entities.EventEntity;
import com.example.rumireaborlykovacalendarapp.data.local.entities.GroupEntity;

@Database(
        entities = {EventEntity.class, GroupEntity.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract GroupDao groupDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "calendar_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
