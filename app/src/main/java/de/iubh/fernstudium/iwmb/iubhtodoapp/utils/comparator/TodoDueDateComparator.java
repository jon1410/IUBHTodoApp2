package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoDueDateComparator implements Comparator<Todo> {

    @Override
    public int compare(Todo o1, Todo o2) {
        return o2.getDueDate().compareTo(o1.getDueDate());
    }

    @Override
    public Comparator<Todo> reversed() {
        return null;
    }
}
