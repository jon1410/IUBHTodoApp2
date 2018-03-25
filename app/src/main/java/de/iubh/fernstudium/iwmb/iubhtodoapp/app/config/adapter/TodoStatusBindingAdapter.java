package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.sql.Timestamp;

import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;

/**
 * Created by ivanj on 25.03.2018.
 */

public class TodoStatusBindingAdapter {

    @BindingAdapter("todoStatus")
    public static void setStatus(TextView textView, TodoStatus status){
        textView.setText(status.getStatusText());
    }

}
