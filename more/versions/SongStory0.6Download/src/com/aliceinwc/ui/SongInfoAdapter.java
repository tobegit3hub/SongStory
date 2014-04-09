package com.aliceinwc.ui;

import java.util.List;

import com.aliceinwc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class SongInfoAdapter extends BaseExpandableListAdapter{
	
	List<Object> group;
	List<List<Object>> child;
	private LayoutInflater mLayoutInflater;
	private LayoutInflater mChildLayoutInflater;
	
	public SongInfoAdapter(Context context){
		mLayoutInflater = LayoutInflater.from(context);
		mChildLayoutInflater = LayoutInflater.from(context);
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mChildLayoutInflater.inflate(R.layout.listview_item, null);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.listview_item, null);
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	
	
	

	
}
