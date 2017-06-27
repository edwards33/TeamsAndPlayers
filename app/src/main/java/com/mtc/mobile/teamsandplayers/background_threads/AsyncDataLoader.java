package com.mtc.mobile.teamsandplayers.background_threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mtc.mobile.teamsandplayers.data.Player;
import com.mtc.mobile.teamsandplayers.data.Team;
import com.mtc.mobile.teamsandplayers.data.TeamsAndPlayersData;
import com.mtc.mobile.teamsandplayers.interfaces.UpdateListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides background connection to URL and notifies MainActivity that new Players and Teams were downloaded.
 */
public class AsyncDataLoader extends AsyncTask<String, Void, Void> {
    ProgressDialog mProgress;
    Context mActivityContext;
    HttpClient mHttpClient;
    TeamsAndPlayersData mTeamsAndPlayersData;
    UpdateListView mUpdateListViw;

    public static final String SEARCH_TYPE_PLAYERS = "players";
    public static final String SEARCH_TYPE_TEAMS = "teams";

    private static final int LOAD_DATA = 2;
    private static final int LOAD_MORE_DATA = 3;

    private static final String H_REQUEST = "HTTP-REQUEST";
    private static final String J_OBJECT = "JSON-OBJECT";

    private static final String TAG_RESULT = "result";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_TEAMS = "teams";

    private static final String TAG_PLAYER_FIRST_NAME = "playerFirstName";
    private static final String TAG_PLAYER_SECOND_NAME = "playerSecondName";
    private static final String TAG_PLAYER_AGE = "playerAge";
    private static final String TAG_PLAYER_CLUB = "playerClub";

    private static final String TAG_TEAM_NAME = "teamName";
    private static final String TAG_TEAM_STADIUM = "teamStadium";
    private static final String TAG_TEAM_CITY = "teamCity";

    private static final String API_SEARCH_STRING = "searchString";
    private static final String API_SEARCH_TYPE = "searchType";
    private static final String API_OFFSET = "offset";

    public AsyncDataLoader(Context context, TeamsAndPlayersData data, UpdateListView updateListViw) {
        this.mActivityContext = context;
        this.mHttpClient = new DefaultHttpClient();
        this.mTeamsAndPlayersData = data;
        this.mUpdateListViw = updateListViw;
    }

    @Override
    protected void onPreExecute() {
        // Showing progress dialog before sending http request
        mProgress = new ProgressDialog(mActivityContext);
        mProgress.setMessage("Please wait..");
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        switch (params.length){
            case AsyncDataLoader.LOAD_DATA:
                loadPlayersAndTeams(params[0], params[1]);
                break;
            case AsyncDataLoader.LOAD_MORE_DATA:
                loadMoreData(params[0], params[1], params[2]);
                break;
        }
        return null;
    }

    protected void onPostExecute(Void unused) {
        mUpdateListViw.updateOnReady();
        // closing progress dialog
        mProgress.dismiss();
    }

