package com.bmacode17.bootcamplocator.models;

/**
 * Created by User on 31-Mar-18.
 */

public class LocationData {
    private double longitude;
    private double latitude;
    private String locationTitle;
    private String locationAddress;
    private String locationImgUrl;
    final String DRAWABLE = "drawable/";

    public LocationData(double latitude, double longitude, String locationTitle, String locationAddress, String locationImgUrl) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationTitle = locationTitle;
        this.locationAddress = locationAddress;
        this.locationImgUrl = locationImgUrl;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationImgUrl() {
        return DRAWABLE + locationImgUrl;
    }
}
