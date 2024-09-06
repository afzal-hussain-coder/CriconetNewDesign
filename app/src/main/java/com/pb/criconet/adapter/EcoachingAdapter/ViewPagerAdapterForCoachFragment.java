package com.pb.criconet.adapter.EcoachingAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pb.criconet.Fragment.CoachFragments.CoachAvailableDateAndSessionFragment;
import com.pb.criconet.Fragment.CoachFragments.CoachPersonalInformationFragment;
import com.pb.criconet.Fragment.CoachFragments.CoachProfesionalInQualifocationFragment;

public class ViewPagerAdapterForCoachFragment extends FragmentPagerAdapter {


    private String fromWhere;
    private Bundle dataBundle;

    // Constructor to accept 'from_where'
    public ViewPagerAdapterForCoachFragment(@NonNull FragmentManager fm, int behavior,String fromWhere) {
        super(fm, behavior);
        this.fromWhere = fromWhere;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("FROM", fromWhere); // Pass 'from_where' to the fragment

        if (position == 0) {
            fragment = new CoachPersonalInformationFragment();
        } else if (position == 1) {
            fragment = new CoachProfesionalInQualifocationFragment();
        } else if (position == 2) {
            fragment = new CoachAvailableDateAndSessionFragment();
        }

        if (fragment != null) {
            fragment.setArguments(bundle); // Attach the bundle to the fragment
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position == 0) {
            title = "Personal Information";
        } else if (position == 1) {
            title = "Professional Qualifications";
        } else if (position == 2) {
            title = "Available Date & Session";
        }
        return title;
    }
}