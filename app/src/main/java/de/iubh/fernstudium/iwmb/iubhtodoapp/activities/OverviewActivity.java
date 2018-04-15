package de.iubh.fernstudium.iwmb.iubhtodoapp.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.Constants;

public class OverviewActivity extends AppCompatActivity {

    String currentUser;

    @Override
    public void onCreate(Bundle savedInstance){
        //TODO - change LoginActivity to call this one instead of ListTodosActivity
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

