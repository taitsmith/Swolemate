package com.taitsmith.swolemate.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Workout;
import com.taitsmith.swolemate.dbutils.WorkoutDbHelper;
import com.taitsmith.swolemate.ui.PastWorkoutsDetailFragment;
import com.taitsmith.swolemate.ui.PastWorkoutsListFragment;

import java.time.LocalDate;

import static android.os.Build.VERSION_CODES.N;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_THOUGHTS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private boolean isTwoPane;
    private PastWorkoutsDetailFragment detailFragment;
    private PastWorkoutsListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listFragment = new PastWorkoutsListFragment();
        detailFragment = new PastWorkoutsDetailFragment();

        isTwoPane = findViewById(R.id.past_workout_detail_fragment) != null;

        setUi();

        makeWorkouts();
    }

    private void setUi() {
        FragmentManager manager = getSupportFragmentManager();

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
        
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    //TODO this was all just set up to make sure provider works.
    private void makeWorkouts() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, LocalDate.now().toString());
        values.put(COLUMN_WORKOUT_NAME, "gettin huge");
        values.put(COLUMN_REPS, 69);
        values.put(COLUMN_WEIGHT, 420);
        values.put(COLUMN_THOUGHTS, "these are some thoughts.");

        ContentResolver resolver = getContentResolver();

        resolver.insert(CONTENT_URI, values);

        readWorkouts();
    }

    private void readWorkouts() {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                null,
                null,
                new String[]{LocalDate.now().toString()},
                null);

        cursor.moveToFirst();

        Toast.makeText(this, cursor.getString(5), Toast.LENGTH_SHORT).show();

        deleteWorkouts();
    }

    private void deleteWorkouts() {
        getContentResolver().delete(CONTENT_URI,
                COLUMN_DATE + "=?",
                new String[]{LocalDate.now().toString()});
    }
}
