package com.mtc.mobile.teamsandplayers.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds arrays of Teams and Players.
 */
public class TeamsAndPlayersData {
    List<Player> mPlayersList;
    List<Team> mTeamsList;

    public static boolean isMorePlayersAvailable;
    public static boolean isMoreTeamsAvailable;

    public TeamsAndPlayersData (){
        this.mPlayersList = new ArrayList<Player>();
        this.mTeamsList = new ArrayList<Team>();
        isMorePlayersAvailable = false;
        isMoreTeamsAvailable = false;
    }

    public void addTeamsList(List<Team> list){
        updateTeamsList(list);
    }

    public void addPlayersList(List<Player> list){
        updatePlayersList(list);
    }

    public void addPlayersAndTeams(List<Team> teamsList, List<Player> playerList){
        updateTeamsList(teamsList);
        updatePlayersList(playerList);
    }

    private void updateTeamsList(List<Team> list){
        for (Team i : list){
            mTeamsList.add(i);
        }
        if (list.size() < 10){
            isMoreTeamsAvailable = false;
        }else{
            isMoreTeamsAvailable = true;
        }
    }

    private void updatePlayersList(List<Player> list){
        for (Player i : list){
            mPlayersList.add(i);
        }
        if (list.size() < 10){
            isMorePlayersAvailable = false;
        }else{
            isMorePlayersAvailable = true;
        }
    }

    public List<Player> getPlayersList() {
        return mPlayersList;
    }

    public List<Team> getTeamsList() {
        return mTeamsList;
    }

    public String getPlayersListOffset(){
        return "" + mPlayersList.size();
    }

    public String getTeamsListOffset(){
        return "" + mTeamsList.size();
    }
}
