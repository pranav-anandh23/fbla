package com.example.fbla;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {

    private static final String PREF_NAME = "fbla_events_pref";
    private static final String KEY_EVENTS = "events_list";

    private static final List<Event> events = new ArrayList<>();
    private static boolean isLoaded = false;

    private static void ensureLoaded(Context context) {
        if (isLoaded) return;

        events.clear();

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_EVENTS, "[]");

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Event event = new Event(
                        obj.optString("title"),
                        obj.optString("description"),
                        obj.optString("date"),
                        obj.optString("time"),
                        obj.optString("location"),
                        obj.optString("color"),
                        obj.optString("dotColor"),
                        obj.optLong("dateMillis")
                );

                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        isLoaded = true;
    }

    private static void saveEvents(Context context) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Event event : events) {
                JSONObject obj = new JSONObject();
                obj.put("title", event.title);
                obj.put("description", event.description);
                obj.put("date", event.date);
                obj.put("time", event.time);
                obj.put("location", event.location);
                obj.put("color", event.color);
                obj.put("dotColor", event.dotColor);
                obj.put("dateMillis", event.dateMillis);
                jsonArray.put(obj);
            }

            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_EVENTS, jsonArray.toString()).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Event> getAllEvents(Context context) {
        ensureLoaded(context);
        return events;
    }

    public static void addEvent(Context context, Event event) {
        ensureLoaded(context);
        events.add(event);
        saveEvents(context);
    }

    public static void removeEvent(Context context, Event event) {
        ensureLoaded(context);
        events.remove(event);
        saveEvents(context);
    }

    public static void updateEvent(Context context, Event oldEvent, Event newEvent) {
        ensureLoaded(context);
        int index = events.indexOf(oldEvent);
        if (index != -1) {
            events.set(index, newEvent);
            saveEvents(context);
        }
    }
}