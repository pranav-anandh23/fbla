package com.example.fbla;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    UserSessionManager session;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // reuse activity_profile.xml
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        session = new UserSessionManager(requireContext());

        ((TextView) view.findViewById(R.id.profileName))
                .setText(session.getName());

        ((TextView) view.findViewById(R.id.profileEmail))
                .setText(session.getEmail());

        ((TextView) view.findViewById(R.id.profileGrade))
                .setText("Grade: " + session.getGrade());

        ((TextView) view.findViewById(R.id.profileGradYear))
                .setText("Graduation Year: " + session.getGradYear());

        ((TextView) view.findViewById(R.id.profileChapter))
                .setText("Chapter: " + session.getChapter());

        ((TextView) view.findViewById(R.id.profileRegion))
                .setText("Region: " + session.getRegion());

        ((TextView) view.findViewById(R.id.profileState))
                .setText("State: " + session.getState());

        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            session.clearSession();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
