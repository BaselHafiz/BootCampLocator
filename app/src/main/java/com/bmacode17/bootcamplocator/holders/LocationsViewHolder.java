package com.bmacode17.bootcamplocator.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmacode17.bootcamplocator.R;
import com.bmacode17.bootcamplocator.models.LocationData;

/**
 * Created by User on 09-Apr-18.
 */

public class LocationsViewHolder extends RecyclerView.ViewHolder {

    public TextView textView_locationTitle;
    public TextView textView_locationAddress;
    public ImageView imageView_locationImg;

    public LocationsViewHolder(View itemView) {

        super(itemView);
        textView_locationTitle = (TextView) itemView.findViewById(R.id.textView_locationTitle);
        textView_locationAddress = (TextView) itemView.findViewById(R.id.textView_locationAddress);
        imageView_locationImg = (ImageView) itemView.findViewById(R.id.imageView_locationImg);
    }

    public void updateUI(LocationData location){

        String uri = location.getLocationImgUrl();
        int resource = imageView_locationImg.getResources()
                .getIdentifier(uri, null, imageView_locationImg.getContext().getPackageName());
        imageView_locationImg.setImageResource(resource);
        textView_locationTitle.setText(location.getLocationTitle());
        textView_locationAddress.setText(location.getLocationAddress());
    }
}
