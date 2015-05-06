package com.rideout.app.mobile.rideout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rideout.app.mobile.rideout.Ride;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RideAdapter extends BaseAdapter {

    private final Context context;

    private final ArrayList<Ride> rides;

    public RideAdapter(Context context) {
        this.context = context;
        this.rides = new ArrayList<>();
    }

    public void addAllRides(List<Ride> newRides) {
        if (newRides != null) {
            this.rides.clear();
            this.rides.addAll(newRides);
        }
    }

    public Ride getReminderAtPosition(int position) {
        return this.rides.get(position);
    }

    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Object getItem(int position) {
        return rides.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Need this view line for the correct layout.
        //View view = inflater.inflate(R.layout.row_layout, parent, false);

        //View view = inflater.inflate(R.layout.row_layout, parent, false);

        //Set the appropriate fields for that row in the list.



        //TextView text = (TextView) view.findViewById(R.id.rowLayoutText);
        //text.setText(this.rides.get(position).getReminderText());


        return null;
    }
}
