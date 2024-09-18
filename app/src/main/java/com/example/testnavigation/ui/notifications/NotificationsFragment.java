package com.example.testnavigation.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavigation.R;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private NotificationsViewModel notificationsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        notificationsViewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notificationAdapter == null) {
                notificationAdapter = new NotificationAdapter(notifications);
                recyclerView.setAdapter(notificationAdapter);
            } else {
                notificationAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}
