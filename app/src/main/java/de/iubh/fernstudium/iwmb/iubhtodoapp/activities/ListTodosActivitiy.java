package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.TodoAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import io.requery.Persistable;

import io.requery.reactivex.ReactiveEntityStore;

public class ListTodosActivitiy extends AppCompatActivity {

    TodoAdapter todoAdapter;
    TodoDBService todoDBService;
    String currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoDBService = new TodoDBService(getDataStore());

        getSupportActionBar().setTitle("Test");
        setContentView(R.layout.list_todo_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);

        todoAdapter = new TodoAdapter(getTodosForUser());
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        if(todoAdapter == null){
            todoAdapter = new TodoAdapter(getTodosForUser());
        }else{
            todoAdapter.setTodos(getTodosForUser());
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoAdapter = null;
    }

    private List<Todo> getTodosForUser(){
        return todoDBService.getTodosAsListForUser(currentUser);
    }

    private ReactiveEntityStore<Persistable> getDataStore(){
        return ((TodoApplication) getApplication()).getDataStore();
    }
}
