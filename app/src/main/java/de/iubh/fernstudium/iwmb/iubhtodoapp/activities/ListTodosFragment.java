package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.TodoApplication;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.TodoAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.services.TodoDBService;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class ListTodosFragment extends Fragment {

    TodoAdapter todoAdapter;
    TodoDBService todoDBService;
    String currentUser;
    boolean onlyTodosForCurrentDate;
    List<Todo> todos;

    private OnListFragmentInteractionListener fragmentInteractionListener;

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Todo todo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = getArguments().getString(Constants.CURR_USER_KEY);
        onlyTodosForCurrentDate = getArguments().getBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY);
        todoDBService = new TodoDBService(getDataStore());
        todos = getTodosForUser(onlyTodosForCurrentDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_todo_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewListTodoFragment);

        todoAdapter = new TodoAdapter(getTodosForUser(onlyTodosForCurrentDate));
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            fragmentInteractionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    private ReactiveEntityStore<Persistable> getDataStore() {
        return ((TodoApplication) getActivity().getApplication()).getDataStore();
    }

    private List<Todo> getTodosForUser(boolean onlyTodosForCurrentDate) {
        if(onlyTodosForCurrentDate){
            return todoDBService.getTodosAsListForUserAndCurrentDate(currentUser);
        }
        return todoDBService.getTodosAsListForUser(currentUser);
    }
}
