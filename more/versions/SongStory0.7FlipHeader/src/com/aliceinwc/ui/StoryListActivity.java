package com.aliceinwc.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONException;

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
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class StoryListActivity extends Activity implements OnTouchListener,
		OnItemClickListener, OnPageChangeListener, OnGestureListener {

	private Context context;
	private LayoutInflater inflater;

	private int totalRunTimes;

	private ExitReminder exitReminder;

	private View touchView;
	
	private boolean isTouchHeader = false;
	
	
	private int currentCategoryNumber = 0;

	private ViewPager storyListPager;

	private StoryListPagerAdapter storyListPagerAdapter;

	private View[] storyListHeaderViews;

	// private ArrayList<StoryListHeaderAdapter> storyListHeaderAdapters;

	private View[] storyLisViews;

	private StoryListAdapter[] storyListAdapters;

	private SongStoryCategory[] songStoryCategories;

	ViewFlipper[] viewFlippers;
	private ViewFlipper currentViewFlipper;
	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.story_list_activity);

		context = this;
		inflater = getLayoutInflater();

		SharedPreferences settings = getSharedPreferences(
				WelcomeActivity.PREFS_SONG_STORY, Context.MODE_PRIVATE);
		totalRunTimes = settings.getInt(WelcomeActivity.TOTAL_RUN_TIMES, 0);

		FileUtil.makeAllDirctory();

		// if it's first time to run this program
		if (totalRunTimes == 1) {

			View imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.shake_bg);
			Toast toast = new Toast(context);
			toast.setView(imageView);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

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
					outputStream = new FileOutputStream(FileUtil.SS_PATH
							+ filename);
					byte[] buffer = new byte[1024];
					int read;
					while ((read = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, read);
					}
					inputStream.close();
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

		}

		exitReminder = new ExitReminder();

		StoryDatabaseAccessor.init(context);

		updateAllSongStory();

		// //test
		// SongStory ss = FileUtil.readJsonFileToObject("model2.json");
		// System.out.println(ss.def);

		// Toast.makeText(context, ss_url, Toast.LENGTH_LONG).show();

		// new RequestUrlTask(context, 0,1).execute(new String[]{});
		// Toast.makeText(context, string, Toast.LENGTH_LONG).show();

		songStoryCategories = new SongStoryCategory[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

		for (int i = 0; i < SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i) {

			songStoryCategories[i] = new SongStoryCategory(context, i);

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
			
			storyListHeaderView.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					 //currentViewFlipper.stopFlipping();
					 currentViewFlipper.setAutoStart(false);
					 
					 isTouchHeader = true;
					 
					 return gestureDetector.onTouchEvent(event);
				}
				
			});

			
			for (int j = 0; j < SongStoryCategory.CATEGORY_IMAGE_NUMBER; ++j) {

				ImageView imageView = new ImageView(context);
				imageView.setImageResource(songStoryCategories[i].imageIds[j]);

				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				viewFlippers[i].addView(imageView, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


			}

			// viewFlippers[i].setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			//
			// return gestureDetector.onTouchEvent(event);
			// //return false;
			// }
			// });
			// viewFlippers[i].set
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
			storyListAdapters[i] = new StoryListAdapter(
					getApplicationContext(), i, songStoryCategories);

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
			
//			storyLisViews[i].setOnTouchListener(this);
		}

		storyListPager = (ViewPager) findViewById(R.id.story_list_pager);
		// storyListPagerAdapter = new StoryListPagerAdapter(storyLisViews);
		storyListPager.setAdapter(new StoryListPagerAdapter(storyLisViews));

		storyListPager.setOnPageChangeListener(this);
		//storyListPager.setOnTouchListener(this);
		// storyListPager.requestDisallowInterceptTouchEvent(true);

		// storyListPager.setOnInterceptTouchEvent

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		// Toast.makeText(context, "Number : "+arg2, Toast.LENGTH_SHORT).show();
		// // 璁eader锛屾墍浠ヤ粠1寮�
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

		if (position == 0) { // header is 0, but never response

		} else if (position == songStoryCategories[currentCategoryNumber].storyNumber + 1) { // the
																								// footer
																								// is
																								// 5
																								// when
																								// there
																								// are
																								// 4
																								// stories

			downloadAndUpdateAllSongStory(context);
			// Toast.makeText(context, "downloading...",
			// Toast.LENGTH_SHORT).show();
		} else { // the first story is 1
			Intent intent = new Intent(context, StoryDetailActivity.class);
			intent.putExtra("SongStory",
					songStoryCategories[currentCategoryNumber].stories
							.get(position - 1));
			((Activity) context).startActivityForResult(intent, 1);
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
		 //return false;
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

//		if (touchView != null && touchView == currentViewFlipper) {
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
//		}
			
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
	public boolean onTouch(View v, MotionEvent event) { //listview set this and v is viewpager
		Toast.makeText(context, "on touch", 0).show();

//		ViewPager viewPager = (ViewPager)v;
//		ListView listView = (ListView) ((StoryListPagerAdapter)viewPager.getAdapter()).storyLisViews[currentCategoryNumber];
//		View headerView = listView.getChildAt(0).findViewById(R.id.story_list_header_flipper);

		 touchView = v;
		 
		 boolean isListView = (v instanceof ListView);
		 System.out.println("XXXXXXXXXXXXXXXXXXXX "+isListView);
		 
		 if (touchView != null && touchView == currentViewFlipper) {
			 currentViewFlipper.stopFlipping();
			 currentViewFlipper.setAutoStart(false);
			 return gestureDetector.onTouchEvent(event);
		 }else{
			 return storyListPager.onTouchEvent(event);
		 }
		
	}

	 @Override
	 public boolean dispatchTouchEvent(MotionEvent event) { // call onFling but not viewpager

//		 if(gestureDetector.onTouchEvent(event)){
//			 event.setAction(MotionEvent.ACTION_CANCEL);
//		 }
	
//	 return super.dispatchTouchEvent(event);
//		 return super.dispatchTouchEvent(event);
		 
		 if(isTouchHeader){
			 if(gestureDetector.onTouchEvent(event)){
			 event.setAction(MotionEvent.ACTION_CANCEL);
			 }
			 return false;
		 }else{
			 return super.dispatchTouchEvent(event);
		 }
	 }

	@Override
	public void onBackPressed() {
		if (exitReminder.isExit) {
			// finish();

			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			Toast.makeText(getApplicationContext(),
					"Press One More Time To Exit", Toast.LENGTH_SHORT).show();
			exitReminder.doExitInTwoSecond();
		}

		// super.onBackPressed();
	}

	private void downloadAndUpdateAllSongStory(final Context context) {

		if (NetworkUtil.isWifiConnected(context)) {

			// NetworkUtil.downloadSsFile(1, 1);
			Toast.makeText(context, "Download...", Toast.LENGTH_LONG).show();
		} else {
			new AlertDialog.Builder(context)
					.setTitle("Try to connect Wifi")
					.setIcon(R.drawable.song_story_icon)
					// .setMessage("")
					.setPositiveButton("Setting",
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
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// dismiss dialog
								}
							}).show();
		}

	}

	private void updateAllSongStory() {

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

			// update listview
			// storyListAdapters[currentCategoryNumber].notifyDataSetChanged();
		}

	}

}
