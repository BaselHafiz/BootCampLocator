package com.bmacode17.bootcamplocator.services;

import android.location.Location;

import com.bmacode17.bootcamplocator.models.LocationData;

import java.util.ArrayList;

/**
 * Created by User on 31-Mar-18.
 */

public class DataService {
    private static final DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }

    public ArrayList<LocationData> getBootCampLocationsWithin10MilesOfZip(int zipcode){
        ArrayList<LocationData> list = new ArrayList<>();
        list.add(new LocationData(35.279,-120.663,"Downtown","762 Huegeia Street , San  louis Obispo","img1"));
        list.add(new LocationData(35.302,-120.658,"On The Campus","1 Grand Ave, San  louis Obispo","img2"));
        list.add(new LocationData(35.267,-120.652,"East Side Tower","2435 Victoria Ave, San  louis Obispo","img3"));
        list.add(new LocationData(35.279,-120.663,"Downtown","762 Huegeia Street , San  louis Obispo","img4"));
        list.add(new LocationData(35.267,-120.652,"East Side Tower","2435 Victoria Ave, San  louis Obispo","img1"));
        return list;
    }
}
