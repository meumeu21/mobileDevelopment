package com.example.rumireaborlykovacalendarapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesDataSource {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private final SharedPreferences prefs;

    public SharedPreferencesDataSource(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(String name, String email) {
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_NAME, "Неизвестный пользователь");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
