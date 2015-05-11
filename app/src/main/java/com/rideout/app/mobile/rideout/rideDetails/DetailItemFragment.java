package com.rideout.app.mobile.rideout.rideDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rideout.app.mobile.rideout.MapsActivity;
import com.rideout.app.mobile.rideout.NavigationDrawerFragment;
import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.Ride;
import com.rideout.app.mobile.rideout.myrides.MyRides;
import com.rideout.app.mobile.rideout.myrides.MyRidesListViewFragment;


import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DetailItemFragment extends ListFragment {

    private static final int TIMEPICKER_FRAGMENT = 1;
    private static final int DATEPICKER_FRAGMENT = 2;
    private static final int PICKUP_CODE = 3;
    private static final int DROPOFF_CODE = 4;

    private Date rideDate;
    private String formattedDate;
    private Date rideTime;
    private String formattedTime;
    private String startLocation;
    private String endLocation;
    private String notes;
    private ArrayList<ParseUser> riders;
    private String formattedRiders = "";

    private List<DetailItem> mItems = new ArrayList<>();
    private DetailArrayAdapter adapter;

    private String rideId;

    //private Ride current;
    //ParseUser user = ParseUser.getCurrentUser();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mItems = new ArrayList<>();

        Bundle args = getArguments();
        rideId = args.getString("ride");

        ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
        query.getInBackground(rideId, new GetCallback<Ride>() {
            public void done(Ride object, ParseException e) {
                if (e == null) {
                    populate(object);

                } else {
                    // something went wrong
                }
            }
        });

        // initialize and set the list adapter
        adapter = new DetailArrayAdapter(getActivity(), mItems);
        //setListAdapter(new MySimpleArrayAdapter(getActivity(), mItems));
        setListAdapter(adapter);
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
        DetailItem item = mItems.get(position);

        switch(position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                // open riders activity
                break;
            case 6:
                final Context context = getActivity();

                if (item.description.equals("Join Ride")) {
                    ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
                    query.getInBackground(rideId, new GetCallback<Ride>() {
                        public void done(Ride object, ParseException e) {
                            if (e == null) {
                                ParseUser user = ParseUser.getCurrentUser();
                                object.addRider(user);
                                object.saveInBackground();
                                populate(object);

                            } else {
                                // something went wrong
                            }
                        }
                    });
                } else {
                    ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
                    query.getInBackground(rideId, new GetCallback<Ride>() {
                        public void done(Ride object, ParseException e) {
                            if (e == null) {
                                ParseUser user = ParseUser.getCurrentUser();
                                object.removeRider(user);
                                object.saveInBackground();
                                if (object.getRiders().size() == 0) {
                                    object.deleteInBackground();
                                    Intent myRides = new Intent(getActivity(), MyRides.class);
                                    startActivity(myRides);
                                } else {
                                    populate(object);
                                }
                            } else {
                                // something went wrong
                            }
                        }
                    });
                }

                //ride.addRider(user);
                //ride.saveInBackground();
                break;
        }

    }

    private void populate(Ride ride) {
        //current = ride;
        ParseUser user = ParseUser.getCurrentUser();
        rideDate = ride.getRideDate();
        rideTime = ride.getRideTime();
        startLocation = ride.getStartLocation();
        endLocation = ride.getEndLocation();
        notes = ride.getNotes();
        riders = ride.getRiders();

        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
        formattedTime = sdf.format(rideTime);

        SimpleDateFormat ddf = new SimpleDateFormat("MM-dd-yyyy");
        formattedDate = ddf.format(rideDate);

        int counter = 0;

        for (ParseUser u : riders) {
            // format rider names here
            if (counter == riders.size() - 1) {
                formattedRiders += u.getString("name");
            } else {
                formattedRiders += u.getString("name") + ", ";
            }
            counter++;
        }

        if (mItems == null) {
            Context context = getActivity();
            CharSequence text = "Data could not be loaded at this time. Please go back and try again";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_clock_o).actionBarSize(), getString(R.string.time),
                formattedTime));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_calendar_o).actionBarSize(), getString(R.string.date),
                formattedDate));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup),
                startLocation));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff),
                endLocation));
        if (notes != "") {
            mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                    Iconify.IconValue.fa_pencil_square).actionBarSize(), getString(R.string.notes),
                    notes));
        }
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_users).actionBarSize(),getString(R.string.riders),
                formattedRiders));
        if (! ride.hasRider(user)) {
            mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                    Iconify.IconValue.fa_check).actionBarSize(), getString(R.string.join),
                    ""));
        } else {
            mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                    Iconify.IconValue.fa_times).actionBarSize(), getString(R.string.leave),
                    ""));
        }
    }

    protected void showNoteDialog() {

        Context context = this.getActivity();

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Ride notes:");

        final EditText input = new EditText(context);
        ad.setView(input);

        ad.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String notes = input.getText().toString().trim();
                        if (notes.length() == 0) {
                            // display some toast here?
                        } else {
                            // pass string to database
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = ad.create();
        alert.show();
    }


    //TODO How to change labels of listviewitems?
    // Listen for results.
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        switch (requestCode) {
            case DATEPICKER_FRAGMENT:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle=data.getExtras();
                    String resultDate = bundle.getString("selectedDate","error");
                    Log.v("Date", resultDate);

                }
                break;
            case TIMEPICKER_FRAGMENT:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String resultTime = bundle.getString("selectedTime", "error");
                    Log.v("Time", resultTime);
                }
                break;
            case PICKUP_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle=data.getExtras();
                    LatLng pickUpLoc = (LatLng)bundle.get("ADDRESS");
                    Log.v("pickUp", pickUpLoc.toString());
                }
                break;
            case DROPOFF_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle=data.getExtras();
                    LatLng dropOffLoc = (LatLng) bundle.get("ADDRESS");

                }
        }
    }


}
