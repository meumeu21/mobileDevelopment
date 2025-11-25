package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

public class UpdateGroup {
    private final GroupRepository repository;

    public UpdateGroup(GroupRepository repository) {
        this.repository = repository;
    }

    public Group execute(Group group) {
        return repository.updateGroup(group);
    }
}
