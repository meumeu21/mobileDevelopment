package com.example.rumireaborlykovacalendarapp.domain.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DayItem {
    private final Date date;
    private final int dayOfMonth;
    private final boolean isCurrentMonth;
    private final boolean isToday;
    private List<Event> events;
    private boolean isSelected;

    private Holiday holiday;

    public DayItem(Date date, int dayOfMonth, boolean isCurrentMonth, boolean isToday) {
        this.date = date;
        this.dayOfMonth = dayOfMonth;
        this.isCurrentMonth = isCurrentMonth;
        this.isToday = isToday;
        this.isSelected = false;
    }

    public Date getDate() { return date; }
    public int getDayOfMonth() { return dayOfMonth; }
    public boolean isCurrentMonth() { return isCurrentMonth; }
    public boolean isToday() { return isToday; }
    public List<Event> getEvents() { return events; }
    public boolean isSelected() { return isSelected; }

    public void setEvents(List<Event> events) { this.events = events; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public Holiday getHoliday() { return holiday; }
    public void setHoliday(Holiday holiday) { this.holiday = holiday; }
    public boolean isHoliday() { return holiday != null; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayItem dayItem = (DayItem) o;
        return Objects.equals(date, dayItem.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}