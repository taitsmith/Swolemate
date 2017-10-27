package com.taitsmith.swolemate.data;

import java.util.List;

/**
 * A person. To be used for sign-in purposes and the option to find a workout buddy. Data will be
 * stored in a Firebase DB and assigned to objects when a Location is selected. Fields are fairly
 * self-explanatory.
 */

public class Person {
    private String name, shortBio, cityLocation;
    private int age;
    private List<String> activities;

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
}
