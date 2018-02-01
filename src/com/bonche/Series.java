package com.bonche;

import static com.bonche.MusicForm.CHILD_AGE_THRESHOLD;
import static com.bonche.MusicForm.DISCOUNT_PENCE_COST;
import static com.bonche.MusicForm.PENCE_COST;

/** Created by Petar Bonchev ( 1607262 )**/

public class Series extends Video {

    private int episode;
    private String episodeTitle;
    private int ageVerification;
    private String imageIconPath = "";

    public Series(String title, int seconds, int age, int episode, String episodeTitle) {
        super(title, seconds, age);
        this.episode = episode;
        this.ageVerification = age;
        this.episodeTitle = episodeTitle;
    }

    public int getEpisode() {
        return episode;
    }
    public void setEpisode(int episode) {
        this.episode = episode;
    }
    public String getEpisodeTitle() {
        return episodeTitle;
    }
    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    @Override
    public int getAgeVerification() {
        return this.ageVerification;
    }

    public String getArtistOrDirector(){ return "Unknown"; }

    @Override
    public String toString() {
        return super.toString()
                + ", Episode: " + this.getEpisode()
                + ", Episode title: " + this.getEpisodeTitle();
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
        if (this.getAgeRestriction() < CHILD_AGE_THRESHOLD) return DISCOUNT_PENCE_COST;
        else return PENCE_COST;
    }


}
