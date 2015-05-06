package com.rideout.app.mobile.rideout.myrides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.rideout.app.mobile.rideout.R;
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

        // initialize the items list
        mItems = new ArrayList<RideItem>();
        Resources resources = getResources();
        //Need to populate from database
        //mItems.add(new RideItem(getString(R.string.num_riders), getString(R.string.time), getString(R.string.ride_name)));

        // initialize and set the list adapter
        //setListAdapter(new MyRidesArrayAdapter(getActivity(), mItems));

        //FOR FUDGING PURPOSES!!!!

        adapter = new MyRidesArrayAdapter(getActivity(), mItems);
        setListAdapter(adapter);
        String time = "6:00 PM - May 1, 2015";
        String title = "Johns Hopkins University -> BWI";
        String num = "1";
        RideItem r = new RideItem(num, title, time);
        mItems.add(r);
        adapter.notifyDataSetChanged();

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putInt("visited",1);
        peditor.commit();
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