    /* Loads data based on the types of search*/
    private void loadMoreData(String url, String searchText, String searchType) {
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = getJsonForSearchType(searchType, searchText);
        String json = jsonObject.toString();
        String response = makeHttpRequest(json, httpPost);
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(response);
            JSONObject resultJsonObject = jsonObj.getJSONObject(TAG_RESULT);

            if(searchType.equals(SEARCH_TYPE_PLAYERS)){
                List<Player> playerList = getPlayersList(resultJsonObject);
                mTeamsAndPlayersData.addPlayersList(playerList); //sends data to TeamsAndPlayersData
            }else{
                List<Team> teamList = getTeamsList(resultJsonObject);
                mTeamsAndPlayersData.addTeamsList(teamList); //sends data to TeamsAndPlayersData
            }
        } catch (JSONException e) {
            Log.e(J_OBJECT, "Error in json parsing: " + e.toString());
        }
    }

    /* Loads data for the both types of search*/
    private void loadPlayersAndTeams(String url, String searchText) {
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = getJsonForSearchText(searchText);
        String json = jsonObject.toString();
        String response = makeHttpRequest(json, httpPost);
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(response);
            JSONObject resultJsonObject = jsonObj.getJSONObject(TAG_RESULT);

            List<Player> playerList = getPlayersList(resultJsonObject);
            List<Team> teamList = getTeamsList(resultJsonObject);

            mTeamsAndPlayersData.addPlayersAndTeams(teamList, playerList);//sends data to TeamsAndPlayersData
        } catch (JSONException e) {
            Log.e(J_OBJECT, "Error in json parsing: " + e.toString());
        }
    }

    /*Executes POST request to the given URL*/
    private String makeHttpRequest(String json, HttpPost httpPost){
        String result = "";
        InputStream inputStream = null;
        try {
            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = mHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            Log.e(H_REQUEST, "Error in http request: " + e.toString());
        }

        return result;
    }

    /* Returns json for certain types of search*/
    private JSONObject getJsonForSearchType(String type, String searchText){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(API_SEARCH_STRING, searchText);
            jsonObject.put(API_SEARCH_TYPE, type);

            String offset = null;
            //get offset related to search type
            if(type.equals(SEARCH_TYPE_PLAYERS)) offset = mTeamsAndPlayersData.getPlayersListOffset();
            if(type.equals(SEARCH_TYPE_TEAMS)) offset = mTeamsAndPlayersData.getTeamsListOffset();

            jsonObject.put(API_OFFSET, offset);

        } catch (JSONException e) {
            Log.e(J_OBJECT, "Error in json object: " + e.toString());
        }
        return jsonObject;
    }

    /*Returns json for all the types of search*/
    private JSONObject getJsonForSearchText(String searchText){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(API_SEARCH_STRING, searchText);
        } catch (JSONException e) {
            Log.e(J_OBJECT, "Error in json object: " + e.toString());
        }
        return jsonObject;
    }

    private List<Player> getPlayersList(JSONObject jsonObject) throws JSONException{
        List<Player> playerList = new ArrayList<Player>();

        try {
            JSONArray playersJsonArray = jsonObject.getJSONArray(TAG_PLAYERS);
            if(playersJsonArray != null){
                for (int i = 0; i < playersJsonArray.length(); i++){
                    Player player = new Player();
                    JSONObject playerJsonObject = playersJsonArray.getJSONObject(i);
                    player.setAge(playerJsonObject.getString(TAG_PLAYER_AGE));
                    player.setClub(playerJsonObject.getString(TAG_PLAYER_CLUB));
                    player.setName(playerJsonObject.getString(TAG_PLAYER_FIRST_NAME) +
                            " " + playerJsonObject.getString(TAG_PLAYER_SECOND_NAME));

                    playerList.add(player);
                }
            }
        } catch (JSONException e) {
            throw new JSONException("Cannot parse JSON for Players Array");
        }
        return playerList;
    }

    private List<Team> getTeamsList(JSONObject jsonObject) throws JSONException{
        List<Team> teamList = new ArrayList<Team>();

        try {
            JSONArray teamsJsonArray = jsonObject.getJSONArray(TAG_TEAMS);
            if(teamsJsonArray != null){
                for (int i = 0; i < teamsJsonArray.length(); i++){
                    Team team = new Team();
                    JSONObject teamJsonObject = teamsJsonArray.getJSONObject(i);
                    team.setStadium(teamJsonObject.getString(TAG_TEAM_STADIUM));
                    team.setCity(teamJsonObject.getString(TAG_TEAM_CITY));
                    team.setName(teamJsonObject.getString(TAG_TEAM_NAME));
                    teamList.add(team);
                }
            }
        } catch (JSONException e) {
            throw new JSONException("Cannot parse JSON for Teams Array");
        }
        return teamList;
    }
}
