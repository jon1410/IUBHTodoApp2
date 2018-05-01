package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.viewholder.TodoViewHolder;
import de.iubh.fernstudium.iwmb.iubhtodoapp.databinding.TodoItemBinding;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

/**
 * Created by ivanj on 18.03.2018.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    List<Todo> todos;
    private TodoViewHolder viewHolder;

    public TodoAdapter(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TodoItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater,  R.layout.todo_item, parent, false);
        viewHolder = new TodoViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo item = todos.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
}
