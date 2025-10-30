package com.example.rumireaborlykovacalendarapp.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.tvEventTitle.setText(event.getTitle());
        holder.tvEventDescription.setText(event.getDescription());

        if (event.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            holder.tvEventDate.setText(sdf.format(event.getDate()));
        } else {
            holder.tvEventDate.setText("Без даты");
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void updateEvents(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventTitle, tvEventDescription, tvEventDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvEventDescription = itemView.findViewById(R.id.tvEventDescription);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
        }
    }
}
