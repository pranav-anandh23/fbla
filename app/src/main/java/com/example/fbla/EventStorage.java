package com.example.fbla;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventStorage {

    private static final String PREF_NAME = "events_pref";
    private static final String KEY_EVENTS = "events";

    public static void saveEvents(Context context, List<Event> events) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_EVENTS, new Gson().toJson(events));
        editor.apply();
    }

    public static List<Event> loadEvents(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_EVENTS, null);
        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<List<Event>>(){}.getType();
        return new Gson().fromJson(json, type);
    }
}
