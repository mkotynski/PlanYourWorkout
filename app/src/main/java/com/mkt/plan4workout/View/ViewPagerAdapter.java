package com.mkt.plan4workout.View;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ExerciseActivity();
        } else if (position == 1) {
            fragment = new CalendarActivity();
        } else if (position == 2) {
        fragment = new PlansActivity();
    }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
