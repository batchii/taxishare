package com.rideout.app.mobile.rideout;

import android.app.Application;

import com.parse.Parse;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
/**
 * Created by Nikhil on 4/14/2015.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Ride.class);

        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "D45GKUfUQVf7NrWWnU2elwzKjtiqxMUrghnKWI4f", "OdInxU5E4mgg4SkrQgw8C8PVLbH9ToIxwiyjv381");

        ParseUser.enableAutomaticUser();

        ParseFacebookUtils.initialize(this);




    }
}
