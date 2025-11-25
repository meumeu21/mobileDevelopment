package com.example.rumireaborlykovacalendarapp.data.remote;

import com.example.rumireaborlykovacalendarapp.data.remote.models.HolidayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HolidayApi {

    @GET("v2/holidays")
    Call<HolidayResponse> getHolidays(
            @Query("api_key") String apiKey,
            @Query("country") String country,
            @Query("year") int year,
            @Query("type") String type
    );
}