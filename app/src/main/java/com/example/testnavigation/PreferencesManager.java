package com.example.testnavigation;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testnavigation.ui.home.TodoItem;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;
import com.example.testnavigation.TodoItemActivity;  // Ensure you import your TodoItem class

public class PreferencesManager {
    private static final String PREFS_NAME = "todo_prefs";
    private static final String TODO_LIST_KEY = "todo_list";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveTodoList(List<TodoItem> todoList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(todoList);
        editor.putString(TODO_LIST_KEY, json);
        editor.apply();
    }

    public List<TodoItem> getTodoList() {
        String json = sharedPreferences.getString(TODO_LIST_KEY, null);
        Type type = new TypeToken<List<TodoItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
