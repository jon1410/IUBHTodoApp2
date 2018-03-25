package de.iubh.fernstudium.iwmb.iubhtodoapp.domain;

import android.content.res.Resources;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;

/**
 * Created by ivanj on 03.03.2018.
 */

public enum TodoStatus {


    OPEN(TodoApplication.resources.getString(R.string.status_open)),
    IN_PROGRESS(TodoApplication.resources.getString(R.string.status_in_progress)),
    DONE(TodoApplication.resources.getString(R.string.status_done));

    private String statusText;

    TodoStatus(String text) {
        this.statusText = text;
    }

    public String getStatusText() {
        return statusText;
    }
}
