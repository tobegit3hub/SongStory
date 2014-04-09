package com.aliceinwc.ui;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class StoryListPagerAdapter extends PagerAdapter {
	
	private View[] storyLisViews;

	public StoryListPagerAdapter(View[] storyLisViews) {
		
		this.storyLisViews = storyLisViews;

	}


	@Override
	public int getCount() {

		// return 0;

		return storyLisViews.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		// return false;

		return arg0 == (arg1);
	}

	@Override
	public Object instantiateItem(View collection, int position) {

		((ViewPager) collection).addView(storyLisViews[position]);

		return storyLisViews[position];
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {

		((ViewPager) collection).removeView(storyLisViews[position]);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}