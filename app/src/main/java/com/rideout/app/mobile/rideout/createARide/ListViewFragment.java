package com.rideout.app.mobile.rideout.createARide;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.location.Address;
        import android.location.Geocoder;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.ListFragment;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.AutoCompleteTextView;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.Toast;

        import com.google.android.gms.maps.model.LatLng;
        import com.joanzapata.android.iconify.IconDrawable;
        import com.joanzapata.android.iconify.Iconify;
        import com.parse.ParseGeoPoint;
        import com.parse.ParseObject;
        import com.parse.ParseUser;
        import com.rideout.app.mobile.rideout.MainActivity.AvailableRidesFragment;

        import com.rideout.app.mobile.rideout.MainActivity.MainActivity;

        import com.rideout.app.mobile.rideout.NavigationDrawerFragment;
        import com.rideout.app.mobile.rideout.R;
        import com.rideout.app.mobile.rideout.Ride;
        import com.rideout.app.mobile.rideout.myrides.MyRides;
        import com.rideout.app.mobile.rideout.myrides.MyRidesListViewFragment;


        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;

        import org.json.JSONObject;


public class ListViewFragment extends ListFragment {

    private static final int TIMEPICKER_FRAGMENT = 1;
    private static final int DATEPICKER_FRAGMENT = 2;
    private static final int PICKUP_CODE = 3;
    private static final int DROPOFF_CODE = 4;


    //Changed from String to Calendar
    private Date resultDate;
    private Date resultTime;
    //private Address pickUpLoc;
    //private Address dropOffLoc;
    private String pickUpLoc;
    private String dropOffLoc;
    private String notes;
    //private LatLng pickUpLoc;
    //private LatLng dropOffLoc;
    private LatLng origin = null;

    private List<ListViewItem> mItems;
    private MySimpleArrayAdapter adapter;

