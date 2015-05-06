package com.rideout.app.mobile.rideout.createARide;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.ListFragment;
        import android.util.Log;
        import android.util.Size;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.google.android.gms.maps.model.LatLng;
        import com.joanzapata.android.iconify.IconDrawable;
        import com.joanzapata.android.iconify.Iconify;
        import com.parse.ParseGeoPoint;
        import com.parse.ParseUser;
        import com.rideout.app.mobile.rideout.MapsActivity;
        import com.rideout.app.mobile.rideout.R;
        import com.rideout.app.mobile.rideout.Ride;
        import com.rideout.app.mobile.rideout.myrides.MyRides;


        import java.util.ArrayList;
        import java.util.List;
        import android.view.ViewGroup.LayoutParams;


public class ListViewFragment extends ListFragment {

    private static final int TIMEPICKER_FRAGMENT = 1;
    private static final int DATEPICKER_FRAGMENT = 2;
    private static final int PICKUP_CODE = 3;
    private static final int DROPOFF_CODE = 4;

    private String resultDate;
    private String resultTime;
    private LatLng pickUpLoc;
    private LatLng dropOffLoc;

    private List<ListViewItem> mItems;
    private MySimpleArrayAdapter adapter;

    //DO YOU SHIT IN THE onActivityResult METHOD AND THE onCreate WHERE THE LISTENER IS FOR THE LET'S RIDE BUTTON

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the items list
        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();
        Drawable calendar = new IconDrawable(this.getActivity(), Iconify.IconValue.fa_calendar_o).actionBarSize();
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_clock_o).actionBarSize(), getString(R.string.time), getString(R.string.select_time)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_calendar_o).actionBarSize(), getString(R.string.date), getString(R.string.select_date)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup), getString(R.string.select_pickup)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff), getString(R.string.select_dropoff)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_pencil_square).actionBarSize(),getString(R.string.notes), getString(R.string.notes_description)));
        mItems.add(new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_check).actionBarSize().color(R.color.red), getString(R.string.create_ride), getString(R.string.create_ride_details)));
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
                Intent mapPickUpIntent = new Intent(getActivity(), MapsActivity.class);
                mapPickUpIntent.putExtra("LOCATION_TYPE", PICKUP_CODE);
                startActivityForResult(mapPickUpIntent,PICKUP_CODE);

                break;
            case 3:
                Intent mapDropOffIntent = new Intent(getActivity(), MapsActivity.class);
                mapDropOffIntent.putExtra("LOCATION_TYPE", DROPOFF_CODE);
                startActivityForResult(mapDropOffIntent,DROPOFF_CODE);
                break;
            case 4:

                break;
            case 5:
                final Context context = getActivity();

               /* Toast.makeText(context, "Entered onClick", Toast.LENGTH_LONG).show();
                final Ride ride = new Ride();
                if (!resultDate.equals(null)) {
                    ride.setRideDate(resultDate);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                }
                if (!resultTime.equals(null)) {
                    ride.setRideTime(resultTime);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                }
                if (pickUpLoc != null) {
                    ParseGeoPoint start = new ParseGeoPoint(pickUpLoc.latitude, pickUpLoc.longitude);
                    ride.setStartLocation(start);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                }
                if (dropOffLoc != null) {
                    ParseGeoPoint end = new ParseGeoPoint(dropOffLoc.latitude, dropOffLoc.longitude);
                    ride.setEndLocation(end);
                } else {
                    Toast.makeText(context, "Fields Incomplete", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(context, "Entered primary information", Toast.LENGTH_LONG).show();
                //Add one for notes
                ParseUser user = ParseUser.getCurrentUser();
                ride.addRider(user);
                ride.saveInBackground();
*/
                Intent intent = new Intent(getActivity(), MyRides.class);
                startActivity(intent);
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
                        resultDate = bundle.getString("selectedDate","error");
                        Log.v("Date", resultDate);

                        mItems.set(1, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_calendar_o).actionBarSize(), getString(R.string.date), "May 1, 2015"));
                        adapter.notifyDataSetChanged();

                    }
                    break;
                case TIMEPICKER_FRAGMENT:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle = data.getExtras();
                        resultTime = bundle.getString("selectedTime", "error");
                        Log.v("Time", resultTime);

                        mItems.set(0, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_clock_o).actionBarSize(), getString(R.string.time), "6:00 PM"));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case PICKUP_CODE:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle=data.getExtras();
                        pickUpLoc = (LatLng)bundle.get("ADDRESS");
                        Log.v("pickUp", pickUpLoc.toString());

                        mItems.set(2, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_map_marker).actionBarSize(), getString(R.string.pickup), "Johns Hopkins University"));

                        adapter.notifyDataSetChanged();
                    }
                    break;
                case DROPOFF_CODE:
                    if(resultCode == Activity.RESULT_OK){
                        Bundle bundle=data.getExtras();
                        dropOffLoc = (LatLng) bundle.get("ADDRESS"); //Changed all these to private variables at the top instead.

                        mItems.set(3, new ListViewItem(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_taxi).actionBarSize(), getString(R.string.dropoff), "BWI"));
                        adapter.notifyDataSetChanged();
                    }
        }
    }




}
