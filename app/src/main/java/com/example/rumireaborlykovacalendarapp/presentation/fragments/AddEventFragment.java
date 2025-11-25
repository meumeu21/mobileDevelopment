package com.example.rumireaborlykovacalendarapp.presentation.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.presentation.viewmodel.MainViewModel;
import com.example.rumireaborlykovacalendarapp.presentation.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    private EditText etEventTitle, etEventDescription;
    private Button btnAddEvent, btnSelectDate, btnStartTime, btnEndTime;
    private CheckBox cbNotify;
    private Spinner spinnerGroup;
    private TextView tvSelectedDate, tvStartTime, tvEndTime;

    private MainViewModel mainViewModel;
    private String userId;

    private Calendar selectedDate;
    private int startHour = 9;
    private int startMinute = 0;
    private int endHour = 10;
    private int endMinute = 0;

    private List<Group> groups = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        userId = getArguments() != null ? getArguments().getString("USER_ID") : null;

        if (userId != null && !userId.isEmpty()) {
            ViewModelFactory factory = new ViewModelFactory(requireContext(), userId);
            mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        }

        initViews(view);
        setupClickListeners();
        setupObservers();

        setDefaultDate();

        return view;
    }

    private void initViews(View view) {
        etEventTitle = view.findViewById(R.id.etEventTitle);
        etEventDescription = view.findViewById(R.id.etEventDescription);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnStartTime = view.findViewById(R.id.btnStartTime);
        btnEndTime = view.findViewById(R.id.btnEndTime);
        cbNotify = view.findViewById(R.id.cbNotify);
        spinnerGroup = view.findViewById(R.id.spinnerGroup);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndTime = view.findViewById(R.id.tvEndTime);

        if (userId == null || userId.isEmpty()) {
            disableForm();
        }
    }

    private void setupObservers() {
        if (mainViewModel != null) {
            mainViewModel.getGroups().observe(getViewLifecycleOwner(), groupsList -> {
                if (groupsList != null) {
                    groups = groupsList;
                    setupGroupSpinner();
                }
            });
        }
    }

    private void setupGroupSpinner() {
        List<String> groupNames = new ArrayList<>();
        groupNames.add("Без группы");

        for (Group group : groups) {
            groupNames.add(group.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);
    }

    private void setDefaultDate() {
        selectedDate = Calendar.getInstance();
        updateDateDisplay();
        updateTimeDisplay();
    }

    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());

        btnStartTime.setOnClickListener(v -> showTimePickerDialog(true));

        btnEndTime.setOnClickListener(v -> showTimePickerDialog(false));

        btnAddEvent.setOnClickListener(v -> addEvent());
    }

    private void showDatePickerDialog() {
        Calendar calendar = selectedDate != null ? selectedDate : Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (selectedDate == null) {
                            selectedDate = Calendar.getInstance();
                        }
                        selectedDate.set(year, month, dayOfMonth);
                        updateDateDisplay();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = isStartTime ? startHour : endHour;
        int currentMinute = isStartTime ? startMinute : endMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isStartTime) {
                            startHour = hourOfDay;
                            startMinute = minute;
                            endHour = (hourOfDay + 1) % 24;
                            endMinute = minute;
                            updateTimeDisplay();
                        } else {
                            endHour = hourOfDay;
                            endMinute = minute;
                            updateTimeDisplay();
                        }
                    }
                },
                currentHour,
                currentMinute,
                true
        );

        timePickerDialog.show();
    }

    private void updateDateDisplay() {
        if (selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()));
        }
    }

    private void updateTimeDisplay() {
        tvStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
        tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute));
    }

    private void addEvent() {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(getContext(), "Войдите в аккаунт чтобы добавить событие", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etEventTitle.getText().toString().trim();
        String description = etEventDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Введите название события", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(getContext(), "Выберите дату события", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endHour < startHour || (endHour == startHour && endMinute <= startMinute)) {
            Toast.makeText(getContext(), "Время окончания должно быть позже времени начала", Toast.LENGTH_SHORT).show();
            return;
        }

        int groupId = -1;
        int selectedPosition = spinnerGroup.getSelectedItemPosition();
        if (selectedPosition > 0 && selectedPosition - 1 < groups.size()) {
            groupId = groups.get(selectedPosition - 1).getId();
        }

        boolean notify = cbNotify.isChecked();
        Date eventDate = selectedDate.getTime();

        if (mainViewModel != null) {
            mainViewModel.createEvent(
                    title, eventDate, startHour, startMinute, endHour, endMinute,
                    notify, groupId, description
            );

            Toast.makeText(getContext(), "Событие успешно создано!", Toast.LENGTH_SHORT).show();
            clearForm();

            if (getActivity() != null) {
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        }
    }

    private void clearForm() {
        etEventTitle.setText("");
        etEventDescription.setText("");
        setDefaultDate();
        spinnerGroup.setSelection(0);
        cbNotify.setChecked(true);
    }

    private void disableForm() {
        btnAddEvent.setEnabled(false);
        btnSelectDate.setEnabled(false);
        btnStartTime.setEnabled(false);
        btnEndTime.setEnabled(false);
        cbNotify.setEnabled(false);
        spinnerGroup.setEnabled(false);

        etEventTitle.setHint("Войдите в аккаунт чтобы добавить событие");
        etEventTitle.setEnabled(false);
        etEventDescription.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainViewModel != null) {
            mainViewModel.refreshData();
        }
    }
}