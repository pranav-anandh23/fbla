package com.example.fbla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);

        UserSessionManager session = new UserSessionManager(requireContext());
        String fullName = session.getUserName();

        if (fullName != null && !fullName.isEmpty()) {
            String firstName = fullName.split(" ")[0];
            tvHeaderTitle.setText("Welcome back, " + firstName + "!");
        } else {
            tvHeaderTitle.setText("Welcome back!");
        }

        loadHomeNews();
        refreshHome();

        ImageButton aiButton = view.findViewById(R.id.aiButton);
        aiButton.setOnClickListener(v -> {
            showDim();
            AiChatFragment fragment = new AiChatFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_up,   // same animation as event creator
                            0,
                            0,
                            R.anim.slide_out_down
                    )
                    .add(R.id.overlay_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        TextView tvViewAll = view.findViewById(R.id.tvViewAll);
        tvViewAll.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EventsFragment())
                    .commit();
        });
    }

    private void loadHomeNews() {
        LinearLayout newsContainer = requireView().findViewById(R.id.newsContainer);
        newsContainer.removeAllViews();

        List<NewsItem> newsList = new ArrayList<>();

        newsList.add(new NewsItem(
                true,
                "Announcements",
                "2026 SLC General Information",
                "State Leadership Conference details and registration info",
                "December 5, 2025",
                "featured_slc.png",
                "https://www.pafbla.org"
        ));

        newsList.add(new NewsItem(
                false,
                "Competitions",
                "2026 SLC Competition Schedules",
                "Who is eligible to attend and compete",
                "December 5, 2025",
                "comp_schedule.png",
                "https://www.pafbla.org"
        ));

        newsList.add(new NewsItem(
                false,
                "Chapter News",
                "PA FBLA Updates for Members",
                "Check the latest announcements, deadlines, and chapter opportunities.",
                "December 10, 2025",
                "featured_slc.png",
                "https://www.pafbla.org"
        ));

        for (NewsItem item : newsList) {
            View newsCard = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_home_news, newsContainer, false);

            ImageView image = newsCard.findViewById(R.id.image);
            TextView category = newsCard.findViewById(R.id.category);
            TextView title = newsCard.findViewById(R.id.title);
            TextView description = newsCard.findViewById(R.id.description);
            TextView date = newsCard.findViewById(R.id.date);
            TextView readMore = newsCard.findViewById(R.id.readMore);

            category.setText(item.getCategory());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getDate());

            loadImage(item.getImage(), image);

            View.OnClickListener openLink = v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                startActivity(intent);
            };

            readMore.setOnClickListener(openLink);
            newsCard.setOnClickListener(openLink);

            newsContainer.addView(newsCard);
        }
    }

    private void loadImage(String fileName, ImageView imageView) {
        try {
            InputStream is = requireContext().getAssets().open("news_images/" + fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_news);
        }
    }

    private void showDim() {
        View dim = requireActivity().findViewById(R.id.dimView);
        dim.setAlpha(0f);
        dim.setVisibility(View.VISIBLE);
        dim.animate()
                .alpha(1f)
                .setDuration(200)
                .start();
    }


    private void refreshHome() {
        LinearLayout upcomingContainer = requireView().findViewById(R.id.upcomingContainer);
        upcomingContainer.removeAllViews();

        allEvents = EventRepository.getAllEvents(requireContext());
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

                View actionContainer = item.findViewById(R.id.actionContainer);
                if (actionContainer != null) {
                    actionContainer.setVisibility(View.GONE);
                }

                View dot = item.findViewById(R.id.colorDot);
                dot.getBackground().setTint(Color.parseColor(e.dotColor));

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