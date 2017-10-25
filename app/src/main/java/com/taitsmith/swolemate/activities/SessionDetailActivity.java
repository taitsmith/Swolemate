package com.taitsmith.swolemate.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;

import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionDate;

public class SessionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        WorkoutDetailFragment detailFragment = new WorkoutDetailFragment();
        FragmentManager manager = getSupportFragmentManager();


        if (getIntent().hasExtra("SESSION_DATE")) {
            setSessionDate(getIntent().getStringExtra("SESSION_DATE"));
            manager.beginTransaction()
                    .add(R.id.detail_fragment, detailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
