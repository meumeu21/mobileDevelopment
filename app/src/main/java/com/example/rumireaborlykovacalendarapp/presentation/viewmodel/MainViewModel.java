package com.example.rumireaborlykovacalendarapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumireaborlykovacalendarapp.data.repository.EventRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.data.repository.GroupRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.domain.models.DayItem;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.models.Holiday;
import com.example.rumireaborlykovacalendarapp.domain.repository.HolidayRepository;
import com.example.rumireaborlykovacalendarapp.domain.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainViewModel extends ViewModel {

    private final EventRepositoryImpl eventRepository;
    private final GroupRepositoryImpl groupRepository;

    private final MutableLiveData<List<DayItem>> calendarDays = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<List<Group>> groups = new MutableLiveData<>();
    private final MutableLiveData<String> currentMonth = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> selectedDayEvents = new MutableLiveData<>();
    private final MutableLiveData<Date> navigateToDate = new MutableLiveData<>();
    public LiveData<Date> getNavigateToDate() { return navigateToDate; }


    private Calendar currentCalendar;
    private Map<Date, List<Event>> eventsByDate = new HashMap<>();
    private DayItem selectedDay;

    private final HolidayRepository holidayRepository;
    private final MutableLiveData<List<Holiday>> holidays = new MutableLiveData<>();

    private boolean holidaysLoaded = false;
    private Set<Integer> loadedYears = new HashSet<>();

    public MainViewModel(EventRepositoryImpl eventRepository,
                         GroupRepositoryImpl groupRepository,
                         HolidayRepository holidayRepository) {
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
        this.holidayRepository = holidayRepository;
        this.currentCalendar = Calendar.getInstance();

        loadAllHolidays();
        loadCurrentMonth();
    }

    public LiveData<List<DayItem>> getCalendarDays() {
        return calendarDays;
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<List<Group>> getGroups() {
        return groups;
    }

    public LiveData<String> getCurrentMonth() {
        return currentMonth;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadCurrentMonth() {
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);

        List<DayItem> days = CalendarUtils.generateMonthDays(year, month);

        Date previousSelectedDate = null;
        if (selectedDay != null) {
            previousSelectedDate = selectedDay.getDate();
            selectedDay = null;
        }

        currentMonth.setValue(CalendarUtils.getMonthName(month) + " " + year);
        loadAllEventsAndUpdateDays(days);

        loadHolidaysForYear(year);

        if (previousSelectedDate != null) {
            Calendar prevCal = Calendar.getInstance();
            prevCal.setTime(previousSelectedDate);

            if (prevCal.get(Calendar.YEAR) == year && prevCal.get(Calendar.MONTH) == month) {
                for (DayItem day : days) {
                    if (day.isCurrentMonth() && isSameDay(day.getDate(), previousSelectedDate)) {
                        selectDay(day);
                        break;
                    }
                }
            }
        }
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) return false;

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void loadHolidaysForYear(int year) {
        if (loadedYears.contains(year)) {
            return;
        }


        holidayRepository.getHolidays(year, new HolidayRepository.HolidayCallback() {
            @Override
            public void onSuccess(List<Holiday> holidays) {
                loadedYears.add(year);

                reloadDaysWithHolidays();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadedYears.add(year);
                reloadDaysWithHolidays();
            }
        });
    }

    public void nextMonth() {
        currentCalendar.add(Calendar.MONTH, 1);
        loadHolidaysForYear(currentCalendar.get(Calendar.YEAR));
        loadCurrentMonth();
    }

    public void previousMonth() {
        currentCalendar.add(Calendar.MONTH, -1);
        loadHolidaysForYear(currentCalendar.get(Calendar.YEAR));
        loadCurrentMonth();
    }

    public void goToToday() {
        currentCalendar = Calendar.getInstance();
        loadCurrentMonth();
    }

    public LiveData<List<Event>> getSelectedDayEvents() {
        return selectedDayEvents;
    }

    public void selectDay(DayItem day) {
        if (day.isCurrentMonth()) {
            if (selectedDay != null) {
                selectedDay.setSelected(false);
            }

            selectedDay = day;
            selectedDay.setSelected(true);

            List<DayItem> currentDays = calendarDays.getValue();
            if (currentDays != null) {
                calendarDays.setValue(currentDays);
            }

            loadEventsForSelectedDate(day.getDate());
        }
    }

    private void loadEventsForSelectedDate(Date date) {
        try {
            System.out.println("DEBUG: Загрузка событий для даты: " + date);

            List<Event> eventsForDate = eventRepository.getEventsForDate(date);
            System.out.println("DEBUG: Найдено пользовательских событий: " + eventsForDate.size());

            List<Event> allEventsForDate = new ArrayList<>();

            for (Event userEvent : eventsForDate) {
                System.out.println("DEBUG: Пользовательское событие: " + userEvent.getTitle() +
                        " время: " + userEvent.getStartHour() + ":" + userEvent.getStartMinute() +
                        " - " + userEvent.getEndHour() + ":" + userEvent.getEndMinute());

                Event formattedEvent = new Event();
                formattedEvent.setId(userEvent.getId());
                formattedEvent.setTitle(userEvent.getTitle());
                formattedEvent.setDate(userEvent.getDate());
                formattedEvent.setStartHour(userEvent.getStartHour());
                formattedEvent.setStartMinute(userEvent.getStartMinute());
                formattedEvent.setEndHour(userEvent.getEndHour());
                formattedEvent.setEndMinute(userEvent.getEndMinute());
                formattedEvent.setDescription(userEvent.getDescription());
                formattedEvent.setNotify(userEvent.isNotify());
                formattedEvent.setGroupId(userEvent.getGroupId());
                formattedEvent.setUserId(userEvent.getUserId());

                allEventsForDate.add(formattedEvent);
            }

            Date normalizedDate = normalizeDate(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(normalizedDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            List<Holiday> monthHolidays = holidayRepository.getHolidaysForMonth(year, month);
            System.out.println("DEBUG: Найдено праздников для месяца: " + monthHolidays.size());

            for (Holiday holiday : monthHolidays) {
                Date holidayDate = normalizeDate(holiday.getDate());
                if (normalizedDate.equals(holidayDate)) {
                    System.out.println("DEBUG: Добавляем праздник: " + holiday.getName());

                    Event holidayEvent = new Event();
                    holidayEvent.setTitle("🎉 " + holiday.getName());
                    holidayEvent.setDate(holidayDate);
                    holidayEvent.setStartHour(0);
                    holidayEvent.setStartMinute(0);
                    holidayEvent.setEndHour(23);
                    holidayEvent.setEndMinute(59);
                    holidayEvent.setDescription(holiday.isDayOff() ? "Выходной день" : "Праздничный день");
                    holidayEvent.setId(-1);
                    allEventsForDate.add(holidayEvent);
                    break;
                }
            }

            System.out.println("DEBUG: Всего событий для отображения: " + allEventsForDate.size());
            selectedDayEvents.postValue(allEventsForDate);

        } catch (Exception e) {
            System.out.println("DEBUG: Ошибка загрузки событий: " + e.getMessage());
            errorMessage.postValue("Ошибка загрузки событий: " + e.getMessage());
            selectedDayEvents.postValue(new ArrayList<>());
        }
    }

    private void loadAllEventsAndUpdateDays(List<DayItem> days) {
        try {
            List<Event> allEvents = eventRepository.getEvents();
            events.postValue(allEvents);

            eventsByDate.clear();
            for (Event event : allEvents) {
                Date eventDate = normalizeDate(event.getDate());
                if (!eventsByDate.containsKey(eventDate)) {
                    eventsByDate.put(eventDate, new ArrayList<>());
                }
                eventsByDate.get(eventDate).add(event);
            }

            List<Group> allGroups = groupRepository.getGroups();
            groups.postValue(allGroups);

            // ОБНОВЛЯЕМ СОБЫТИЯ ДЛЯ КАЖДОГО ДНЯ
            for (DayItem day : days) {
                Date normalizedDate = normalizeDate(day.getDate());
                List<Event> dayEvents = eventsByDate.get(normalizedDate);
                day.setEvents(dayEvents != null ? dayEvents : new ArrayList<>());

                // ЛОГ ДЛЯ ОТЛАДКИ
                if (dayEvents != null && !dayEvents.isEmpty()) {
                    System.out.println("DEBUG: День " + day.getDayOfMonth() + " имеет событий: " + dayEvents.size());
                }
            }

            if (holidaysLoaded) {
                linkHolidaysToDays(days);
            }

            calendarDays.postValue(days);

        } catch (Exception e) {
            errorMessage.postValue("Ошибка загрузки данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void linkHolidaysToDays(List<DayItem> days) {
        if (days == null || days.isEmpty()) return;

        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);


        List<Holiday> monthHolidays = holidayRepository.getHolidaysForMonth(year, month);

        int linkedCount = 0;
        for (DayItem day : days) {
            if (!day.isCurrentMonth()) continue;

            Date normalizedDayDate = normalizeDate(day.getDate());

            for (Holiday holiday : monthHolidays) {
                Date normalizedHolidayDate = normalizeDate(holiday.getDate());
                if (normalizedDayDate.equals(normalizedHolidayDate)) {
                    day.setHoliday(holiday);
                    linkedCount++;
                    break;
                }
            }
        }
    }

    private void loadAllHolidays() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        loadHolidaysForYear(currentYear);

        loadHolidaysForYear(currentYear + 1);

        loadHolidaysForYear(currentYear - 1);
    }

    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date normalized = cal.getTime();
        System.out.println("DEBUG: Нормализация даты: " + date + " -> " + normalized);

        return normalized;
    }

    public void loadEventsForDate(Date date) {
        try {
            List<Event> eventsForDate = eventRepository.getEventsForDate(date);
            events.postValue(eventsForDate);
        } catch (Exception e) {
            errorMessage.postValue("Ошибка загрузки событий: " + e.getMessage());
        }
    }

    public void refreshData() {
        loadCurrentMonth();

        if (selectedDay != null) {
            System.out.println("DEBUG: Обновление событий для выбранного дня после refresh");
            loadEventsForSelectedDate(selectedDay.getDate());
        }
    }

    public LiveData<List<Holiday>> getHolidays() {
        return holidays;
    }

    public void loadHolidaysForMonth(int year, int month) {
        holidayRepository.getHolidays(year, new HolidayRepository.HolidayCallback() {
            @Override
            public void onSuccess(List<Holiday> holidaysList) {
                List<Holiday> monthHolidays = filterHolidaysByMonth(holidaysList, year, month);
                holidays.postValue(monthHolidays);

                reloadDaysWithHolidays();
            }

            @Override
            public void onFailure(String errorMessage) {
                List<Holiday> localHolidays = holidayRepository.getHolidaysForMonth(year, month);
                holidays.postValue(localHolidays);

                reloadDaysWithHolidays();

                MainViewModel.this.errorMessage.postValue("Не удалось загрузить праздники из сети, используются локальные данные");
            }
        });
    }

    private void reloadDaysWithHolidays() {
        List<DayItem> currentDays = calendarDays.getValue();
        if (currentDays != null) {
            linkHolidaysToDays(currentDays);
            calendarDays.postValue(currentDays);
        }
    }

    private List<Holiday> filterHolidaysByMonth(List<Holiday> allHolidays, int year, int month) {
        List<Holiday> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Holiday holiday : allHolidays) {
            calendar.setTime(holiday.getDate());
            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
                result.add(holiday);
            }
        }

        return result;
    }

    public void createEvent(String title, Date date, int startHour, int startMinute,
                            int endHour, int endMinute, boolean notify, int groupId,
                            String description) {
        try {
            System.out.println("DEBUG: Создание события: " + title +
                    " дата: " + date +
                    " время: " + startHour + ":" + startMinute + " - " + endHour + ":" + endMinute);

            Event event = eventRepository.createEvent(
                    title, date, startHour, startMinute, endHour, endMinute,
                    notify, groupId, description
            );

            System.out.println("DEBUG: Событие создано с ID: " + event.getId());

            refreshData();

            if (selectedDay != null) {
                Date normalizedNewEventDate = normalizeDate(date);
                Date normalizedSelectedDate = normalizeDate(selectedDay.getDate());

                if (normalizedNewEventDate.equals(normalizedSelectedDate)) {
                    System.out.println("DEBUG: Событие создано на выбранную дату, обновляем список");
                    loadEventsForSelectedDate(selectedDay.getDate());
                }
            }

            navigateToDate.postValue(date);

        } catch (Exception e) {
            System.out.println("DEBUG: Ошибка создания события: " + e.getMessage());
            errorMessage.postValue("Ошибка создания события: " + e.getMessage());
        }
    }

    public void jumpToDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        currentCalendar = cal;
        loadCurrentMonth();

        for (DayItem d : calendarDays.getValue()) {
            if (d.isCurrentMonth() && isSameDay(d.getDate(), date)) {
                selectDay(d);
                break;
            }
        }
    }
}