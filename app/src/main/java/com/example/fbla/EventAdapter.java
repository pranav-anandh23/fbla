package com.example.fbla;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final List<Event> events;
    private final EventActionListener listener;
    private final boolean showActions;
    private long monthStart = Long.MIN_VALUE;
    private long monthEnd = Long.MAX_VALUE;

    public interface EventActionListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public EventAdapter(List<Event> events, EventActionListener listener, boolean showActions) {
        this.events = events;
        this.listener = listener;
        this.showActions = showActions;
    }


    /// ------------------ METHODS ----------------------

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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(v);
    }

    // Set Values/Listeners
    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        Event e = events.get(i);

        h.title.setText(e.title);
        h.description.setText("        " + e.description);
        h.date.setText("        Date: " + e.date);
        h.time.setText("        Time: " + e.time);
        h.location.setText("        Location: " + e.location);
        h.dot.getBackground().setTint(Color.parseColor(e.dotColor));

        // BG Color
        Drawable bg = h.itemView.getBackground();
        if (bg instanceof LayerDrawable) {
            LayerDrawable layer = (LayerDrawable) bg;
            Drawable inner = layer.getDrawable(1);
            inner.setTint(Color.parseColor(e.color));
        }

        int position = h.getAdapterPosition();

        // Action Buttons
        if (!showActions) {
            h.actionContainer.setVisibility(View.GONE);
        } else {
            h.actionContainer.setVisibility(View.VISIBLE);
            h.btnEdit.setOnClickListener(v -> {
                if (listener != null)
                    listener.onEdit(position);
            });
            h.btnDelete.setOnClickListener(v -> {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDelete(position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date, time, location;
        ImageView btnEdit, btnDelete;
        View dot;
        LinearLayout actionContainer;

        ViewHolder(View v) {
            super(v);
            dot = v.findViewById(R.id.colorDot);
            title = v.findViewById(R.id.tvTitle);
            description = v.findViewById(R.id.tvDescription);
            date = v.findViewById(R.id.tvDate);
            time = v.findViewById(R.id.tvTime);
            location = v.findViewById(R.id.tvLocation);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            actionContainer = v.findViewById(R.id.actionContainer);
        }
    }

}