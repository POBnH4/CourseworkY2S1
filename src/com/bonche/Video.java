package com.bonche;

/** Created by Petar Bonchev ( 1607262 )**/

public abstract class Video extends MediaFile {
    private int ageRestriction;

    public Video(String title, int seconds, int age) {
        super(title, seconds);
        this.ageRestriction = age;
    }

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int age) {
        this.ageRestriction = age;
    }


    @Override
    public String toString() {
        return super.toString()
                + ", Age Restriction: " + ageRestriction;
    }

}
