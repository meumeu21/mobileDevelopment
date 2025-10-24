package com.example.rumireaborlykovacalendarapp.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final EventRepository eventRepository;

    private final MutableLiveData<List<Event>> localEvents = new MutableLiveData<>();
    private final MutableLiveData<List<Event>> networkEvents = new MutableLiveData<>();

    private final MediatorLiveData<List<Event>> combinedEvents = new MediatorLiveData<>();

    public MainViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        Log.d("MainViewModel", "MainViewModel created");

        loadLocalEvents();
        loadNetworkEvents();

        combinedEvents.addSource(localEvents, local -> combineData(local, networkEvents.getValue()));
        combinedEvents.addSource(networkEvents, network -> combineData(localEvents.getValue(), network));
    }

    private void loadLocalEvents() {
        try {
            List<Event> events = eventRepository.getEvents();
            localEvents.setValue(events);
            Log.d("MainViewModel", "Loaded local events: " + events.size());
        } catch (Exception e) {
            Log.e("MainViewModel", "Error loading local events", e);
        }
    }

    private void loadNetworkEvents() {
        List<Event> mockNetworkEvents = new ArrayList<>();
        mockNetworkEvents.add(new Event(101, "Online meeting", new Date(), "Remote sync with team"));
        mockNetworkEvents.add(new Event(102, "Conference stream", new Date(), "Streaming tech talk online"));
        networkEvents.setValue(mockNetworkEvents);
        Log.d("MainViewModel", "Loaded mock network events: " + mockNetworkEvents.size());
    }

    private void combineData(List<Event> local, List<Event> network) {
        List<Event> result = new ArrayList<>();
        if (local != null) result.addAll(local);
        if (network != null) result.addAll(network);
        combinedEvents.setValue(result);
    }

    public LiveData<List<Event>> getCombinedEvents() {
        return combinedEvents;
    }

    @Override
    protected void onCleared() {
        Log.d("MainViewModel", "MainViewModel cleared");
        super.onCleared();
    }
}
