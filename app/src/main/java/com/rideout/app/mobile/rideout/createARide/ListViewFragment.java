package com.rideout.app.mobile.rideout.createARide;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.ListFragment;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.util.Size;
        import android.view.Gravity;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
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


        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import android.view.ViewGroup.LayoutParams;

        import org.json.JSONObject;


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


    public AutoCompleteTextView myAutoComplete;

    PlacesTask placesTask;
    ParserTask parserTask;
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
        myAutoComplete = new AutoCompleteTextView(getActivity());

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
                showAutocompleteDialog();
                break;
            case 3:
                /*Intent mapDropOffIntent = new Intent(getActivity(), MapsActivity.class);
                mapDropOffIntent.putExtra("LOCATION_TYPE", DROPOFF_CODE);
                startActivityForResult(mapDropOffIntent,DROPOFF_CODE);*/
                showAutocompleteDialog();

                break;
            case 4:
                showNoteDialog();

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

    protected void showAutocompleteDialog(){
        Context context = this.getActivity();
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Location:");



        ad.setView(myAutoComplete);

        myAutoComplete.setThreshold(1);

        myAutoComplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        myAutoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myAutoComplete.showDropDown();
                return false;
            }
        });

        ad.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        String loc = myAutoComplete.getText().toString().trim();
                        if(loc.length() == 0){
                            //change stuff here
                        } else {
                            // pass string to database
                        }
                    }
                })
                .setNegativeButton("CanceL", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = ad.create();
        alert.show();



    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";


            //TODO This needs to be put on a proxy web app, very insecure this way,
            // http://stackoverflow.com/questions/14654758/google-places-api-request-denied-for-android-autocomplete-even-with-the-right-a
            // Obtain browser key from https://code.google.com/apis/console
            String key = "key="+getString(R.string.autocomplete_key);
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }



    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            myAutoComplete.setAdapter(adapter);
            myAutoComplete.showDropDown();
        }
    }



}
