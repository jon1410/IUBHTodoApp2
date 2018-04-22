package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import java.util.Collections;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator.TodoDueDateComparator;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator.TodoFavoriteFlagComparator;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator.TodoIdComparator;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.comparator.TodoStatusComparator;

public class TodoSorter {

    public static List<Todo> sortTodosByFavoriteFlag(List<Todo> todos){
        Collections.sort(todos, new TodoFavoriteFlagComparator());
        return todos;
    }

    public static List<Todo> sortTodosByDueDate(List<Todo> todos){
        Collections.sort(todos, new TodoDueDateComparator());
        return todos;
    }

    public static List<Todo> sortTodosByStatus(List<Todo> todos){
        Collections.sort(todos, new TodoStatusComparator());
        return todos;
    }

    public static List<Todo> sortTodosById(List<Todo> todos){
        Collections.sort(todos, new TodoIdComparator());
        return todos;
    }
}
