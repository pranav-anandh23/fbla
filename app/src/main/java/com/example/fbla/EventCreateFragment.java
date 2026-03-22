package com.example.fbla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;


public class EventCreateFragment extends Fragment {

    public EventCreateFragment() {
        // Required empty public constructor
    }

    // Components
    private EditText etTitle, etDate, etTime, etLocation, etDescription;
    private Button btnCancel, btnConfirm;

    // Variables to store
    private boolean isEditMode = false;
    private int editPosition = -1;
    private Event editingEvent;

    private String title, date, time, location, description, color, dotColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // -------------------- View Instances
        etTitle = view.findViewById(R.id.et_title);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etLocation = view.findViewById(R.id.et_location);
        etDescription = view.findViewById(R.id.et_description);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);

        if (getArguments() != null && getArguments().containsKey("event")) {
            isEditMode = true;
            editPosition = getArguments().getInt("edit_position");
            editingEvent = getArguments().getParcelable("event");

            if (editingEvent != null)
                prefillFields(editingEvent);
        }


        // -------------------- Colors
        View[] colorViews = {
                view.findViewById(R.id.color_blue),
                view.findViewById(R.id.color_green),
                view.findViewById(R.id.color_purple),
                view.findViewById(R.id.color_orange),
                view.findViewById(R.id.color_red),
                view.findViewById(R.id.color_pink)
        };
        for (View colorView : colorViews) {
            colorView.setOnClickListener(v -> {

                // Reset all
                for (View cv : colorViews) {
                    cv.setForeground(null);
                    cv.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                }

                // Add indicator
                v.setForeground(
                        ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.color_selected_border
                        )
                );

                v.animate()
                        .scaleX(1.08f)
                        .scaleY(1.08f)
                        .setDuration(120)
                        .start();

                // Store color
                color = (String) v.getTag();

                // Store dot color
                switch (color) {
                    // BLUE
                    case "#DBEAFE":
                        dotColor = "#2196F3";
                        break;
                    // GREEN
                    case "#DBFCE7":
                        dotColor = "#4CAF50";
                        break;
                    // PURPLE
                    case "#F3E8FF":
                        dotColor = "#9C27B0";
                        break;
                    // ORANGE
                    case "#FFEDD4":
                        dotColor = "#FF9800";
                        break;
                    // RED
                    case "#FFE2E2":
                        dotColor = "#F44336";
                        break;
                    // PINK
                    case "#FCE7F3":
                        dotColor = "#E91E63";
                        break;
                    default:
                        break;
                }
            });
        }

        // -------------------------- Elements

        // Date
        etDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, dayOfMonth) -> {
                        String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                        etDate.setText(selectedDate);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Time
        etTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(
                    requireContext(),
                    (view12, hourOfDay, minute) -> {
                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int hour = hourOfDay % 12;
                        if (hour == 0) hour = 12;
                        String timeText = String.format(
                                Locale.getDefault(),
                                "%d:%02d %s",
                                hour,
                                minute,
                                amPm
                        );
                        etTime.setText(timeText);
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
            ).show();
        });

        // Cancel Clicks
        btnCancel.setOnClickListener(v -> {
            hideDim();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        View dimView = requireActivity().findViewById(R.id.dimView);
        dimView.setVisibility(View.VISIBLE);

        dimView.setOnClickListener(v -> {
            // same as cancel
            requireActivity().getSupportFragmentManager().popBackStack();
            hideDim();
        });

        // Confirm button
        btnConfirm.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            title = etTitle.getText().toString().trim();
            date = etDate.getText().toString().trim();
            time = etTime.getText().toString().trim();
            location = etLocation.getText().toString().trim();
            description = etDescription.getText().toString().trim();

            Event newEvent = new Event(title, description, date, time, location, color, dotColor, dateToMillis(date));

            if (listener != null) {
                if (isEditMode) {
                    listener.onEventEdited(editPosition, newEvent);
                } else {
                    listener.onEventCreated(newEvent);
                }
            }

            requireActivity().getSupportFragmentManager().popBackStack();
            hideDim();
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_create, container, false);

    }

    ///  ----------------- METHODS ------------------------------------

    // Date to Milliseconds (for comparison)
    private long dateToMillis(String date) {
        try {
            String[] parts = date.split("/");
            int month = Integer.parseInt(parts[0]) - 1;
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, 23, 59, 59);
            cal.set(Calendar.MILLISECOND, 999);

            return cal.getTimeInMillis();
        } catch (Exception e) {
            return 0;
        }
    }

    // Dimming
    private void hideDim() {
        View dim = requireActivity().findViewById(R.id.dimView);
        dim.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> dim.setVisibility(View.GONE))
                .start();
    }

    // Event Creation
    public interface EventCreateListener {
        void onEventCreated(Event event);
        void onEventEdited(int position, Event event);
    }

    private EventCreateListener listener;

    public void setEventCreateListener(EventCreateListener listener) {
        this.listener = listener;
    }

    // Required items
    private boolean validateFields() {
        boolean valid = true;

        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Required");
            valid = false;
        }

        if (etDate.getText().toString().trim().isEmpty()) {
            etDate.setError("Required");
            valid = false;
        }

        if (etTime.getText().toString().trim().isEmpty()) {
            etTime.setError("Required");
            valid = false;
        }

        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Required");
            valid = false;
        }

        if (color == null) {
            Toast.makeText(getContext(), "Please select a color", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    // Prefill Fields in Edit Screen
    private void prefillFields(Event event) {
        etTitle.setText(event.title);
        etDate.setText(event.date);
        etTime.setText(event.time);
        etLocation.setText(event.location);
        etDescription.setText(event.description);
        color = event.color;

        // highlight selected color
        assert getView() != null;
        View selectedColor = getView().findViewWithTag(color);
        if (selectedColor != null) {
            selectedColor.performClick();
        }
    }
}