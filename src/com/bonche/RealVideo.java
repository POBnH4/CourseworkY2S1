package com.bonche;

import javafx.scene.media.Media;

/** Created by Petar Bonchev ( 1607262 )**/


public class RealVideo {
    private String title;
    private String artist;
    private Media video;

    public RealVideo(String title, String artist, Media video) {
        this.title = title;
        this.artist = artist;
        this.video = video;
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

    public Media getVideo() {
        return video;
    }

    public void setVideo(Media song) {
        this.video = song;
    }
}
