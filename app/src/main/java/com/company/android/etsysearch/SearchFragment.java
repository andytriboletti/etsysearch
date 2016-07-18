package com.company.android.etsysearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.company.android.etsysearch.thirdparty.EndlessRecyclerOnScrollListener;
import com.company.android.etsysearch.thirdparty.RecyclerViewEmptySupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by andytriboletti on 7/18/16.
 */

public class SearchFragment extends Fragment implements ListingInterface {


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
    private MyOnClickListener myOnClickListener;
    private LinearLayoutManager linearLayoutManager;


    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        setHasOptionsMenu(true);
        recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.recycler_view);

        emptyView = (TextView) rootView.findViewById(android.R.id.empty);
        recyclerView.setEmptyView(emptyView);

        adapter = new ListingAdapter(myListings, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ButterKnife.bind(rootView);


        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                loadMoreDataFromApi(page);

            }
        });

        refill();


        return rootView;
    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            this.query = savedInstanceState.getString(QUERY);
            this.myListings = savedInstanceState.getParcelableArrayList(LISTINGS);

            refill();
        }

    }



    private ListingAdapter getAdapter() {
        if(adapter == null) {
            adapter = new ListingAdapter(myListings, this);
        }

        return adapter;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        client = new OkHttpClient();
        adapter = getAdapter();
        myOnClickListener = new MyOnClickListener();

        prefs = getActivity().getSharedPreferences(CommonConstants.PREFS, Context.MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(getActivity());


    }

    private void refill() {
        if(myListings != null && myListings.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);

        }
        else {
            emptyView.setVisibility(View.GONE);
            adapter.refill(myListings);
            adapter.notifyDataSetChanged();
        }

        if(query != null) {
            getActivity().setTitle(query);
        }
    }


    void loadListings(final String query, final int page, final boolean clear) {
        new Thread()
        {
            public void run() {
                try {
                    String minPriceString = prefs.getString(CommonConstants.MIN_PRICE, "");
                    String maxPriceString = prefs.getString(CommonConstants.MAX_PRICE, "");

                    String url = "https://api.etsy.com/v2/listings/active?api_key=" + CommonConstants.API_KEY +
                            "&includes=MainImage&page=" + page + "&keywords=" + query;
                    if(!minPriceString.equals("")) {
                        url +="&min_price=" + minPriceString;
                    }
                    if(!maxPriceString.equals("")) {
                        url+="&max_price=" + maxPriceString;
                    }
                    String response = SearchFragment.this.run(url);
                    Timber.d(response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                    if(clear) {
                        myListings = new ArrayList<Listing>();
                    }
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject row = results.getJSONObject(i);
                        if(row.has("title")) {
                            String title = row.getString("title");
                            String price = row.getString("price");
                            String description = row.getString("description");
                            JSONObject mainImage = row.getJSONObject("MainImage");
                            String image = mainImage.getString("url_fullxfull");
                            Listing myListing = new Listing(title, image, description, price);
                            myListings.add(myListing);
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emptyView.setVisibility(View.GONE);

                            adapter.refill(myListings);
                            adapter.notifyDataSetChanged();


                        }
                    });


                    Timber.d("done");
                } catch (IOException | JSONException e) {
                    Timber.e("exception", e);
                    e.printStackTrace();
                }

            }

        }.start();


    }


    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // Append more data into the adapter
    private void loadMoreDataFromApi(int offset) {
        Timber.d("load more");
        loadListings(this.query, offset, false);

    }

    public MyOnClickListener getClickListener() {
        return myOnClickListener;
    }
        class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = recyclerView.getChildPosition(v);
            Timber.d("Clicked and Position is ",String.valueOf(i));

            Timber.d("clicked " + i);
            Listing thisListing = myListings.get(i);
            Timber.d("going to item: " + thisListing.getTitle());
            App.currentListing = thisListing;
            Intent intent = new Intent(getActivity(), ListingDetailActivity.class);
            startActivity(intent);
        }
    }

}
