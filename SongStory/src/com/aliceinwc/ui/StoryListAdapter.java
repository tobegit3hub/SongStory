package com.aliceinwc.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.data.SongStoryCategory;

public class StoryListAdapter extends BaseAdapter {
	private SongStoryCategory[] songStoryCategories;
	private LayoutInflater mLayoutInflater;
	public ArrayList<SongStory> stories;

	public StoryListAdapter(Context context, int categoryNumber,
			SongStoryCategory[] songStoryCategories) {
		this.songStoryCategories = songStoryCategories;
		mLayoutInflater = LayoutInflater.from(context);

		stories = songStoryCategories[categoryNumber].stories;

	}

	@Override
	public int getCount() {

		// return 0;

		return stories.size();
	}

	@Override
	public Object getItem(int position) {

		// return null;

		return stories.get(position);
	}

	@Override
	public long getItemId(int position) {

		// return 0;

		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// return null;

		final SongStory data = (SongStory) getItem(position);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.story_list_item,
					null);

			TextView songNameView = (TextView) convertView
					.findViewById(R.id.story_list_item_song_name);
			TextView singerNameView = (TextView) convertView
					.findViewById(R.id.story_list_item_singer_name);
			TextView songInformationView = (TextView) convertView
					.findViewById(R.id.story_list_item_song_information);
			ImageView songImageView = (ImageView) convertView
					.findViewById(R.id.story_list_item_song_image);

			songNameView.setText(stories.get(position).songName);
			singerNameView.setText(stories.get(position).singerName);
			songInformationView.setText(stories.get(position).songInformation);
			songImageView.setImageBitmap(BitmapFactory
					.decodeFile("sdcard/SongStory/image/"
							+ stories.get(position).storyImageName2));

			// Toast.makeText(context, "number : "+position,
			// Toast.LENGTH_SHORT).show(); //涓嶈header锛屼粠0寮�
			// convertView.setTag(SongStory);
		}
		if (position != 0) {
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.item_cutline);
			imageView.setVisibility(View.GONE);
		}

		return convertView;
	}

}
