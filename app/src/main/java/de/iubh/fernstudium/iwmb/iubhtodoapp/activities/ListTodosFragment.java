package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
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
        Log.v("settingTodos", sortedTodos.toString());
        todoAdapter = new TodoAdapter(sortedTodos);
        todoAdapter.notifyDataSetChanged();
        reloadFragment();
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(view.getContext(), "Clicked Item on Position: " + position, Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(this.getContext(), TodoDetailActivity.class);
        detailIntent.putExtra(Constants.SEL_TODO_KEY, todos.get(position));
        startActivity(detailIntent);
    }

    @Override
    public void onLongClick(View view, int position) {
        //TODO: create Dialog for Delete
        Toast.makeText(view.getContext(), "Long Click Item on Position: " + position, Toast.LENGTH_LONG).show();
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
}
