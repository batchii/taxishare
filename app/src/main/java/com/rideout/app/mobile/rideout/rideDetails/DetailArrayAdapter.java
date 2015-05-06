package com.rideout.app.mobile.rideout.rideDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rideout.app.mobile.rideout.R;
import com.rideout.app.mobile.rideout.createARide.ListViewItem;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rideout.app.mobile.rideout.R;

import java.util.List;

public class DetailArrayAdapter extends ArrayAdapter<DetailItem> {

    public DetailArrayAdapter(Context context, List<DetailItem> items) {
        super(context, R.layout.detail_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DetailItem item = getItem(position);
        viewHolder.ivIcon.setImageDrawable(item.icon);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }

}

