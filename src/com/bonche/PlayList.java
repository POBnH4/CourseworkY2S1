package com.bonche;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

import static com.bonche.MusicForm.FIRST_INDEX;
import static com.bonche.MusicForm.THROW_EXCEPTION_NUMBER;

/** Created by Petar Bonchev ( 1607262 )**/

public class PlayList {
    private ArrayList<MediaFile> items;
    private User user;
    private String playlistName;
    private MusicForm musicForm;

    public PlayList(User user, MusicForm musicForm, String playlistName) {
        this.user = user;
        this.items = new ArrayList<>();
        this.musicForm = musicForm;
        this.playlistName = (playlistName == null) ? ("Playlist " + this.musicForm.getPlayLists().size()) : playlistName;
        freebieMedia();
    }

    private void freebieMedia() {
        items.add(new Series("Game of Thrones", 3000, 18, 29, "The Rains of Castamere"));
        items.add(new Music("Losing my religion", 200, "REM"));
        items.add(new Film("The Last Jedi", 9120, 12, "Rian Johnson"));
    }

    public User getUser() {
        return user;
    }

    public int getNumItems() {
        return items.size();
    }

    public ArrayList<MediaFile> getItems() {
        return items;
    }

    public MediaFile getMedia(int i) {
        return items.get(i);
    }

    public void addMedia(MediaFile m) {
        if (this.user.purchase(m)) items.add(m);
    }

    public String getScheduleAsString() {
        StringBuilder rs = new StringBuilder();
        for (MediaFile m : items) {
            rs.append(m.toString()).append("\n");
        }
        return rs.toString();
    }

    public int getTotalDuration() {
        int time = 0;
        for (MediaFile m : items) {
            time += m.getDuration();
        }
        return time;
    }

    public int getTotalCost() {
        int cost = 0;
        for (MediaFile m : items) {
            cost += m.costInPence();
        }
        return cost;
    }

    public int getIndex(MediaFile mediaFile) {
        return IntStream.range(0, this.items.size())
                .filter(i -> this.items.get(i) == mediaFile)
                .findFirst()
                .orElse(THROW_EXCEPTION_NUMBER);
    }

    public boolean contains(MediaFile file) {
        return IntStream.range(FIRST_INDEX, this.items.size())
                .anyMatch(i -> this.items.get(i) == file);
    }

    @Override
    public String toString() {
        String str = "Number of Items:" + this.getNumItems() + "\t"
                + "Time: " + getTotalDuration()
                + getScheduleAsString();

        return str;
    }

    public void sortItemsByDuration() {
        this.items.sort(new MediaFile.DurationComparator());
    }

    public void sortItemsByTitle() {
        this.items.sort((o1, o2) -> Integer.compare(o1.getTitle().compareTo(o2.getTitle()), 0));
    }

    public void sortItemsByCost() {
        this.items.sort(new MediaFile.CostComparator());
    }

    public void sortItemsByArtistOrDirector() {
        this.items.sort(Comparator.comparing(MediaFile::getArtistOrDirector));
    }

    public void shuffleItems() {
        Collections.shuffle(this.items);
    }

    public void addFreeMedia(MediaFile m) {
        items.add(m);
    }

    public void removeMedia(int i) {
        items.remove(i);
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void moveMedia(int i, int j) {
        if (i < j && i >= 0 && i < items.size() && j < items.size()) {
            MediaFile temp = items.remove(j);
            items.add(i, temp);
        }
    }

    public void readFromFile(File f) {
        try {
            Scanner s = new Scanner(f);
            s.useDelimiter(",|\n");
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                String type = parts[0];
                String title = parts[1];
                int duration = Integer.parseInt(parts[2]);
                if (type.equals("Music")) {
                    String artist = parts[3];
                    Music item = new Music(title, duration, artist);
                    this.musicForm.addToJList(item);
                } else if (type.equals("Film")) {
                    int age = Integer.parseInt(parts[3]);
                    String director = parts[4];
                    Film item = new Film(title, duration, age, director);
                    this.musicForm.addToJList(item);
                } else if (type.equals("Series")) {
                    int age = Integer.parseInt(parts[3]);
                    int episode = Integer.parseInt(parts[4]);
                    String episodeTitle = parts[5];
                    Series item =
                            new Series(title, duration, age, episode, episodeTitle);
                    this.musicForm.addToJList(item);
                }
            }
            s.close();
        } catch (FileNotFoundException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error with the file has occurred.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
        ((DefaultTreeModel) this.musicForm.getPlaylistTree().getModel()).nodeStructureChanged(this.musicForm.getRoot());
    }


    public void saveToFile(File f) {
        boolean flag = false;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (MediaFile m : items) {
                String line = "";
                if (m instanceof Music) {
                    line += "Music," + m.getTitle() + ","
                            + m.getDuration() + "," + m.getArtistOrDirector();
                } else if (m instanceof Film) {
                    line += "Film," + m.getTitle() + ","
                            + m.getDuration() + ","
                            + ((Film) m).getAgeRestriction() + ","
                            + m.getArtistOrDirector();
                } else if (m instanceof Series) {
                    line += "Series," + m.getTitle() + ","
                            + m.getDuration() + ","
                            + ((Series) m).getAgeRestriction() + ","
                            + ((Series) m).getEpisode() + ","
                            + ((Series) m).getEpisodeTitle();
                }
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            flag = true;
        }
        if (flag)
            JOptionPane.showMessageDialog(null, "An error with the file has occurred.", "Alert", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "All the elements in your current playlist have been saved to the chosen directory.", "FYI", JOptionPane.INFORMATION_MESSAGE);
    }

}
