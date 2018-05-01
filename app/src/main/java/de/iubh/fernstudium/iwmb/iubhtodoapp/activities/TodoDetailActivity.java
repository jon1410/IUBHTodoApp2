package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Date;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoDetailActivity extends AppCompatActivity {

    private Todo selectedTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);
        selectedTodo = getIntent().getParcelableExtra(Constants.SEL_TODO_KEY);
        populateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_todo_activity, menu);
        return super.onCreateOptionsMenu(menu);
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
        RadioButton favouriteBtn = findViewById(R.id.idFavDetailButton);
        favouriteBtn.setChecked(selectedTodo.getFavoriteFlag());
        //TODO: add Link to Contact in TODO
    }
}
