package com.taitsmith.swolemate.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;

import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionPosition;

public class SessionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
        FragmentManager manager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("SESSION_ID")) {
            setSessionPosition(getIntent().getIntExtra("SESSION_ID", 0));
            manager.beginTransaction()
                    .add(R.id.detail_fragment, fragment)
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
