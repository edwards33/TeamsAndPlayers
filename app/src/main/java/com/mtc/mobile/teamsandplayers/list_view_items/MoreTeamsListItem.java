package com.mtc.mobile.teamsandplayers.list_view_items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter.RowType;
import com.mtc.mobile.teamsandplayers.background_threads.AsyncDataLoader;
import com.mtc.mobile.teamsandplayers.data.TeamsAndPlayersData;
import com.mtc.mobile.teamsandplayers.interfaces.Item;
import com.mtc.mobile.teamsandplayers.interfaces.UpdateListView;

/**
 * Adds More Teams button into the listview.
 */
public class MoreTeamsListItem implements Item {
    AsyncDataLoader mDataLoader;
    String mSearchText;
    Context context;
    UpdateListView mUpdateListView;
    TeamsAndPlayersData mTeamsAndPlayersData;

    public MoreTeamsListItem(Context context, String searchText, UpdateListView view, TeamsAndPlayersData data){
        this.context = context;
        this.mSearchText = searchText;
        this.mUpdateListView = view;
        this.mTeamsAndPlayersData = data;
    }

    @Override
    public int getViewType() {
        return RowType.MORE_TEAMS_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.more_teams_item, null);// More Teams layout initialization
        } else {
            view = convertView;
        }

        Button loadTeamsButton = (Button) view.findViewById(R.id.load_teams);

        loadTeamsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDataLoader = new AsyncDataLoader(context, mTeamsAndPlayersData, mUpdateListView);
                mDataLoader.execute(new String[]{context.getString(R.string.api_url), mSearchText, AsyncDataLoader.SEARCH_TYPE_TEAMS});
            }
        });

        return view;
    }
}
