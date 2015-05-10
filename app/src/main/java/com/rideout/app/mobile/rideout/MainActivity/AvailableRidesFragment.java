package com.rideout.app.mobile.rideout.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.Ride;
import com.rideout.app.mobile.rideout.myrides.MyRidesArrayAdapter;
import com.rideout.app.mobile.rideout.myrides.RideItem;
import com.rideout.app.mobile.rideout.rideDetails.RideDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AvailableRidesFragment extends ListFragment {

    private List<RideItem> mItems;
    private MyRidesArrayAdapter adapter;

    SharedPreferences myPrefs;
    //Populate from database here
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
        adapter = new MyRidesArrayAdapter(getActivity(), mItems);
        setListAdapter(adapter);

        //Get the user's current location
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double currlong = location.getLongitude();
        double currlat = location.getLatitude();

        ParseGeoPoint southwestOf = new ParseGeoPoint(currlat - .003436, currlong - .0064);
        ParseGeoPoint northeastOf = new ParseGeoPoint(currlat + .002074, currlong + .007832);


        ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
        query.whereWithinGeoBox("origin", southwestOf, northeastOf);
        query.findInBackground(new FindCallback<Ride>() {
            private ArrayList<RideItem> temp = new ArrayList<RideItem>();
            public void done(List<Ride> rideList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + rideList.size() + " rides");
                    for (int i = 0; i < rideList.size(); i++) {
                        Ride r = rideList.get(i);

                        //Time and date verification, otherwise don't show and delete from db
                        Date currDate = new Date();
                        long curr = currDate.getTime();
                        if ((curr > r.getRideDate().getTime()) && (curr > r.getRideTime().getTime())) {
                            r.deleteInBackground();
                            continue;
                        }

                        String num = "" + r.getRiders().size();
                        String title = r.getStartLocation() + " -> " + r.getEndLocation();

                        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
                        String formattedTime = sdf.format(r.getRideTime());

                        SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = ddf.format(r.getRideDate());


                        String description = formattedTime + " on " + formattedDate;
                        Log.d("NUM", "Num Riders: " + r.getRiders().size());
                        Log.d("TIME", "Ride Time: " + r.getRideTime());
                        Log.d("DATE", "Ride Date: " + r.getRideDate());
                        RideItem ri = new RideItem(num, title, description);
                        mItems.add(ri);
                        Log.d("BEFORE", "NUM " + mItems.get(0).numberRiders + " TITLE " + mItems.get(0).title + " DESC " + mItems.get(0).description);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
                adapter.notifyDataSetChanged();
            }
        });




        //FUDGE SHIT
        /*
        Resources resources = getResources();
        //Need to populate from database
        //mItems.add(new RideItem(getString(R.string.num_riders), getString(R.string.time), getString(R.string.ride_name)));
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

<<<<<<< HEAD
        int visited = myPrefs.getInt("visited", 0);
        if (visited == 1) {
            String time = "6:00 PM - May 1, 2015";
            String title = "Johns Hopkins University -> BWI";
            String num = "1";
            RideItem r = new RideItem(num, title, time);
            mItems.add(r);
        }*/
        // initialize and set the list  adapter

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        RideItem item = mItems.get(position);

        Intent rideDetails = new Intent(getActivity(), RideDetails.class);
        startActivity(rideDetails);

        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }

}
