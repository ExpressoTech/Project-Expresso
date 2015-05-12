package com.expresso.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.expresso.adapter.TabsPagerAdapter;
import com.expresso.drawer.NavigationDrawerCallbacks;
import com.expresso.drawer.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener,NavigationDrawerCallbacks {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
    private PagerSlidingTabStrip tabsStrip;
    private Toolbar toolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        getWidgetReferences();
        bindWidgetEvents();
        initialization();

        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
	}


    private void getWidgetReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_header);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    }

    private void bindWidgetEvents() {
    }

    private void initialization() {
        // set tabs and view pager
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        // set up toolbar
        setSupportActionBar(toolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected, show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

}
