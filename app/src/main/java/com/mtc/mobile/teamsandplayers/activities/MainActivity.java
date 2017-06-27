package com.mtc.mobile.teamsandplayers.activities;

import android.app.ListActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter;
import com.mtc.mobile.teamsandplayers.background_threads.AsyncDataLoader;
import com.mtc.mobile.teamsandplayers.data.Player;
import com.mtc.mobile.teamsandplayers.data.Team;
import com.mtc.mobile.teamsandplayers.data.TeamsAndPlayersData;
import com.mtc.mobile.teamsandplayers.interfaces.Item;
import com.mtc.mobile.teamsandplayers.interfaces.UpdateListView;
import com.mtc.mobile.teamsandplayers.list_view_items.HeaderListItem;
import com.mtc.mobile.teamsandplayers.list_view_items.MorePlayersListItem;
import com.mtc.mobile.teamsandplayers.list_view_items.MoreTeamsListItem;
import com.mtc.mobile.teamsandplayers.list_view_items.NoResultsListItem;
import com.mtc.mobile.teamsandplayers.list_view_items.PlayerListItem;
import com.mtc.mobile.teamsandplayers.list_view_items.TeamListItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    TeamsAndPlayersArrayAdapter mAdapter;
    EditText mSearchText;
    TeamsAndPlayersData mTeamsAndPlayersData;
    AsyncDataLoader mDataLoader;
    UpdateListView mUpdateListView;
    String mSearchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTeamsAndPlayersData = new TeamsAndPlayersData();

        mSearchText = (EditText) findViewById(R.id.search_text);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                searchListOfTeamsAndPlayers();
            }
        });

        List<Item> items = new ArrayList<Item>();
        items.add(new NoResultsListItem());
        mAdapter = new TeamsAndPlayersArrayAdapter(this, items);
        setListAdapter(mAdapter);

        /*Implements UpdateListView interface*/
        mUpdateListView = new UpdateListView() {
            @Override
            public void updateOnReady() {
                loadDataIntoListView();
            }
        };
    }

    /*Searches a text in the Database using background thread*/
    public void searchListOfTeamsAndPlayers(){
        if(isConnected()){
            mTeamsAndPlayersData = new TeamsAndPlayersData();
            mDataLoader = new AsyncDataLoader(this, mTeamsAndPlayersData, mUpdateListView);
            mSearchString = mSearchText.getText().toString();
            mDataLoader.execute(new String[]{getString(R.string.api_url), mSearchString});
        }else{
            Toast.makeText(this, "Check your Internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    /* Checks network connection*/
    public boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /*Loads Players and Teams data into ListView if new data were downloaded from the URL.*/
    public void loadDataIntoListView() {
        List<Player> playerList = mTeamsAndPlayersData.getPlayersList();
        List<Team> teamList = mTeamsAndPlayersData.getTeamsList();

        List<Item> items = new ArrayList<Item>();
        if(playerList.size() == 0 && teamList.size() == 0){ // if no result found
            items.add(new NoResultsListItem());
        }else{
            int indexOfListView = 0;
            if(playerList.size() != 0){ // if list of players is not empty
                items.add(new HeaderListItem(getString(R.string.header_players)));

                for (int i = 0; i < playerList.size(); i++){
                    Player player = playerList.get(i);
                    items.add(new PlayerListItem(player));
                }
                if (TeamsAndPlayersData.isMorePlayersAvailable){
                    items.add(new MorePlayersListItem(this, mSearchString, mUpdateListView, mTeamsAndPlayersData));
                }else{
                    items.add(new NoResultsListItem());
                }
            }

            if(teamList.size() != 0){ // if list of teams is not empty
                items.add(new HeaderListItem(getString(R.string.header_teams)));

                for (int i = 0; i < teamList.size(); i++){
                    Team team = teamList.get(i);
                    items.add(new TeamListItem(team));
                }
                if (TeamsAndPlayersData.isMoreTeamsAvailable){
                    items.add(new MoreTeamsListItem(this, mSearchString, mUpdateListView, mTeamsAndPlayersData));
                }else{
                    items.add(new NoResultsListItem());
                }
            }
        }
        mAdapter = new TeamsAndPlayersArrayAdapter(this, items);
        setListAdapter(mAdapter);
    }
}
