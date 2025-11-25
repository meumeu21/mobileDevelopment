package com.example.rumireaborlykovacalendarapp.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.domain.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> events;

    public EventsAdapter(List<Event> events) {
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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final ImageView ivEventImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvDescription = itemView.findViewById(R.id.tvEventDescription);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);
        }

        public void bind(Event event) {
            String time;
            if (event.getId() == -1) {
                time = "Весь день";
                tvTitle.setText(event.getTitle());
            } else {
                time = String.format("%02d:%02d - %02d:%02d",
                        event.getStartHour(), event.getStartMinute(),
                        event.getEndHour(), event.getEndMinute());
                tvTitle.setText(event.getTitle());
            }

            tvTime.setText(time);

            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                String description = event.getDescription();
                if (description.length() > 30) {
                    description = description.substring(0, 30) + "…";
                }
                tvDescription.setText(description);
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            if (event.getId() != -1) {
                String imgUrl = "https://picsum.photos/100?random=" + event.getId();

                Picasso.get()
                        .load(imgUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(ivEventImage);

                ivEventImage.setVisibility(View.VISIBLE);
            } else {
                ivEventImage.setVisibility(View.GONE);
            }
        }

    }
}