package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs.OverviewOnLongClickDialog;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.CalendarUtils;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.TodoSorter;

public class OverviewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OverviewOnLongClickDialog.OverviewOnLongClickDialogListener {

    String currentUser;
    ListTodosFragment fragmentTodosForToday;
    ListTodosFragment fragmentAllTodos;
    ListTodosFragment fragmentDoneTodos;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    Spinner sortSpinner;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.overview_activity);

        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);

        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        fragmentTodosForToday = new ListTodosFragment();
        Bundle bundleForTodosForToday = new Bundle();
        bundleForTodosForToday.putString(Constants.CURR_USER_KEY, currentUser);
        bundleForTodosForToday.putString(Constants.ORDER_BY_KEY, Constants.ORDER_BY_DATE);
        bundleForTodosForToday.putBoolean(Constants.ONLY_DONE_TODOS_KEY, false);
        bundleForTodosForToday.putBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY, true);
        fragmentTodosForToday.setArguments(bundleForTodosForToday);

        fragmentAllTodos = new ListTodosFragment();
        Bundle bundleForAllTodos = new Bundle();
        bundleForAllTodos.putString(Constants.CURR_USER_KEY, currentUser);
        bundleForTodosForToday.putString(Constants.ORDER_BY_KEY, Constants.ORDER_BY_DATE);
        bundleForAllTodos.putBoolean(Constants.ONLY_DONE_TODOS_KEY, false);
        bundleForAllTodos.putBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY, false);
        fragmentAllTodos.setArguments(bundleForAllTodos);

        fragmentDoneTodos = new ListTodosFragment();
        Bundle bundleForDoneTodos = new Bundle();
        bundleForDoneTodos.putString(Constants.CURR_USER_KEY, currentUser);
        bundleForDoneTodos.putBoolean(Constants.ONLY_DONE_TODOS_KEY, true);
        fragmentDoneTodos.setArguments(bundleForDoneTodos);

        // Add Fragments to adapter one by one
        adapter.addFragment(fragmentTodosForToday, getText(R.string.fragment_today_title).toString());
        adapter.addFragment(fragmentAllTodos, getText(R.string.fragment_all_todos_title).toString());
        adapter.addFragment(fragmentDoneTodos, getText(R.string.fragment_done_title).toString());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(sortSpinner != null){
                    sortSpinner.setSelection(0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.sortTodosSpinner);
        sortSpinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> dropDownValuesAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerDropdown, android.R.layout.simple_spinner_item);
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(dropDownValuesAdapter);
        sortSpinner.setOnItemSelectedListener(this);
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String changeType = data.getStringExtra(Constants.CHANGE_TYPE_KEY);
            switch (changeType){
                case Constants.CHANGE_TYPE_INSERT:
                    handleInsertCallBack(data);
                    break;
                case Constants.CHANGE_TYPE_UPDATE:
                    handleUpdateCallback(data);
                default: break;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.addTodo:
                Intent newTodoActivityIntent = new Intent(this, NewTodoActivity.class);
                newTodoActivityIntent.putExtra(Constants.CURR_USER_KEY, currentUser);
                startActivityForResult(newTodoActivityIntent, 1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        int index = viewPager.getCurrentItem();
        ListTodosFragment currentFragment = (ListTodosFragment) adapter.getItem(index);
        List<Todo> sortedTodos = sortTodos(currentFragment.getTodos(), value);
        currentFragment.updateTodos(sortedTodos);
    }

    @Override
    public void onShowTodoDetails(DialogFragment dialog) {
        ListTodosFragment currentFragment = getCurrentFragment();
        int position = currentFragment.selectedPositionForLongClick;
        currentFragment.showDetail(position);
    }

    @Override
    public void onDeleteTodo(DialogFragment dialog) {
        ListTodosFragment currentFragment = getCurrentFragment();
        int todoIdToDelete = currentFragment.getTodoIdForSelectedTodo();
        fragmentAllTodos.deleteTodo(todoIdToDelete);
        fragmentTodosForToday.deleteTodo(todoIdToDelete);
        fragmentDoneTodos.deleteTodo(todoIdToDelete);
    }

    @Override
    public void onFinishTodo(DialogFragment dialog) {
        ListTodosFragment currentFragment = getCurrentFragment();
        Todo selectedTodo = currentFragment.getSelectedTodo();
        Todo changedTodo = currentFragment.changeStatusToDone(selectedTodo);
        fragmentDoneTodos.addIfNotExists(changedTodo);
        fragmentTodosForToday.removeIfExists(changedTodo);
        fragmentAllTodos.removeIfExists(changedTodo);
    }

    private void handleUpdateCallback(Intent data) {
        boolean updated = data.getBooleanExtra(Constants.TODO_CHANGED_KEY, false);
        if(updated) {
            //Depending, if Tab in Viepager was already opened
            //not all Fragments may be initiliazed
            if(fragmentDoneTodos.isInitialized()){
                fragmentDoneTodos.reloadDoneTodos();
            }

            if(fragmentTodosForToday.isInitialized()){
                fragmentTodosForToday.reloadTodosForToday();
            }

            if(fragmentAllTodos.isInitialized()){
                fragmentAllTodos.reloadAllTodos();
            }
        }
    }

    private void handleInsertCallBack(Intent data) {
        Todo newTodo = data.getParcelableExtra(Constants.NEW_TODO_KEY);
        if(CalendarUtils.isToday(new Date(newTodo.getDueDate().getTime()))){
            fragmentTodosForToday.addNewTodo(newTodo);
        }
        fragmentAllTodos.addNewTodo(newTodo);
    }

    private ListTodosFragment getCurrentFragment(){
        int index = viewPager.getCurrentItem();
        ListTodosFragment currentFragment = (ListTodosFragment) adapter.getItem(index);
        return currentFragment;
    }

    private List<Todo> sortTodos(List<Todo> todos, String sortBy) {
        List<Todo> sortedTodos;

        if(TextUtils.isEmpty(sortBy)){
            return todos;
        }

        if(Constants.ORDER_BY_FAV.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByFavoriteFlag(todos);
        }else if(Constants.ORDER_BY_STATUS_IN_PROGRESS.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByStatusInProgress(todos);
        }else if(Constants.ORDER_BY_STATUS_OPEN.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByStatusOpen(todos);
        }else if(Constants.ORDER_BY_DATE.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByDueDate(todos);
        }else if(sortBy.startsWith(Constants.ORDER_BY_DB)){
            sortedTodos = TodoSorter.sortTodosById(todos);
        }
        else{
            sortedTodos = todos;
        }

        return sortedTodos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Nothing clicked!", Toast.LENGTH_LONG).show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}

