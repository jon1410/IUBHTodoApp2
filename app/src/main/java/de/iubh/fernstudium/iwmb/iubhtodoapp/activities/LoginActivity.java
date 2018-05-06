package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.User;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.UserDBService;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.CreateNewUserDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.InvalidPasswordException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.UserNotFoundException;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.PasswordUtil;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, CreateNewUserDialog.CreateNewUserDialogListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View progressView;
    private View loginFormView;
    private UserDBService userDBService;
    private TodoDBService todoDBService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailView = findViewById(R.id.email);
        populateAutoComplete();

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button emailSignInButton = findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        ReactiveEntityStore<Persistable> entityDataStore = ((TodoApplication) getApplication()).getDataStore();
        userDBService = new UserDBService(entityDataStore);
        todoDBService = new TodoDBService(entityDataStore);

        //init Data for Tests
        initTestData();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(emailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        emailView.setError(null);
        passwordView.setError(null);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            doLogin(email, password);
        }
    }

    private void doLogin(String email, String password) {

        User user = null;
        try {
            user = userDBService.getUser(email);
        } catch (UserNotFoundException e) {
            //user does not exist -> show Dialog to create it
            CreateNewUserDialog dialog = new CreateNewUserDialog();
            dialog.show(getFragmentManager(), "createUserDialog");
            return;
        }

        try {
            boolean passwordsEq = PasswordUtil.checkPw(password, user.getEncryptedPw());
            if(!passwordsEq){
                showLoginError(getString(R.string.error_not_matching_password));
            }
        } catch (InvalidPasswordException e) {
            showLoginError(getString(R.string.error_incorrect_password));
        }
        showProgress(false);
        Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra(Constants.CURR_USER_KEY, user.getUserName());
        startActivity(intent);
    }

    private void showLoginError(String errorMsg) {
        passwordView.setError(errorMsg);
        passwordView.requestFocus();
        showProgress(false);
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }


    private void initTestData() {
        String adminUser = "admin@todo.de";
        try {
            User admin = userDBService.getUser(adminUser);
        } catch (UserNotFoundException e) {
            userDBService.createUser(adminUser, "admin");
            for (int i=0; i<=5; i++){
                if(i==3){
                    Todo t = todoDBService.createTodo("Test-Todo " + i, "Todo-Title " + i, Calendar.getInstance(), adminUser, true);
                }else{
                    todoDBService.createTodo("Test-Todo " + i, "Todo-Title " + i, Calendar.getInstance(), adminUser);
                }
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailView.setAdapter(adapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        showProgress(false);
        boolean created = userDBService.createUser(emailView.getText().toString(), passwordView.getText().toString());
        if(created){
            Toast.makeText(this,getString(R.string.dialog_user_created), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,getString(R.string.dialog_user_could_not_be_created), Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        showProgress(false);
        Toast.makeText(this,getString(R.string.dialog_user_not_created), Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

