package com.company.android.etsysearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.company.android.etsysearch.thirdparty.RecyclerViewEmptySupport;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {
    private OkHttpClient client;
    private ListingAdapter adapter;
    private ListView listView;
    private ArrayList<Listing> myListings = new ArrayList<Listing>();
    private String query;
    private TextView emptyView;
    private static final String QUERY = "query";
    private static final String LISTINGS = "listings";
    private SharedPreferences prefs;
    private static final int SEARCH_FILTER_CODE = 1;  // The request code
    private RecyclerViewEmptySupport recyclerView;
    //private MyOnClickListener myOnClickListener;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        if(savedInstanceState == null) {
            SearchFragment fragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

        linearLayoutManager = new LinearLayoutManager(this);

    }




    @Override
    public void onResume() {
        super.onResume();

    }
    private void refresh() {
        if(query != null) {
            SearchFragment currentFragment = (SearchFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

            currentFragment.loadListings(query, 1, true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        getMenuInflater().inflate( R.menu.search, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.d(query);
                setTitle(query);
                SearchActivity.this.query=query;
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                SearchFragment currentFragment = (SearchFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);


                currentFragment.loadListings(query, 1, true);
                if( ! searchView.isIconified()) {

                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Timber.d("query");
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(SearchActivity.this, SearchFilterActivity.class);
            startActivityForResult(intent, SEARCH_FILTER_CODE);

            return true;
        }

        return true;
   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                refresh();
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUERY, this.query);
        outState.putParcelableArrayList(LISTINGS, myListings);

    }


}

