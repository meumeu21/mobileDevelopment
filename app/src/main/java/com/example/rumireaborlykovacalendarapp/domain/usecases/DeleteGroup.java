package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

public class DeleteGroup {
    private final GroupRepository repository;

    public DeleteGroup(GroupRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteGroup(id);
    }
}
