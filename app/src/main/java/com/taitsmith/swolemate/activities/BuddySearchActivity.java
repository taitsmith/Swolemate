package com.taitsmith.swolemate.activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;
import com.taitsmith.swolemate.ui.BuddyListFragment;
import com.taitsmith.swolemate.ui.MyDetailsFragment;
import com.taitsmith.swolemate.ui.SavedLocationsFragment;
import com.taitsmith.swolemate.utils.LocationListenerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.utils.FirebaseUtils.updateMyLocation;
import static com.taitsmith.swolemate.utils.FirebaseUtils.whoAmI;


public class BuddySearchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager manager;
    private MyDetailsFragment detailsFragment;
    private BuddyListFragment buddyListFragment;
    private SavedLocationsFragment locationsFragment;

    public static FirebaseUser user;
    public static FirebaseAuth auth;
    public static Person me;
    public static String uid;

    public static List<Person> buddyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy);
        ButterKnife.bind(this);

        manager = getSupportFragmentManager();
        detailsFragment = new MyDetailsFragment();
        buddyListFragment = new BuddyListFragment();
        locationsFragment = new SavedLocationsFragment();
        buddyList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        me = whoAmI();

        getMyLocation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_details:
                    toolbar.setTitle(getString(R.string.buddy_activity_title_details));
                    manager.beginTransaction()
                            .replace(R.id.buddyFragmentContainer, detailsFragment)
                            .commit();
                    return true;
                case R.id.navigation_buddy_list:
                    toolbar.setTitle(getString(R.string.buddy_activity_title_buddy_list));
                    manager.beginTransaction()
                            .replace(R.id.buddyFragmentContainer, buddyListFragment)
                            .commit();
                    return true;
                case R.id.navigation_my_locations:
                    toolbar.setTitle(getString(R.string.buddy_activity_title_saved_locations));
                    manager.beginTransaction()
                            .replace(R.id.buddyFragmentContainer, locationsFragment)
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

    //get last known GPS location and start listener for location changes.
    private void getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListenerUtil(this);

        try {
            Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> address = geocoder.getFromLocation(last.getLatitude(), last.getLongitude(), 1);

            updateMyLocation(address.get(0).getLocality());

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    10, locationListener);
        } catch (SecurityException | IOException e) {
            Toast.makeText(this, getString(R.string.toast_need_permission), Toast.LENGTH_SHORT).show();
        }
    }

}
