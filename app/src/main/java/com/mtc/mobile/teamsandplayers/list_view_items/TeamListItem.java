package com.mtc.mobile.teamsandplayers.list_view_items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter.RowType;;
import com.mtc.mobile.teamsandplayers.data.Team;
import com.mtc.mobile.teamsandplayers.interfaces.Item;

/**
 * Provides the listview with Teams items.
 */
public class TeamListItem implements Item {
    private final String teamName;
    private final String city;
    private final String stadium;

    public TeamListItem(Team team) {
        this.teamName = team.getName();
        this.city = team.getCity();
        this.stadium = team.getStadium();
    }

    @Override
    public int getViewType() {
        return RowType.TEAM_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.team_item, null);// Team layout initialization
        } else {
            view = convertView;
        }

        TextView teamView = (TextView) view.findViewById(R.id.team_value);
        TextView cityView = (TextView) view.findViewById(R.id.city_value);
        TextView stadiumView = (TextView) view.findViewById(R.id.stadium_value);
        teamView.setText(teamName);
        cityView.setText(city);
        stadiumView.setText(stadium);

        return view;
    }
}
