package com.example.fbla;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    protected HomeFragment homeFragment = new HomeFragment();
    protected Fragment eventsFragment = new EventsFragment();
    protected Fragment newsFragment = new NewsFragment();
    protected Fragment resourcesFragment = new ResourcesFragment();
    protected Fragment profileFragment = new ProfileFragment();

    private View dimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment());

        // Background Dimming
        dimView = findViewById(R.id.dimView);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.tab_home);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.tab_home) {
                selectedFragment = homeFragment;
            } else if (item.getItemId() == R.id.tab_events) {
                selectedFragment = eventsFragment;
            } else if (item.getItemId() == R.id.tab_news) {
                selectedFragment = newsFragment;
            } else if (item.getItemId() == R.id.tab_resources) {
                selectedFragment = resourcesFragment;
            } else if (item.getItemId() == R.id.tab_profile) {
                selectedFragment = profileFragment;
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
