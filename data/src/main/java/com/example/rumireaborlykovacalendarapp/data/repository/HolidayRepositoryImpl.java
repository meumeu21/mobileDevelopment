package com.example.rumireaborlykovacalendarapp.data.repository;

import com.example.rumireaborlykovacalendarapp.data.remote.HolidayApi;
import com.example.rumireaborlykovacalendarapp.data.remote.models.HolidayResponse;
import com.example.rumireaborlykovacalendarapp.domain.models.Holiday;
import com.example.rumireaborlykovacalendarapp.domain.repository.HolidayRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HolidayRepositoryImpl implements HolidayRepository {

    private final HolidayApi holidayApi;
    private final SimpleDateFormat dateFormat;
    private final Map<Integer, List<Holiday>> holidaysCache = new HashMap<>();
    private boolean isLoaded = false;

    private final String API_KEY = "HFQbgepsQq3PKvqFoBW7fLbyN6nntpJg!";
    private final String COUNTRY = "RU";

    public HolidayRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendarific.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        holidayApi = retrofit.create(HolidayApi.class);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Override
    public void getHolidays(int year, HolidayCallback callback) {
        if (holidaysCache.containsKey(year)) {
            callback.onSuccess(holidaysCache.get(year));
            return;
        }

        holidayApi.getHolidays(API_KEY, COUNTRY, year, "national").enqueue(new Callback<HolidayResponse>() {
            @Override
            public void onResponse(Call<HolidayResponse> call, Response<HolidayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Holiday> holidays = convertToHolidays(response.body());
                    holidaysCache.put(year, holidays);
                    isLoaded = true;
                    System.out.println("DEBUG: Успешно загружено праздников из Calendarific для " + year + ": " + holidays.size());
                    callback.onSuccess(holidays);
                } else {
                    System.out.println("DEBUG: Ошибка Calendarific API для " + year + ": " + response.message());
                    List<Holiday> localHolidays = generateRussianHolidaysForYear(year);
                    holidaysCache.put(year, localHolidays);
                    isLoaded = true;
                    callback.onSuccess(localHolidays);
                }
            }

            @Override
            public void onFailure(Call<HolidayResponse> call, Throwable t) {
                System.out.println("DEBUG: Ошибка сети Calendarific для " + year + ": " + t.getMessage());
                List<Holiday> localHolidays = generateRussianHolidaysForYear(year);
                holidaysCache.put(year, localHolidays);
                isLoaded = true;
                callback.onSuccess(localHolidays);
            }
        });
    }

    @Override
    public List<Holiday> getHolidaysForMonth(int year, int month) {
        System.out.println("DEBUG: Запрос праздников для " + year + "-" + (month + 1));

        List<Holiday> allHolidays = getAllHolidaysForYear(year);
        List<Holiday> monthHolidays = filterHolidaysByMonth(allHolidays, year, month);

        System.out.println("DEBUG: Всего праздников в году " + year + ": " + allHolidays.size());
        System.out.println("DEBUG: Праздников в месяце " + (month + 1) + ": " + monthHolidays.size());

        return monthHolidays;
    }

    @Override
    public List<Holiday> getAllHolidays() {
        List<Holiday> allHolidays = new ArrayList<>();
        for (List<Holiday> yearHolidays : holidaysCache.values()) {
            allHolidays.addAll(yearHolidays);
        }
        return allHolidays;
    }

    @Override
    public boolean isHolidaysLoaded() {
        return isLoaded;
    }

    private List<Holiday> getAllHolidaysForYear(int year) {
        if (holidaysCache.containsKey(year)) {
            return holidaysCache.get(year);
        }
        return generateRussianHolidaysForYear(year);
    }

    private List<Holiday> convertToHolidays(HolidayResponse apiResponse) {
        List<Holiday> holidays = new ArrayList<>();

        if (apiResponse.getResponse() == null || apiResponse.getResponse().getHolidays() == null) {
            return holidays;
        }

        for (HolidayResponse.Holiday apiHoliday : apiResponse.getResponse().getHolidays()) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(apiHoliday.getDate().getDatetime().getYear(),
                        apiHoliday.getDate().getDatetime().getMonth() - 1,
                        apiHoliday.getDate().getDatetime().getDay());

                Date date = calendar.getTime();

                boolean isDayOff = isDayOff(apiHoliday.getTypes());

                holidays.add(new Holiday(apiHoliday.getName(), date, isDayOff));

            } catch (Exception e) {
                System.out.println("DEBUG: Ошибка конвертации праздника: " + e.getMessage());
            }
        }

        return holidays;
    }

    private boolean isDayOff(List<String> types) {
        if (types == null || types.isEmpty()) {
            return false;
        }

        for (String type : types) {
            if ("National".equalsIgnoreCase(type) ||
                    "Federal".equalsIgnoreCase(type) ||
                    "Public".equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    private List<Holiday> generateRussianHolidaysForYear(int year) {
        List<Holiday> holidays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        addHoliday(holidays, calendar, year, Calendar.JANUARY, 1, "Новый год", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 2, "Новогодние каникулы", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 3, "Новогодние каникулы", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 4, "Новогодние каникулы", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 5, "Новогодние каникулы", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 6, "Новогодние каникулы", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 7, "Рождество Христово", true);
        addHoliday(holidays, calendar, year, Calendar.JANUARY, 8, "Новогодние каникулы", true);

        addHoliday(holidays, calendar, year, Calendar.FEBRUARY, 23, "День защитника Отечества", true);
        addHoliday(holidays, calendar, year, Calendar.MARCH, 8, "Международный женский день", true);
        addHoliday(holidays, calendar, year, Calendar.MAY, 1, "Праздник Весны и Труда", true);
        addHoliday(holidays, calendar, year, Calendar.MAY, 9, "День Победы", true);
        addHoliday(holidays, calendar, year, Calendar.JUNE, 12, "День России", true);
        addHoliday(holidays, calendar, year, Calendar.NOVEMBER, 4, "День народного единства", true);

        return holidays;
    }

    private void addHoliday(List<Holiday> holidays, Calendar calendar, int year, int month, int day, String name, boolean isDayOff) {
        calendar.set(year, month, day);
        holidays.add(new Holiday(name, calendar.getTime(), isDayOff));
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
}