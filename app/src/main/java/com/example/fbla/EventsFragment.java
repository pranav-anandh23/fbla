package com.example.fbla;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {


    // Class Parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Calendar cal2;
    private String mParam1;
    private String mParam2;

    public EventsFragment() {
        // Required empty public constructor
    }
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Variables
    private long selectedDateMillis = System.currentTimeMillis();

    // Event Box Instances/Variables
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    CalendarView calendarView;
    private final List<Event> events = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update Recycler View
        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventAdapter(events, new EventAdapter.EventActionListener() {
            @Override
            public void onEdit(int position) {
                Event event = events.get(position);

                Bundle args = new Bundle();
                args.putInt("edit_position", position);
                args.putParcelable("event", event);

                EventCreateFragment fragment = new EventCreateFragment();
                fragment.setArguments(args);

                fragment.setEventCreateListener(new EventCreateFragment.EventCreateListener() {
                    @Override
                    public void onEventCreated(Event event) {
                        // not used in edit mode
                    }

                    @Override
                    public void onEventEdited(int position, Event event) {
                        EventRepository.getAllEvents().set(position, event);
                        events.set(position, event);
                        events.sort(Comparator.comparingLong(e -> e.dateMillis));
                        adapter.notifyDataSetChanged();

                    }
                });
                showDim();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_up,0,  0, R.anim.slide_out_down
                        )
                        .add(R.id.overlay_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDelete(int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Delete", (d, w) -> {
                            EventRepository.removeEvent(events.get(position));
                            events.remove(position);

                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }, true);

        recyclerView.setAdapter(adapter);


        // Update Calendar
        /* TextView eventsHeader = view.findViewById(R.id.eventsHeader2);
        Calendar cal = Calendar.getInstance();
        String currentMonth = cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
        );
        eventsHeader.setText("Events in " + currentMonth);*/
        TextView eventsHeader = view.findViewById(R.id.eventsHeader2);
        eventsHeader.setText("All Events:");

        // Calendar Month Listener
        calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((cV, year, month, day) -> {
            cal2 = Calendar.getInstance();
            cal2.set(year, month, 1, 0, 0, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            //String currentMonth2 =
            //eventsHeader.setText("Events in " + currentMonth2);
        });

        // Event Button
        Button btnAdd = view.findViewById(R.id.button2);
        btnAdd.setOnClickListener(v -> {
            //String selectedDate = getSelectedCalendarDate();
            showDim();

            EventCreateFragment fragment = new EventCreateFragment();
            fragment.setEventCreateListener(new EventCreateFragment.EventCreateListener() {
                @Override
                public void onEventCreated(Event event) {
                    events.add(event);
                    EventRepository.addEvent(event);
                    events.sort(Comparator.comparingLong(e -> e.dateMillis));
                    adapter.notifyDataSetChanged();
                    Log.d("EVENT CREATED", event.title + " | " + event.description + " | " + event.date + " | " + event.time + " | " + event.location);
                }

                @Override
                public void onEventEdited(int position, Event event) {
                    // Not used in create mode
                }
            });


            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_up,0,  0, R.anim.slide_out_down
                    )

                    .add(R.id.overlay_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);

    }

    /// METHODS -------------------------------------
    private void showDim() {
        View dim = requireActivity().findViewById(R.id.dimView);
        dim.setAlpha(0f);
        dim.setVisibility(View.VISIBLE);
        dim.animate()
                .alpha(1f)
                .setDuration(200)
                .start();
    }

    private void updateEventsForMonth(long monthStart, long monthEnd) {
        events.clear();

        for (Event e : EventRepository.getAllEvents()) {
            if (e.dateMillis >= monthStart && e.dateMillis <= monthEnd) {
                events.add(e);
            }
        }

        events.sort(Comparator.comparingLong(ev -> ev.dateMillis));
        adapter.notifyDataSetChanged();
    }

}