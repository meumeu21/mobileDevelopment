package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

public class CreateGroup {
    private final GroupRepository repository;

    public CreateGroup(GroupRepository repository) {
        this.repository = repository;
    }

    public Group execute(String name) {
        return repository.createGroup(name);
    }
}
