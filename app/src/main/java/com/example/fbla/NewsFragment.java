package com.example.fbla;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.activity_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView newsRecycler = view.findViewById(R.id.newsRecycler);
        newsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NewsItem> newsList = new ArrayList<>();

        newsList.add(new NewsItem(true, "Announcements",
                "2026 SLC General Information",
                "State Leadership Conference details and registration info",
                "December 5, 2025",
                "featured_slc.png",
                "https://www.pafbla.org"));

        newsList.add(new NewsItem(false, "Competitions",
                "2026 SLC Competition Schedules",
                "Who is eligible to attend and compete",
                "December 5, 2025",
                "comp_schedule.png",
                "https://www.pafbla.org"));

        newsRecycler.setAdapter(new NewsAdapter(requireContext(), newsList));
    }
}
