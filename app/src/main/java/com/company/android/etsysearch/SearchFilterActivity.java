package com.company.android.etsysearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFilterActivity extends AppCompatActivity  {
    @Bind(R.id.minPrice)
    EditText minPrice;
    @Bind(R.id.maxPrice)
    EditText maxPrice;
    private SharedPreferences prefs;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        ButterKnife.bind(this);
        prefs = this.getSharedPreferences(CommonConstants.PREFS, Context.MODE_PRIVATE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String minPriceString = prefs.getString(CommonConstants.MIN_PRICE, "");
        minPrice.setText(minPriceString);

        String maxPriceString = prefs.getString(CommonConstants.MAX_PRICE, "");
        maxPrice.setText(maxPriceString);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    @OnClick(R.id.save)
    public void save() {
        Float minPriceFloat;
        Float maxPriceFloat;
        String minPriceString;
        String maxPriceString;
        try {
            minPriceFloat = Float.valueOf(minPrice.getText().toString());
            minPriceString = String.valueOf(minPriceFloat);
        } catch (NumberFormatException e) {
            minPriceString = "";
        }

        try {
            maxPriceFloat = Float.valueOf(maxPrice.getText().toString());
            maxPriceString = String.valueOf(maxPriceFloat);

        } catch (NumberFormatException e) {
            maxPriceString = "";

        }

        prefs.edit().putString(CommonConstants.MIN_PRICE, minPriceString).apply();
        prefs.edit().putString(CommonConstants.MAX_PRICE, maxPriceString).apply();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
