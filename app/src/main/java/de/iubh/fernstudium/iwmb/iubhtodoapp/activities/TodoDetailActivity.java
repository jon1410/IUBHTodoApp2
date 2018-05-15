package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.SelectContactsDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.ContactListAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TodoDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Todo selectedTodo;
    private boolean favStatus;
    private TodoDBService todoDBService;
    AutoCompleteTextView autoCompleteTextView;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoDBService = new TodoDBService(getDataStore());
        setContentView(R.layout.todo_detail_activity);
        selectedTodo = getIntent().getParcelableExtra(Constants.SEL_TODO_KEY);
        autoCompleteTextView = findViewById(R.id.idLinkedToDetailAutoCompleteContent);
        contactListAdapter = new ContactListAdapter(this, R.layout.contact_item, ContactUtils.getContacts());
        autoCompleteTextView.setAdapter(contactListAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactDTO contactDTO = (ContactDTO) autoCompleteTextView.getAdapter().getItem(position);
                Toast.makeText(TodoDetailActivity.this, "Clicked " + contactDTO.getName(), Toast.LENGTH_LONG).show();
            }

        });
        populateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.idMenuMail:
                Toast.makeText(this, "New Email clicked!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClickDueDate(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.init(this, this);
        newFragment.show(getSupportFragmentManager(), "dueDatePicker");
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
        //TODO: finish call DB-Service
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dueDateEditText = findViewById(R.id.idDueDateDetailContent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        dueDateEditText.setText(DateFormat.format(Constants.DATE_FORMAT, calendar));
    }

    public void exportViewToPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1, 1, 1)
                .setContentRect(new Rect(0, 0, 0, 0)).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        View content = getCurrentFocus().getRootView();
        content.draw(page.getCanvas());

        document.finishPage(page);
        // write the document content
        String fileName = constructFileName();
        //TODO: set new FileName to TODO
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            document.writeTo(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private String constructFileName() {
        return selectedTodo.getId() + "_Todo_Export_" + selectedTodo.getTitle() + ".pdf";
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

    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getApplication()).getDataStore();
    }

}
