package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Geofencer;
import com.taitsmith.swolemate.data.PastSessionsAdapter;
import com.taitsmith.swolemate.data.Workout;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;
import com.taitsmith.swolemate.utils.DbContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.fragment;
import static android.os.Build.VERSION_CODES.M;
import static com.taitsmith.swolemate.activities.SwolemateApplication.permissionGranted;
import static com.taitsmith.swolemate.ui.AlertDialogs.aboutDialog;
import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionPosition;
import static com.taitsmith.swolemate.utils.HelpfulUtils.addLocation;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createSessionList;
import static com.taitsmith.swolemate.utils.HelpfulUtils.makeUpWorkouts;
import static com.taitsmith.swolemate.ui.AlertDialogs.informPermissions;


public class MainActivity extends AppCompatActivity implements
        PastSessionsListFragment.OnWorkoutClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;
    @Nullable
    @BindView(R.id.past_workouts_list_view)
    ListView workoutsListView;

    private FragmentManager manager;
    private boolean isTwoPane;
    private WorkoutDetailFragment detailFragment;
    private PastSessionsListFragment listFragment;
    private GoogleApiClient googleApiClient;
    private Geofencer geofencer;

    private static final int PLACE_PICKER_REQUEST = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listFragment = new PastSessionsListFragment();
        detailFragment = (WorkoutDetailFragment) manager.findFragmentByTag("DETAIL_FRAGMENT");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();

        geofencer = new Geofencer(this, googleApiClient);

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
        //since we've got two possible layouts for tablets and regular sized things,
        //it'll cause all sorts of problems if we try to add fragments in a view that
        //doesn't exist.
        if (isTwoPane) {

            if (detailFragment == null) {
                detailFragment = new WorkoutDetailFragment();
            }
            manager.beginTransaction()
                    .add(R.id.past_workout_detail_fragment, detailFragment, "DETAIL_FRAGMENT")
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
            setSessionPosition(position);
            manager.beginTransaction()
                    .remove(detailFragment)
                    .add(R.id.past_workout_detail_fragment, detailFragment, "DETAIL_FRAGMENT")
                    .addToBackStack("DETAIL_FRAGMENT")
                    .commit();
        } else {
            Intent intent = new Intent(this, SessionDetailActivity.class);
            intent.putExtra("SESSION_ID", position);
            startActivity(intent);
        }
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
        Intent intent;
        switch (itemSelected) {
            case R.id.menu_fake_data:
                makeUpWorkouts(this);
                return true;
            case R.id.menu_about:
                aboutDialog(this);
                return true;
            case R.id.menu_workout_instructions:
                intent = new Intent(this, InstructionSummaryActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_set_home:
                if (!permissionGranted) {
                    Toast.makeText(this, getString(R.string.need_permissions_toast), Toast.LENGTH_SHORT).show();
                } else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                addLocation(this, place);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (permissionGranted) {
            refreshPlaces();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void refreshPlaces() {
        Cursor places = getContentResolver().query(
                DbContract.GymLocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (places == null || places.getCount() == 0) {
            Log.d("LOG: ", "no places");
            return;
        }

        List<String> placeIds = new ArrayList<>();

        while (places.moveToNext()) {
            placeIds.add(places.getString(places.getColumnIndex(
                    DbContract.GymLocationEntry.COLUMN_PLACE_ID)));
        }

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(
                googleApiClient,
                placeIds.toArray(new String[placeIds.size()]));

        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                geofencer.updateGeofenceList(places);
            }
        });

        geofencer.registerAllGeofences();
    }
}
