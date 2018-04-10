package com.taitsmith.swolemate.ui;

import android.content.Context;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;
import com.taitsmith.swolemate.utils.BuddyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.activities.BuddyActivity.me;

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

    OnBuddyClickListener listener;
    public static BuddyBaseAdapter adapter;

    //so we can select someone from th list to see more
    public interface OnBuddyClickListener {
        void onBuddySelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnBuddyClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Must implement OnBuddyClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buddy_list, container, false);
        ButterKnife.bind(this, rootView);

        final List<Person> buddyList = new ArrayList<>();
        adapter = new BuddyBaseAdapter(getContext(), buddyList);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("People").orderByChild("cityLocation").equalTo(me.getCityLocation());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person =  snapshot.getValue(Person.class);
                    if (!person.isHidden()) {
                        buddyList.add(person);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LOG", databaseError.toException().toString());
            }
        });


        buddyListView.setAdapter(adapter);
        buddyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person buddy = buddyList.get(i);

                Toast.makeText(getContext(), buddy.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
