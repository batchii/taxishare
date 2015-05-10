package com.rideout.app.mobile.rideout.myrides;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by atab7_000 on 4/13/2015.
 */
public class RideItem {
    public final String numberRiders;       // the drawable for the ListView item ImageView
    public final String title;        // the text for the ListView item title
    public final String description;  // the text for the ListView item description

    public RideItem(String numRiders, String title, String description) {
        this.numberRiders = numRiders + " Riders";
        this.title = title;
        this.description = description;
    }
}
