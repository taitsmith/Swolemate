package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.utils.BuddyAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.activities.BuddyActivity.buddyList;

/**
 * Shows you a list of buddies. Calls to the Firebase DB to find all buddies whose set location matches
 * the location of the current user. Filters out those who are listed as 'hidden', and then displays
 * them in a recyclerview.
 */

public class BuddyListFragment extends Fragment {
    @BindView(R.id.buddyListRecyclerView)
    RecyclerView buddyListRecycler;
    @BindView(R.id.buddyListProgressBar)
    ProgressBar loadingIndicator;
    @BindView(R.id.buddyListErrorView)
    TextView errorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buddy_list, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        BuddyAdapter adapter = new BuddyAdapter(buddyList, getContext());

        buddyListRecycler.setLayoutManager(manager);
        buddyListRecycler.setAdapter(adapter);

        return rootView;
    }
}
