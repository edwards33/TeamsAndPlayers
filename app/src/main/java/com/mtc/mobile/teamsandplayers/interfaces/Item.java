package com.mtc.mobile.teamsandplayers.interfaces;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Interface for listview items in activity_main layout
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
