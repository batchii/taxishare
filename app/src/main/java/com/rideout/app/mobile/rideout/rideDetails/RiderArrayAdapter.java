package com.rideout.app.mobile.rideout.rideDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rideout.app.mobile.rideout.R;

import java.util.List;

/**
 * Created by taylorwashington on 5/10/15.
 */
public class RiderArrayAdapter extends ArrayAdapter<Rider> {

    public RiderArrayAdapter(Context context, List<Rider> items) {
        super(context, R.layout.rider_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.photoIcon = (ImageView) convertView.findViewById(R.id.photoIcon);
            viewHolder.nameString = (TextView) convertView.findViewById(R.id.nameString);
            viewHolder.phoneIcon = (ImageView) convertView.findViewById(R.id.phoneIcon);
            viewHolder.textIcon = (ImageView) convertView.findViewById(R.id.textIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Rider item = getItem(position);
        viewHolder.photoIcon.setImageDrawable(item.photo);
        viewHolder.nameString.setText(item.name);
        viewHolder.phoneIcon.setImageDrawable(item.phone);
        viewHolder.textIcon.setImageDrawable(item.text);

        return convertView;
    }

    private static class ViewHolder {
        ImageView photoIcon;
        TextView nameString;
        ImageView phoneIcon;
        ImageView textIcon;
    }

}
