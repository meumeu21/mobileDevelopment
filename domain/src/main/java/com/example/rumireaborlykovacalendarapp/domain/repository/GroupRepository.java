package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;

import java.util.List;

public interface GroupRepository {
    List<Group> getGroups();
    Group getSpecificGroup(int id);
    Group createGroup(String name);
    void deleteGroup(String id);
    Group updateGroup(Group group);
}
