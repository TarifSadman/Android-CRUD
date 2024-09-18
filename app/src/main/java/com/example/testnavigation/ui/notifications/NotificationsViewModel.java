package com.example.testnavigation.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notification) {
        List<Notification> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            currentNotifications.add(notification);
            notifications.setValue(currentNotifications);
        }
    }

    public void removeNotification(Notification notification) {
        List<Notification> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            currentNotifications.remove(notification);
            notifications.setValue(currentNotifications);
        }
    }

    public int getNotificationCount() {
        List<Notification> currentNotifications = notifications.getValue();
        return currentNotifications != null ? currentNotifications.size() : 0;
    }
}
