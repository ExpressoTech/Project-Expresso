package com.expresso.adapter.UserProfileAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.expresso.activity.R;
import com.expresso.fragment.UserProfile.CalenderView;
import com.expresso.fragment.UserProfile.MapFragment;
import com.expresso.fragment.UserProfile.StoriesFragment;

/**
 * Created by rohit on 3/7/15.
 */
public class UserProfileTabsPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private int tabIcons[] = {R.drawable.book, R.drawable.calendar, R.drawable.map};
    public UserProfileTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new StoriesFragment();
            case 1:
                // Games fragment activity
                return new CalenderView();
            case 2:
                // Movies fragment activity
                return new MapFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}
