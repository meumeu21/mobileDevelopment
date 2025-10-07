package com.example.rumireaborlykovacalendarapp.data.remote;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NetworkApi {

    public List<Event> fetchEventsFromServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = sdf.parse("2025-10-06");
        } catch (ParseException e) {
            parsedDate = new Date();
        }

        return Arrays.asList(
                new Event(1, "Встреча с преподавателем", parsedDate, "Консультация по диплому"),
                new Event(2, "День рождения друга", parsedDate, "Поздравить Алексея"),
                new Event(3, "Сдача проекта", parsedDate, "Отправить курсовую работу")
        );
    }
}
