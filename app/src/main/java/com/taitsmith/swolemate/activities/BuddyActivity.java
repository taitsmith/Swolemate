package com.taitsmith.swolemate.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.BuddyListFragment;
import com.taitsmith.swolemate.ui.MyDetailsFragment;
import com.taitsmith.swolemate.ui.SavedLocationsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.ui.BuddyListFragment.setMyLocation;

public class BuddyActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager manager;
    private MyDetailsFragment detailsFragment;
    private BuddyListFragment buddyListFragment;
    private SavedLocationsFragment locationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);
        ButterKnife.bind(this);

        manager = getSupportFragmentManager();
        detailsFragment = new MyDetailsFragment();
        buddyListFragment = new BuddyListFragment();
        locationsFragment = new SavedLocationsFragment();

        setSupportActionBar(toolbar);

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
                    setMyLocation("Oakland");
                    manager.beginTransaction()
                            .remove(detailsFragment)
                            .add(R.id.buddyFragmentContainer, buddyListFragment)
                            .commit();
                    return true;
                case R.id.navigation_my_locations:
                    manager.beginTransaction()
                            .add(R.id.buddyFragmentContainer, locationsFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.personal_details_menu, menu);
        return true;
    }
}
