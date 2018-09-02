package com.taitsmith.swolemate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuddyViewActivity extends AppCompatActivity {
    @BindView(R.id.buddyNameTextView)
    TextView buddyNameView;
    @BindView(R.id.buddyLocationTextView)
    TextView buddyLocation;
    @BindView(R.id.buddyPortait)
    ImageView buddyPortrait;
    @BindView(R.id.buddyBioTV)
    TextView buddyBio;
    @BindView(R.id.bikeCheckBox)
    CheckBox bikeBox;
    @BindView(R.id.liftCheckBox)
    CheckBox liftBox;
    @BindView(R.id.crossfitCheckBox)
    CheckBox crossfitBox;
    @BindView(R.id.swimCheckBox)
    CheckBox swimBox;
    @BindView(R.id.runCheckBox)
    CheckBox runBox;
    @BindView(R.id.otherCheckBox)
    CheckBox somethingElseBox;

    private Person buddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buddy_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra("SELECTED_BUDDY")) {
            buddy = intent.getParcelableExtra("SELECTED_BUDDY");
        }

        Glide.with(this).load(buddy.getPhotoUrl()).into(buddyPortrait);

        Log.d("BUDDY PHOTO: ", buddy.getPhotoUrl());

        buddyNameView.setText(buddy.getName());
        buddyLocation.setText(buddy.getCityLocation());
        buddyBio.setText(buddy.getShortBio());

        List<String> buddyActivities = buddy.getActivities();

        if (buddyActivities != null && buddyActivities.size() != 0) {
            swimBox.setChecked(buddyActivities.contains("Swim"));
            bikeBox.setChecked(buddyActivities.contains("Bike"));
            runBox.setChecked(buddyActivities.contains("Run"));
            crossfitBox.setChecked(buddyActivities.contains("Crossfit"));
            somethingElseBox.setChecked(buddyActivities.contains("Something else"));
            liftBox.setChecked(buddyActivities.contains("Lift"));
        }
    }
}
