package com.rideout.app.mobile.rideout.myrides;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rideout.app.mobile.rideout.R;

import java.util.List;

/**
 * Created by atab7_000 on 4/13/2015.
 */
public class MyRidesArrayAdapter extends ArrayAdapter<RideItem>{
;

    public MyRidesArrayAdapter(Context context, List<RideItem> items) {
        super(context, R.layout.myrides_listitem, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.myrides_listitem, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvNumRiders = (TextView) convertView.findViewById(R.id.tvNumRiders);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        RideItem item = getItem(position);
        viewHolder.tvNumRiders.setText(item.numberRiders);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);

        return convertView;
    }

    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     */
    private static class ViewHolder {
        TextView tvNumRiders;
        TextView tvTitle;
        TextView tvDescription;
    }

}
