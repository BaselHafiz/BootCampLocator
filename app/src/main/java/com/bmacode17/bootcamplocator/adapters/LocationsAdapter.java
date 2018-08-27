package com.bmacode17.bootcamplocator.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmacode17.bootcamplocator.R;
import com.bmacode17.bootcamplocator.holders.LocationsViewHolder;
import com.bmacode17.bootcamplocator.models.LocationData;

import java.util.ArrayList;

/**
 * Created by User on 09-Apr-18.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder> {

    private ArrayList<LocationData> locations;

    public LocationsAdapter(ArrayList<LocationData> locations) {
        this.locations = locations;
    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View locationLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_location,parent,false);

        LocationsViewHolder viewHolder = new LocationsViewHolder(locationLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationsViewHolder holder, int position) {

        final LocationData loc = locations.get(position);
        holder.updateUI(loc);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
