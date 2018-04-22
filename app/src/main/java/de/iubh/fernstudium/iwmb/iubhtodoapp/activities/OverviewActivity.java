package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.TodoSorter;

public class OverviewActivity extends AppCompatActivity implements ListTodosFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    String currentUser;
    ListTodosFragment fragmentTodosForToday;
    ListTodosFragment fragmentAllTodos;
    ViewPagerAdapter adapter;
    ViewPager viewPager;

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
        bundleForTodosForToday.putBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY, true);
        fragmentTodosForToday.setArguments(bundleForTodosForToday);

        fragmentAllTodos = new ListTodosFragment();
        Bundle bundleForAllTodos = new Bundle();
        bundleForAllTodos.putString(Constants.CURR_USER_KEY, currentUser);
        bundleForTodosForToday.putString(Constants.ORDER_BY_KEY, Constants.ORDER_BY_DATE);
        bundleForAllTodos.putBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY, false);
        fragmentAllTodos.setArguments(bundleForAllTodos);

        // Add Fragments to adapter one by one
        adapter.addFragment(fragmentTodosForToday, "Todos for Today");
        adapter.addFragment(fragmentAllTodos, "All Todos");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.sortTodosSpinner);
        Spinner sortSpinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> dropDownValuesAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerDropdown, android.R.layout.simple_spinner_item);
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(dropDownValuesAdapter);
        sortSpinner.setOnItemSelectedListener(this);
        dropDownValuesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.addTodo:
                Toast.makeText(this, "add new Todo clicked!", Toast.LENGTH_LONG).show();
                Intent newTodoActivityIntent = new Intent(this, NewTodoActivity.class);
                newTodoActivityIntent.putExtra(Constants.CURR_USER_KEY, currentUser);
                startActivity(newTodoActivityIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onListFragmentInteraction(Todo todo) {
        Toast.makeText(this, "selected an ITEM", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        int index = viewPager.getCurrentItem();
        ListTodosFragment currentFragment = (ListTodosFragment) adapter.getItem(index);
        List<Todo> sortedTodos = sortTodos(currentFragment.getTodos(), value);
        currentFragment.updateTodos(sortedTodos);
    }

    private List<Todo> sortTodos(List<Todo> todos, String sortBy) {
        List<Todo> sortedTodos;

        if(TextUtils.isEmpty(sortBy)){
            return todos;
        }

        if(Constants.ORDER_BY_FAV.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByFavoriteFlag(todos);
        }else if(Constants.ORDER_BY_STATUS.equalsIgnoreCase(sortBy)){
            sortedTodos = TodoSorter.sortTodosByStatus(todos);
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

