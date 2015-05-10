package com.rideout.app.mobile.rideout.myrides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.Ride;
import com.rideout.app.mobile.rideout.rideDetails.RideDetails;


import java.util.ArrayList;
import java.util.List;


public class MyRidesListViewFragment extends ListFragment {

    private List<RideItem> mItems;
    private MyRidesArrayAdapter adapter;

    SharedPreferences myPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<RideItem>();
        adapter = new MyRidesArrayAdapter(getActivity(), mItems);
        setListAdapter(adapter);

        ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
        query.findInBackground(new FindCallback<Ride>() {
            private ArrayList<RideItem> temp = new ArrayList<RideItem>();
            public void done(List<Ride> rideList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + rideList.size() + " rides");
                    for (int i = 0; i < rideList.size(); i++) {
                        Ride r = rideList.get(i);
                        String num = "" + r.getRiders().size();
                        String title = r.getStartLocation() + " -> " + r.getEndLocation();
                        String description = r.getRideTime() + " on " + r.getRideDate();
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

        //Log.d("AFTER", "NUM " + mItems.get(0).numberRiders + " TITLE " + mItems.get(0).title + " DESC " + mItems.get(0).description);


        // initialize the items list

        Resources resources = getResources();
        //Need to populate from database
        //mItems.add(new RideItem(getString(R.string.num_riders), getString(R.string.time), getString(R.string.ride_name)));

        // initialize and set the list adapter
        //setListAdapter(new MyRidesArrayAdapter(getActivity(), mItems));

        //FOR FUDGING PURPOSES!!!!


        //String time = "6:00 PM - May 1, 2015";
        //String title = "Johns Hopkins University -> BWI";
        //String num = "1";
        //RideItem r = new RideItem(num, title, time);
        //mItems.add(r);



        //For FUDGING
/*        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("visited",1);
        peditor.commit();*/
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
