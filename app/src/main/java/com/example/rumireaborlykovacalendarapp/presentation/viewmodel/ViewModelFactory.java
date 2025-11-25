package com.example.rumireaborlykovacalendarapp.presentation.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumireaborlykovacalendarapp.data.repository.EventRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.data.repository.GroupRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.data.repository.HolidayRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.domain.repository.HolidayRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private final String userId;

    public ViewModelFactory(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            int numericUserId = convertUserIdToInt(userId);

            EventRepositoryImpl eventRepository = new EventRepositoryImpl(context, numericUserId);
            GroupRepositoryImpl groupRepository = new GroupRepositoryImpl(context, numericUserId);
            HolidayRepository holidayRepository = new HolidayRepositoryImpl();

            return (T) new MainViewModel(eventRepository, groupRepository, holidayRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

    private int convertUserIdToInt(String userId) {
        return userId.hashCode();
    }
}