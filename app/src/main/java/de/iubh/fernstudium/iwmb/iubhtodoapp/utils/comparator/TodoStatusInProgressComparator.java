package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.TodoStatus;

public class TodoStatusInProgressComparator implements Comparator<Todo> {
    @Override
    public int compare(Todo o1, Todo o2) {
        //IP has higher Ordinal than OPEN
        return o2.getStatus().compareTo(o1.getStatus());
    }

}