    public Geocoder geocoder;
    //DO YOUR SHIT IN THE onActivityResult METHOD AND THE onCreate WHERE THE LISTENER IS FOR THE LET'S RIDE BUTTON

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geocoder  = new Geocoder(getActivity());
        // initialize the items list
        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();
        Drawable calendar = new IconDrawable(this.getActivity(), Iconify.IconValue.fa_calendar_o).actionBarSize();
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_clock_o).actionBarSize(), getString(R.string.time), getString(R.string.select_time)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_calendar_o).actionBarSize(), getString(R.string.date), getString(R.string.select_date)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup), getString(R.string.select_pickup)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff), getString(R.string.select_dropoff)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_pencil_square).actionBarSize(),getString(R.string.notes), getString(R.string.notes_description)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_check).actionBarSize().colorRes(R.color.green_primary), getString(R.string.create_ride), getString(R.string.create_ride_details)));
        // initialize and set the list adapter
        adapter = new MySimpleArrayAdapter(getActivity(), mItems);
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
        ListViewItem item = mItems.get(position);
        switch(position){
            case 0:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(this, TIMEPICKER_FRAGMENT);
                timePicker.show(getFragmentManager().beginTransaction(), "timePicker");
            break;
            case 1:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(this, DATEPICKER_FRAGMENT);

                datePicker.show(getFragmentManager().beginTransaction(), "datePicker");
                break;
            case 2:
                Intent mapPickUpIntent = new Intent(getActivity(), GooglePlacesAutocompleteActivity.class);
                mapPickUpIntent.putExtra("LOCATION_TYPE", PICKUP_CODE);
                startActivityForResult(mapPickUpIntent,PICKUP_CODE);
                break;
            case 3:
                Intent mapDropOffIntent = new Intent(getActivity(), GooglePlacesAutocompleteActivity.class);
                mapDropOffIntent.putExtra("LOCATION_TYPE", DROPOFF_CODE);
                startActivityForResult(mapDropOffIntent,DROPOFF_CODE);

                break;
            case 4:
                showNoteDialog();

                break;
            case 5:
                final Context context = getActivity();
                Date currDate = new Date();
                long curr = currDate.getTime();
                final Ride ride = new Ride();
                if (resultDate != null) {
                    if ((curr > resultDate.getTime()) && (curr > resultTime.getTime())) {
                        Toast.makeText(context, "Invalid Date/Time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ride.setRideDate(resultDate);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                    return;
                }
                if (resultTime != null) {
                    if ((curr > resultDate.getTime()) && (curr > resultTime.getTime())) {
                        Toast.makeText(context, "Invalid Date/Time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ride.setRideTime(resultTime);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pickUpLoc != null) {
                    ride.setStartLocation(pickUpLoc);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                    return;
                }
                if (dropOffLoc != null) {
                    ride.setEndLocation(dropOffLoc);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                    return;
                }
                if (notes != null) {
                    ride.setNotes(notes);
                } else {
                    ride.setNotes("");
                }
                if (origin != null) {
                    ParseGeoPoint loc = new ParseGeoPoint(origin.latitude, origin.longitude);
                    ride.setOrigin(loc);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, "Entered primary information", Toast.LENGTH_LONG).show();
                //Add one for notes
                ParseUser user = ParseUser.getCurrentUser();
                ride.setRiders(user);
                ride.saveInBackground();


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = new MyRidesListViewFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

                NavigationDrawerFragment.mDrawerListView.setItemChecked(1, true);
                ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_my_rides));
                break;
        }

    }


    //TODO How to change labels of listviewitems?
    // Listen for results.
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            // See which child activity is calling us back.
            switch (requestCode) {
                case DATEPICKER_FRAGMENT:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle=data.getExtras();

                        Calendar c = (Calendar)bundle.get("selectedDate");
                        //resultDate = bundle.get("selectedDate", "error"); //Changed from getString
                        resultDate = c.getTime();
                        //format the date for display!
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                        String formattedDate = sdf.format(resultDate);

                        Log.v("Date", formattedDate);

                        mItems.set(1, new ListViewItem(new IconDrawable(this.getActivity(),
                                Iconify.IconValue.fa_calendar_o).actionBarSize(),
                                getString(R.string.date), formattedDate));
                        adapter.notifyDataSetChanged();

                    }
                    break;
                case TIMEPICKER_FRAGMENT:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle = data.getExtras();

                        Calendar c = (Calendar)bundle.get("selectedTime");
                        //resultTime = bundle.get("selectedTime", "error"); //Changed from getString
                        //format the time for display
                        resultTime = c.getTime();

                        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
                        String formattedTime = sdf.format(resultTime);

                        Log.v("Time", formattedTime);

                        mItems.set(0, new ListViewItem(new IconDrawable(this.getActivity(),
                                Iconify.IconValue.fa_clock_o).actionBarSize(),
                                getString(R.string.time), formattedTime));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case PICKUP_CODE:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle=data.getExtras();
                        String address = (String) bundle.get("ADDRESS");
                        pickUpLoc = address;
                        try{
                            Address loc = geocoder.getFromLocationName(address, 1).get(0);
                            //pickUpLoc = loc;
                            origin = new LatLng(loc.getLatitude(), loc.getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("pickUp", pickUpLoc.toString());

                        mItems.set(2, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup), address));

                        adapter.notifyDataSetChanged();
                    }
                    break;
                case DROPOFF_CODE:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle=data.getExtras();
                        String address = (String) bundle.get("ADDRESS");
                        dropOffLoc = address;
                        try{
                            Address loc = geocoder.getFromLocationName(address, 1).get(0);
                            //dropOffLoc = loc;
                            //dropOffLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mItems.set(3, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff), address));
                        adapter.notifyDataSetChanged();
                    }
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
                        notes = input.getText().toString().trim();
                        if (notes.length() == 0) {
                            // display some toast here?
                        } else {
                            // pass string to database
                            mItems.set(4, new ListViewItem(new IconDrawable(getActivity(),
                                    Iconify.IconValue.fa_pencil_square).actionBarSize(),
                                    getString(R.string.notes), notes));
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




}
