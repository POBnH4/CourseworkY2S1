package com.bonche;

/** Created by Petar Bonchev ( 1607262 )**/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.bonche.AddForm.MAXIMUM_NAME_CHARACTERS;

public class Database {


    /**
     * THE SQL CODE BELOW IS HARD CODED BECAUSE I WANTED TO TEST THE FUNCTIONALITY FIRST.
     * I KNOW THAT THE WAY I WROTE IT IS NOT EFFICIENT
     **/


    public final String DATABASE_LOCATION = "jdbc:sqlite:C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\DatabaseFiles\\MusicPlayer.db";
    private ArrayList<User> databaseList;
    private Statement statement;
    private User currentUser = null;
    private final int MAXIMUM_TYPE_CHARACTERS = 6;

    public Database() {
        this.databaseList = new ArrayList<>();
        initializeConnection();
    }

    public void initializeConnection() {
        try {
            Connection connection = DriverManager.getConnection(this.DATABASE_LOCATION);
            this.statement = connection.createStatement();
            this.statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the database connection!");
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String createPlaylist(PlayList playListName) {
        return "CREATE TABLE IF NOT EXISTS " + playListName + " (\n"
                + "PLAYLIST_NAME VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") PRIMARY KEY NOT NULL,\n"
                + "USERNAME VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") NOT NULL REFERENCES " + currentUser.getUsername() + "(USERNAME)" + ");";

    }

    public String createMedia(String mediaFileTitle, PlayList playListName) {
        return "CREATE TABLE IF NOT EXISTS " + mediaFileTitle + " (\n"
                + "class_type VARCHAR(" + MAXIMUM_TYPE_CHARACTERS + ") NOT NULL,\n"
                + "title VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") NOT NULL,\n"
                + "artist_name VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") DEFAULT 'Unknown',\n"
                + "duration INTEGER NOT NULL,\n"
                + "age_limit INTEGER,\n"
                + "episode_number INTEGER ,\n"
                + "episode_title VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") DEFAULT 'Unknown',\n"
                + "PLAYLIST_NAME VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") NOT NULL REFERENCES" + playListName.getPlaylistName() + "(PLAYLIST_NAME)" + ");";
    }

    public String insertInto(String user, String password) {
        //The name of the table is the same as the user's name;
        return "INSERT INTO " + user
                + "(USERNAME,PASSWORD)\n"
                + " VALUES(" + user + "," + password + ");";
    }

    public String createUserTable(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "USERNAME VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") PRIMARY KEY NOT NULL,\n"
                + "PASSWORD VARCHAR(" + MAXIMUM_NAME_CHARACTERS + ") NOT NULL);";
    }

    public String insertInto(PlayList playList, MediaFile mediaFile) {
        if (mediaFile instanceof Music) {
            return "INSERT INTO " + mediaFile.getTitle()
                    + "(class_type,title,artist_name,duration,age_limit,episode_number,episode_title,PLAYLIST_NAME)\n"
                    + " VALUES(" + "MUSIC,"
                    + mediaFile.getTitle() + ","
                    + mediaFile.getArtistOrDirector() + ","
                    + mediaFile.getDuration() + ","
                    + playList.getPlaylistName()
                    + ");";
        }
        if (mediaFile instanceof Film) {
            return "INSERT INTO " + mediaFile.getTitle()
                    + "(class_type,title,artist_name,duration,age_limit,episode_number,episode_title,PLAYLIST_NAME)\n"
                    + " VALUES(" + "FILM,"
                    + mediaFile.getTitle() + ","
                    + mediaFile.getArtistOrDirector() + ","
                    + mediaFile.getDuration()
                    + mediaFile.getAgeVerification() + ","
                    + playList.getPlaylistName()
                    + ");";
        }
        return "INSERT INTO " + mediaFile.getTitle()
                + "(class_type,title,artist_name,duration,age_limit,episode_number,episode_title,PLAYLIST_NAME)\n"
                + " VALUES(" + "SERIES,"
                + mediaFile.getTitle() + ","
                + mediaFile.getArtistOrDirector() + ","
                + mediaFile.getDuration() + ","
                + mediaFile.getAgeVerification() +
                +((Series) mediaFile).getEpisode() + ","
                + ((Series) mediaFile).getEpisodeTitle() + ","
                + playList.getPlaylistName()
                + ");";
    }

    public String deletePlaylistFromDatabase(PlayList playlistTables) {
        return "DROP TABLE " + playlistTables.getPlaylistName();
    }

    public String deleteMediaFromDatabase(MediaFile mediaFiles) {
        return "DROP TABLE " + mediaFiles.getTitle();
    }

    public String resetDatabase() {
        // method to delete everything ( for reset button);
        return "";
    }

    public String removeFromDatabase() {
        return ""; // method to remove a mediaFile(second button, JList);
    }

    public void execute(String statement) {
        try {
            this.statement.execute(statement);
            this.statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the database connection!");
        }
    }

    public ArrayList<User> getDatabaseList() {
        return databaseList;
    }

}
