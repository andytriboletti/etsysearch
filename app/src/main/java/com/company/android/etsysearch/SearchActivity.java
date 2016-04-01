package com.company.android.etsysearch;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {
    OkHttpClient client;
    ListingAdapter adapter;
    ListView myListView;
    ArrayList<Listing> myListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client = new OkHttpClient();
        myListView = (ListView) findViewById(R.id.listView);

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
                adapter=null;
                loadPosts(query);
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

        return false;
   }

    public void loadPosts(final String query) {
        new Thread()
        {
            public void run() {
                try {
                    String API_KEY = "liwecjs0c3ssk6let4p1wqt9";
                    String url = "https://api.etsy.com/v2/listings/active?api_key=" + API_KEY + "&includes=MainImage&keywords=" + query;
                    String response = SearchActivity.this.run(url);
                    Timber.d(response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                myListings = new ArrayList<Listing>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject row = results.getJSONObject(i);
                String title = row.getString("title");
                JSONObject mainImage = row.getJSONObject("MainImage");
                String image = mainImage.getString("url_fullxfull");
                Listing myListing = new Listing(title, image);
                myListings.add(myListing);
            }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = getAdapter();


                            myListView.setAdapter(adapter);

                        }
                    });


                    Timber.d("done");
                } catch (IOException e) {
                    Timber.e("exception", e);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Timber.e("exception", e);

                    e.printStackTrace();
                }

                    }

        }.start();


    }
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public ListingAdapter getAdapter() {
        ListingAdapter p;
        if(adapter == null) {
            p = new ListingAdapter(SearchActivity.this, R.layout.row, myListings);
        }
        else {
            p = adapter;
        }
        return p;
    }
}

