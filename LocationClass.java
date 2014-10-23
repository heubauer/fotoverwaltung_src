package com.heubauer.fotoverwaltung;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Edwin on 22.10.2014.
 */
public class LocationClass {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;

    public LocationClass(LocationManager locationManager){

        this.locationManager= locationManager;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                currentLocation = location;

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

    public Location getCurrentLocation(){

        if (currentLocation == null){
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return currentLocation;
    }

    public void stopOnLocationChanged(){
        locationManager.removeUpdates(locationListener);
    }

    public void showLoactionOnMap(Location location){

    }

}
