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
import com.parse.ParseUser;
import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.Ride;
import com.rideout.app.mobile.rideout.rideDetails.RideDetails;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
        query.whereEqualTo("riders", user);
        query.findInBackground(new FindCallback<Ride>() {
            private ArrayList<RideItem> temp = new ArrayList<RideItem>();
            public void done(List<Ride> rideList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + rideList.size() + " rides");
                    rideList = sort(rideList);
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

                        SimpleDateFormat ddf = new SimpleDateFormat("MM-dd-yyyy");
                        String formattedDate = ddf.format(r.getRideDate());


                        String description = formattedTime + " on " + formattedDate;
                        Log.d("NUM", "Num Riders: " + r.getRiders().size());
                        Log.d("TIME", "Ride Time: " + r.getRideTime());
                        Log.d("DATE", "Ride Date: " + r.getRideDate());
                        RideItem ri = new RideItem(num, title, description, r);
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
        String ride_id = item.ride.getObjectId();

        Intent rideDetails = new Intent(getActivity(), RideDetails.class);
        rideDetails.putExtra("rideid", ride_id);
        startActivity(rideDetails);

        // do something
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
    }

    private List<Ride> sort(List<Ride> rides) {
        // just return if the array list is null
        if (rides == null)
            return rides;

        // just return if the array list is empty or only has a single element
        if (rides.size() == 0 || rides.size() == 1)
            return rides;

        // declare an int variable to hold value of index at which the element
        // has the smallest value
        int smallestIndex;

        // declare an int variable to hold the smallest value for each iteration
        // of the outer loop
        Ride smallest;

        // for each index in the array list
        for (int curIndex = 0; curIndex < rides.size(); curIndex++) {

			/* find the index at which the element has smallest value */
            // initialize variables
            smallest = rides.get(curIndex);
            smallestIndex = curIndex;

            for (int i = curIndex + 1; i < rides.size(); i++) {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.setTime(rides.get(i).getRideDate());
                c2.setTime(rides.get(i).getRideTime());
                c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
                c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
                Date comp = c1.getTime();

                Calendar s1 = Calendar.getInstance();
                Calendar s2 = Calendar.getInstance();
                s1.setTime(smallest.getRideDate());
                s2.setTime(smallest.getRideTime());
                s1.set(Calendar.HOUR_OF_DAY, s2.get(Calendar.HOUR_OF_DAY));
                s1.set(Calendar.MINUTE, s2.get(Calendar.MINUTE));
                Date small = s1.getTime();




                if ((small.getTime() > comp.getTime())){
                    // update smallest
                    smallest = rides.get(i);
                    smallestIndex = i;
                }
            }

			/* swap the value */
            // do nothing if the curIndex has the smallest value
            if (smallestIndex == curIndex)
                ;
                // swap values otherwise
            else {
                Ride temp = rides.get(curIndex);
                rides.set(curIndex, rides.get(smallestIndex));
                rides.set(smallestIndex, temp);
            }

        }
        return rides;
    }

}
