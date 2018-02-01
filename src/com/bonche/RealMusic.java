package com.bonche;

import javafx.scene.media.Media;

/** Created by Petar Bonchev ( 1607262 )**/


public class RealMusic {

    private String title;
    private String artist;
    private Media song;

    public RealMusic(String title, String artist, Media song) {
        this.title = title;
        this.artist = artist;
        this.song = song;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Media getSong() {
        return song;
    }

    public void setSong(Media song) {
        this.song = song;
    }
}
