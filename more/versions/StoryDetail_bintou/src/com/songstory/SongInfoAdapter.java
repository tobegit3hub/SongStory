package com.songstory;

import java.util.List;

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


	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mChildLayoutInflater.inflate(R.layout.listview_item, null);
		}
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	public Object getGroup(int groupPosition) {
		return null;
	}

	public int getGroupCount() {
		return 0;
	}

	public long getGroupId(int groupPosition) {
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.listview_item, null);
		}
		
		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	
	
	

	
}
