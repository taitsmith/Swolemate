package com.taitsmith.swolemate.activities;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.taitsmith.swolemate.R;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;


/**
 * Set everything up, check permissions, etc.
 */

public class SwolemateApplication extends Application {
    public static List<Integer> gifList;
    @Override
    public void onCreate() {
        super.onCreate();

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
