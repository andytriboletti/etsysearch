package com.company.android.etsysearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {
    private OkHttpClient client;
    private ListingAdapter adapter;
    private ListView listView;
    private ArrayList<Listing> myListings = new ArrayList<Listing>();
    private String query;
    private TextView emptyView;
    private static String QUERY = "query";
    private static String LISTINGS = "listings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client = new OkHttpClient();
        adapter = getAdapter();
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        emptyView = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Timber.d("clicked " + i);
                Listing thisListing = myListings.get(i);
                Timber.d("going to item: " + thisListing.getTitle());
                App.currentListing = thisListing;
                Intent intent = new Intent(SearchActivity.this, ListingDetail.class);
                startActivity(intent);

            }

        });

        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreDataFromApi(page);
                return true;
            }
        });


    }

    // Append more data into the adapter
    public void loadMoreDataFromApi(int offset) {
        Timber.d("load more");
        loadListings(this.query, offset, false);

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
                //adapter=null;
                SearchActivity.this.query=query;
                listView.setSelectionAfterHeaderView();
                loadListings(query, 1, true);
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

    public void loadListings(final String query, final int page, final boolean clear) {
        new Thread()
        {
            public void run() {
                try {
                    String API_KEY = "liwecjs0c3ssk6let4p1wqt9";
                    String url = "https://api.etsy.com/v2/listings/active?api_key=" + API_KEY + "&includes=MainImage&page=" + page + "&keywords=" + query;
                    String response = SearchActivity.this.run(url);
                    Timber.d(response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
               if(clear) {
                   myListings = new ArrayList<Listing>();
               }
            for (int i = 0; i < results.length(); i++) {
                JSONObject row = results.getJSONObject(i);
                String title = row.getString("title");
                String description = row.getString("description");
                JSONObject mainImage = row.getJSONObject("MainImage");
                String image = mainImage.getString("url_fullxfull");
                Listing myListing = new Listing(title, image, description);
                myListings.add(myListing);
            }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.GONE);

                            adapter.refill(myListings);
                            adapter.notifyDataSetChanged();


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
        if(adapter == null) {
            adapter = new ListingAdapter(SearchActivity.this, R.layout.row, myListings);
        }

        return adapter;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUERY, this.query);
        outState.putParcelableArrayList(LISTINGS, myListings);

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.query = savedInstanceState.getString(QUERY);
        this.myListings = savedInstanceState.getParcelableArrayList(LISTINGS);

        if(myListings != null && myListings.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);

        }
        else {
            emptyView.setVisibility(View.GONE);
            adapter.refill(myListings);
        }

        if(query != null) {
            setTitle(query);
        }

    }
}

