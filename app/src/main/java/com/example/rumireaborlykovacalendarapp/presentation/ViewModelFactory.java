package com.example.rumireaborlykovacalendarapp.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumireaborlykovacalendarapp.data.repository.EventRepositoryImpl;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            EventRepository eventRepository = new EventRepositoryImpl(context);
            return (T) new MainViewModel(eventRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
