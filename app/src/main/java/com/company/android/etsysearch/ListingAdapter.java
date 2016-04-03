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
import java.util.List;


public class ListingAdapter extends ArrayAdapter<Listing> {
    private ArrayList<Listing> items;


    public ListingAdapter(Context context, ArrayList<Listing> items) {
        super(context, R.layout.row, items);
        this.items = items;
    }




    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.row, parent, false);
        }

        Listing myListing = getItem(position);
        TextView title = (TextView)convertView.findViewById(R.id.titleTextView);
        TextView price = (TextView)convertView.findViewById(R.id.priceTextView);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(myListing.getImage()).into(imageView);

        title.setText(myListing.getTitle());
        price.setText(myListing.getPrice());


        return convertView;
    }

    public void refill(List<Listing> listings) {
        this.items.clear();
        this.items.addAll(listings);
        notifyDataSetChanged();
    }
}