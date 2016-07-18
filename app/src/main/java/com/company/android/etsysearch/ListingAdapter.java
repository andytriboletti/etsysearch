package com.company.android.etsysearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.MyViewHolder> {
    private List<Listing> items;
    private ListingInterface context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleTextView);
            price = (TextView) view.findViewById(R.id.priceTextView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public ListingAdapter(List<Listing> itemsList, ListingInterface context) {
        this.items = itemsList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        itemView.setOnClickListener(context.getClickListener());

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Listing listing = items.get(position);
        holder.title.setText(listing.getTitle());
        holder.price.setText(listing.getPrice());
        Picasso.with(((SearchFragment)context).getActivity()).load(listing.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void refill(List<Listing> listings) {
        this.items.clear();
        this.items.addAll(listings);
        notifyDataSetChanged();
    }
}