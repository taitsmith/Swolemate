package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.GymLocation;
import com.taitsmith.swolemate.data.Person;
import com.taitsmith.swolemate.utils.FirebaseUtils;
import com.taitsmith.swolemate.utils.GymAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * Fragment to be used in the MyDetails view on the {@link com.taitsmith.swolemate.activities.BuddyActivity}
 * Allows users to set their personal details to be displayed to other users (stored in Firebase DB)
 * as well as view as the {@link GymLocation}
 */

public class MyDetailsFragment extends Fragment {
    @BindView(R.id.portraitImageView)
    ImageView personalPortait;
    @BindView(R.id.myNameTextView)
    TextView personalName;
    @BindView(R.id.liftCheckBox)
    CheckBox liftBox;
    @BindView(R.id.bikeCheckBox)
    CheckBox bikeBox;
    @BindView(R.id.crossfitCheckBox)
    CheckBox crossfitBox;
    @BindView(R.id.swimCheckBox)
    CheckBox swimBox;
    @BindView(R.id.runCheckBox)
    CheckBox runBox;
    @BindView(R.id.otherCheckBox)
    CheckBox otherBox;
    @BindView(R.id.keepMeHiddenCheckBox)
    CheckBox keepMeHidden;
    @BindView(R.id.shortBioEditText)
    EditText bioEditText;
    @BindView(R.id.savedGymsListView)
    ListView gymListView;
    @BindView(R.id.fabSaveDetails)
    FloatingActionButton saveDetailsFab;

    private FirebaseUser user;
    private FirebaseAuth auth;
    private boolean detailsUpdated;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_personal_details, container, false);
        ButterKnife.bind(this, rootView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        detailsUpdated = false;

        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(personalPortait);
            personalName.setText(user.getDisplayName());
        }
        return rootView;
    }

    private List<String> getActivities() {
        List<String> activities = new ArrayList<>();

        if (otherBox.isChecked()) {
            activities.add(getString(R.string.personal_details_other));
        } if (liftBox.isChecked()) {
            activities.add(getString(R.string.personal_details_lift));
        } if (swimBox.isChecked()) {
            activities.add(getString(R.string.personal_details_swim));
        } if (bikeBox.isChecked()){
            activities.add(getString(R.string.personal_details_bike));
        } if (runBox.isChecked()) {
            activities.add(getString(R.string.personal_details_run));
        } if (crossfitBox.isChecked()) {
            activities.add(getString(R.string.personal_details_crossfit));
        }
        return activities;
    }

    @OnClick(R.id.fabSaveDetails)
    public void saveDetails() {
        Person person = new Person(user.getDisplayName(), bioEditText.getText().toString(),
                "Oakland", getActivities(), 26, keepMeHidden.isChecked());
        FirebaseUtils.saveMyDetails(person, user.getUid());
    }
}
