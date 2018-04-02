package de.iubh.fernstudium.iwmb.iubhtodoapp.dialogs;

import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;


public class DatePickerFragment extends android.support.v4.app.DialogFragment {

    Context context;
    DatePickerDialog.OnDateSetListener listener;

    public void init(Context context, DatePickerDialog.OnDateSetListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(context, listener, year, month, day);
    }
}
