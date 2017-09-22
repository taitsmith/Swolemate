package com.taitsmith.swolemate.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Geofence so we know when the user is at the gym.
 */

public class Geofencer implements ResultCallback {
    public static final String TAG = Geofencer.class.getSimpleName();
    public static final float GEOFENCE_RADIUS = 50; //50 meters
    public static final long TIMEOUT = 24*60*60*1000; //24 hours

    private List<Geofence> geofenceList; //can you tell I love lists
    private PendingIntent geofencePendingIntent;
    private GoogleApiClient googleApiClient;
    private Context context;

    public Geofencer(Context context, GoogleApiClient client) {
        this.context = context;
        googleApiClient = client;
        geofencePendingIntent = null;
        geofenceList = new ArrayList<>();
    }

    public void registerAllGeofences() {
        if (googleApiClient == null || !googleApiClient.isConnected() ||
                geofenceList == null || geofenceList.size() == 0) {
            return; //all that for nothing.
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent())
                    .setResultCallback(this);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void unregisterGeofences() {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            return;
        }

        try {
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    getGeofencePendingIntent())
                    .setResultCallback(this);
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void updateGeofenceList(PlaceBuffer places) {
        geofenceList = new ArrayList<>();
        if (places == null || places.getCount() == 0) {
            return;
        }

        for (Place place : places) {
            String placeUID = place.getId();
            double placeLat = place.getLatLng().latitude;
            double placeLong = place.getLatLng().longitude;

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(placeUID)
                    .setExpirationDuration(TIMEOUT)
                    .setCircularRegion(placeLat, placeLong, GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            geofenceList.add(geofence);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(context, GeofenceReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, result.getStatus().toString());
    }
}
