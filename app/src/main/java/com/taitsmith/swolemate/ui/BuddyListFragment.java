package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.BuddyViewActivity;
import com.taitsmith.swolemate.data.Person;
import com.taitsmith.swolemate.utils.BuddyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.activities.BuddySearchActivity.me;

/**
 * Shows you a list of buddies. Calls to the Firebase DB to find all buddies whose set location matches
 * the location of the current user. Filters out those who are listed as 'hidden', and then displays
 * them in a recyclerview.
 */

public class BuddyListFragment extends Fragment {
    @BindView(R.id.buddyListView)
    ListView buddyListView;
    @BindView(R.id.buddyListProgressBar)
    ProgressBar loadingIndicator;
    @BindView(R.id.buddyListErrorView)
    TextView errorView;

    Query query;
    ValueEventListener buddyListener;
    public BuddyBaseAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buddy_list, container, false);
        ButterKnife.bind(this, rootView);

        hideUi(true);

        final List<Person> buddyList = new ArrayList<>();
        adapter = new BuddyBaseAdapter(getContext(), buddyList);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        query = database.child("People").orderByChild("cityLocation").equalTo(me.getCityLocation());

        buddyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buddyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person =  snapshot.getValue(Person.class);
                    if (!person.isHidden()) {
                        buddyList.add(person);
                    }
                }
                adapter.notifyDataSetChanged();
                hideUi(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LOG", databaseError.toException().toString());
            }
        };

        query.addValueEventListener(buddyListener);

        buddyListView.setAdapter(adapter);
        buddyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person buddy = buddyList.get(i);
                Intent intent = new Intent(getContext(), BuddyViewActivity.class);
                intent.putExtra("SELECTED_BUDDY", buddy);
                startActivity(intent);
            }
        });

        return rootView;
    }

    void hideUi(boolean hide) {
        if (hide) {
            buddyListView.setVisibility(View.INVISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            buddyListView.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeEventListener(buddyListener);
    }
}
