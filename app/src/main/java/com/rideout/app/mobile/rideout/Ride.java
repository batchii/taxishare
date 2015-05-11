package com.rideout.app.mobile.rideout;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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


    //CHANGED FROM STRINGS TO CALENDAR

    public Date getRideDate() {
        return getDate("date");
    }

    public void setRideDate(Date date) {
        put("date", date);
    }

    public Date getRideTime() {
        return getDate("time");
    }

    public void setRideTime(Date time) {
        put("time", time);
    }

    /*
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
*/

    public ParseGeoPoint getOrigin() { return getParseGeoPoint("origin"); }

    public void setOrigin(ParseGeoPoint loc) {
        put("origin", loc);
    }
    //Use the Android GeoCode library.  Have it convert an address to a lattitude and longitude and then use those coordinates for Parse GeoPoints.

    public String getStartLocation() {
        return getString("start");
    }

    public void setStartLocation(String start) {
        put("start", start);
    }

    public String getEndLocation() {
        return getString("end");
    }

    public void setEndLocation(String end) {
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

    public void setRiders(ParseUser user) {
        ArrayList<ParseUser> temp = new ArrayList<>();
        temp.add(user);
        put("riders", temp);
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

    public boolean hasRider(ParseUser user) {
        ArrayList<ParseUser> users = getRiders();
        return users.contains(user);
    }



    //Updating the Riders list!!!!
    //Push notifcation to all the phones involved in ride so that i run the query then.  Right way to do it.
    //Inefficient but easier way: query every x amount of time.  Much slower.

}