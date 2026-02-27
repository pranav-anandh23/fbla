package com.example.fbla;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Load EventsFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new EventsFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_news) {
                startActivity(new Intent(this, NewsFragment.class));
                finish();
                return true;
            }

            if (id == R.id.nav_resources) {
                startActivity(new Intent(this, ResourcesFragment.class));
                finish();
                return true;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileFragment.class));
                finish();
                return true;
            }

            return false;
        });

    }
}
