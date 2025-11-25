package com.example.rumireaborlykovacalendarapp.domain.repository;

import com.example.rumireaborlykovacalendarapp.domain.models.Holiday;

import java.util.List;

public interface HolidayRepository {
    void getHolidays(int year, HolidayCallback callback);
    List<Holiday> getHolidaysForMonth(int year, int month);
    List<Holiday> getAllHolidays();
    boolean isHolidaysLoaded();

    interface HolidayCallback {
        void onSuccess(List<Holiday> holidays);
        void onFailure(String errorMessage);
    }
}