package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.GymLocation;
import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.taitsmith.swolemate.activities.MainActivity.pastSessionsAdapter;
import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

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
 *
 * NOTE none of that is relevant.
 */

public class HelpfulUtils {

    //pretty self explanatory.
    public static RealmResults<Session> createSessionList() {
        Realm realm = Realm.getInstance(realmConfiguration);

        return realm.where(Session.class)
                .findAllSorted("_id", Sort.DESCENDING);
    }

    //create a realm object for a gym location when selected from the
    //place picker, otherwise tell the user they've previously selected
    //that location. Stores lat/long for Geofence purposes
    public static void addLocation(Context context, Place place) {
        Realm realm = Realm.getInstance(realmConfiguration);
        realm.beginTransaction();

        String name = (String) place.getName();
        String id = place.getId();
        double placeLong = place.getLatLng().longitude;
        double placeLat = place.getLatLng().latitude;

        Log.d("LOG", place.getAddress().toString());

        try {
            if (realm.where(GymLocation.class)
                    .equalTo("placeId", id)
                    .findFirst() != null) {
                Toast.makeText(context, context.getString(R.string.toast_place_exists), Toast.LENGTH_SHORT).show();
            } else {
                //use the Place's ID as its primary key
                GymLocation location = realm.createObject(GymLocation.class, id);

                location.setPlaceName(name);
                location.setPlaceLat(placeLat);
                location.setPlaceLong(placeLong);

                Toast.makeText(context, context.getString(R.string.toast_place_saved, name), Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.toast_something_wrong), Toast.LENGTH_SHORT).show();
        }

        realm.commitTransaction();
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
    private static int getNextRealmKey() {
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

    //create a new Session object for Realm if we can't find one with
    //the current datestamp, otherwise increment its workout count
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

        try {
            pastSessionsAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //two pane mode.
        }
    }

    //long press on a session in the main activity allows users to delete it
    //and its corresponding workouts.
    public static void deleteSessionAndWorkouts(int id, Context context) {
        Realm realm = Realm.getInstance(realmConfiguration);

        RealmResults<Session> sessionToDelete = realm.where(Session.class)
                .findAllSorted("_id", Sort.DESCENDING);
        Session session = sessionToDelete.get(id);

        RealmResults<Workout> workoutsToDelete = realm.where(Workout.class)
                .equalTo("date", session.getDate())
                .findAll();

        realm.beginTransaction();
        session.deleteFromRealm();
        workoutsToDelete.deleteAllFromRealm();
        realm.commitTransaction();

        Toast.makeText(context, context.getString(R.string.toast_deletion_successful),
                Toast.LENGTH_SHORT).show();

        pastSessionsAdapter.notifyDataSetChanged();
    }

    public static void workoutListSwipeHandler(Workout workout) {
        Realm realm = Realm.getInstance(realmConfiguration);
        String date = workout.getDate();

        realm.beginTransaction();

        workout.deleteFromRealm();

        Session session = realm.where(Session.class)
                .equalTo("date", date)
                .findFirst();

        int count = session.getWorkoutCount() - 1;

        if (count == 0) {
            session.deleteFromRealm();
        } else {
            session.setWorkoutCount(count);
        }

        realm.commitTransaction();
    }
}
