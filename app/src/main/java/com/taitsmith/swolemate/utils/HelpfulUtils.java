package com.taitsmith.swolemate.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.google.android.gms.location.places.Place;
import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.utils.DbContract.*;

/**
 * A home for all friendly and helpful utilities that involve interacting with the database through
 * the content provider.
 *
 * createSessionList takes the list of Dates from shared preferences and finds all {@link Workout}
 * matching each date and turns them into a List, then adds that list to another list of {@link Session}.
 * There's a lot of lists being thrown around here, but it makes sense. This is used to populate the
 * ListView in {@link com.taitsmith.swolemate.activities.MainActivity}.
 *
 * createWorkoutList is called from the onCreateView of {@link com.taitsmith.swolemate.ui.WorkoutDetailFragment}
 * to fill the recycler view with all workouts completed in the selected session. It's passed an int,
 * gets a date from the sessionDates set, finds all workouts with that date and returns a List of them.
 *
 * addLocation is called when PlacePicker returns valid data as far as setting a location for a gym.
 * It just adds the location (name, lat, long) to the database to be queried later for Geofencing
 * purposes.
 *
 * makeUpWorkouts just makes some fake data to populate the list in main activity for display and
 * testing purposes
 */

public class HelpfulUtils {

    public static RealmResults<Session> createSessionList() {
        Realm realm = Realm.getInstance(realmConfiguration);

        return realm.where(Session.class)
                .findAllSorted("_id", Sort.DESCENDING);
    }

    public static void addLocation(Context context, Place place) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        String name = (String) place.getName();
        String id = place.getId();
        double placeLong = place.getLatLng().longitude;
        double placeLat = place.getLatLng().latitude;

        values.put(DbContract.GymLocationEntry.COLUMN_LOCATION_NAME, name);
        values.put(GymLocationEntry.COLUMN_LOCATION_LAT, placeLat);
        values.put(GymLocationEntry.COLUMN_LOCATION_LONG, placeLong);
        values.put(GymLocationEntry.COLUMN_PLACE_ID, id);

        resolver.insert(GymLocationEntry.CONTENT_URI, values);
    }

    //so we can display a short version of the date on the add workout page
    public static String getFormattedDate(String length){
        DateTime dateTime = DateTime.now();
        DateTimeFormatter formatter;

        if (length.equals("long")) {
            formatter = DateTimeFormat.forPattern("MM-dd-yyyy");
        } else {
            formatter = DateTimeFormat.forPattern("dd MMM");
        }

        return dateTime.toString(formatter);
    }


    //thanks to Bachiet Tansime on SO for this autoincrement key replacement
    public static int getNextRealmKey() {
        Realm realm = Realm.getInstance(realmConfiguration);
        try {
            Number number = realm.where(Session.class).max("_id");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public static void createOrUpdateSession(String date) {
        Realm realm = Realm.getInstance(realmConfiguration);
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
        }
        Session session;

        try {
             session = realm.where(Session.class)
                    .equalTo("date", date)
                    .findFirst();

            session.setWorkoutCount(session.getWorkoutCount() + 1);
        } catch (NullPointerException e) {
            session = realm.createObject(Session.class, getNextRealmKey());
            session.setDate(date);
            session.setWorkoutCount(1);
        }

        realm.commitTransaction();
    }
}
