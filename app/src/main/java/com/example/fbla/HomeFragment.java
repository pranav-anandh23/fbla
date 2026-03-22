package com.example.fbla;


import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String name, chapterName;
    private List<Event> allEvents;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        refreshHome();

        // View All Events
        TextView tvViewAll = view.findViewById(R.id.tvViewAll);
        tvViewAll.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EventsFragment())
                    .commit();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void refreshHome() {
        LinearLayout upcomingContainer = requireView().findViewById(R.id.upcomingContainer);
        upcomingContainer.removeAllViews();

        allEvents = EventRepository.getAllEvents();
        long now = System.currentTimeMillis();

        List<Event> upcoming = new ArrayList<>();
        for (Event e : allEvents) {
            if (e.dateMillis >= now) {
                upcoming.add(e);
            }
        }

        upcoming.sort(Comparator.comparingLong(e -> e.dateMillis));
        if (upcoming.size() > 3) {
            upcoming = upcoming.subList(0, 3);
        }

        // Logic/Cases
        if (upcoming.isEmpty()) {
            ImageView icon = new ImageView(getContext());
            icon.setImageResource(R.drawable.calendar);
            icon.setAlpha(0.6f);

            TextView text = new TextView(getContext());
            text.setText("No Events Upcoming");
            text.setTextColor(Color.GRAY);
            text.setTextSize(14);
            text.setGravity(Gravity.CENTER);

            LinearLayout empty = new LinearLayout(getContext());
            empty.setOrientation(LinearLayout.VERTICAL);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, 40, 0, 40);

            empty.addView(icon);
            empty.addView(text);

            upcomingContainer.addView(empty);
        } else {
            for (Event e : upcoming) {
                View item = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_event, upcomingContainer, false);

                TextView title = item.findViewById(R.id.tvTitle);
                TextView description = item.findViewById(R.id.tvDescription);
                TextView date = item.findViewById(R.id.tvDate);
                TextView time = item.findViewById(R.id.tvTime);
                TextView location = item.findViewById(R.id.tvLocation);

                title.setText(e.title);
                description.setText("        " + e.description);
                date.setText("        Date: " + e.date);
                time.setText("        Time: " + e.time);
                location.setText("        Location: " + e.location);

                // hide actions container
                View actionContainer = item.findViewById(R.id.actionContainer);
                if (actionContainer != null) {
                    actionContainer.setVisibility(View.GONE);
                }

                // dot color
                View dot = item.findViewById(R.id.colorDot);
                dot.getBackground().setTint(Color.parseColor(e.dotColor));

                // background color
                Drawable bg = item.getBackground();
                if (bg instanceof LayerDrawable) {
                    LayerDrawable layer = (LayerDrawable) bg;
                    Drawable inner = layer.getDrawable(1);
                    inner.setTint(Color.parseColor(e.color));
                }

                upcomingContainer.addView(item);
            }
        }
    }
}