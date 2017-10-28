package com.taitsmith.swolemate.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * To hold location data for each user selected gym location and register related geofences.
 */

public class GymLocation extends RealmObject {
    private String placeName;
    private double placeLat, placeLong;

    @PrimaryKey
    private String placeId;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLong() {
        return placeLong;
    }

    public void setPlaceLong(double placeLong) {
        this.placeLong = placeLong;
    }

    public String getPlaceId() {
        return placeId;
    }
}
