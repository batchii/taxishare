package com.rideout.app.mobile.rideout.rideDetails;

import android.graphics.drawable.Drawable;

public class DetailItem {

    public final Drawable icon;  // Rider photo/category icon
    public final String title;   // the text for the ListView item title
    public  String description;  // the text for the ListView item description
    //public final Drawable icon2; // optional icons for call and text (riders)
    //public final Drawable icon3;

    public DetailItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        //this.icon2 = icon2;
        //this.icon3 = icon3;
    }
}
