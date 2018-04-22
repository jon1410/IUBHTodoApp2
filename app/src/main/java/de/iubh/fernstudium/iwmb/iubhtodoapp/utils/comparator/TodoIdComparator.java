package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoIdComparator implements Comparator<Todo> {


    @Override
    public int compare(Todo o1, Todo o2) {
        return ((Integer)o1.getId()).compareTo(o2.getId());
    }
}
