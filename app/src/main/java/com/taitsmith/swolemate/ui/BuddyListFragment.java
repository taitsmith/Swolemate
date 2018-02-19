package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;
import com.taitsmith.swolemate.utils.BuddyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static com.taitsmith.swolemate.activities.BuddyActivity.me;
import static com.taitsmith.swolemate.utils.FirebaseUtils.getBuddyList;

/**
 * Shows you a list of people. Calls to the Firebase DB to find all people whose set location matches
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

        loadingIndicator.setVisibility(View.VISIBLE);

        getBuddyListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Person>>() {
                    @Override
                    public void onCompleted() {
                        loadingIndicator.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText(R.string.toast_something_wrong);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Person> people) {
                        if (people.size() == 0) {
                            errorView.setVisibility(View.VISIBLE);
                            errorView.setText(R.string.buddy_activity_no_people_available);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        BuddyAdapter adapter = new BuddyAdapter(people, getContext());
                        buddyListRecycler.setLayoutManager(manager);
                        buddyListRecycler.setAdapter(adapter);
                    }
                });

        return rootView;
    }

    public Observable<List<Person>> getBuddyListObservable() {
        return rx.Observable.defer(new Func0<rx.Observable<List<Person>>>() {
            @Override
            public Observable<List<Person>> call() {
                return Observable.just(getBuddyList(me.getCityLocation()));
            }
        });
    }



}
