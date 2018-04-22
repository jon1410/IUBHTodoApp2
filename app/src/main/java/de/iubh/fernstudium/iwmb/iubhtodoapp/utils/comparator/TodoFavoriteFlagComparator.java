package de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator;

import java.util.Comparator;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class TodoFavoriteFlagComparator implements Comparator<Todo> {
    @Override
    public int compare(Todo o1, Todo o2) {
        return Boolean.compare(o2.getFavoriteFlag(), o1.getFavoriteFlag());
    }
}
