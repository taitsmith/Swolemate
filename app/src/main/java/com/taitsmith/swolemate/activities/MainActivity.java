package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Geofencer;
import com.taitsmith.swolemate.data.GymLocation;
import com.taitsmith.swolemate.utils.PastSessionsAdapter;
import com.taitsmith.swolemate.utils.WeeklySummary;
import com.taitsmith.swolemate.ui.WorkoutDetailFragment;
import com.taitsmith.swolemate.ui.PastSessionsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.permissionGranted;
import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.ui.AlertDialogs.aboutDialog;
import static com.taitsmith.swolemate.ui.AlertDialogs.sessionListDeleteItem;
import static com.taitsmith.swolemate.ui.WorkoutDetailFragment.setSessionDate;
import static com.taitsmith.swolemate.utils.HelpfulUtils.addLocation;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createOrUpdateSession;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createSessionList;
import static com.taitsmith.swolemate.ui.AlertDialogs.informPermissions;


public class MainActivity extends AppCompatActivity implements
        PastSessionsListFragment.OnWorkoutClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.addWorkoutFab)
    FloatingActionButton addWorkoutFab;
    @Nullable
    @BindView(R.id.past_workouts_list_view)
    ListView workoutsListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager manager;
    private boolean isTwoPane;
    private WorkoutDetailFragment detailFragment;
    private PastSessionsListFragment listFragment;
    private GoogleApiClient googleApiClient;
    private Geofencer geofencer;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Realm realm;

    private static final int PLACE_PICKER_REQUEST = 34;

    public static PastSessionsAdapter pastSessionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();

        setSupportActionBar(toolbar);

        listFragment = new PastSessionsListFragment();
        detailFragment = (WorkoutDetailFragment) manager.findFragmentByTag("DETAIL_FRAGMENT");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        realm = Realm.getInstance(realmConfiguration);

        isTwoPane = findViewById(R.id.past_workout_detail_fragment) != null;

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


        setUi();
    }

    private void setUi() {
        //your fairly standard two-pane setup
        if (isTwoPane) {

            if (detailFragment == null) {
                detailFragment = new WorkoutDetailFragment();
            }
            manager.beginTransaction()
                    .replace(R.id.past_workout_detail_fragment, detailFragment, "DETAIL_FRAGMENT")
                    .add(R.id.past_workouts_list_fragment, listFragment)
                    .commit();
        } else {
            //So here's the situation. ListView inside a fragment in a CollapsingToolbarLayout has
            //issues with nested scrolling for some reason. I spent quite a bit of time on it and there's
            //presumably some simple workaround, but for now we'll swap out the fragment for a plain
            //old list view. This obviously cancels out the whole point of reusability in fragments.
            pastSessionsAdapter = new PastSessionsAdapter(this, createSessionList());
            workoutsListView.setAdapter(pastSessionsAdapter);
            workoutsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String date = createSessionList().get(position).getDate();
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this)
                            .toBundle();
                    Intent intent = new Intent(MainActivity.this, SessionDetailActivity.class);
                    intent.putExtra("SESSION_DATE", date);
                    startActivity(intent, bundle);
                }
            });

            //we'll let the user long-press for the option to delete
            workoutsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    sessionListDeleteItem(MainActivity.this, position);
                    return true;
                }
            });
            }
    }

    @OnClick(R.id.addWorkoutFab)
    public void onClickAddWorkout() {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        startActivity(intent);
    }

    //the workout list fragment has an interface for click listening.
    @Override
    public void onWorkoutSelected(int position) {
        if (detailFragment == null) {
            detailFragment = new WorkoutDetailFragment();
        }
        if (isTwoPane) {
            setSessionDate(createSessionList().get(position).getDate());
            manager.beginTransaction()
                    .remove(detailFragment)
                    .add(R.id.past_workout_detail_fragment, detailFragment, "DETAIL_FRAGMENT")
                    .addToBackStack("DETAIL_FRAGMENT")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (user != null) { //change the sign in option once a user signs in
            menu.findItem(R.id.menu_sign_in).setTitle("Sign out");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        Intent intent;
        switch (itemSelected) {
            case R.id.menu_about:
                aboutDialog(this);
                return true;
            case R.id.menu_workout_instructions:
                intent = new Intent(this, InstructionSummaryActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_set_home:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.toast_need_permission), Toast.LENGTH_SHORT).show();
                } else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(this, getString(R.string.toast_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case R.id.menu_weekly_summary:
                WeeklySummary summary = new WeeklySummary(this);
                summary.execute();
                return true;
            case R.id.menu_sign_in:
                if (user != null) {
                    auth.signOut();
                    Toast.makeText(this, getString(R.string.toast_sign_out), Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
            case R.id.menu_settings:
                createOrUpdateSession("10-11-2017");
                createOrUpdateSession("10-12-2017");
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
        Toast.makeText(this, getString(R.string.toast_something_wrong), Toast.LENGTH_SHORT).show();
        Log.e("Log" , connectionResult.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, getString(R.string.toast_something_wrong), Toast.LENGTH_SHORT).show();
    }

    public void refreshPlaces() {

        RealmResults<GymLocation> gymLocations = realm.where(GymLocation.class)
                .findAll();

        if (gymLocations.size() == 0) {
            return;
        }

        List<String> placeIds = new ArrayList<>();

        for (GymLocation gl : gymLocations) {
            placeIds.add(gl.getPlaceId());
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
