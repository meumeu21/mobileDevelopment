package com.example.rumireaborlykovacalendarapp.data.local.mappers;

import com.example.rumireaborlykovacalendarapp.data.local.entities.GroupEntity;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupMapper {

    public static Group toDomain(GroupEntity e) {
        if (e == null) return null;

        return new Group(
                e.id,
                e.name,
                e.color,
                e.userId
        );
    }

    public static GroupEntity toEntity(String name, String color, int userId) {
        GroupEntity entity = new GroupEntity();
        entity.name = name;
        entity.color = color;
        entity.userId = userId;
        return entity;
    }

    public static List<Group> toDomainList(List<GroupEntity> entities) {
        List<Group> list = new ArrayList<>();
        for (GroupEntity e : entities) list.add(toDomain(e));
        return list;
    }
}
