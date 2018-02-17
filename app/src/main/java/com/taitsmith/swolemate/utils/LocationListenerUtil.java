package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.taitsmith.swolemate.ui.MyDetailsFragment.myLocation;
import static com.taitsmith.swolemate.utils.FirebaseUtils.updateMyLocation;

/**
 * To get user's location for the find buddy activity. When location changes, the user's
 * user's cityLocation is updated and sent to firebase. This allows users to view people nearby without
 * having to update their location manually.
 */

public class LocationListenerUtil implements LocationListener {
    private Context context;

    public LocationListenerUtil(Context context) {
        this.context = context;
    }


    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                myLocation.append(addresses.get(0).getLocality());
                updateMyLocation(addresses.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
