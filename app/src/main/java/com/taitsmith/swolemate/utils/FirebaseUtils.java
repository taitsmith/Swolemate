package com.taitsmith.swolemate.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taitsmith.swolemate.data.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * For handling necessary Firebase communications.
 */

public class FirebaseUtils {

    public static void saveMyDetails(Person person, String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("People").child(uid).setValue(person);
    }

    //we want to be able to get the user's details from the DB
    //to display them when they open their details page
    public static Person getMyDetails(FirebaseUser user) {
        final List<Person> person = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("People");

        reference = reference.child(user.getUid());

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                person.add(dataSnapshot.getValue(Person.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addListenerForSingleValueEvent(listener);

        return person.get(0);
    }

    //Calls the Firebase db to get a list of people who share the same
    //location as the current user, then adds them to a list if they're listed
    //as visible.
    public static List<Person> getBuddyList(final String myLocation) {
        final List<Person> buddyList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("People").orderByChild("cityLocation").equalTo(myLocation);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LOG", Long.toString(dataSnapshot.getChildrenCount()));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person =  snapshot.getValue(Person.class);

                    if (!person.isHidden()) {
                        buddyList.add(person);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LOG", databaseError.toException().toString());
            }
        });
        return buddyList;
    }
}