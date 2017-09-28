package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.PastSessionsAdapter;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sessionDates;
import static com.taitsmith.swolemate.activities.SwolemateApplication.sharedPreferences;
import static com.taitsmith.swolemate.dbutils.SessionCreator.createSessionList;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.*;
import static com.taitsmith.swolemate.ui.AlertDialogs.informPermissions;
import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionPosition;


public class MainActivity extends AppCompatActivity implements PastSessionsListFragment.OnWorkoutClickListener {
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;
    @Nullable
    @BindView(R.id.past_workouts_list_view)
    ListView workoutsListView;

    private boolean isTwoPane;
    private WorkoutDetailFragment detailFragment;
    private PastSessionsListFragment listFragment;

    private static final int PLACE_PICKER_REQUEST = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listFragment = new PastSessionsListFragment();
        detailFragment = new WorkoutDetailFragment();

        makeUpWorkouts();

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

            //So here's the situation. ListView inside a fragment in a CollapsingToolbarLayout has
            //issues with nested scrolling for some reason. I spent quite a bit of time on it and there's
            //presumably some simple workaround, but for now we'll swap out the fragment for a plain
            //old list view. This obviously cancels out the whole point of reusability in fragments.
            PastSessionsAdapter adapter = new PastSessionsAdapter(this, createSessionList(this));
            workoutsListView.setAdapter(adapter);
            workoutsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(MainActivity.this, SessionDetailActivity.class);
                    intent.putExtra("SESSION_ID", position);
                    startActivity(intent);
                }
            });
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

    //the workout list fragment has an interface for click listening.
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
        String[] fakeDates = {"2017-09-13", "2017-09-14", "2017-09-15", "2017-09-16", "2017-09-17",
                "2017-09-18", "2017-09-19", "2017-09-20", "2017-09-21"};

        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            ContentValues values = new ContentValues();
            ContentResolver resolver = getContentResolver();
            for (int j = 0; j < 4; j++) {
                values.put(WorkoutEntry.COLUMN_DATE, fakeDates[i]);
                values.put(WorkoutEntry.COLUMN_WEIGHT, 420);
                values.put(WorkoutEntry.COLUMN_REPS, r.nextInt(50));
                values.put(WorkoutEntry.COLUMN_SETS, r.nextInt(5));
                values.put(WorkoutEntry.COLUMN_THOUGHTS, "i am so strong");
                values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, "LIFTING A CAR");

                sessionDates.add(fakeDates[i]);

                resolver.insert(WorkoutEntry.CONTENT_URI, values);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("DATES", sessionDates);
        editor.apply();
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
                return true;
            case R.id.menu_workout_instructions:
                Intent intent = new Intent(this, InstructionSummaryActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_set_home:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                ContentResolver resolver = getContentResolver();
                ContentValues values = new ContentValues();
                Place place = PlacePicker.getPlace(data, this);
                String name = (String) place.getName();
                double placeLong = place.getLatLng().longitude;
                double placeLat = place.getLatLng().latitude;

                values.put(GymLocationEntry.COLUMN_LOCATION_NAME, name);
                values.put(GymLocationEntry.COLUMN_LOCATION_LAT, placeLat);
                values.put(GymLocationEntry.COLUMN_LOCATION_LONG, placeLong);

                resolver.insert(GymLocationEntry.CONTENT_URI, values);
            }
        }
    }
}
