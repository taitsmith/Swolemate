package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.PastSessionsAdapter;
import com.taitsmith.swolemate.data.Session;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.SessionCreator.createSessionList;

/**
 * Fragment that displays a list of past sessions (one single trip to the gym consisting of
 * multiple workouts) in the main activity. The LoaderCallbacks is implemented here since it won't
 * allow you to call FragmentManager.commit() from onLoadFinished, which is what would need to happen
 * if it were in MainActivity.
 * TODO make an error TV for no workouts in DB, show a progress bar.
 */

public class PastSessionsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Session>>{
    OnWorkoutClickListener listener;
    private GridView gridView;

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
        gridView = rootView.findViewById(R.id.past_workouts_grid_view);

        LoaderManager.LoaderCallbacks<List<Session>> callbacks = this;
        getActivity().getSupportLoaderManager().initLoader(420, null, callbacks);

        return rootView;
    }

    //db queries can be time consuming so we'll use a loader.
    @Override
    public Loader<List<Session>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Session>>(getContext()) {
            List<Session> sessionList = null;

            @Override
            protected void onStartLoading() {
                if (sessionList != null) {
                    deliverResult(sessionList);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Session> loadInBackground() {
                List<Session> sessionList = new ArrayList<>();
                try {
                    sessionList = createSessionList(getContext());
                } catch (CursorIndexOutOfBoundsException e) {
                    Log.d("LOG ", e.toString());
                }
                return sessionList;
            }

            @Override
            public void deliverResult(List<Session> data) {
                sessionList = data;
                super.deliverResult(sessionList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Session>> loader, final List<Session> data) {
        final PastSessionsAdapter adapter = new PastSessionsAdapter(getContext(), data);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listener.onWorkoutSelected(position);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Session>> loader) {
        //i'm just here because i have to be.
    }
}
