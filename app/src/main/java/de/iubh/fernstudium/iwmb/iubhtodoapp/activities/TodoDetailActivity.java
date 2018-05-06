package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.SelectContactsDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TodoDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {

    private Todo selectedTodo;
    private boolean favStatus;
    private TodoDBService todoDBService;
    AutoCompleteTextView autoCompleteTextView;
    List<ContactDTO> contactsForAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoDBService = new TodoDBService(getDataStore());
        setContentView(R.layout.todo_detail_activity);
        selectedTodo = getIntent().getParcelableExtra(Constants.SEL_TODO_KEY);
        autoCompleteTextView = findViewById(R.id.addContactToToDoAutoCompleteTextView);
        setUpAutoComplete();
        autoCompleteTextView.setThreshold(1);

        populateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_todo_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickDueDate(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.init(this, this);
        newFragment.show(getSupportFragmentManager(), "dueDatePicker");
    }

    public void onClickSelectContact(View view) {
        SelectContactsDialog selectContactsDialog = new SelectContactsDialog();
        selectContactsDialog.show(getSupportFragmentManager(), "selectContactDialog");
    }

    public void onClickReturn(View view) {
        finish();
    }

    public void onClickSaveChanges(View view) {
        String title = ((EditText) findViewById(R.id.idTitleDetailContent)).getText().toString();
        String description = ((EditText) findViewById(R.id.idDescContent)).getText().toString();
        EditText statusEdt = findViewById(R.id.idStatusDetailContent);
        EditText dueDateEdt = findViewById(R.id.idDueDateDetailContent);
        ToggleButton favouriteBtn = findViewById(R.id.idFavDetailButton);
        //TODO: finish
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dueDateEditText = findViewById(R.id.idDueDateDetailContent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        dueDateEditText.setText(DateFormat.format(Constants.DATE_FORMAT, calendar));
    }


    private void populateView() {
        EditText title = findViewById(R.id.idTitleDetailContent);
        title.setText(selectedTodo.getTitle());
        EditText description = findViewById(R.id.idDescContent);
        description.setText(selectedTodo.getDescription());
        EditText status = findViewById(R.id.idStatusDetailContent);
        status.setText(selectedTodo.getStatus().getStatusText());
        EditText dueDate = findViewById(R.id.idDueDateDetailContent);
        dueDate.setText(DateFormat.format(Constants.DATE_FORMAT, new Date(selectedTodo.getDueDate().getTime())));
        ToggleButton favouriteBtn = findViewById(R.id.idFavDetailButton);
        favStatus = selectedTodo.getFavoriteFlag();
        favouriteBtn.setChecked(favStatus);
        //TODO: add Link to Contact in TODO
    }

    private void setUpAutoComplete() {
        //TODO: check permission
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getApplication()).getDataStore();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<ContactDTO> contacts = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //TODO create new DTO, add it to list
            cursor.moveToNext();
        }

        // add list to contactsForAutoComplete
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            };

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    private static final String SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";

}
