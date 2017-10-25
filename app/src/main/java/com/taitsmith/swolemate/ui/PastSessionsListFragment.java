package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.PastSessionsAdapter;
import com.taitsmith.swolemate.data.Session;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.data;
import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createSessionList;

/**
 * Fragment that displays a list of past sessions (one single trip to the gym consisting of
 * multiple workouts) in the main activity. The LoaderCallbacks is implemented here since it won't
 * allow you to call FragmentManager.commit() from onLoadFinished, which is what would need to happen
 * if it were in MainActivity.
 * TODO make an error TV for no workouts in DB, show a progress bar.
 */

public class PastSessionsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<RealmResults<Session>>{
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

        LoaderManager.LoaderCallbacks<RealmResults<Session>> callbacks = this;
        getActivity().getSupportLoaderManager().initLoader(11, null, callbacks);

        return rootView;
    }

    //db queries can be time consuming so we'll use a loader.
    @Override
    public Loader<RealmResults<Session>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<RealmResults<Session>>(getContext()) {
            RealmResults<Session> sessionList = null;

            @Override
            protected void onStartLoading() {
                if (sessionList != null) {
                    deliverResult(sessionList);
                } else {
                    forceLoad();
                }
            }

            @Override
            public RealmResults<Session> loadInBackground() {
                Realm realm = Realm.getInstance(realmConfiguration);

                return realm.where(Session.class)
                        .findAllSorted("_id", Sort.DESCENDING);
            }

            @Override
            public void deliverResult(RealmResults<Session> data) {
                sessionList = data;
                super.deliverResult(sessionList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<RealmResults<Session>> loader, final RealmResults<Session> data) {
        final PastSessionsAdapter adapter = new PastSessionsAdapter(getContext(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listener.onWorkoutSelected(position);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<RealmResults<Session>> loader) {
        //i'm just here because i have to be.
    }
}
