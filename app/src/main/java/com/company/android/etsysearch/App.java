package com.company.android.etsysearch;

import android.app.Application;

import timber.log.Timber;


public class App extends Application {
    public static Listing currentListing;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }



}