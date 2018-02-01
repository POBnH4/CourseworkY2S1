package com.bonche;

import java.util.ArrayList;

import static com.bonche.MusicForm.SECONDS_IN_A_MINUTE;

/** Created by Petar Bonchev ( 1607262 )**/

public class Film extends Video {

    private String director;
    private int ageVerification;
    private ArrayList<String> actors;
    private final int COST_MULTIPLIER = 2;
    private String imageIconPath = "";

    public Film(String title, int seconds, int age, String director) {
        super(title, seconds, age);
        this.ageVerification = age;
        this.director = director;
        actors = new ArrayList<>();
    }

    @Override
    public int getAgeVerification() {
        return this.ageVerification;
    }

    public String getArtistOrDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void addActor(String actor) {
        actors.add(actor);
    }

    @Override
    public String toString() {
        return super.toString() + ", Director: " + director;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    @Override
    public void setImageIconPath(String imageIconPath) {
        this.imageIconPath = imageIconPath;
    }

    public String getImageIconPath(){
        return imageIconPath;
    }

    @Override
    public int costInPence() {
        return (getDuration() / SECONDS_IN_A_MINUTE) * COST_MULTIPLIER;
    }
}
