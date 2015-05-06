package com.rideout.app.mobile.rideout;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Subclass for parse object. Allows for IDE code completion, and
 * is best practice. Make sure subclass is declared in Application
 * class though.
 */
@ParseClassName("Ride")
public class Ride extends ParseObject {

    //Rides have Date, Time, Pickup Location, DropOff Location, Notes, Riders, Estimated Cost

    public Ride() {
        // A default constructor is required.
    }

    //Might have to convert to String instead.
    public String getRideDate() {
        return getString("date");
    }

    public void setRideDate(String date) {
        put("date", date);
    }

    public String getRideTime() {
        return getString("time");
    }

    public void setRideTime(String time) {
        put("time", time);
    }



    //Use the Android GeoCode library.  Have it convert an address to a lattitude and longitude and then use those coordinates for Parse GeoPoints.

    public ParseGeoPoint getStartLocation() {
        return getParseGeoPoint("start");
    }

    public void setStartLocation(ParseGeoPoint start) {
        put("start", start);
    }

    public ParseGeoPoint getEndLocation() {
        return getParseGeoPoint("end");
    }

    public void setEndLocation(ParseGeoPoint end) {
        put("end", end);
    }



    public String getNotes() {
        return getString("notes");
    }

    public void setNotes(String notes) {
        put("notes", notes);
    }

    public ArrayList<ParseUser> getRiders() {
        return (ArrayList)get("riders");
    }

    public void addRider(ParseUser user) {
        //first get the arraylist of riders
        //add the user to the arraylist
        //"re-put" the arraylist back onto parse..
        ArrayList<ParseUser> temp = getRiders();
        if (temp == null) {
            temp = new ArrayList<ParseUser>();
        }
        temp.add(user);
        put("riders", temp);
    }
    public void removeRider(ParseUser user) {
        ArrayList<ParseUser> temp = getRiders();
        temp.remove(user);
        if(temp.size() == 0) {
            //Destroy the ride?
        }
        put("riders", temp);
    }



    //Updating the Riders list!!!!
    //Push notifcation to all the phones involved in ride so that i run the query then.  Right way to do it.
    //Inefficient but easier way: query every x amount of time.  Much slower.

}