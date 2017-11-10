package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.GymLocation;
import com.taitsmith.swolemate.utils.GymAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * Fragment to be used in the MyDetails view on the {@link com.taitsmith.swolemate.activities.BuddyActivity}
 * Allows users to set their personal details to be displayed to other users (stored in Firebase DB)
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
    @BindView(R.id.shortBioEditText)
    EditText bioEditText;
    @BindView(R.id.savedGymsListView)
    ListView gymListView;

    private FirebaseUser user;
    private FirebaseAuth auth;
    private GymAdapter adapter;
    private RealmResults<GymLocation> gymLocations;
    private Realm realm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_personal_details, container, false);
        ButterKnife.bind(this, rootView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        realm = Realm.getInstance(realmConfiguration);

        gymLocations = realm.where(GymLocation.class).findAll();

        adapter = new GymAdapter(getContext(), gymLocations);

        gymListView.setAdapter(adapter);

        gymListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteLocation(position);
                return true;
            }
        });


        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(personalPortait);
            personalName.setText(user.getDisplayName());
        }

        return rootView;
    }

    private void deleteLocation(int position) {
        GymLocation location = gymLocations.get(position);

        realm.beginTransaction();
        location.deleteFromRealm();
        realm.commitTransaction();

        adapter.notifyDataSetChanged();
    }


}
