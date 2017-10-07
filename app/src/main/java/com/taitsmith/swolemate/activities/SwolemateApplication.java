package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Geofencer;
import com.taitsmith.swolemate.data.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;


/**
 * Sets up our important lists, shared preferences.
 */

public class SwolemateApplication extends Application {
    public static List<Integer> gifList;
    public static Set<String> sessionDates;
    public static SharedPreferences sharedPreferences;
    public static boolean permissionGranted;
    public static TypedArray workoutArray;
    public static List<String> sortedDates;

    @Override
    public void onCreate() {
        super.onCreate();

        //list of workout names
        workoutArray = getResources().obtainTypedArray(R.array.workout_list);

        permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;

        sharedPreferences = getSharedPreferences("SHARED_PREFS", 0);

        //keep our list of past dates in a shared preferences instead of having a
        //whole separate table in the DB just for that.
        sessionDates = sharedPreferences.getStringSet("DATES", new HashSet<String>());

        //inconvenient, but you can't call Collections.sort() on  Set
        //and you cant keep an ArrayList in shared preferences
        sortedDates = new ArrayList<>(sessionDates);

        Collections.sort(sortedDates);

        //and then you want it ascending.
        Collections.reverse(sortedDates);

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
        gifList.add((R.drawable.squat));
        gifList.add(R.drawable.shoulderpress);
        gifList.add(R.drawable.deadlift);
        gifList.add(R.drawable.tricep);
    }
}
