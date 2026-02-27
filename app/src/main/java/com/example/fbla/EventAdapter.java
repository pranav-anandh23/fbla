package com.example.fbla;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public interface EventActionListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    private final List<Event> events;
    private final EventActionListener listener;

    public EventAdapter(List<Event> events, EventActionListener listener) {
        this.events = events;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, location;
        ImageButton editBtn, deleteBtn;

        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.eventTitle);
            date = v.findViewById(R.id.eventDate);
            location = v.findViewById(R.id.eventLocation);
            editBtn = v.findViewById(R.id.editEvent);
            deleteBtn = v.findViewById(R.id.deleteEvent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_event, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Event e = events.get(pos);

        h.title.setText(e.title);
        h.date.setText(e.date + " • " + e.time);
        h.location.setText(e.location);

        if (listener != null) {
            h.editBtn.setOnClickListener(v -> listener.onEdit(pos));
            h.deleteBtn.setOnClickListener(v -> listener.onDelete(pos));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
