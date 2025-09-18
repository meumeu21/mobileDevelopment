package com.example.rumireaborlykovacalendarapp.presentation;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.data.repository.FakeEventRepository;
import com.example.rumireaborlykovacalendarapp.data.repository.FakeGroupRepository;
import com.example.rumireaborlykovacalendarapp.data.repository.FakeUserRepository;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.example.rumireaborlykovacalendarapp.domain.models.Group;
import com.example.rumireaborlykovacalendarapp.domain.models.User;
import com.example.rumireaborlykovacalendarapp.domain.repository.EventRepository;
import com.example.rumireaborlykovacalendarapp.domain.repository.GroupRepository;
import com.example.rumireaborlykovacalendarapp.domain.repository.UserRepository;
import com.example.rumireaborlykovacalendarapp.domain.usecases.CreateEvent;
import com.example.rumireaborlykovacalendarapp.domain.usecases.CreateGroup;
import com.example.rumireaborlykovacalendarapp.domain.usecases.DeleteEvent;
import com.example.rumireaborlykovacalendarapp.domain.usecases.DeleteGroup;
import com.example.rumireaborlykovacalendarapp.domain.usecases.GetEventsForDate;
import com.example.rumireaborlykovacalendarapp.domain.usecases.GetGroups;
import com.example.rumireaborlykovacalendarapp.domain.usecases.GetSpecificEvent;
import com.example.rumireaborlykovacalendarapp.domain.usecases.GetUpcomingEvents;
import com.example.rumireaborlykovacalendarapp.domain.usecases.GetUserProfile;
import com.example.rumireaborlykovacalendarapp.domain.usecases.LoginUser;
import com.example.rumireaborlykovacalendarapp.domain.usecases.Logout;
import com.example.rumireaborlykovacalendarapp.domain.usecases.RegisterUser;
import com.example.rumireaborlykovacalendarapp.domain.usecases.RestorePassword;
import com.example.rumireaborlykovacalendarapp.domain.usecases.UpdateEvent;
import com.example.rumireaborlykovacalendarapp.domain.usecases.UpdateUserProfile;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PlannerDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserRepository userRepo = new FakeUserRepository();
        EventRepository eventRepo = new FakeEventRepository();
        GroupRepository groupRepo = new FakeGroupRepository();

        // Аутентификация
        LoginUser loginUser = new LoginUser(userRepo);
        User loggedInUser = loginUser.execute("test@example.com", "1234");
        Log.d(TAG, "Login: " + (loggedInUser != null ? loggedInUser.getName() : "неудача"));

        RegisterUser registerUser = new RegisterUser(userRepo);
        User newUser = registerUser.execute("Аня", "anya@example.com", "pass");
        Log.d(TAG, "Register: " + newUser.getName());

        RestorePassword restorePassword = new RestorePassword(userRepo);
        String restoreMessage = restorePassword.execute("anya@example.com");
        Log.d(TAG, restoreMessage);

        // Календарь
        GetEventsForDate getEventsForDate = new GetEventsForDate(eventRepo);
        List<Event> todayEvents = getEventsForDate.execute(new Date());
        Log.d(TAG, "Сегодняшние события: " + todayEvents.size());

        GetUpcomingEvents getUpcomingEvents = new GetUpcomingEvents(eventRepo);
        Log.d(TAG, "Все события: " + getUpcomingEvents.execute().size());

        GetSpecificEvent getSpecificEvent = new GetSpecificEvent(eventRepo);
        Event event = getSpecificEvent.execute("1");
        Log.d(TAG, "Событие ID=1: " + (event != null ? event.getTitle() : "не найдено"));

        // Управление событиями
        CreateEvent createEvent = new CreateEvent(eventRepo);
        Event newEvent = createEvent.execute("Поход в кино", new Date());
        Log.d(TAG, "Создано событие: " + newEvent.getTitle());

        UpdateEvent updateEvent = new UpdateEvent(eventRepo);
        newEvent.setTitle("Поход в театр");
        updateEvent.execute(newEvent);
        Log.d(TAG, "Обновлено событие: " + newEvent.getTitle());

        DeleteEvent deleteEvent = new DeleteEvent(eventRepo);
        deleteEvent.execute(newEvent.getId());
        Log.d(TAG, "Удалено событие ID=" + newEvent.getId());

        // Группы событий
        GetGroups getGroups = new GetGroups(groupRepo);
        List<Group> groups = getGroups.execute();
        Log.d(TAG, "Группы: " + groups.size());

        CreateGroup createGroup = new CreateGroup(groupRepo);
        Group newGroup = createGroup.execute("Хобби");
        Log.d(TAG, "Добавлена группа: " + newGroup.getName());

        DeleteGroup deleteGroup = new DeleteGroup(groupRepo);
        deleteGroup.execute(newGroup.getId());
        Log.d(TAG, "Удалена группа: " + newGroup.getName());

        // Профиль
        GetUserProfile getUserProfile = new GetUserProfile(userRepo);
        User profile = getUserProfile.execute();
        Log.d(TAG, "Профиль: " + profile.getName());

        UpdateUserProfile updateUserProfile = new UpdateUserProfile(userRepo);
        profile.setName("Анна Обновлённая");
        User updatedProfile = updateUserProfile.execute(profile);
        Log.d(TAG, "Обновлено имя: " + updatedProfile.getName());

        Logout logout = new Logout(userRepo);
        logout.execute();
        Log.d(TAG, "Пользователь вышел из системы");
    }
}
