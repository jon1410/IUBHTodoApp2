package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Calendar;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.UserDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.UserNotFoundException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.CalendarUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class NewTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String dateFormat = "dd.MM.yyyy";
    private TodoDBService todoDBService;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_todo_activity);
        EditText dueDateEditText = findViewById(R.id.todoDueDate);
        dueDateEditText.setText(DateFormat.format(dateFormat, Calendar.getInstance()));
        todoDBService = new TodoDBService(getDataStore());
        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);
    }

    public void onClickDueDate(View view){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.init(this, this);
        newFragment.show(getSupportFragmentManager(), "dueDatePicker");
    }

    public void onClickCreateNewTodo(View view){
        Toast.makeText(this, "Create New Todo Button clicked...!", Toast.LENGTH_LONG).show();
        String title = ((EditText) findViewById(R.id.todoTitle)).getText().toString();
        String description = ((EditText) findViewById(R.id.todoDesc)).getText().toString();
        String dueDate = ((EditText) findViewById(R.id.todoTitle)).getText().toString();
        boolean favoriteCheckboxChecked = ((CheckBox) findViewById(R.id.toIsFavourtie)).isChecked();

        //TODO: check if linked to User...
        todoDBService.createTodo(description, title, CalendarUtils.fromStringToCalendar(dueDate),
                currentUser, favoriteCheckboxChecked);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dueDateEditText = findViewById(R.id.todoDueDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        dueDateEditText.setText(DateFormat.format(dateFormat, calendar));
    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getApplication()).getDataStore();
    }

}
