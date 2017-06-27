package com.mtc.mobile.teamsandplayers.list_view_items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter.RowType;
import com.mtc.mobile.teamsandplayers.data.Player;
import com.mtc.mobile.teamsandplayers.interfaces.Item;

/**
 * Provides the listview with Player items.
 */
public class PlayerListItem implements Item {
    private final String playerName;
    private final String age;
    private final String club;

    public PlayerListItem(Player player) {
        this.playerName = player.getName();
        this.age = player.getAge();
        this.club = player.getClub();
    }

    @Override
    public int getViewType() {
        return RowType.PLAYER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.player_item, null);// Player layout initialization
        } else {
            view = convertView;
        }

        TextView playerView = (TextView) view.findViewById(R.id.player_value);
        TextView ageView = (TextView) view.findViewById(R.id.age_value);
        TextView clubView = (TextView) view.findViewById(R.id.club_value);
        playerView.setText(playerName);
        ageView.setText(age);
        clubView.setText(club);

        return view;
    }
}
