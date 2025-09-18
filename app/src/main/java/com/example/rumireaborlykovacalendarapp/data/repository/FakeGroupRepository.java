package com.example.rumireaborlykovacalendarapp.data.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeGroupRepository implements GroupRepository {

    private List<Group> groups = new ArrayList<>();

    public FakeGroupRepository() {
        // Добавляем тестовые группы
        groups.add(new Group("1", "Учёба"));
        groups.add(new Group("2", "Работа"));
        groups.add(new Group("3", "Спорт"));
    }

    @Override
    public List<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    @Override
    public Group createGroup(String name) {
        Group newGroup = new Group(String.valueOf(groups.size() + 1), name);
        groups.add(newGroup);
        return newGroup;
    }

    @Override
    public void deleteGroup(String id) {
        groups.removeIf(g -> g.getId().equals(id));
    }
}
