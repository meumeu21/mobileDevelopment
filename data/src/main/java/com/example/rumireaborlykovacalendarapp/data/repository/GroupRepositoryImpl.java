package com.example.rumireaborlykovacalendarapp.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.rumireaborlykovacalendarapp.data.local.AppDatabase;
import com.example.rumireaborlykovacalendarapp.data.local.entities.GroupEntity;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

import static com.example.rumireaborlykovacalendarapp.data.local.mappers.GroupMapper.*;

import java.util.List;

public class GroupRepositoryImpl implements GroupRepository {

    private final AppDatabase db;
    private final int userId;

    public GroupRepositoryImpl(Context context, int userId) {
        this.userId = userId;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    public List<Group> getGroups() {
        return toDomainList(db.groupDao().getAll(userId));
    }

    public Group getSpecificGroup(int id) {
        return toDomain(db.groupDao().getGroup(id, userId));
    }

    @Override
    public Group createGroup(String name) {
        GroupEntity e = new GroupEntity();
        e.name = name;
        e.color = "#FFFFFF";
        e.userId = userId;

        long id = db.groupDao().insert(e);
        return new Group((int) id, name, e.color, userId);
    }

    @Override
    public void deleteGroup(String id) {
        db.groupDao().delete(Integer.parseInt(id), userId);
    }

    @Override
    public Group updateGroup(Group group) {
        db.groupDao().update(
                group.getId(),
                group.getName(),
                group.getColor(),
                group.getUserId()
        );

        return group;
    }
}

