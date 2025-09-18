package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;

import java.util.List;

public interface GroupRepository {
    List<Group> getGroups();
    Group createGroup(String name);
    void deleteGroup(String id);
}
