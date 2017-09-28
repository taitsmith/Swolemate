package com.taitsmith.swolemate.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.taitsmith.swolemate.R;

import java.util.ArrayList;
import java.util.Collection;
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

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("SHARED_PREFS", 0);

       if (sharedPreferences.contains("DATES")) {
           sessionDates = sharedPreferences.getStringSet("DATES", null);
       } else {
           sessionDates = new HashSet<>();
       }

       for (String s : sessionDates) {
           Log.d("LOG ", s);
       }

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

        /* Originally this was a list of Drawables in the form:
        gifList.add(ContextCompat.getDrawable(this, R.id.XXXXX));
        It turns out Glide is kind of finnicky when it comes to getting
        a Drawable from that list and passing it into the .load() method.
        Using an int works just fine though.
         */
    }
}
