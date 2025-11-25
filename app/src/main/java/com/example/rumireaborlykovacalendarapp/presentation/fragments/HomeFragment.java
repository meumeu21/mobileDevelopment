package com.example.rumireaborlykovacalendarapp.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.domain.models.DayItem;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.presentation.adapters.CalendarAdapter;
import com.example.rumireaborlykovacalendarapp.presentation.adapters.EventsAdapter;
import com.example.rumireaborlykovacalendarapp.presentation.viewmodel.MainViewModel;
import com.example.rumireaborlykovacalendarapp.presentation.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView calendarRecyclerView, eventsRecyclerView;
    private TextView tvCurrentMonth, tvSelectedDate, tvNoEvents;
    private ImageButton btnPrevMonth, btnNextMonth, btnToday;
    private CalendarAdapter calendarAdapter;
    private EventsAdapter eventsAdapter;
    private NestedScrollView eventsPanel;
    private View dragHandle;
    private LinearLayout weekDaysHeader, eventsPanelHeader, calendarContainer;

    private float initialY;
    private boolean isDragging = false;

    private float calendarWeight = 3f;
    private float eventsWeight = 1f;
    private final float MIN_CALENDAR_WEIGHT = 0.8f;
    private final float MAX_CALENDAR_WEIGHT = 4f;
    private final float MIN_EVENTS_WEIGHT = 0.5f;
    private final float MAX_EVENTS_WEIGHT = 3f;

    private final float SENSITIVITY = 0.004f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String userId = getArguments() != null ? getArguments().getString("USER_ID") : null;
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(getContext(), "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
            return view;
        }

        ViewModelFactory factory = new ViewModelFactory(requireContext(), userId);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        initViews(view);
        setupObservers();
        setupClickListeners();
        setupDragHandle();

        return view;
    }

    private void initViews(View view) {
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);
        btnToday = view.findViewById(R.id.btnToday);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvNoEvents = view.findViewById(R.id.tvNoEvents);
        eventsPanel = view.findViewById(R.id.events_panel);
        dragHandle = view.findViewById(R.id.drag_handle);
        weekDaysHeader = view.findViewById(R.id.weekDaysHeader);
        eventsPanelHeader = view.findViewById(R.id.events_panel_header);
        calendarContainer = view.findViewById(R.id.calendar_container);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(gridLayoutManager);
        calendarAdapter = new CalendarAdapter(null);
        calendarRecyclerView.setAdapter(calendarAdapter);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsAdapter = new EventsAdapter(null);
        eventsRecyclerView.setAdapter(eventsAdapter);
    }

    private void setupObservers() {
        mainViewModel.getCalendarDays().observe(getViewLifecycleOwner(), days -> {
            System.out.println("DEBUG: Observer получил дней: " + (days != null ? days.size() : "null"));
            if (days != null && !days.isEmpty()) {
                calendarAdapter.setDays(days);
                if (days.size() > 0) {
                    System.out.println("DEBUG: Первый день в адаптере: " + days.get(0).getDayOfMonth() +
                            ", текущий месяц: " + days.get(0).isCurrentMonth());
                }
            }
        });

        mainViewModel.getCurrentMonth().observe(getViewLifecycleOwner(), month -> {
            System.out.println("DEBUG: Текущий месяц в UI: " + month);
            if (month != null) {
                tvCurrentMonth.setText(month);
            }
        });

        mainViewModel.getSelectedDayEvents().observe(getViewLifecycleOwner(), events -> {
            System.out.println("DEBUG: HomeFragment получил события: " + (events != null ? events.size() : 0));
            if (events != null) {
                for (Event event : events) {
                    System.out.println("DEBUG: Событие в списке: " + event.getTitle() + " ID: " + event.getId());
                }
            }
            updateEventsPanel(events);
        });

        mainViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        mainViewModel.getNavigateToDate().observe(getViewLifecycleOwner(), date -> {
            if (date != null) {
                mainViewModel.jumpToDate(date);
            }
        });
    }

    private void setupClickListeners() {
        btnPrevMonth.setOnClickListener(v -> mainViewModel.previousMonth());
        btnNextMonth.setOnClickListener(v -> mainViewModel.nextMonth());
        btnToday.setOnClickListener(v -> mainViewModel.goToToday());

        calendarAdapter.setOnDayClickListener(day -> {
            if (day.isCurrentMonth()) {
                mainViewModel.selectDay(day);
                updateSelectedDate(day);
            }
        });
    }

    private void setupDragHandle() {
        eventsPanelHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        isDragging = true;
                        eventsPanelHeader.setPressed(true);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float deltaY = initialY - event.getRawY();

                            float weightChange = deltaY * SENSITIVITY;

                            calendarWeight = Math.max(MIN_CALENDAR_WEIGHT,
                                    Math.min(MAX_CALENDAR_WEIGHT, calendarWeight - weightChange));
                            eventsWeight = Math.max(MIN_EVENTS_WEIGHT,
                                    Math.min(MAX_EVENTS_WEIGHT, eventsWeight + weightChange));

                            updateLayoutWeights();

                            initialY = event.getRawY();
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        eventsPanelHeader.setPressed(false);
                        return true;
                }
                return false;
            }
        });
    }

    private void updateLayoutWeights() {
        LinearLayout.LayoutParams calendarParams = (LinearLayout.LayoutParams) calendarContainer.getLayoutParams();
        calendarParams.weight = calendarWeight;
        calendarContainer.setLayoutParams(calendarParams);

        LinearLayout.LayoutParams eventsParams = (LinearLayout.LayoutParams) eventsPanel.getLayoutParams();
        eventsParams.weight = eventsWeight;
        eventsPanel.setLayoutParams(eventsParams);

        calendarContainer.requestLayout();
        eventsPanel.requestLayout();
    }

    private void updateSelectedDate(DayItem day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM", new Locale("ru"));
        String dateString = dateFormat.format(day.getDate());
        tvSelectedDate.setText("События на " + dateString);
    }

    private void updateEventsPanel(List<Event> events) {
        System.out.println("DEBUG: updateEventsPanel вызван с " + (events != null ? events.size() : "null") + " событиями");

        if (events == null || events.isEmpty()) {
            tvNoEvents.setVisibility(View.VISIBLE);
            eventsRecyclerView.setVisibility(View.GONE);
            System.out.println("DEBUG: Нет событий для отображения");
        } else {
            tvNoEvents.setVisibility(View.GONE);
            eventsRecyclerView.setVisibility(View.VISIBLE);
            eventsAdapter.setEvents(events);
            System.out.println("DEBUG: Отображено " + events.size() + " событий");

            for (Event event : events) {
                System.out.println("DEBUG: Событие в адаптере: " + event.getTitle() +
                        " ID: " + event.getId() + " Дата: " + event.getDate());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null) {
            mainViewModel.refreshData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventsPanelHeader != null) {
            eventsPanelHeader.setOnTouchListener(null);
        }
    }
}