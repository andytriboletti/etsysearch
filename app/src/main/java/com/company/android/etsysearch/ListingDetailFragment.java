package com.company.android.etsysearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by andytriboletti on 7/18/16.
 */

public class ListingDetailFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListingDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listing_detail, container, false);

//        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            // ((TextView) rootView.findViewById(R.id.account_detail)).setText(mItem.details);
//        }

       // setContentView(R.layout.fragment_listing_detail);

        ButterKnife.bind(rootView);

        return rootView;
    }


    @Bind(R.id.titleTextView)

    TextView titleTextView;
    @Bind(R.id.priceTextView)

    TextView priceTextView;
    @Bind(R.id.imageView)

    ImageView imageView;
    @Bind(R.id.webView)

    WebView webView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        Listing listing = App.currentListing;

        String title = listing.getTitle();
        String content = listing.getDescription();
        String price = listing.getPrice();
        titleTextView.setText(title);
        priceTextView.setText(price);
        try {
            webView.loadData(URLEncoder.encode(content, "utf-8"), "text/html", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        webView.loadData(content, "text/html; charset=UTF-8", null);
        Timber.d(content);
        getActivity().setTitle(title);
        Picasso.with(getActivity()).load(listing.getImage()).into(imageView);

//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return true;
    }

}
