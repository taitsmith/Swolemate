package com.taitsmith.swolemate.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.MyDetailsFragment;

import butterknife.BindView;

public class BuddyActivity extends AppCompatActivity {
    private android.support.v4.app.FragmentManager manager;
    private MyDetailsFragment detailsFragment;
    private FirebaseAuth auth;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);
        manager = getSupportFragmentManager();
        detailsFragment = new MyDetailsFragment();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager.beginTransaction()
                            .add(R.id.buddyFragmentContainer, detailsFragment)
                            .commit();

                    return true;
                case R.id.navigation_dashboard:

                    return true;
            }
            return false;
        }
    };
}
