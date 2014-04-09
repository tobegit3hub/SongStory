package com.aliceinwc.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.data.SongStoryCategory;
import com.aliceinwc.database.StoryDatabaseAccessor;
import com.aliceinwc.util.ExitReminder;
import com.aliceinwc.util.NetworkUtil;
import com.aliceinwc.util.FileUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class StoryListActivity extends Activity implements OnTouchListener,
		OnItemClickListener, OnPageChangeListener, OnGestureListener, SensorEventListener {

	private final int HEADER_FLIP_INTERVAL = 5000;
	public final static int MESSAGE_DOWNLOAD_FAIL = 0;
	public final static int MESSAGE_DOWNLOAD_COMPLETE = 1;
	public final static int MESSAGE_DOWNLOAD_PROCESSING = 2;
	
	public final static int LIST_TO_DETAIL_REQUEST = 1;


	private static Activity activity;
	private LayoutInflater inflater;

	private ExitReminder exitReminder;

	private GestureDetector gestureDetector;
	
	private SensorManager mSensorManager;
	
	private Vibrator vibrator;
	
//	private SoundPool soundPool;
//	private int ding_music_id;
	
	
	 public static NotificationManager nm;
	 public static RemoteViews rv;
	 public static Notification notification;
	    
	 private Animation animationTranslate, animationRotate, animationScale;
	 
	 
	 private boolean isFromResult = false;
	 private int resultCategoryNumber = 0;
	 private int resultSongNumber = 0;
	 
	 private long lastShakeTime = 0;

	private int totalRunTimes;

	private static int currentCategoryNumber = 0;
	private static int downloadingCategoryNumber = 0;

	private ImageButton shakeOrInButton;

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
		
		initNotification();

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
				imageView.setAdjustViewBounds(true);
				
				//imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//				viewFlippers[i].addView(imageView, new LayoutParams(
//						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				viewFlippers[i].addView(imageView);


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
		
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  


//        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);  
//        ding_music_id = soundPool.load(this, R.raw.ding_ding, 0);  
//        soundPool.play(sourceid, 1, 1, 0, -1, 1);  
        
	}

	private void initView() {

		shakeOrInButton = (ImageButton)activity.findViewById(R.id.shake_or_in);
		shakeOrInButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isFromResult){
					
					Intent intent = new Intent(activity, StoryDetailActivity.class);
					intent.putExtra("continue", true);
//					intent.putExtra("SongStory",
//							songStoryCategories[resultCategoryNumber].stories
//									.get(randomSongNumber));
					try {
						intent.putExtra("SongStory", StoryDatabaseAccessor.getSongStory(resultCategoryNumber, resultSongNumber));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					activity.startActivityForResult(intent, LIST_TO_DETAIL_REQUEST);
					
				}else{
//					randomlyGotoStoryDetail();
					
					ImageView imageView = new ImageView(activity);
					imageView.setBackgroundResource(R.drawable.shake_bg);
					
			        Toast toast = new Toast(activity);
			        toast.setView(imageView);
			        toast.setDuration(Toast.LENGTH_SHORT);
			        toast.setGravity(Gravity.CENTER, 0, 0);
			        
			        toast.show();
				}
			}		
		});
		
		
		
	}
	
	private void initNotification(){
		
		 nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	        Intent intent = new Intent();
	        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent, 0);        
	        rv = new RemoteViews(getPackageName(), R.layout.download_notification);
	        rv.setProgressBar(R.id.task_progress, 100, 0, false);
	        
	        notification = new Notification(R.drawable.song_story_icon, getResources().getString(R.string.connecting_network_update), android.os.SystemClock.uptimeMillis());
	        notification.defaults = Notification.DEFAULT_SOUND;
	        notification.flags = Notification.FLAG_AUTO_CANCEL|Notification.FLAG_ONGOING_EVENT|Notification.FLAG_ONLY_ALERT_ONCE;
	        notification.contentIntent = pIntent;
	        notification.contentView = rv;
	}
	
	
	private void randomlyGotoStoryDetail(){
		Random random = new Random();
		int randomCategoryNumber = Math.abs(random.nextInt()) % SongStoryCategory.CATEGORY_TOTAL_NUMBER;
		while(songStoryCategories[randomCategoryNumber].storyNumber == 0){
			randomCategoryNumber = Math.abs(random.nextInt()) % SongStoryCategory.CATEGORY_TOTAL_NUMBER;
		}
		
		int randomSongNumber = Math.abs(random.nextInt()) % songStoryCategories[randomCategoryNumber].storyNumber;
		
		Intent intent = new Intent(activity, StoryDetailActivity.class);
		if(isFromResult==true && resultCategoryNumber==randomCategoryNumber && resultSongNumber==songStoryCategories[randomCategoryNumber].stories
				.get(randomSongNumber).songNumber){
			intent.putExtra("continue", true);
		}else{
			intent.putExtra("continue", false);
		}
		intent.putExtra("SongStory",
				songStoryCategories[randomCategoryNumber].stories
						.get(randomSongNumber));
		activity.startActivityForResult(intent, LIST_TO_DETAIL_REQUEST);
	}

	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		StoryDetailActivity.setFullScreenOrNot(activity);
		
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		
		
        SharedPreferences settings = getSharedPreferences(WelcomeActivity.PREFS_SONG_STORY,
				Context.MODE_PRIVATE);
		boolean isCrackMode = settings.getBoolean(WelcomeActivity.IS_CRACK_MODE, false);
		if(isCrackMode){
			for(int i=0; i<SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i){
				songStoryCategories[i].title = getResources().getStringArray(R.array.crack_story_list_category_title)[i];
				((TextView) storyListViews[i].findViewById(R.id.story_list_category_title)).setText(songStoryCategories[i].title);
			}
			
			ImageView imageView = new ImageView(activity);
			imageView.setBackgroundResource(R.drawable.heart);
	        Toast toast = new Toast(activity);
	        toast.setView(imageView);
	        toast.setDuration(Toast.LENGTH_SHORT);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();	        
			
		}else{
			for(int i=0; i<SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i){
				songStoryCategories[i].title = getResources().getStringArray(R.array.story_list_category_title)[i];
				((TextView) storyListViews[i].findViewById(R.id.story_list_category_title)).setText(songStoryCategories[i].title);
			}
		}
		
	}

	
	@Override
	protected void onPause() {
		
		super.onPause();
		
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onStop() {

		super.onStop();

		mSensorManager.unregisterListener(this);
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
			if(isFromResult==true && currentCategoryNumber==resultCategoryNumber && songStoryCategories[currentCategoryNumber].stories
					.get(position - 1).songNumber==resultSongNumber){
				intent.putExtra("continue", true);
			}else{
				intent.putExtra("continue", false);
			}
			activity.startActivityForResult(intent, LIST_TO_DETAIL_REQUEST);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if(arg0 == 1){ // 1 for scrolling, 2 for complete, 3 for doing nothing
			mySetAnimation(false);
		}
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
				currentViewFlipper.showPrevious();

				//reset auto flip to next picture
				currentViewFlipper.setOutAnimation(this,
						R.anim.animaition_right_out);
				currentViewFlipper.setInAnimation(this,
						R.anim.animaition_right_in);
				
			} else if (e2.getX() - e1.getX() < -120
					&& Math.abs(velocityX) > 150) {
				currentViewFlipper.setOutAnimation(this,
						R.anim.animaition_right_out);
				currentViewFlipper.setInAnimation(this,
						R.anim.animaition_right_in);
				currentViewFlipper.showNext();
				
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
			
			SharedPreferences settings = activity.getSharedPreferences(
					WelcomeActivity.PREFS_SONG_STORY, Context.MODE_PRIVATE);

	    		SharedPreferences.Editor editor = settings.edit();
	    		editor.putBoolean(StoryDetailActivity.IS_FULL_SCREEN, false);
	    		editor.commit();

//			android.os.Process.killProcess(android.os.Process.myPid());
//	    	stopService(new Intent(this, MusicPlayService.class));
//			System.exit(0);
	    		finish();
		} else {
			String oneMoreTimeString = activity.getResources().getString(
					R.string.press_one_more_time_to_exit);
			Toast.makeText(getApplicationContext(), oneMoreTimeString,
					Toast.LENGTH_SHORT).show();
			exitReminder.doExitInTwoSecond();
		}

	}

	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
    	stopService(new Intent(this, MusicPlayService.class));
		System.exit(0);
	}

	private void downloadAndUpdateAllSongStory(final Context context,
			int categoryNumber, int songNumber) {

		// if (NetworkUtil.isWifiConnected(context)) {
		if (NetworkUtil.isNetworkConnected(context)) {
			
			
			NetworkUtil.downloadSsFile(categoryNumber, songNumber);
			
		} else {
			new AlertDialog.Builder(context)
					.setTitle(R.string.network_connection_problem)
					//.setIcon(R.drawable.song_story_icon)
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
			String[] files = assetManager.list("ss");
			// String[] files = assetManager.list("");
			InputStream inputStream = null;
			OutputStream outputStream = null;

			for (String filename : files) {

				if (new File(filename).isDirectory()) {
					continue;
				}

				// Toast.makeText(context, filename, 0).show();
				inputStream = assetManager.open("ss/"+filename);
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

	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if ((Math.abs(values[0]) > 16 || Math.abs(values[1]) > 16)) { 
				
				if(System.currentTimeMillis()-lastShakeTime > 1000){
				
					vibrator.vibrate(1000);
//					soundPool.play(ding_music_id, 1, 1, 0, 0, 1);  
					Toast.makeText(activity, R.string.shake_and_shake, Toast.LENGTH_LONG).show();
					
					randomlyGotoStoryDetail();
					
					lastShakeTime = System.currentTimeMillis();
				}
				
			}
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		

		if(requestCode==LIST_TO_DETAIL_REQUEST && resultCode==RESULT_OK){
			
			//randomOrInButton.setImageResource(R.drawable.right_arrow);
			shakeOrInButton.setBackgroundResource(R.drawable.right_arrow_button);
			
			isFromResult = true;
			resultCategoryNumber = data.getIntExtra("category_number", 0);
			resultSongNumber = data.getIntExtra("song_number", 1);

		}
		
	}

	
	private void mySetAnimation(boolean isFirstTouch) {

		if(isFirstTouch == false){
		shakeOrInButton.startAnimation(animRotate(0f, 360f, 0.5f, 0.5f));
		}

		}

		public Animation animRotate(float fromDegrees, float toDegrees,
		float pivotXValue, float pivotYValue) {
		animationRotate = new RotateAnimation(fromDegrees, toDegrees,
		Animation.RELATIVE_TO_SELF, pivotXValue,
		Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setDuration(500);
		animationRotate.setAnimationListener(new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
		animationRotate.setFillAfter(true);
		}
		});
		return animationRotate;
		}
}
