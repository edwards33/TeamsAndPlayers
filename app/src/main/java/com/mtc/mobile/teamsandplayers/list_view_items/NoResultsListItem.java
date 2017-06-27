package com.mtc.mobile.teamsandplayers.list_view_items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter.RowType;
import com.mtc.mobile.teamsandplayers.interfaces.Item;

/**
 * Adds No Results label into the listview.
 */
public class NoResultsListItem implements Item {

    @Override
    public int getViewType() {
        return RowType.NO_RESULTS_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.no_results_item, null);// No Results layout initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.no_results_label);

        return view;
    }
}
