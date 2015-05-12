package com.expresso.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.expresso.activity.R;
import com.expresso.fragment.HomeFragment;
import com.expresso.fragment.NotificationsFragment;
import com.expresso.fragment.SearchFragment;
import com.expresso.fragment.UserFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private int tabIcons[] = {
            R.drawable.home_custom,
            R.drawable.search_custom,
            R.drawable.notification_custom,
            R.drawable.user_custom};

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new HomeFragment();
		case 1:
			return new SearchFragment();
		case 2:
			return new NotificationsFragment();
        case 3:
            return new UserFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

    /*
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
    */

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

}
