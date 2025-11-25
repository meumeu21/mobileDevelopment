package com.example.rumireaborlykovacalendarapp.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HolidayResponse {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response {
        @SerializedName("holidays")
        private List<Holiday> holidays;

        public List<Holiday> getHolidays() {
            return holidays;
        }
    }

    public static class Holiday {
        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("date")
        private DateInfo date;

        @SerializedName("type")
        private List<String> types;

        @SerializedName("locations")
        private String locations;

        @SerializedName("states")
        private String states;

        public String getName() { return name; }
        public String getDescription() { return description; }
        public DateInfo getDate() { return date; }
        public List<String> getTypes() { return types; }
        public String getLocations() { return locations; }
        public String getStates() { return states; }
    }

    public static class DateInfo {
        @SerializedName("iso")
        private String iso;

        @SerializedName("datetime")
        private DateTime datetime;

        public String getIso() { return iso; }
        public DateTime getDatetime() { return datetime; }
    }

    public static class DateTime {
        @SerializedName("year")
        private int year;

        @SerializedName("month")
        private int month;

        @SerializedName("day")
        private int day;

        public int getYear() { return year; }
        public int getMonth() { return month; }
        public int getDay() { return day; }
    }
}