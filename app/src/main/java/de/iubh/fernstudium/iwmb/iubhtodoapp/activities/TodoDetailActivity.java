package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.DatePickerFragment;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.SendEmailDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.ContactListAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.TodoEntity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.CalendarUtils;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.EmailUtil;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ITextUtil;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class TodoDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, SendEmailDialog.SendEmailDialogListener {

    private Todo selectedTodo;
    private boolean favStatus;
    private TodoDBService todoDBService;
    private ITextUtil iTextUtil;
    private ProgressBar progressBar;
    private ImageButton pdfButton;
    private ContactDTO selectedContact;
    AutoCompleteTextView autoCompleteTextView;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iTextUtil = new ITextUtil();
        todoDBService = new TodoDBService(getDataStore());
        setContentView(R.layout.todo_detail_activity);
        progressBar = findViewById(R.id.detailProgress);
        pdfButton = findViewById(R.id.idButtomShowPdf);
        selectedTodo = getIntent().getParcelableExtra(Constants.SEL_TODO_KEY);
        autoCompleteTextView = findViewById(R.id.idLinkedToDetailAutoCompleteContent);
        contactListAdapter = new ContactListAdapter(this, R.layout.contact_item, ContactUtils.getContacts());
        autoCompleteTextView.setAdapter(contactListAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = (ContactDTO) autoCompleteTextView.getAdapter().getItem(position);
                Toast.makeText(TodoDetailActivity.this, "Clicked " + selectedContact.getName(), Toast.LENGTH_LONG).show();
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
                if(TextUtils.isEmpty(selectedTodo.getFileName())){
                    sendPlainTextEmail();
                }else{
                    SendEmailDialog sendEmailDialog = new SendEmailDialog();
                    sendEmailDialog.show(getSupportFragmentManager(), "SENDMAILATTACH");
                }
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
        String statusEdt = ((EditText) findViewById(R.id.idStatusDetailContent)).getText().toString();
        String dueDateEdt = ((EditText) findViewById(R.id.idDueDateDetailContent)).getText().toString();
        ToggleButton favouriteBtn = findViewById(R.id.idFavDetailButton);
        int contactId = 0;
        if(selectedContact != null){
            contactId = selectedContact.getId();
        }

        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTitle(title);
        todoEntity.setDescription(description);
        todoEntity.setFileName(selectedTodo.getFileName());
        todoEntity.setStatus(TodoStatus.fromValue(statusEdt));
        todoEntity.setDueDate(CalendarUtils.fromStringToTimestamp(dueDateEdt));
        todoEntity.setFavoriteFlag(favouriteBtn.isChecked());
        todoEntity.setContactId(contactId);

        Todo changedTodo = todoDBService.changeTodo(selectedTodo.getId(), todoEntity);
        String toastText = null;
        if(changedTodo != null){
            selectedTodo = changedTodo;
            populateView();
            toastText = getText(R.string.ok_change_todo).toString();
        }else{
            toastText = getText(R.string.error_change_todo).toString();
        }
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    public void onClickShowPdf(View view){
        Intent pdfIntent = new Intent(this, PDFActivity.class);
        pdfIntent.putExtra(Constants.FILENAME_KEY, selectedTodo.getFileName());
        startActivity(pdfIntent);
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
                Todo updatedTodo = todoDBService.updateFileName(selectedTodo.getId(), fileName);
                selectedTodo = updatedTodo;
                pdfButton.setVisibility(View.VISIBLE);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        sendEmailWithAttachment();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        sendPlainTextEmail();
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
        if(TextUtils.isEmpty(selectedTodo.getFileName())){
            pdfButton.setVisibility(View.INVISIBLE);
        }else{
            pdfButton.setVisibility(View.VISIBLE);
        }
        if(selectedTodo.getContactId() > 0){
            String contactName = ContactUtils.getContactName(selectedTodo.getContactId());
            if(!TextUtils.isEmpty(contactName)){
                autoCompleteTextView.setText(contactName);
            }
        }
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

    private void sendEmailWithAttachment() {
        Intent emailIntent = createBaseEmailIntent();
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_content_attachment).toString());
        String fileName = getFilesDir() + "/" + selectedTodo.getFileName();
        Uri attachmentUri = FileProvider.getUriForFile(this, "de.iubh.fernstudium.iwmb.iubhtodoapp.fileprovider", new File(fileName));
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);

        startActivity(Intent.createChooser(emailIntent, "Send mail with Attachment"));
    }

    private void sendPlainTextEmail() {
        Intent emailIntent = createBaseEmailIntent();
        emailIntent.putExtra(Intent.EXTRA_TEXT, EmailUtil.createEmailPlainTextContent(selectedTodo));
        startActivity(Intent.createChooser(emailIntent, "Send mail"));
    }

    private Intent createBaseEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); TODO: evtl. check Email-Adress
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject).toString());
        return emailIntent;
    }

}
