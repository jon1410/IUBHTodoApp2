package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoStatusOpenComparator implements Comparator<Todo> {

    @Override
    public int compare(Todo o1, Todo o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }

}
