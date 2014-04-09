package com.aliceinwc.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.data.SongStoryCategory;
import com.aliceinwc.database.StoryDatabaseAccessor;
import com.aliceinwc.util.ExitReminder;
import com.aliceinwc.util.NetworkUtil;
import com.aliceinwc.util.FileUtil;
import com.aliceinwc.util.DownloadSsFileTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class StoryListActivity extends Activity implements OnTouchListener,
		OnItemClickListener, OnPageChangeListener, OnGestureListener {

	private final int HEADER_FLIP_INTERVAL = 5000;
	public final static int MESSAGE_DOWNLOAD_FAIL = 0;
	public final static int MESSAGE_DOWNLOAD_COMPLETE = 1;
	public final static int MESSAGE_DOWNLOAD_PROCESSING = 2;

	private static Activity activity;
	private LayoutInflater inflater;

	private ExitReminder exitReminder;

	private GestureDetector gestureDetector;

	private int totalRunTimes;

	private static int currentCategoryNumber = 0;
	private static int downloadingCategoryNumber = 0;

	private ImageButton randomOrInButton;

	private View touchView;

	private boolean isTouchHeader = false;

	private ViewPager storyListPager;

	private StoryListPagerAdapter storyListPagerAdapter;

	private View[] storyListHeaderViews;

	private View[] storyListViews;

	private static StoryListAdapter[] storyListAdapters;

	private static SongStoryCategory[] songStoryCategories;

	private ViewFlipper[] viewFlippers;
	private ViewFlipper currentViewFlipper;

	public final static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == MESSAGE_DOWNLOAD_FAIL) {
				Toast.makeText(activity, R.string.already_newest_resource,
						Toast.LENGTH_LONG).show();
			} else if (msg.what == MESSAGE_DOWNLOAD_COMPLETE) {
				Toast.makeText(activity, R.string.download_complete,
						Toast.LENGTH_LONG).show();

				updateAllSongStory();

				try {
					ArrayList<SongStory> newStories;
					newStories = StoryDatabaseAccessor
							.getSongStories(downloadingCategoryNumber);
					songStoryCategories[downloadingCategoryNumber].stories = newStories;
					songStoryCategories[downloadingCategoryNumber].storyNumber = newStories
							.size();
					storyListAdapters[downloadingCategoryNumber].stories = newStories;
				} catch (JSONException e) {
					e.printStackTrace();
				}

				storyListAdapters[downloadingCategoryNumber]
						.notifyDataSetChanged();

			} else if (msg.what == MESSAGE_DOWNLOAD_PROCESSING) {
				Toast.makeText(activity, R.string.downloading,
						Toast.LENGTH_LONG).show();
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.story_list_activity);

		activity = this;

		FileUtil.makeAllDirctory();

		SharedPreferences settings = getSharedPreferences(
				WelcomeActivity.PREFS_SONG_STORY, Context.MODE_PRIVATE);
		totalRunTimes = settings.getInt(WelcomeActivity.TOTAL_RUN_TIMES, 0);

		if (totalRunTimes == 1) {
			FileUtil.makeAllDirctory();
			readAssertFiles();
			// StoryListActivity.upzipAllSsFiles();
		} else {

		}

		initData();

		initView();

		StoryDatabaseAccessor.init(activity);

		updateAllSongStory();

		songStoryCategories = new SongStoryCategory[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {

			songStoryCategories[i] = new SongStoryCategory(activity, i);

			try {
				songStoryCategories[i].stories = StoryDatabaseAccessor
						.getSongStories(i);
				songStoryCategories[i].storyNumber = songStoryCategories[i].stories
						.size();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		storyListHeaderViews = new View[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		// storyListHeaderAdapters = new ArrayList<StoryListHeaderAdapter>();

		viewFlippers = new ViewFlipper[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		gestureDetector = new GestureDetector(this);

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {
			// viewFlippers[i] = new ViewFlipper(context);

			View storyListHeaderView = inflater.inflate(
					R.layout.story_list_header, null);
			viewFlippers[i] = (ViewFlipper) storyListHeaderView
					.findViewById(R.id.story_list_header_flipper);
			storyListHeaderViews[i] = storyListHeaderView;

			storyListHeaderView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					// currentViewFlipper.stopFlipping();
					// currentViewFlipper.setAutoStart(false);

					isTouchHeader = true;

					return gestureDetector.onTouchEvent(event);
				}

			});

			for (int j = 0; j < SongStoryCategory.CATEGORY_IMAGE_NUMBER; ++j) {

				ImageView imageView = new ImageView(activity);
				imageView.setImageResource(songStoryCategories[i].imageIds[j]);

				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				viewFlippers[i].addView(imageView, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			}

			viewFlippers[i].setAutoStart(true);
			viewFlippers[i].setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.animaition_right_out));
			viewFlippers[i].setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.animaition_right_in));
			viewFlippers[i].setFlipInterval(HEADER_FLIP_INTERVAL);
			viewFlippers[i].startFlipping();
		}
		currentViewFlipper = viewFlippers[0];

		storyListViews = new View[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
		storyListAdapters = new StoryListAdapter[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {
			storyListAdapters[i] = new StoryListAdapter(
					getApplicationContext(), i, songStoryCategories);

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

			storyListViews[i] = completeListView;

			// storyLisViews[i].setOnTouchListener(this);
		}

		storyListPager = (ViewPager) findViewById(R.id.story_list_pager);
		// storyListPagerAdapter = new StoryListPagerAdapter(storyLisViews);
		storyListPager.setAdapter(new StoryListPagerAdapter(storyListViews));

		storyListPager.setOnPageChangeListener(this);

	}

	private void initData() {

		inflater = getLayoutInflater();

		exitReminder = new ExitReminder();

	}

	private void initView() {

		randomOrInButton = (ImageButton)activity.findViewById(R.id.random_or_in);
		randomOrInButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				randomlyGotoStoryDetail();
			}		
		});
		
		
		
	}
	
	private void randomlyGotoStoryDetail(){
		Random random = new Random();
		int randomCategoryNumber = Math.abs(random.nextInt()) % SongStoryCategory.CATEGORY_TOTAL_NUMBER;
		while(songStoryCategories[randomCategoryNumber].storyNumber == 0){
			randomCategoryNumber = Math.abs(random.nextInt()) % SongStoryCategory.CATEGORY_TOTAL_NUMBER;
		}
		
		int randomSongNumber = Math.abs(random.nextInt()) % songStoryCategories[randomCategoryNumber].storyNumber;
		
		Intent intent = new Intent(activity, StoryDetailActivity.class);
		intent.putExtra("SongStory",
				songStoryCategories[randomCategoryNumber].stories
						.get(randomSongNumber));
		((Activity) activity).startActivityForResult(intent, 1);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (position == 0) { // header is 0, but never response

		} else if (position == songStoryCategories[currentCategoryNumber].storyNumber + 1) { // footer

			SongStoryCategory currentCategory = songStoryCategories[currentCategoryNumber];
			int wantedSongNumber;
			if (currentCategory.storyNumber >= 1) {
				wantedSongNumber = currentCategory.stories
						.get(currentCategory.storyNumber - 1).songNumber + 1;
			} else {
				wantedSongNumber = 1;
			}

			downloadingCategoryNumber = currentCategoryNumber;

			downloadAndUpdateAllSongStory(activity, currentCategoryNumber,
					wantedSongNumber);

		} else { // the first story is 1
			Intent intent = new Intent(activity, StoryDetailActivity.class);
			intent.putExtra("SongStory",
					songStoryCategories[currentCategoryNumber].stories
							.get(position - 1));
			((Activity) activity).startActivityForResult(intent, 1);
		}

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
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		// if (touchView != null && touchView == currentViewFlipper) {
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
		// }

		isTouchHeader = false;
		return true;
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
	public boolean onTouch(View v, MotionEvent event) { // listview set this and
														// v is viewpager
		Toast.makeText(activity, "on touch", 0).show();

		// ViewPager viewPager = (ViewPager)v;
		// ListView listView = (ListView)
		// ((StoryListPagerAdapter)viewPager.getAdapter()).storyLisViews[currentCategoryNumber];
		// View headerView =
		// listView.getChildAt(0).findViewById(R.id.story_list_header_flipper);

		touchView = v;

		boolean isListView = (v instanceof ListView);
		System.out.println("XXXXXXXXXXXXXXXXXXXX " + isListView);

		if (touchView != null && touchView == currentViewFlipper) {
			currentViewFlipper.stopFlipping();
			currentViewFlipper.setAutoStart(false);
			return gestureDetector.onTouchEvent(event);
		} else {
			return storyListPager.onTouchEvent(event);
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) { // call onFling but
															// not viewpager

		// if(gestureDetector.onTouchEvent(event)){
		// event.setAction(MotionEvent.ACTION_CANCEL);
		// }

		// return super.dispatchTouchEvent(event);
		// return super.dispatchTouchEvent(event);

		if (isTouchHeader) {
			if (gestureDetector.onTouchEvent(event)) {
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
			return false;
		} else {
			return super.dispatchTouchEvent(event);
		}
	}

	@Override
	public void onBackPressed() {
		if (exitReminder.isExit) {
			// finish();

			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			String oneMoreTimeString = activity.getResources().getString(
					R.string.press_one_more_time_to_exit);
			Toast.makeText(getApplicationContext(), oneMoreTimeString,
					Toast.LENGTH_SHORT).show();
			exitReminder.doExitInTwoSecond();
		}

	}

	private void downloadAndUpdateAllSongStory(final Context context,
			int categoryNumber, int songNumber) {

		// if (NetworkUtil.isWifiConnected(context)) {
		if (NetworkUtil.isNetworkConnected(context)) {
			NetworkUtil.downloadSsFile(categoryNumber, songNumber);
		} else {
			new AlertDialog.Builder(context)
					.setTitle(R.string.network_connection_problem)
					.setIcon(R.drawable.song_story_icon)
					// .setMessage("")
					.setPositiveButton(R.string.setting,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									context.startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
									// startActivity(new
									// Intent(Settings.ACTION_WIRELESS_SETTINGS));//杩涘叆鏃犵嚎缃戠粶閰嶇疆鐣岄潰
									// startActivity(new
									// Intent(Settings.ACTION_WIFI_SETTINGS));
									// //杩涘叆鎵嬫満涓殑wifi缃戠粶璁剧疆鐣岄潰
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// dismiss dialog
								}
							}).show();
		}

	}

	private static void updateAllSongStory() {

		if (FileUtil.getAllSsFiles().length != 0
				|| FileUtil.getAllJsonFiles().length != 0) {

			if (FileUtil.getAllSsFiles().length != 0) {
				upzipAllSsFiles();
			}

			ArrayList<SongStory> stories = FileUtil.readJsonFilesAndDelete();

			// for(SongStory songStory : stories){
			// StoryDatabaseAccessor.insertSongStory(songStory);
			//
			// }

			for (int i = 0; i < stories.size(); ++i) {
				StoryDatabaseAccessor.insertSongStory(stories.get(i));
			}

			// update listview
			// storyListAdapters[currentCategoryNumber].notifyDataSetChanged();
		}

	}

	public static void upzipAllSsFiles() {
		String[] ssFiles = FileUtil.getAllSsFiles();
		for (String fileName : ssFiles) {
			FileUtil.unzipSsFile(fileName);
		}
	}

	// copy ss files from assert to sdcard
	public void readAssertFiles() {

		try {
			AssetManager assetManager = getAssets();
			String[] files = assetManager.list("");
			// String[] files = assetManager.list("");
			InputStream inputStream = null;
			OutputStream outputStream = null;

			for (String filename : files) {

				if (new File(filename).isDirectory()) {
					continue;
				}

				// Toast.makeText(context, filename, 0).show();
				inputStream = assetManager.open(filename);
				outputStream = new FileOutputStream(FileUtil.SS_PATH + filename);
				byte[] buffer = new byte[1024];
				int read;
				while ((read = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, read);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
