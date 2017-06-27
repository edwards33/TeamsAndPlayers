package com.mtc.mobile.teamsandplayers.list_view_items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mtc.mobile.teamsandplayers.R;
import com.mtc.mobile.teamsandplayers.arrayadapters.TeamsAndPlayersArrayAdapter.RowType;
import com.mtc.mobile.teamsandplayers.interfaces.Item;

/**
 * Provides the listview with header items .
 */
public class HeaderListItem implements Item {
    private final String headerName;

    public HeaderListItem(String name) {
        this.headerName = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.header, null);// Header layout initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.delimiter);
        text.setText(headerName);

        return view;
    }
}
