package com.example.rumireaborlykovacalendarapp.domain.models;

import java.util.Date;

public class Event {
    private Integer id;
    private String title;
    private Date date;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private boolean notify;
    private int groupId;
    private String description;
    private int userId;
    public Event() {

    }

    public Event(
            Integer id,
            String title,
            Date date,
            int startHour,
            int startMinute,
            int endHour,
            int endMinute,
            boolean notify,
            int groupId,
            String description,
            int userId
    ) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.notify = notify;
        this.groupId = groupId;
        this.description = description;
        this.userId = userId;
    }

    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public String getTitle() { return title; }
    public Date getDate() { return date; }
    public int getStartHour() { return startHour; }
    public int getStartMinute() { return startMinute; }
    public int getEndHour() { return endHour; }
    public int getEndMinute() { return endMinute; }
    public boolean isNotify() { return notify; }
    public int getGroupId() {return groupId; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(Date date) { this.date = date; }
    public void setStartHour(int startHour) { this.startHour = startHour; }
    public void setStartMinute(int startMinute) { this.startMinute = startMinute; }
    public void setEndHour(int endHour) { this.endHour = endHour; }
    public void setEndMinute(int endMinute) { this.endMinute = endMinute; }
    public void setNotify(boolean notify) {this.notify = notify; }
    public void setGroupId(int groupId) { this.groupId = groupId; }
    public void setDescription(String description) { this.description = description; }
}
