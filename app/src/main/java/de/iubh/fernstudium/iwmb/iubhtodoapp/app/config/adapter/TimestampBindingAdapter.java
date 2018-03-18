package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter;

import android.databinding.BindingAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.sql.Timestamp;

/**
 * Created by ivanj on 18.03.2018.
 */

public class TimestampBindingAdapter {

    @BindingAdapter("timestamp")
    public static void setTimestamp(TextView textView, Timestamp timestamp){
        textView.setText(timestamp.toString());
    }
}
