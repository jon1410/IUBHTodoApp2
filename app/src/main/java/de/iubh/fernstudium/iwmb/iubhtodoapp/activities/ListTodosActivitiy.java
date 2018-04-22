package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
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

        getSupportActionBar().setTitle(getString(R.string.your_todos));
        setContentView(R.layout.list_todo_activity);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);

        todoAdapter = new TodoAdapter(getTodosForUser());
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        MenuItem item = menu.findItem(R.id.sortTodosSpinner);
        Spinner sortSpinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> dropDownValuesAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerDropdown, android.R.layout.simple_spinner_item);
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(dropDownValuesAdapter);

        /*
        String[] dropDownValuesResource = getResources().getStringArray(R.array.spinnerDropdown);
        ArrayAdapter<String> dropDownValuesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dropDownValuesResource){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // this part is needed for hiding the original view
                View view = super.getView(position, convertView, parent);
                view.setVisibility(View.GONE);
                return view;
            }
        };
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        */
        sortSpinner.setAdapter(dropDownValuesAdapter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.addTodo:
                Toast.makeText(this, "add new Todo clicked!", Toast.LENGTH_LONG).show();
                Intent newTodoActivityIntent = new Intent(this, NewTodoActivity.class);
                newTodoActivityIntent.putExtra(Constants.CURR_USER_KEY, currentUser);
                startActivity(newTodoActivityIntent);
                break;
            case R.id.sortTodosSpinner:
                Toast.makeText(this, "sort Todos clicked!", Toast.LENGTH_LONG).show();
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        if (todoAdapter == null) {
            todoAdapter = new TodoAdapter(getTodosForUser());
        } else {
            todoAdapter.setTodos(getTodosForUser());
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoAdapter = null;
    }

    private List<Todo> getTodosForUser() {
        return todoDBService.getTodosAsListForUser(currentUser);
    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getApplication()).getDataStore();
    }
}
