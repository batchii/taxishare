package com.rideout.app.mobile.rideout.createARide;

import android.graphics.drawable.Drawable;

/**
 * Created by atab7_000 on 4/13/2015.
 */
public class ListViewItem {
    public final Drawable icon;       // the drawable for the ListView item ImageView
    public final String title;        // the text for the ListView item title
    public  String description;  // the text for the ListView item description

    public ListViewItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }
}
