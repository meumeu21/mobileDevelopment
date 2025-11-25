package com.example.rumireaborlykovacalendarapp.domain.models;

import java.util.Date;

public class Holiday {
    private String name;
    private Date date;
    private boolean isDayOff;

    public Holiday(String name, Date date, boolean isDayOff) {
        this.name = name;
        this.date = date;
        this.isDayOff = isDayOff;
    }

    public String getName() { return name; }
    public Date getDate() { return date; }
    public boolean isDayOff() { return isDayOff; }
}