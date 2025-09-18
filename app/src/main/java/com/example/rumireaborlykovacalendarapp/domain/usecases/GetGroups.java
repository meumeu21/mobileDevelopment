package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

import java.util.List;

public class GetGroups {
    private final GroupRepository repository;

    public GetGroups(GroupRepository repository) {
        this.repository = repository;
    }

    public List<Group> execute() {
        return repository.getGroups();
    }
}
