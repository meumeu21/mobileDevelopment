package com.example.rumireaborlykovacalendarapp.domain.models;

import java.util.Date;

public class Event {
    private Integer id;
    private String title;
    private Date date;
    private String description;

    public Event(Integer id, String title, Date date, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setDate(Date date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
}
