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
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.rideout.app.mobile.rideout.MapsActivity;
import com.rideout.app.mobile.rideout.R;


import java.util.ArrayList;
import java.util.List;


public class DetailItemFragment extends ListFragment {

    private static final int TIMEPICKER_FRAGMENT = 1;
    private static final int DATEPICKER_FRAGMENT = 2;
    private static final int PICKUP_CODE = 3;
    private static final int DROPOFF_CODE = 4;

    private List<DetailItem> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
        Resources resources = getResources();
        Drawable calendar = new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_calendar_o).actionBarSize();
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_clock_o).actionBarSize(), getString(R.string.time),
                "6:00 PM", null, null));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_calendar_o).actionBarSize(), getString(R.string.date),
                "May 1, 2015", null, null));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup),
                "Johns Hopkins University", null, null));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff),
                "BWI", null, null));
        mItems.add(new DetailItem(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_pencil_square).actionBarSize(),getString(R.string.notes),
                "None", null, null));
        // initialize and set the list adapter
        setListAdapter(new DetailArrayAdapter(getActivity(), mItems));
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
                //DialogFragment timePicker = new TimePickerFragment();
                //timePicker.setTargetFragment(this, TIMEPICKER_FRAGMENT);
                //timePicker.show(getFragmentManager().beginTransaction(), "timePicker");
                break;
            case 1:
                //DialogFragment datePicker = new DatePickerFragment();
                //datePicker.setTargetFragment(this, DATEPICKER_FRAGMENT);

                //datePicker.show(getFragmentManager().beginTransaction(), "datePicker");
                break;
            case 2:
                /*Intent mapPickUpIntent = new Intent(getActivity(), MapsActivity.class);
                mapPickUpIntent.putExtra("LOCATION_TYPE", PICKUP_CODE);
                startActivityForResult(mapPickUpIntent,PICKUP_CODE);
*/
                break;
            case 3:
                /*Intent mapDropOffIntent = new Intent(getActivity(), MapsActivity.class);
                mapDropOffIntent.putExtra("LOCATION_TYPE", DROPOFF_CODE);
                startActivityForResult(mapDropOffIntent,DROPOFF_CODE);*/
                break;
            case 4:
                //  showNoteDialog();
                break;
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
