package com.rideout.app.mobile.rideout.rideDetails;

import android.graphics.drawable.Drawable;

/**
 * Created by taylorwashington on 5/10/15.
 */
public class Rider {

    public final Drawable photo;  // Rider photo/category icon
    public final String name;   // the text for the ListView item title
    public final Drawable phone; // optional icons for call and text (riders)
    public final Drawable text;

    public Rider(Drawable photo, String name, Drawable phone, Drawable text) {
        this.photo = photo;
        this.name = name;
        this.phone = phone;
        this.text = text;
    }
}
