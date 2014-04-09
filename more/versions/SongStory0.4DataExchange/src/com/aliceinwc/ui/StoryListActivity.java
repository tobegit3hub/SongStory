package com.aliceinwc.ui;

import java.io.File;
import java.util.ArrayList;

import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.data.SongStoryCategory;
import com.aliceinwc.database.StoryDatabaseAccessor;
import com.aliceinwc.util.NetworkUtil;
import com.aliceinwc.util.FileUtil;
import com.aliceinwc.util.RequestUrlTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class StoryListActivity extends Activity implements OnTouchListener,
		OnItemClickListener, OnPageChangeListener, OnGestureListener {

	private Context context;
	private LayoutInflater inflater;

	private int currentCategoryNumber = 0; 

	private ViewPager storyListPager;

	private StoryListPagerAdapter storyListPagerAdapter;

	private View[] storyListHeaderViews;

	// private ArrayList<StoryListHeaderAdapter> storyListHeaderAdapters;

	private View[] storyLisViews;

	private StoryListAdapter[] storyListAdapters;

	private SongStoryCategory[] songStoryCategories;

	private View touchView;
	ViewFlipper[] viewFlippers;
	private ViewFlipper currentViewFlipper;
	private GestureDetector gestureDetector;

	void mkdirIfNotExist(String directory_path) {
		File directory = new File(directory_path);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.story_list_activity);

		context = this;
		inflater = getLayoutInflater();

		FileUtil.mkdirAllDirctory();

		StoryDatabaseAccessor.init(context);

		//updateAllSongStory();

		// ArrayList<SongStory> sss = StoryDatabaseAccessor.getSongStories(0);
		
		new RequestUrlTask(context, 0,1).execute(new String[]{});
		//Toast.makeText(context, string, Toast.LENGTH_LONG).show();

		songStoryCategories = new SongStoryCategory[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {

			songStoryCategories[i] = new SongStoryCategory(context, i);

		}

		// temp
		songStoryCategories[0].stories = new ArrayList<SongStory>();
		songStoryCategories[1].stories = new ArrayList<SongStory>();
		songStoryCategories[2].stories = new ArrayList<SongStory>();
		songStoryCategories[3].stories = new ArrayList<SongStory>();
		songStoryCategories[4].stories = new ArrayList<SongStory>();

		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[0].stories.add(new SongStory());
		songStoryCategories[1].stories.add(new SongStory());
		songStoryCategories[1].stories.add(new SongStory());
		songStoryCategories[1].stories.add(new SongStory());
		songStoryCategories[1].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[2].stories.add(new SongStory());
		songStoryCategories[3].stories.add(new SongStory());
		songStoryCategories[4].stories.add(new SongStory());
		songStoryCategories[4].stories.add(new SongStory());

		storyListHeaderViews = new View[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		// storyListHeaderAdapters = new ArrayList<StoryListHeaderAdapter>();

		viewFlippers = new ViewFlipper[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		gestureDetector = new GestureDetector(this);
		;

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {
			// viewFlippers[i] = new ViewFlipper(context);

			View storyListHeaderView = inflater.inflate(
					R.layout.story_list_header, null);
			viewFlippers[i] = (ViewFlipper) storyListHeaderView
					.findViewById(R.id.story_list_header_flipper);
			storyListHeaderViews[i] = storyListHeaderView;

			for (int j = 0; j < SongStoryCategory.CATEGORY_IMAGE_NUMBER; ++j) {

				ImageView imageView = new ImageView(context);
				imageView.setImageResource(songStoryCategories[i].imageIds[j]);

				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				viewFlippers[i].addView(imageView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			}

			viewFlippers[i].setOnTouchListener(this);
			viewFlippers[i].setAutoStart(true);
			viewFlippers[i].setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.animaition_right_out));
			viewFlippers[i].setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.animaition_right_in));
			viewFlippers[i].setFlipInterval(3000);
			viewFlippers[i].startFlipping();
		}
		currentViewFlipper = viewFlippers[0];

		storyLisViews = new View[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		storyListAdapters = new StoryListAdapter[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {
			storyListAdapters[i] = new StoryListAdapter(i);

			// storyListHeaderViews.add(inflater.inflate(R.layout.story_list_header,
			// null));
			// ((ImageView)storyListHeaderViews.get(i).findViewById(R.id.story_list_header_image)).setImageResource(songStoryCategories[i].imageIds[0]);
			// storyListHeaderAdapters.add(new StoryListHeaderAdapter(i));

			// ViewPager storyListHeaderViewPager = (ViewPager)
			// storyListHeaderViews.get(i).findViewById(R.id.story_list_header_pager);

			// storyListHeaderViewPager.setAdapter(new
			// StoryListHeaderAdapter(i));

		}

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {

			View completeListView = inflater.inflate(R.layout.story_list_view,
					null);

			((TextView) completeListView
					.findViewById(R.id.story_list_category_title))
					.setText(songStoryCategories[i].title);
			((TextView) completeListView
					.findViewById(R.id.story_list_category_title_specification))
					.setText(songStoryCategories[i].titleSpecification);

			ListView listView = (ListView) completeListView
					.findViewById(R.id.story_list_view);

			listView.addHeaderView(storyListHeaderViews[i]);

			listView.addFooterView(inflater.inflate(R.layout.story_list_footer,
					null));

			listView.setAdapter(storyListAdapters[i]);

			listView.setOnItemClickListener(this);

			storyLisViews[i] = completeListView;
		}

		storyListPager = (ViewPager) findViewById(R.id.story_list_pager);
		storyListPagerAdapter = new StoryListPagerAdapter();
		storyListPager.setAdapter(storyListPagerAdapter);

		storyListPager.setOnPageChangeListener(this);
	}

	private class StoryListPagerAdapter extends PagerAdapter {

		public StoryListPagerAdapter() {

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

	private class StoryListAdapter extends BaseAdapter {

		ArrayList<SongStory> stories;

		public StoryListAdapter(int categoryNumber) {

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
				convertView = getLayoutInflater().inflate(
						R.layout.story_list_item, null);

				// Toast.makeText(context, "number : "+position,
				// Toast.LENGTH_SHORT).show(); //不计header，从0开始
				// convertView.setTag(SongStory);
			}

			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		// Toast.makeText(context, "Number : "+arg2, Toast.LENGTH_SHORT).show();
		// // 计header，所以从1开始
		// if (v.getTag() instanceof FilePublish)
		// return;
		// Post p = (Post) (v.getTag());
		// if (p != null) {
		// Intent intent = new Intent(this, PostDetailActivity.class);
		// intent.putExtra(PostDetailActivity.INTENT_FROM_BAR, true);
		// intent.putExtra(PostDetailActivity.INTENT_KEY_POST, p);
		// intent.putExtra(PostDetailActivity.INTENT_KEY_POST_LIST_KEY,
		// mPostListKeys.get(this.mCurrentTab));
		// // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivityForResult(intent, PostDetailActivity.REQUEST_CODE);
		// }
		
		Intent intent = new Intent(context, StoryDetailActivity.class);
		SongStory songStory = new SongStory();
		intent.putExtra("SongStory", songStory);
		
		((Activity)context).startActivityForResult(intent, 1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

		currentCategoryNumber = arg0;

		currentViewFlipper = viewFlippers[arg0];
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		// return false;
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (touchView != null && touchView == currentViewFlipper) {
			if (e1 != null && e2 != null) {
				if (e2.getX() - e1.getX() > 120 && Math.abs(velocityX) > 150) {
					currentViewFlipper.setOutAnimation(this,
							R.anim.animaition_left_out);
					currentViewFlipper.setInAnimation(this,
							R.anim.animaition_left_in);
					currentViewFlipper.showNext();
				} else if (e2.getX() - e1.getX() < -120
						&& Math.abs(velocityX) > 150) {
					currentViewFlipper.setOutAnimation(this,
							R.anim.animaition_right_out);
					currentViewFlipper.setInAnimation(this,
							R.anim.animaition_right_in);
					currentViewFlipper.showPrevious();
				}
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		touchView = v;
		if (touchView != null && touchView == currentViewFlipper) {
			currentViewFlipper.stopFlipping();
			currentViewFlipper.setAutoStart(false);
		}
		return gestureDetector.onTouchEvent(event);
	}

	private void downloadAndUpdateAllSongStory(final Context context) {
		// check network
		// connect with the server
		// download ss file
		// check ss folder to find all ss files
		// unzip ss file
		// execute xml file to generate SongStory object
		// insert into database

		if (NetworkUtil.isWifiConnected(context)) {
			// String result = NetworkUtil.requestSsUrl(0, 2);
			// new DownloadFileTask().execute(new String[]{});

			// Toast.makeText(context, "wifi connected",
			// Toast.LENGTH_LONG).show();
		} else {
			new AlertDialog.Builder(context)
					.setTitle("确定设置Wifi网络吗")
					.setIcon(R.drawable.ic_launcher)
					// .setMessage("")
					.setPositiveButton("设置",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									context.startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
									// startActivity(new
									// Intent(Settings.ACTION_WIRELESS_SETTINGS));//进入无线网络配置界面
									// startActivity(new
									// Intent(Settings.ACTION_WIFI_SETTINGS));
									// //进入手机中的wifi网络设置界面
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// dismiss dialog
								}
							}).show();
		} // end of network

		updateAllSongStory();

	}

	private void updateAllSongStory() {
		// unzip all the ss files, then delete ss files
		// read all the json files and insert into database, then deletem json
		// files

		if (FileUtil.getAllSsFiles().length != 0) {

			String[] ssFiles = FileUtil.getAllSsFiles();
			for (String fileName : ssFiles) {
				FileUtil.unzipSsFile(fileName);
			}

			ArrayList<SongStory> stories = FileUtil.readJsonFilesAndDelete();

			// for(SongStory songStory : stories){
			// StoryDatabaseAccessor.insertSongStory(songStory);
			//
			// }

			for (int i = 0; i < stories.size(); ++i) {
				StoryDatabaseAccessor.insertSongStory(stories.get(0));
			}

		}

	}

}
