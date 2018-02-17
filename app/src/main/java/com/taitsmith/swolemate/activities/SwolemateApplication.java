package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.support.v4.app.ActivityCompat;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Session;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Sets up our important lists, shared preferences.
 */

public class SwolemateApplication extends Application {
    public static List<Integer> gifList;
    public static boolean permissionGranted;
    public static TypedArray workoutArray;
    public static TypedArray workoutInstructions;
    public static List<String> sortedDates;
    public static RealmConfiguration realmConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        JodaTimeAndroid.init(this);

        realmConfiguration = new RealmConfiguration.Builder()
                .name("swolemate.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded() //TODO delete this at some point
                .build();


        //list of workout names
        workoutArray = getResources().obtainTypedArray(R.array.workout_list);
        workoutInstructions = getResources().obtainTypedArray(R.array.workout_instructions);

        permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;

         /* Originally this was a list of Drawables in the form:
        gifList.add(ContextCompat.getDrawable(this, R.id.XXXXX));
        It turns out Glide is kind of finnicky when it comes to getting
        a Drawable from that list and passing it into the .load() method.
        Using an int works just fine though.
         */
        gifList = new ArrayList<>();

        gifList.add(R.drawable.legraise);
        gifList.add(R.drawable.bicepcurl);
        gifList.add(R.drawable.calf);
        gifList.add(R.drawable.benchpress);
        gifList.add(R.drawable.forearmcurl);
        gifList.add(R.drawable.stiffdeadlift);
        gifList.add(R.drawable.latpulldown);
        gifList.add(R.drawable.squat);
        gifList.add(R.drawable.shoulderpress);
        gifList.add(R.drawable.deadlift);
        gifList.add(R.drawable.tricep);
    }
}
