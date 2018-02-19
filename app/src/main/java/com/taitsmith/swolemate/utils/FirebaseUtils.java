package com.taitsmith.swolemate.utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.taitsmith.swolemate.data.Person;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.activities.BuddyActivity.uid;
import static com.taitsmith.swolemate.activities.BuddyActivity.user;


/**
 * For handling necessary Firebase communications.
 */

public class FirebaseUtils {

    public static void saveMyDetails(Person person, String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("People").child(uid).setValue(person);
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
                buddyList.clear();
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

    public static void updateMyLocation(String location) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("People").child(uid).child("cityLocation").setValue(location);
    }

    //gets the user information based on the currently signed in account.
    //sa
    public static Person whoAmI() {
        final Person me = new Person();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("People");

        reference = reference.child(user.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                me.setShortBio(person.getShortBio());
                me.setCityLocation(person.getCityLocation());
                me.setActivities(person.getActivities());
                me.setAge(person.getAge());
                me.setHidden(person.isHidden());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return me;
    }
}
