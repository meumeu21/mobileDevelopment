package com.example.rumireaborlykovacalendarapp.domain.usecases;

import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;

public class RestorePassword {
    private final UserRepository repository;

    public RestorePassword(UserRepository repository) {
        this.repository = repository;
    }

    public String execute(String email) {
        return "Ссылка для восстановления пароля отправлена на " + email;
    }
}
