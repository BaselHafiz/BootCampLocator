package com.bmacode17.bootcamplocator.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bmacode17.bootcamplocator.R;
import com.bmacode17.bootcamplocator.fragments.LocationsListFragment;
import com.bmacode17.bootcamplocator.models.LocationData;
import com.bmacode17.bootcamplocator.services.DataService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationClient2;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private boolean mLocationPermissionGranted = false;
    private LocationsListFragment listFragment;

    private void getDeviceLocationVersion1() {

        mFusedLocationClient2 = LocationServices.getFusedLocationProviderClient(this);
        try {
            @SuppressLint("MissingPermission")
            Task location = mFusedLocationClient2.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "onComplete: Location is found !");
                        Location currentLocation = (Location) task.getResult();
                        Log.d(TAG, "onComplete: Location: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        setUserMarker(latLng);
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                        try {
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            int zipcode = Integer.parseInt(addresses.get(0).getPostalCode());
                            Log.d(TAG, "onComplete: The zipcode is " + zipcode);
                            updateMapForZip(zipcode);
                        } catch (Exception exception) {
                            Log.d(TAG, "onComplete: Can't get zipcode");
                        }
                    } else {
                        Log.d(TAG, "onComplete : current location is null! ");
                        Toast.makeText(MapsActivity.this, "Unable to get current location !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException ex) {
            Log.d(TAG, "GetDeviceLocation : SecurityException: " + ex.getMessage());
        }
    }

    public void setUserMarker(LatLng latLng) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mGoogleMap.addMarker(markerOptions);
        }

        public void updateMapForZip(int zipcode){
            ArrayList<LocationData> bootCampLocations = DataService.getInstance().getBootCampLocationsWithin10MilesOfZip(zipcode);
            for (int i = 0; i < bootCampLocations.size() ; i++) {
                LocationData location = bootCampLocations.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
                markerOptions.title(location.getLocationTitle());
                markerOptions.snippet(location.getLocationAddress());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
                mGoogleMap.addMarker(markerOptions);
            }
        }

        private void hideList(){
            getSupportFragmentManager().beginTransaction().hide(listFragment).commit();
        }

        private void showList(){
            getSupportFragmentManager().beginTransaction().show(listFragment).commit();
        }

        public void loadLocationsListFragment(){

            FragmentManager fragmentManager = getSupportFragmentManager();
            listFragment = (LocationsListFragment) fragmentManager.findFragmentById(R.id.container_locations_recyclerView);

            if(listFragment == null){
                listFragment = listFragment.newInstance();
                fragmentManager.beginTransaction().add(R.id.container_locations_recyclerView,listFragment).commit();
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            final EditText editText_zipcode = (EditText) findViewById(R.id.editText_zipcode);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                getDeviceLocationVersion1();
            } else {
                checkLocationPermission();
            }

            loadLocationsListFragment();
            hideList();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            editText_zipcode.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                        String zipcodeStr = editText_zipcode.getText().toString();
                        int zipcodeInt = Integer.parseInt(zipcodeStr);
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editText_zipcode.getWindowToken(),0);
                        showList();
                        updateMapForZip(zipcodeInt);
                        return true;
                    }
                    return false;
                }
            });
        }

        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
        }

        private void checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(this)
                            .setTitle("Location Permission Needed")
                            .setMessage("This app needs the Location permission, please accept to use location functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(MapsActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getDeviceLocationVersion1();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
        }
    }