package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.ContactListAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.CalendarUtils;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class NewTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String dateFormat = "dd.MM.yyyy";
    private TodoDBService todoDBService;
    private String currentUser;
    AutoCompleteTextView autoCompleteTextView;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_todo_activity);
        EditText dueDateEditText = findViewById(R.id.todoDueDate);
        dueDateEditText.setText(DateFormat.format(dateFormat, Calendar.getInstance()));
        todoDBService = new TodoDBService(getDataStore());
        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);
        autoCompleteTextView = findViewById(R.id.addContactToToDoAutoCompleteTextView);
        contactListAdapter = new ContactListAdapter(this, R.layout.contact_item, ContactUtils.getContacts());
        autoCompleteTextView.setAdapter(contactListAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactDTO contactDTO= (ContactDTO) autoCompleteTextView.getAdapter().getItem(position);
                Toast.makeText(NewTodoActivity.this,"Clicked " + contactDTO.getName(),Toast.LENGTH_LONG).show();
            }

        });
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
        String dueDate = ((EditText) findViewById(R.id.todoDueDate)).getText().toString();
        boolean favoriteCheckboxChecked = ((CheckBox) findViewById(R.id.toIsFavourtie)).isChecked();

        //TODO: check if linked to User...
        todoDBService.createTodo(description, title, CalendarUtils.fromStringToCalendar(dueDate),
                currentUser, favoriteCheckboxChecked);

        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra(Constants.CURR_USER_KEY, currentUser);
        startActivity(intent);
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
