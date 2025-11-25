package com.example.rumireaborlykovacalendarapp.presentation.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumireaborlykovacalendarapp.R;
import com.example.rumireaborlykovacalendarapp.domain.models.DayItem;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private List<DayItem> days;
    private OnDayClickListener onDayClickListener;

    public CalendarAdapter(List<DayItem> days) {
        this.days = days;
    }

    public List<DayItem> getDays() {
        return days;
    }
    public void setDays(List<DayItem> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.onDayClickListener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem day = days.get(position);
        holder.bind(day);

        holder.itemView.setOnClickListener(v -> {
            if (onDayClickListener != null && day.isCurrentMonth()) {
                onDayClickListener.onDayClick(day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return days != null ? days.size() : 0;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayNumber;
        private final View eventIndicator;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNumber = itemView.findViewById(R.id.tvDayNumber);
            eventIndicator = itemView.findViewById(R.id.eventIndicator);
        }

        public void bind(DayItem day) {
            dayNumber.setText(String.valueOf(day.getDayOfMonth()));

            dayNumber.setBackground(null);

            if (!day.isCurrentMonth()) {
                dayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                itemView.setEnabled(false);
                itemView.setClickable(false);
            } else {
                itemView.setEnabled(true);
                itemView.setClickable(true);

                if (day.isToday()) {
                    setCircleBackground(dayNumber, R.color.blue);
                    dayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                } else if (day.isSelected()) {
                    setCircleBackground(dayNumber, R.color.light_blue);
                    dayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                } else {
                    dayNumber.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                    dayNumber.setBackground(null);
                }
            }

            boolean hasEvents = day.getEvents() != null && !day.getEvents().isEmpty();
            boolean hasHoliday = day.isHoliday();

            if (hasEvents || hasHoliday) {
                eventIndicator.setVisibility(View.VISIBLE);
                if (!hasHoliday) {
                    eventIndicator.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.blue));
                }
            } else {
                eventIndicator.setVisibility(View.INVISIBLE);
            }
        }

        private void setCircleBackground(TextView textView, int colorRes) {
            GradientDrawable circle = new GradientDrawable();
            circle.setShape(GradientDrawable.OVAL);
            circle.setColor(ContextCompat.getColor(itemView.getContext(), colorRes));
            textView.setBackground(circle);
        }
    }

    public interface OnDayClickListener {
        void onDayClick(DayItem day);
    }
}