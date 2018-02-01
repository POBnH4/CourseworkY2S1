package com.bonche;

import javax.swing.*;
import java.util.Comparator;

/** Created by Petar Bonchev ( 1607262 )**/


public abstract class MediaFile implements Comparable<MediaFile> {

    private int duration;
    private String title;
    private String imageFileName;
    private ImageIcon imageIcon = null;
    private static String imageFileDirectory = "src/resources/";

    /**
     * I changed the name from Media to MediaFile
     * because there are some other java library classes which
     * have " Media" as their name and I need to use them;
     */
    public MediaFile(String name, int seconds) {
        this.title = name;
        this.duration = seconds;
        this.imageFileName = "";
    }

    public abstract int getAgeVerification();

    public abstract String getArtistOrDirector();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public static String getImageFileDirectory() {
        return imageFileDirectory;
    }

    public static void setImageFileDirectory(String imageFileDirectory) {
        MediaFile.imageFileDirectory = imageFileDirectory;
    }

    @Override

    public String toString() {
        return this.getTitle()
                + ", Duration: " + this.getDuration() + "s, " +
                "Cost: " + costInPence() + "p";
    }

    public abstract int costInPence();

    public abstract void setImageIconPath(String imageIcon);
    public abstract String getImageIconPath();
    @Override
    public int compareTo(MediaFile o) {
        return this.getTitle().compareTo(o.getTitle());
    }

    public static class DurationComparator implements Comparator<MediaFile> {
        public int compare(MediaFile m1, MediaFile m2) {
            return m2.getDuration() - m1.getDuration();
        }
    }

    public static class CostComparator implements Comparator<MediaFile> {
        public int compare(MediaFile m1, MediaFile m2) {
            return m2.costInPence() - m1.costInPence();
        }
    }

}
