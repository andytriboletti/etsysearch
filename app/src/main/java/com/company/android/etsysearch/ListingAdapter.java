package com.company.android.etsysearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListingAdapter extends ArrayAdapter<Listing> {


    public ListingAdapter(Context context, int textViewResourceId, ArrayList<Listing> items) {
        super(context, textViewResourceId, items);
    }




    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.row, parent, false);
        }

        Listing myListing = getItem(position);
        TextView title = (TextView)convertView.findViewById(R.id.titleTextView);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(myListing.getImage()).into(imageView);

        title.setText(myListing.getTitle());


        return convertView;
    }
}