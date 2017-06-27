package com.mtc.mobile.teamsandplayers.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mtc.mobile.teamsandplayers.interfaces.Item;

import java.util.List;

/**
 * Takes a listview items that implements Item interface.
 */
public class TeamsAndPlayersArrayAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;

    public enum RowType {
        NO_RESULTS_ITEM, MORE_PLAYERS_ITEM, MORE_TEAMS_ITEM, TEAM_ITEM, PLAYER_ITEM, HEADER_ITEM
    }

    public TeamsAndPlayersArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}
