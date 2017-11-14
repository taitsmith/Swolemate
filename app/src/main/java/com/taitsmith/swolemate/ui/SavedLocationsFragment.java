package com.taitsmith.swolemate.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.GymLocation;
import com.taitsmith.swolemate.utils.GymAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * Shows a list of saved locations, allows user to delete them from DB
 */

public class SavedLocationsFragment extends Fragment {
    @BindView(R.id.savedGymsListView)
    ListView saveLocationsListView;

    private Realm realm;
    private RealmResults<GymLocation> gymLocations;
    private GymAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_locations, container, false);

        ButterKnife.bind(this, rootView);

        realm = Realm.getInstance(realmConfiguration);

        gymLocations = realm.where(GymLocation.class).findAll();

        adapter = new GymAdapter(getContext(), gymLocations);

        saveLocationsListView.setAdapter(adapter);

        saveLocationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteLocation(position);
                return true;
            }
        });

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
