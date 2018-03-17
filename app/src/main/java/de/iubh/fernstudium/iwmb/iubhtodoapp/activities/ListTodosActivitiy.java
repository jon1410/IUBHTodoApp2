package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.BindingHolder;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.databinding.TodoItemBinding;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

public class ListTodosActivitiy extends AppCompatActivity {

    TodoAdapter todoAdapter;
    TodoDBService todoDBService;
    String currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_todo_activity);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        todoAdapter = new TodoAdapter();
        recyclerView.setAdapter(todoAdapter);
        todoDBService = new TodoDBService(((TodoApplication) getApplication()).getDataStore());
        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);
    }

    class TodoAdapter extends QueryRecyclerAdapter<TodoEntity, BindingHolder<TodoItemBinding>> implements View.OnClickListener {

        public TodoAdapter() {
            super(TodoEntity.$TYPE);
        }

        @Override
        public Result<TodoEntity> performQuery() {
            return null;
        }

        @Override
        public BindingHolder<TodoItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            TodoItemBinding binding = TodoItemBinding.inflate(inflater);
            binding.getRoot().setTag(binding);
            binding.getRoot().setOnClickListener(this);
            return new BindingHolder<>(binding);
        }

        @Override
        public void onBindViewHolder(TodoEntity item, BindingHolder<TodoItemBinding> holder, int position) {
            super.onBindViewHolder(holder, position);
        }

        @Override
        public void onClick(View v) {
            // TODO: 17.03.2018 add onClick behaviour
        }
    }
}
