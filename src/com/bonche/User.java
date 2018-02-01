package com.bonche;

import java.util.ArrayList;

import static com.bonche.MusicForm.POUND_SYMBOL;

/** Created by Petar Bonchev ( 1607262 )**/

public class User {

    private final String username;
    private final String password;
    private int wallet;
    private int age;
    private ArrayList<PlayList> playLists;
    public final int PENCE_IN_POUND = 100;

    public User(String name, String password, int pence, int age) {
        this.username = name;
        this.password = password;
        this.wallet = pence;
        this.age = age;
        this.playLists = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void topUp(int pence) {
        wallet += pence;
    }

    public int getWallet() {
        return wallet;
    }

    public String getPassword() {
        return password;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(ArrayList<PlayList> playLists) {
        this.playLists = playLists;
    }

    public boolean purchase(MediaFile m) {
        if (wallet >= m.costInPence()) {
            wallet -= m.costInPence();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return username + " " + POUND_SYMBOL + (wallet / PENCE_IN_POUND) + "." + (wallet % PENCE_IN_POUND);
    }

}
