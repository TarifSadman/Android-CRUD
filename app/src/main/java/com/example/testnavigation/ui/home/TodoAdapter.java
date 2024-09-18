package com.example.testnavigation.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testnavigation.R;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final List<TodoItem> todoList;
    private OnTodoActionListener actionListener;

    public TodoAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem todoItem = todoList.get(position);
        holder.textViewTask.setText(todoItem.getTask());
        holder.checkBoxDone.setChecked(todoItem.isDone());

        holder.buttonEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(position);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(position);
            }
        });

        holder.checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (actionListener != null) {
                actionListener.onMarkDone(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setOnTodoActionListener(OnTodoActionListener listener) {
        this.actionListener = listener;
    }

    public interface OnTodoActionListener {
        void onEdit(int position);
        void onDelete(int position);
        void onMarkDone(int position, boolean isChecked);
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewTask;
        final ImageButton buttonEdit;
        final ImageButton buttonDelete;
        final CheckBox checkBoxDone;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.text_view_task);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            checkBoxDone = itemView.findViewById(R.id.checkbox_done);
        }
    }
}
