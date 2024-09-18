package com.example.testnavigation.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavigation.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationsList;

    public NotificationAdapter(List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationsList.get(position);
        holder.textViewTitle.setText(notification.getTitle());
        holder.textViewMessage.setText(notification.getMessage());

        holder.buttonDismiss.setOnClickListener(v -> {
            notificationsList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewMessage;
        ImageButton buttonDismiss;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_notification_title);
            textViewMessage = itemView.findViewById(R.id.text_view_notification_message);
            buttonDismiss = itemView.findViewById(R.id.button_dismiss);
        }
    }
}

