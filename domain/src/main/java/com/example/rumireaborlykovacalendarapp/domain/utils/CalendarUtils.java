package com.example.rumireaborlykovacalendarapp.domain.utils;

import com.example.rumireaborlykovacalendarapp.domain.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {

    public static List<DayItem> generateMonthDays(int year, int month) {
        System.out.println("DEBUG: Генерация дней для " + year + "-" + (month + 1));

        List<DayItem> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        System.out.println("DEBUG: firstDayOfWeek: " + firstDayOfWeek + ", daysInMonth: " + daysInMonth);

        int startOffset = (firstDayOfWeek - calendar.getFirstDayOfWeek() + 7) % 7;

        calendar.add(Calendar.MONTH, -1);
        int prevMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar today = Calendar.getInstance();

        for (int i = prevMonthDays - startOffset + 1; i <= prevMonthDays; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            days.add(new DayItem(date, i, false, isToday(date, today)));
        }

        calendar.set(year, month, 1);
        for (int i = 1; i <= daysInMonth; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            days.add(new DayItem(date, i, true, isToday(date, today)));
        }

        calendar.add(Calendar.MONTH, 1);
        int remainingDays = 35 - days.size();
        for (int i = 1; i <= remainingDays; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            days.add(new DayItem(date, i, false, isToday(date, today)));
        }

        return days;
    }

    private static boolean isToday(Date date, Calendar today) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                dateCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                dateCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthName(int month) {
        String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        System.out.println("DEBUG: getMonthName для индекса " + month + ": " + months[month]);
        return months[month];
    }
}