package com.taitsmith.swolemate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.PastWorkoutsDetailFragment;
import com.taitsmith.swolemate.ui.PastWorkoutsListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;

    private boolean isTwoPane;
    private PastWorkoutsDetailFragment detailFragment;
    private PastWorkoutsListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listFragment = new PastWorkoutsListFragment();
        detailFragment = new PastWorkoutsDetailFragment();

        isTwoPane = findViewById(R.id.past_workout_detail_fragment) != null;

        setUi();
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
}
