package com.heubauer.fotoverwaltung;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class LocationClass {

    private final LocationManager locationManager;
    private final LocationListener locationListener;
    private LatLng currentLocation;

    public LocationClass(LocationManager locationManager){

        this.locationManager= locationManager;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
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
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    public LatLng getCurrentLocation(){

        if (currentLocation == null){
            LatLng latlng = new LatLng(0, 0);
            if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                latlng = new LatLng(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            }
            currentLocation = latlng;
        }
        return currentLocation;
    }

    public void stopOnLocationChanged(){
        locationManager.removeUpdates(locationListener);
    }

    public void startOnLocationChanged(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
