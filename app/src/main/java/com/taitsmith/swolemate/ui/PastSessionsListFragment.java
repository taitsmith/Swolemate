package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.utils.PastSessionsAdapter;
import com.taitsmith.swolemate.data.Session;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * Fragment that displays a list of past sessions (one single trip to the gym consisting of
 * multiple workouts) in the main activity.
 */

public class PastSessionsListFragment extends Fragment{
    OnWorkoutClickListener listener;
    private ListView listView;

    public interface OnWorkoutClickListener {
        void onWorkoutSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnWorkoutClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Must implement OnWorkoutClickListenter");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_workouts_fragment, container, false);

        listView = rootView.findViewById(R.id.past_workouts_grid_view);

        Realm realm = Realm.getInstance(realmConfiguration);

        RealmResults<Session> realmResults = realm.where(Session.class)
                .findAllSorted("_id", Sort.DESCENDING);

        final PastSessionsAdapter adapter = new PastSessionsAdapter(getContext(), realmResults);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listener.onWorkoutSelected(position);
            }
        });

        return rootView;
    }
}
