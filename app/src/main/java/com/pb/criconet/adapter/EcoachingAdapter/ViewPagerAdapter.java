package com.pb.criconet.adapter.EcoachingAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pb.criconet.Fragment.CoachFragments.CoachAvailableDateAndSessionFragment;
import com.pb.criconet.Fragment.CoachFragments.CoachPersonalInformationFragment;
import com.pb.criconet.Fragment.CoachFragments.CoachProfesionalInQualifocationFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position == 0) {
            fragment = new CoachPersonalInformationFragment();
        } else if (position == 1) {
            fragment = new CoachProfesionalInQualifocationFragment();
        } else if (position == 2) {
            fragment = new CoachAvailableDateAndSessionFragment();
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