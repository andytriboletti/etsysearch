package com.company.android.etsysearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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


public class ListingDetail extends AppCompatActivity {
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
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        Listing listing = App.currentListing;
        setContentView(R.layout.activity_listing_detail);

        ButterKnife.bind(this);
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
        setTitle(title);
        Picasso.with(this).load(listing.getImage()).into(imageView);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

}