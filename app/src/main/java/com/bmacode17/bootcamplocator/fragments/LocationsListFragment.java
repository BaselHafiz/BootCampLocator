package com.bmacode17.bootcamplocator.fragments;


import android.graphics.Rect;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmacode17.bootcamplocator.R;
import com.bmacode17.bootcamplocator.adapters.LocationsAdapter;
import com.bmacode17.bootcamplocator.services.DataService;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
public class LocationsListFragment extends Fragment {

    public LocationsListFragment() {
        // Required empty public constructor
    }

    public static LocationsListFragment newInstance() {
        LocationsListFragment fragment = new LocationsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLocations);
        recyclerView.setHasFixedSize(true);
        LocationsAdapter adapter = new LocationsAdapter(DataService.getInstance().getBootCampLocationsWithin10MilesOfZip(123));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(1));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    class HorizontalSpaceItemDecorator extends RecyclerView.ItemDecoration {

        private final int spacer;

        public HorizontalSpaceItemDecorator(int spacer) {
            this.spacer = spacer;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = spacer;
        }
    }
}
