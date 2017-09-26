package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sessionDates;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_SETS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_THOUGHTS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;
import static com.taitsmith.swolemate.ui.AlertDialogs.informPermissions;
import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionPosition;


public class MainActivity extends AppCompatActivity implements PastSessionsListFragment.OnWorkoutClickListener {
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;

    public static boolean hasBeenUpdated;

    private SharedPreferences preferences;
    private boolean isTwoPane;
    private WorkoutDetailFragment detailFragment;
    private PastSessionsListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferences = getSharedPreferences("SHARED_PREFS", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listFragment = new PastSessionsListFragment();
        detailFragment = new WorkoutDetailFragment();

        //if they haven't granted the location permission, show them a dialog explaining why
        //we need it, then request the permission.
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            informPermissions(MainActivity.this);
        }

        isTwoPane = findViewById(R.id.past_workout_detail_fragment) != null;

        setUi();
    }

    private void setUi() {
        FragmentManager manager = getSupportFragmentManager();

        //since we've got two possible layouts for tablets and regular sized things,
        //it'll cause all sorts of problems if we try to add fragments in a view that
        //doesn't exist.
        if (isTwoPane) {
            manager.beginTransaction()
                    .add(R.id.past_workout_detail_fragment, detailFragment)
                    .add(R.id.past_workouts_list_fragment, listFragment)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(R.id.past_workouts_list_fragment, listFragment)
                    .commit();
        }
        
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("366D79E3B74228190ACAB86D24457619")
                .build();
        adView.loadAd(adRequest);
    }

    @OnClick(R.id.addWorkoutFab)
    public void onClickAddWorkout() {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWorkoutSelected(int position) {
        if (isTwoPane) {
            FragmentManager manager = getSupportFragmentManager();
            WorkoutDetailFragment fragment = new WorkoutDetailFragment();
            setSessionPosition(position);
            manager.beginTransaction()
                    .replace(R.id.past_workout_detail_fragment, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, SessionDetailActivity.class);
            intent.putExtra("SESSION_ID", position);
            startActivity(intent);
        }
    }

    //instead of manually going in and entering a ton of fake workouts for testing, we'll
    //create X sessions containing Y workouts each.
    public void makeUpWorkouts() {
        String[] fakeDates = {"2017-9-13", "2017-9-14", "2017-9-15", "2017-9-16", "2017-9-17",
                "2017-9-18", "2017-9-19", "2017-9-20", "2017-9-21"};

        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            ContentValues values = new ContentValues();
            ContentResolver resolver = getContentResolver();
            for (int j = 0; j < 4; j++) {
                values.put(COLUMN_DATE, fakeDates[i]);
                values.put(COLUMN_WEIGHT, 420);
                values.put(COLUMN_REPS, r.nextInt(50));
                values.put(COLUMN_SETS, r.nextInt(5));
                values.put(COLUMN_THOUGHTS, "i am so strong");
                values.put(COLUMN_WORKOUT_NAME, "LIFTING A CAR");
                sessionDates.add(fakeDates[i]);

                resolver.insert(CONTENT_URI, values);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        switch (itemSelected) {
            case R.id.menu_fake_data:
                makeUpWorkouts();
                return true;
            case R.id.menu_about:
                deleteFakeData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteFakeData() {
        ContentResolver resolver = getContentResolver();

        resolver.delete(CONTENT_URI, null, null);
    }
}
