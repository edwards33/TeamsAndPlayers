package com.mtc.mobile.teamsandplayers.data;

/**
 * Holds Team data.
 */
public class Team {
    String mName;
    String mCity;
    String mStadium;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getStadium() {
        return mStadium;
    }

    public void setStadium(String stadium) {
        this.mStadium = stadium;
    }
}
