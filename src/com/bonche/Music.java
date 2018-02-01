package com.bonche;

import static com.bonche.MusicForm.DURATION_FEE;
import static com.bonche.MusicForm.DURATION_FEE_SET_FREE;
import static com.bonche.MusicForm.DURATION_THRESHOLD;

/** Created by Petar Bonchev ( 1607262 )**/

public class Music extends MediaFile {

    private String artist;
    public final int MUSIC_AGE_VERIFICATION = 0;
    private String imageIconPath = "";

    public Music(String songTitle, int seconds, String artist) {
        super(songTitle, seconds);
        this.artist = artist;
    }

    @Override
    public int getAgeVerification() {
        return MUSIC_AGE_VERIFICATION;
    }

    public String getArtistOrDirector() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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
        if (getDuration() < DURATION_THRESHOLD) return DURATION_FEE_SET_FREE;
        else return DURATION_FEE;
    }

    @Override
    public String toString() {
        return super.toString()
                + ",  Artist: " + this.getArtistOrDirector();
    }

}
