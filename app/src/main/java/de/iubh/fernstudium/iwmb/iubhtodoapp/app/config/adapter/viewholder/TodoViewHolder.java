package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.viewholder;

import android.support.v7.widget.RecyclerView;

import de.iubh.fernstudium.iwmb.iubhtodoapp.databinding.TodoItemBinding;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

/**
 * Created by ivanj on 18.03.2018.
 */

public class TodoViewHolder extends RecyclerView.ViewHolder {

    private final TodoItemBinding binding;

    public TodoViewHolder(TodoItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Todo item) {
        binding.setTodo(item);
        binding.executePendingBindings();
    }
}
