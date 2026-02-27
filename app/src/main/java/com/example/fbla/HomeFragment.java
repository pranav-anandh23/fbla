package com.example.fbla;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView welcomeText, chapterText;
    private RecyclerView upcomingRecycler;
    private EventAdapter adapter;
    private List<Event> upcomingEvents = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeText = view.findViewById(R.id.welcomeText);
        chapterText = view.findViewById(R.id.chapterText);
        upcomingRecycler = view.findViewById(R.id.upcomingRecycler);

        UserSessionManager session = new UserSessionManager(requireContext());

        // ✅ First name only
        String firstName = session.getName().split(" ")[0];
        welcomeText.setText("Welcome back, " + firstName + "!");
        chapterText.setText(session.getChapter());

        adapter = new EventAdapter(upcomingEvents, null);
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingRecycler.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUpcomingEvents();
    }

    private void loadUpcomingEvents() {
        upcomingEvents.clear();

        long now = System.currentTimeMillis();
        List<Event> allEvents = EventStorage.loadEvents(requireContext());

        for (Event e : allEvents) {
            if (e.dateMillis >= now) {
                upcomingEvents.add(e);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
