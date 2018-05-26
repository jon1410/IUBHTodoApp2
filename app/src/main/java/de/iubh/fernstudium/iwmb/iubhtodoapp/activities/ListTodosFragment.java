package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.OverviewOnLongClickDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.listener.RecyclerViewItemClickListener;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.TodoAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class ListTodosFragment extends Fragment implements RecyclerViewItemClickListener.OnItemClickListener {

    TodoAdapter todoAdapter;
    TodoDBService todoDBService;
    String currentUser;
    boolean onlyTodosForCurrentDate;
    String orderBy;
    List<Todo> todos;
    int selectedPositionForLongClick = -1;
    int listIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getArguments().getString(Constants.CURR_USER_KEY);
        onlyTodosForCurrentDate = getArguments().getBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY);
        orderBy = getArguments().getString(Constants.ORDER_BY_KEY);
        todoDBService = new TodoDBService(getDataStore());
        todos = getTodosForUser(onlyTodosForCurrentDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("ActivityResultFragment", "sfsdgsg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_todo_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewListTodoFragment);

        todoAdapter = new TodoAdapter(todos);
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(rootView.getContext(), recyclerView, this));
        return rootView;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void updateTodos(List<Todo> sortedTodos) {
        todoAdapter = new TodoAdapter(sortedTodos);
        todoAdapter.notifyDataSetChanged();
        reloadFragment();
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(view.getContext(), "Clicked Item on Position: " + position, Toast.LENGTH_SHORT).show();
        showDetail(position);
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(view.getContext(), "Long Click Item on Position: " + position, Toast.LENGTH_LONG).show();
        selectedPositionForLongClick = position;
        DialogFragment onLongClickDialog = new OverviewOnLongClickDialog();
        onLongClickDialog.show(getActivity().getSupportFragmentManager(), "SHOW_LONG_CLICK_DIALOG");
    }

    private void reloadFragment() {
        FragmentTransaction fragmentTransaction = (getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getActivity().getApplication()).getDataStore();
    }

    private List<Todo> getTodosForUser(boolean onlyTodosForCurrentDate) {
        if (onlyTodosForCurrentDate) {
            return todoDBService.getTodosAsListForUserAndCurrentDate(currentUser);
        }
        return todoDBService.getTodosAsListForUser(currentUser);
    }

    public void showDetail(int position) {
        Intent detailIntent = new Intent(getContext(), TodoDetailActivity.class);
        detailIntent.putExtra(Constants.SEL_TODO_KEY, todos.get(position));
        startActivityForResult(detailIntent, 1);
    }

    public int getTodoIdForSelectedTodo() {
        Todo todo = todos.get(selectedPositionForLongClick);
        if (todo != null) {
            return todo.getId();
        } else {
            return -1;
        }
    }

    public void deleteTodo(int todoId) {
        Todo todoToDelete = getTodoForId(todoId);
        if (todoToDelete != null && listIndex >= 0) {
            todoDBService.deleteTodo(todoToDelete.getId());
            if (todoToDelete.getFileName() != null) {
                File f = new File(todoToDelete.getFileName());
                f.delete();
            }
            removeTodo();
        }
    }

    public void addNewTodo(Todo todo) {
        int size = todos.size();
        todos.add(size, todo);
        todoAdapter.notifyItemInserted(size);
        reloadFragment();
    }

    public void reloadChangedTodo(Todo todo) {
        Todo t = getTodoForId(todo.getId());
        if (todo != null && listIndex >= 0) {
            reloadTodo(todo);
        }
    }

    public void addIfNotExists(Todo todo) {
        Todo t = getTodoForId(todo.getId());
        if (todo != null && listIndex >= 0) {
            reloadTodo(todo);
        }else{
            addNewTodo(todo);
        }
    }

    public void removeIfExists(Todo todo) {
        Todo t = getTodoForId(todo.getId());
        if (todo != null && listIndex >= 0) {
            removeTodo();
        }
    }

    private void removeTodo() {
        todos.remove(listIndex);
        todoAdapter.notifyItemRemoved(listIndex);
        listIndex = 0;
        reloadFragment();
    }

    private void reloadTodo(Todo todo){
        todos.set(listIndex, todo);
        todoAdapter.notifyItemChanged(listIndex);
        listIndex = 0;
        reloadFragment();
    }

    private Todo getTodoForId(int id) {
        for (int i = 0; i < todos.size(); i++) {
            Todo t = todos.get(i);
            if (t.getId() == id) {
                listIndex = i;
                return t;
            }
        }
        listIndex = -1;
        return null;
    }

}
