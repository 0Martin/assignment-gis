package com.example.martin.mapbox;

import android.location.*;
import android.location.Location;
import android.os.Bundle;

public class CustomLocationListener implements LocationListener {

    private MainActivity mMainActivity;

    public CustomLocationListener(MainActivity mainActivity){
        mMainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longtitude = location.getLongitude();
        mMainActivity.setLocation(latitude, longtitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
