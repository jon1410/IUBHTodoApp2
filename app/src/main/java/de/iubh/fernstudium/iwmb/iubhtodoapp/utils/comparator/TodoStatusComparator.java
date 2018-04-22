package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoStatusComparator implements Comparator<Todo> {

    @Override
    public int compare(Todo o1, Todo o2) {
        return 0;
    }

    @Override
    public Comparator<Todo> reversed() {
        return null;
    }
}
