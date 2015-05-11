package com.rideout.app.mobile.rideout.rideDetails;

import android.support.v4.app.ListFragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.rideout.app.mobile.rideout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylorwashington on 5/10/15.
 */
public class RiderFragment extends ListFragment {

    private List<Rider> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<>();
        //Resources resources = getResources();

        mItems.add(new Rider(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_user).actionBarSize(), "Name Here",
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_phone).actionBarSize(),
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_comment).actionBarSize()));

        mItems.add(new Rider(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_user).actionBarSize(), "Name Here",
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_phone).actionBarSize(),
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_comment).actionBarSize()));

        mItems.add(new Rider(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_user).actionBarSize(), "Name Here",
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_phone).actionBarSize(),
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_comment).actionBarSize()));

        // initialize and set the list adapter
        setListAdapter(new RiderArrayAdapter(getActivity(), mItems));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    public void addRider(String name) {
        mItems.add(new Rider(new IconDrawable(this.getActivity(),
                Iconify.IconValue.fa_user).actionBarSize(), name,
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_phone).actionBarSize(),
                new IconDrawable(this.getActivity(), Iconify.IconValue.fa_envelope).actionBarSize()));
    }
}

