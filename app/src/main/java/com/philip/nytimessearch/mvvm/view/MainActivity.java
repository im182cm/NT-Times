package com.philip.nytimessearch.mvvm.view;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import dagger.android.support.DaggerAppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.philip.nytimessearch.R;

public class MainActivity extends DaggerAppCompatActivity {
    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, listFragment, ListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listFragment.querySubmit(query);
                // prevent calling querySubmit twice.
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            item.expandActionView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
