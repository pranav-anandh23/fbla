package com.example.fbla;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {

    private static final List<Event> events = new ArrayList<>();

    public static List<Event> getAllEvents() {
        return events;
    }

    public static void addEvent(Event event) {
        events.add(event);
    }

    public static void removeEvent(Event event) {
        events.remove(event);
    }
}
