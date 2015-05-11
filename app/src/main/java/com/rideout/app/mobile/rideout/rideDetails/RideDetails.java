package com.rideout.app.mobile.rideout.rideDetails;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.createARide.ListViewFragment;


public class RideDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        String rideId = getIntent().getStringExtra("rideid");

        DetailItemFragment details = new DetailItemFragment();
        Bundle args = new Bundle();
        args.putString("ride", rideId);
        details.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.detail_fragment, details);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Execute the changes specified
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ride_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
