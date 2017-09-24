package com.taitsmith.swolemate.activities;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.taitsmith.swolemate.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Set everything up, check permissions, etc.
 */

public class SwolemateApplication extends Application {
    public static List<Drawable> gifList;
    @Override
    public void onCreate() {
        super.onCreate();

        gifList = new ArrayList<>();

        gifList.add(getResources().getDrawable(R.drawable.legraise));
        gifList.add(getResources().getDrawable(R.drawable.bicepcurl));
        gifList.add(getResources().getDrawable(R.drawable.calf));
        gifList.add(getResources().getDrawable(R.drawable.benchpress));
        gifList.add(getResources().getDrawable(R.drawable.forearmcurl));
        gifList.add(getResources().getDrawable(R.drawable.stiffdeadlift));
        gifList.add(getResources().getDrawable(R.drawable.latpulldown));
        gifList.add(getResources().getDrawable(R.drawable.squat));
        gifList.add(getResources().getDrawable(R.drawable.shoulderpress));
        gifList.add(getResources().getDrawable(R.drawable.deadlift));
        gifList.add(getResources().getDrawable(R.drawable.tricep));
    }
}
