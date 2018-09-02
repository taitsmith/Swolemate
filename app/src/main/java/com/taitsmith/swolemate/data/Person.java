package com.taitsmith.swolemate.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

import javax.annotation.Nullable;

/**
 * A person. To be used for sign-in purposes and the option to find a workout buddy. Data will be
 * stored in a Firebase DB and assigned to objects when a Location is selected. Fields are fairly
 * self-explanatory.
 */

public class Person implements Parcelable {
    private String name;
    private String shortBio;
    private String cityLocation;

    private String photoUrl;
    private int age;
    private List<String> activities;
    private boolean hidden;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Person(String name, String shortBio, String cityLocation,
                  List<String> activities, @Nullable int age, @Nullable boolean hide, String photoUrl) {
        this.name = name;
        this.shortBio = shortBio;
        this.cityLocation = cityLocation;
        this.activities = activities;
        this.age = age;
        this.hidden = hide;
        this.photoUrl = photoUrl;
    }

    public Person() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //we want to pass people form place to place
    //general opinion claims parcelable is more efficient
    //than serializable.
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(shortBio);
        parcel.writeString(cityLocation);
        parcel.writeList(activities);
        parcel.writeString(photoUrl);
    }

    public static final Parcelable.Creator<Person> CREATOR =
            new Parcelable.Creator<Person>() {
                @Override
                public Person createFromParcel(Parcel parcel) {
                    return new Person(parcel);
                }

                @Override
                public Person[] newArray(int i) {
                    return new Person[i];
                }
            };

    private Person(Parcel in) {
        name = in.readString();
        shortBio = in.readString();
        cityLocation = in.readString();
        List<String> activities = in.createStringArrayList();
        photoUrl = in.readString();
    }
}
