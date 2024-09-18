package com.example.testnavigation.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavigation.PreferencesManager;
import com.example.testnavigation.R;
import com.example.testnavigation.ui.notifications.Notification;
import com.example.testnavigation.ui.notifications.NotificationsViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private List<TodoItem> todoList = new ArrayList<>();
    private EditText editTextTask;
    private Button buttonAddTask;
    private NotificationsViewModel notificationsViewModel;
    private int editingPosition = -1;
    private BottomNavigationView bottomNavigationView;
    private PreferencesManager preferencesManager;

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    private void updateNotificationBadge() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (bottomNavigationView != null) {
                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
                badge.setVisible(true);
                badge.setNumber(notificationsViewModel.getNotificationCount());
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_todo);
        editTextTask = root.findViewById(R.id.edit_text_task);
        buttonAddTask = root.findViewById(R.id.button_add_task);

        todoAdapter = new TodoAdapter(todoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoAdapter);

        notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        preferencesManager = new PreferencesManager(requireContext());  // Initialize PreferencesManager

        // Load ToDo list from PreferencesManager
        loadTodoList();

        todoAdapter.setOnTodoActionListener(new TodoAdapter.OnTodoActionListener() {
            @Override
            public void onEdit(int position) {
                TodoItem item = todoList.get(position);
                editTextTask.setText(item.getTask());
                editingPosition = position;
                buttonAddTask.setText("Update");  // Change button text to "Update" when editing
            }

            @Override
            public void onDelete(int position) {
                TodoItem item = todoList.get(position);
                todoList.remove(position);
                todoAdapter.notifyItemRemoved(position);

                // Notify ViewModel
                Notification notification = new Notification("Task Deleted", "Task: " + item.getTask());
                notificationsViewModel.addNotification(notification);
                new Handler(Looper.getMainLooper()).post(() -> updateNotificationBadge());
                saveTodoList();
            }

            @Override
            public void onMarkDone(int position, boolean isChecked) {
                TodoItem item = todoList.get(position);
                item.setDone(isChecked);
                todoAdapter.notifyItemChanged(position);

                String action = isChecked ? "Task Completed" : "Task Marked Incomplete";
                Notification notification = new Notification(action, "Task: " + item.getTask());
                notificationsViewModel.addNotification(notification);
                new Handler(Looper.getMainLooper()).post(() -> updateNotificationBadge());
                saveTodoList();
            }
        });

        buttonAddTask.setOnClickListener(v -> {
            String task = editTextTask.getText().toString();
            if (!task.isEmpty()) {
                if (editingPosition >= 0) {
                    TodoItem item = todoList.get(editingPosition);
                    item.setTask(task);
                    todoAdapter.notifyItemChanged(editingPosition);

                    Notification notification = new Notification("Task Updated", "Task: " + task);
                    notificationsViewModel.addNotification(notification);
                    editingPosition = -1;
                    buttonAddTask.setText("Add");
                } else {
                    TodoItem newItem = new TodoItem(task, false);
                    todoList.add(newItem);
                    todoAdapter.notifyItemInserted(todoList.size() - 1);

                    Notification notification = new Notification("Task Added", "Task: " + task);
                    notificationsViewModel.addNotification(notification);
                }
                editTextTask.setText("");
                new Handler(Looper.getMainLooper()).post(this::updateNotificationBadge);
                saveTodoList();
            }
        });

        return root;
    }

    private void saveTodoList() {
        preferencesManager.saveTodoList(todoList);
    }

    private void loadTodoList() {
        List<TodoItem> savedTodoList = preferencesManager.getTodoList();
        if (savedTodoList != null) {
            todoList.clear();
            todoList.addAll(savedTodoList);
            todoAdapter.notifyDataSetChanged();
        }
    }
}
