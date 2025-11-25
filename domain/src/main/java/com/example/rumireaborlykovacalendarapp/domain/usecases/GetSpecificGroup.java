package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;

import java.util.List;

public class GetSpecificGroup {
    private final GroupRepository repository;

    public GetSpecificGroup(GroupRepository repository) {
        this.repository = repository;
    }

    public Group execute(int id) {
        return repository.getSpecificGroup(id);
    }
}
