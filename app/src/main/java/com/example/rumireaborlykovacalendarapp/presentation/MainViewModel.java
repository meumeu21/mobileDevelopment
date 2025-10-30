package com.example.rumireaborlykovacalendarapp.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final EventRepository eventRepository;
    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MainViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        Log.d("MainViewModel", "MainViewModel created");
        loadEvents();
    }

    public void loadEvents() {
        isLoading.setValue(true);

        try {
            List<Event> events = eventRepository.getEvents();
            eventsLiveData.setValue(events);
            Log.d("MainViewModel", "Loaded events: " + events.size());
            errorMessage.setValue(null);
        } catch (Exception e) {
            Log.e("MainViewModel", "Error loading events", e);
            errorMessage.setValue("Ошибка загрузки событий");
            eventsLiveData.setValue(null);
        } finally {
            isLoading.setValue(false);
        }
    }

    public LiveData<List<Event>> getEvents() {
        return eventsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void refreshEvents() {
        loadEvents();
    }

    @Override
    protected void onCleared() {
        Log.d("MainViewModel", "MainViewModel cleared");
        super.onCleared();
    }
}