package com.example.fbla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

public class EventCreateFragment extends Fragment {

    public interface EventCreateListener {
        void onEventCreated(Event event);
        void onEventEdited(int position, Event event);
    }

    private EventCreateListener listener;
    private int editPosition = -1;

    public void setEventCreateListener(EventCreateListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_event_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        EditText inputTitle = view.findViewById(R.id.inputTitle);
        EditText inputDate = view.findViewById(R.id.inputDate);
        EditText inputTime = view.findViewById(R.id.inputTime);
        EditText inputLocation = view.findViewById(R.id.inputLocation);
        EditText inputDescription = view.findViewById(R.id.inputDescription);
        Button saveBtn = view.findViewById(R.id.saveEventButton);

        Calendar calendar = Calendar.getInstance();

        // -------- DATE PICKER --------
        inputDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    requireContext(),
                    (dp, y, m, d) -> {
                        calendar.set(y, m, d);
                        inputDate.setText(
                                String.format(Locale.US, "%02d/%02d/%d", m + 1, d, y)
                        );
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // -------- TIME PICKER --------
        inputTime.setOnClickListener(v -> {
            new TimePickerDialog(
                    requireContext(),
                    (tp, h, min) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, h);
                        calendar.set(Calendar.MINUTE, min);
                        inputTime.setText(String.format(Locale.US, "%02d:%02d", h, min));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
            ).show();
        });

        // -------- EDIT MODE --------
        if (getArguments() != null && getArguments().containsKey("event")) {
            Event existing = getArguments().getParcelable("event");
            editPosition = getArguments().getInt("edit_position", -1);

            if (existing != null) {
                inputTitle.setText(existing.title);
                inputDate.setText(existing.date);
                inputTime.setText(existing.time);
                inputLocation.setText(existing.location);
                inputDescription.setText(existing.description);
            }
        }

        saveBtn.setOnClickListener(v -> {
            Event event = new Event(
                    inputTitle.getText().toString(),
                    inputDescription.getText().toString(),
                    inputDate.getText().toString(),
                    inputTime.getText().toString(),
                    inputLocation.getText().toString(),
                    calendar.getTimeInMillis()
            );

            if (listener != null) {
                if (editPosition >= 0) {
                    listener.onEventEdited(editPosition, event);
                } else {
                    listener.onEventCreated(event);
                }
            }

            hideDim();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void hideDim() {
        View dim = requireActivity().findViewById(R.id.dimView);
        if (dim != null) {
            dim.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> dim.setVisibility(View.GONE))
                    .start();
        }
    }
}
