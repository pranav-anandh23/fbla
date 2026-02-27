package com.example.fbla;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> events;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load persisted events
        events = EventStorage.loadEvents(requireContext());

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
                    public void onEventCreated(Event e) {
                        // not used
                    }

                    @Override
                    public void onEventEdited(int pos, Event edited) {
                        events.set(pos, edited);
                        sortAndSave();
                    }
                });

                showDim();
                openOverlay(fragment);
            }

            @Override
            public void onDelete(int position) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Delete", (d, w) -> {
                            events.remove(position);
                            sortAndSave();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Header month
        TextView header = view.findViewById(R.id.eventsHeader2);
        Calendar cal = Calendar.getInstance();
        String month = cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
        );
        header.setText("Events in " + month);

        // Add button
        Button addBtn = view.findViewById(R.id.button2);
        addBtn.setOnClickListener(v -> {
            EventCreateFragment fragment = new EventCreateFragment();

            fragment.setEventCreateListener(new EventCreateFragment.EventCreateListener() {
                @Override
                public void onEventCreated(Event event) {
                    events.add(event);
                    sortAndSave();
                }

                @Override
                public void onEventEdited(int position, Event event) {
                    // not used
                }
            });

            showDim();
            openOverlay(fragment);
        });
    }

    /* ---------------- HELPERS ---------------- */

    private void sortAndSave() {
        events.sort(Comparator.comparingLong(e -> e.dateMillis));
        adapter.notifyDataSetChanged();
        EventStorage.saveEvents(requireContext(), events);
    }

    private void openOverlay(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_up, 0,
                        0, R.anim.slide_out_down
                )
                .add(R.id.overlay_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showDim() {
        View dim = requireActivity().findViewById(R.id.dimView);
        if (dim != null) {
            dim.setAlpha(0f);
            dim.setVisibility(View.VISIBLE);
            dim.animate().alpha(1f).setDuration(200).start();
        }
    }
}
