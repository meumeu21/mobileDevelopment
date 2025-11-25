package com.example.rumireaborlykovacalendarapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.rumireaborlykovacalendarapp.data.local.entities.GroupEntity;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert
    long insert(GroupEntity group);

    @Query("SELECT * FROM groups WHERE userId=:userId")
    List<GroupEntity> getAll(int userId);
    @Query("SELECT * FROM groups WHERE id=:id AND userId=:userId LIMIT 1")
    GroupEntity getGroup(int id, int userId);

    @Query("DELETE FROM groups WHERE id=:id AND userId=:userId")
    void delete(int id, int userId);

    @Query("UPDATE groups SET name=:name, color=:color WHERE id=:id AND userId=:userId")
    void update(int id, String name, String color, int userId);
}

