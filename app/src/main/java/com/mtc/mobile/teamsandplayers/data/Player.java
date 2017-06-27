package com.mtc.mobile.teamsandplayers.data;

/**
 * Holds Player data.
 */
public class Player {
    String mName;
    String mAge;
    String mClub;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String age) {
        this.mAge = age;
    }

    public String getClub() {
        return mClub;
    }

    public void setClub(String club) {
        this.mClub = club;
    }
}
