package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;
import de.iubh.fernstudium.iwmb.iubhtodoapp.db.entities.Todo;

public class OverviewActivity extends AppCompatActivity implements ListTodosFragment.OnListFragmentInteractionListener {

    String currentUser;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.overview_activity);

        currentUser = getIntent().getStringExtra(Constants.CURR_USER_KEY);

        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ListTodosFragment fragmentTodosForToday = new ListTodosFragment();
        Bundle bundleForTodosForToday = new Bundle();
        bundleForTodosForToday.putString(Constants.CURR_USER_KEY, currentUser);
        bundleForTodosForToday.putBoolean(Constants.SHOW_TODOS_FOR_TODAY_KEY, true);
        fragmentTodosForToday.setArguments(bundleForTodosForToday);

        ListTodosFragment fragmentAllTodos = new ListTodosFragment();
        Bundle bundleForAllTodos = new Bundle();
        bundleForAllTodos.putString(Constants.CURR_USER_KEY, currentUser);
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

