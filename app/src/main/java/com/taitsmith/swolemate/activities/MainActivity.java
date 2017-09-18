package com.taitsmith.swolemate.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.dbutils.SessionCreator.createSessions;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;


public class MainActivity extends AppCompatActivity {
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

    //this is all debugging stuff.
    @OnClick(R.id.makeFakeDataFab)
    public void makeStuffUp() {
        List<Session> sessions = createSessions(this);
    }

    public static List<Session> makeUpSomeData() {
        List<Session> sessions = new ArrayList<>();

        for (int i = 0; i <60; i++) {
            Session session = new Session();
            session.setDate(Integer.toString(i));
            sessions.add(session);
        }
        return sessions;
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

}
