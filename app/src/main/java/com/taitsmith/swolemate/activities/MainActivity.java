package com.taitsmith.swolemate.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.dbutils.SessionCreator;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_SETS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;
import static com.taitsmith.swolemate.ui.PastSessionsListFragment.setSessionList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Session>>{
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;
    @BindView(R.id.makeFakeDataFab)
    FloatingActionButton makeFakeData;
    @BindView(R.id.deleteDataFab)
    FloatingActionButton deleteData;


    private boolean isTwoPane;
    private WorkoutDetailFragment detailFragment;
    private PastSessionsListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listFragment = new PastSessionsListFragment();
        detailFragment = new WorkoutDetailFragment();

        isTwoPane = findViewById(R.id.past_workout_detail_fragment) != null;

        LoaderManager.LoaderCallbacks<List<Session>> callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(420, null, callbacks);
    }

    private void setUi(List<Session> sessionList) {
        FragmentManager manager = getSupportFragmentManager();

        //we have to use the sessionList from our loader to set the
        //data for the fragment
        setSessionList(sessionList);

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

    @OnClick(R.id.deleteDataFab)
    public void deleteStuff() {
        ContentResolver resolver = getContentResolver();
        try {
            resolver.delete(CONTENT_URI,
                    null,
                    null);
        } catch (Exception e) {
            Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
        }
    }

    //instead of manually going in and entering a ton of fake workouts for testing, we'll
    //create X sessions containing Y workouts each.
    @OnClick(R.id.makeFakeDataFab)
    public void makeUpWorkouts() {
        for (int i = 0; i < 3; i++) {
            ContentValues values = new ContentValues();
            ContentResolver resolver = getContentResolver();
            for (int j = 0; j < 4; j++) {
                values.put(COLUMN_DATE, Integer.toString(i + 1));
                values.put(COLUMN_WEIGHT, 420);
                values.put(COLUMN_REPS, 20);
                values.put(COLUMN_SETS, 5);
                values.put(COLUMN_WORKOUT_NAME, "i am so strong");

                resolver.insert(CONTENT_URI, values);
            }
        }
    }

    //db queries can be time consuming so we'll use a loader.
    @Override
    public Loader<List<Session>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Session>>(this) {
            List<Session> sessionList = null;

            @Override
            protected void onStartLoading() {
                Toast.makeText(MainActivity.this, "Loading data...", Toast.LENGTH_SHORT).show();
                if (sessionList != null) {
                    deliverResult(sessionList);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Session> loadInBackground() {
                return SessionCreator.createSessionList(MainActivity.this);
            }

            @Override
            public void deliverResult(List<Session> data) {
                sessionList = data;
                super.deliverResult(sessionList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Session>> loader, final List<Session> data) {
        Toast.makeText(this, "All set!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(Loader<List<Session>> loader) {
        //i'm just here because i have to be.
    }
}
