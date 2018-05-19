package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.ContactListAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ITextUtil;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TodoDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Todo selectedTodo;
    private boolean favStatus;
    private TodoDBService todoDBService;
    private ITextUtil iTextUtil;
    private ProgressBar progressBar;
    AutoCompleteTextView autoCompleteTextView;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iTextUtil = new ITextUtil();
        todoDBService = new TodoDBService(getDataStore());
        setContentView(R.layout.todo_detail_activity);
        progressBar = findViewById(R.id.detailProgress);
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
            case R.id.idMenuPdf:
                Toast.makeText(this, "Generating PDF clicked!", Toast.LENGTH_LONG).show();
                exportViewToPdf();
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
        showProgressBar(true);
        String fileName = constructFileName();
        Log.v("FILENAME.." , "Filename to write is: " + fileName);
        String toastText = null;
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            boolean pdfCreated = iTextUtil.createPdfFromTodo(selectedTodo, outputStream);
            if(pdfCreated){
                toastText = getString(R.string.pdf_created);
            }else{
                toastText = getString(R.string.pdf_not_created);
            }
            outputStream.close();
        } catch (Exception e) {
            Log.e("ERRORPDF","PDF could not be created....");
        }
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        showProgressBar(false);
    }

    private void showProgressBar(boolean show) {
        if(show){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }else{
            progressBar.setVisibility(View.GONE);
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
        return getCustomApplication().getDataStore();
    }

    private TodoApplication getCustomApplication(){
        return (TodoApplication) getApplication();
    }

}
