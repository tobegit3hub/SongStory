package com.aliceinwc.ui;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.lyrics.Lyric;
import com.aliceinwc.lyrics.LyricView;
import com.aliceinwc.util.FileUtil;

@SuppressLint("NewApi")
public class StoryDetailActivity extends Activity {
	
	public final static String IS_FULL_SCREEN = "is_full_screen";

	private Activity activity;
	
	private boolean isContinue;
	

	private ImageButton mPlay;
	private ImageButton mPause;
	private ImageButton mStop;
	private ImageButton mBack;
	private ImageButton mUpArrow;
	private ImageButton mHome;

	private ImageButton menuAboutButton;
	private ImageButton menuShareButton;
	private ImageButton menuFullScreenButton;

	private ImageView mPoster;
	private ImageView mSpecialEdition;

	private boolean areLevel2Showing = false;
	private RelativeLayout relate_level2;
	private RelativeLayout relate_level1;

	private Animation animationTranslate, animationRotate, animationScale;

	private LinearLayout mTopLayout;
	private LinearLayout mBottomLayout;
	private LinearLayout mFirstLayout, mSecondLayout, mThirdLayout;
	private LinearLayout mMainContentLayout;

	private TextView mContent;
	private LyricView lyricView;
	private TextView mSongName;
	private TextView mSingerName;
	private TextView mAlbumName;
	private TextView mStoryTitle1;
	private TextView mFirstInfo;
	private TextView mStoryTitle2;
	private TextView mStoryTitle3;

	private TextView mDetailDetailTitle;
	private TextView mDetailDetailContent;
	private ImageView storyImageView1;
	private ImageView storyImageView2;

	private GestureDetector mGestureDetector;

	private SeekBar seekBar;

	public static Lyric mLyric;

	private Intent intent;

	private int height;
	private int width;
	private int layoutHeight;

	private long theCurrentPosition = 0;
	private long duration = 10 * 1000;

	private boolean hasTouch = false;
	private boolean hasGone = false;;
	private boolean isPlaying = false;
	private boolean isSeekBarChange = false;

	private Thread mthread;
	private MusicInfo musicInfo;

	private SongStory songStory;

	private String songStoryPath;
	private String musicPath;
	private String lyricsPath;
	private String imagePath;
	


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.story_detail_activity);

		activity = this;

		songStory = (SongStory) getIntent().getSerializableExtra("SongStory");
		isContinue = getIntent().getBooleanExtra("continue", false);

		songStoryPath = Environment.getExternalStorageDirectory().getPath()
				+ "/SongStory";
		musicPath = songStoryPath + "/music/";
		lyricsPath = songStoryPath + "/lyrics/";
		imagePath = songStoryPath + "/image/";

		String path = musicPath + songStory.songFileName;


		musicInfo = new MusicInfo(this, path);

		intent = new Intent(StoryDetailActivity.this, MusicPlayService.class);
		intent.putExtra("choice", "play");
		intent.putExtra("path", path);
		intent.putExtra("continue", isContinue);
		startService(intent);
		isPlaying = true;

		buttonInit();
		layoutInit();
		inSideButtonInit();
		menuButtonInit();
		textInit();
		imageViewInit();

		seekBarInit();

		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		mGestureDetector.setIsLongpressEnabled(true);
		mthread = new Thread(new UIUpdateThread());
		mthread.start();

		
		if(isContinue){
			theCurrentPosition = MusicPlayService.getCurrentPosition();
			
			if (isPlaying) {
				if (!hasGone) {
					lyricView.updateIndex(theCurrentPosition);
					mHandler.post(mUpdateResults);
					seekBar.setProgress((int) theCurrentPosition);
				}
			}
		}

	}
	
	

	@Override
	protected void onResume() {
		
		super.onResume();
		
		setFullScreenOrNot(activity);
	}



	private void imageViewInit() {
		mSpecialEdition = (ImageView) findViewById(R.id.specialedition);
		mPoster = (ImageView) findViewById(R.id.poster);

		mSpecialEdition.setImageBitmap(BitmapFactory.decodeFile(imagePath + "/"
				+ songStory.albumImageName));
		mPoster.setImageBitmap(BitmapFactory.decodeFile(imagePath + "/"
				+ songStory.storyImageName0));

	}

	private void textInit() {
		// mContent = (TextView) findViewById(R.id.detail_detail_content);
		lyricView = (LyricView) findViewById(R.id.lycView);

		mLyric = new Lyric(new File(lyricsPath, songStory.lyricsFileName));
		
		
		// mLyric = new Lyric(new
		// File(Environment.getExternalStorageDirectory().getPath()+"/001.lrc"));

		lyricView.setmLyric(mLyric);
		lyricView.setSentencelist(mLyric.list);
		lyricView.setNotCurrentPaintColor(Color.GRAY);
		lyricView.setCurrentPaintColor(Color.WHITE);
		lyricView.setLrcTextSize(25); // samsung
//		lyricView.setLrcTextSize(17); // xiaomi
		lyricView.setTextTypeFace(Typeface.SERIF);
		lyricView.setBackgroundColor(Color.BLACK);
		lyricView.setTextHeight(40);

		mSongName = (TextView) findViewById(R.id.songname);
		mSingerName = (TextView) findViewById(R.id.singername);
		mAlbumName = (TextView) findViewById(R.id.album_name);
		mStoryTitle1 = (TextView) findViewById(R.id.first_title);
		mFirstInfo = (TextView) findViewById(R.id.first_info);
		mStoryTitle2 = (TextView) findViewById(R.id.second_title);
		mStoryTitle3 = (TextView) findViewById(R.id.third_title);

		mDetailDetailTitle = (TextView) findViewById(R.id.detail_detail_title);
		mDetailDetailContent = (TextView) findViewById(R.id.detail_detail_content);
		storyImageView1 = (ImageView) findViewById(R.id.story_image_1);
		storyImageView2 = (ImageView) findViewById(R.id.story_image_2);

		mSongName.setText(songStory.songName);
		mSingerName.setText(songStory.singerName);
		mAlbumName.setText(songStory.albumName);
		mStoryTitle1.setText(songStory.storyTitle1);
		mFirstInfo.setText(songStory.storyContent1);
		mStoryTitle2.setText(songStory.storyTitle2);
		mStoryTitle3.setText(songStory.storyTitle3);

	}

	private void layoutInit() {
		mTopLayout = (LinearLayout) findViewById(R.id.top_layout);
		mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		mFirstLayout = (LinearLayout) findViewById(R.id.first_content);
		mSecondLayout = (LinearLayout) findViewById(R.id.second_content);
		mThirdLayout = (LinearLayout) findViewById(R.id.third_content);
		mMainContentLayout = (LinearLayout) findViewById(R.id.main_content);
		relate_level2 = (RelativeLayout) findViewById(R.id.relate_level2);
		relate_level1 = (RelativeLayout) findViewById(R.id.relate_level1);
		
		mMainContentLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				Log.e("============", "touch");
				return mGestureDetector.onTouchEvent(event);
			}
		});
			


		mFirstLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasTouch == false) {
					hasTouch = true;
					mySetAnimation(false);

					mDetailDetailTitle.setText(songStory.storyTitle1);
					mDetailDetailContent.setText(songStory.storyContent1);

					storyImageView1.setVisibility(View.VISIBLE);
					storyImageView2.setVisibility(View.VISIBLE);
					storyImageView1.setImageBitmap(BitmapFactory
							.decodeFile(FileUtil.IMAGE_PATH
									+ songStory.storyImageName1));
					storyImageView2.setImageBitmap(BitmapFactory
							.decodeFile(FileUtil.IMAGE_PATH
									+ songStory.storyImageName2));

					Typeface typeFace = Typeface.createFromAsset(getAssets(),
							"fonts/lanting_black_simple.ttf");

					mDetailDetailContent.setTypeface(typeFace);
				}
			}
		});
		mSecondLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasTouch == false) {
					hasTouch = true;
					mySetAnimation(false);

					mDetailDetailTitle.setText(songStory.storyTitle2);
					mDetailDetailContent.setText(songStory.storyContent2);

					storyImageView1.setVisibility(View.GONE);
					storyImageView2.setVisibility(View.GONE);
				}
			}
		});
		mThirdLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasTouch == false) {
					hasTouch = true;
					mySetAnimation(false);

					mDetailDetailTitle.setText(songStory.storyTitle3);
					mDetailDetailContent.setText(songStory.storyContent3);

					storyImageView1.setVisibility(View.GONE);
					storyImageView2.setVisibility(View.GONE);
				}
			}
		});
	}

	private void inSideButtonInit() {

		mUpArrow = (ImageButton) findViewById(R.id.up_arrow);
		mUpArrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasTouch == true) {
					if (hasGone == false) {
						layoutHeight = mTopLayout.getHeight();
						mUpArrow.startAnimation(animRotate(0, 180, 0.5f, 0.5f));
						mBottomLayout.startAnimation(animTranslateTwo(0,
								-layoutHeight, 0, 0, 1000));
						mTopLayout.startAnimation(setAnimScale(1f, 0f, 1f, 1f));

					} else {
						mUpArrow.startAnimation(animRotate(-180, 0, 0.5f, 0.5f));

						mTopLayout.startAnimation(setAnimScale(1f, 1f, 1f, 0f));
						mBottomLayout.startAnimation(animTranslateTwo(0, 0, 0,
								-layoutHeight, 1000));
					}
					
					
				}
			}
		});
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void buttonInit() {

		Display display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();

		mPlay = (ImageButton) findViewById(R.id.play);
		mPause = (ImageButton) findViewById(R.id.pause);
		mStop = (ImageButton) findViewById(R.id.stop);
		mBack = (ImageButton) findViewById(R.id.turnback);

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying == true) {
//					intent.putExtra("choice", "pause");
//					isPlaying = !isPlaying;
				} else {
					isPlaying = !isPlaying;
					intent.putExtra("choice", "GoOn");
					// mthread.start();
				}
				startService(intent);

			}
		});
		mStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isPlaying == true) {
					intent.putExtra("choice", "pause");
					isPlaying = !isPlaying;
				}
				startService(intent);
//				stopService(intent);
			}
		});
		mPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying == true) {
					intent.putExtra("choice", "pause");
					isPlaying = !isPlaying;
				} else {

				}
				startService(intent);
			}
		});

	}

	private void menuButtonInit() {

		mHome = (ImageButton) findViewById(R.id.home);
		mHome.setOnClickListener(new OnClickListener() {

			@Override
			@SuppressLint("NewApi")
			public void onClick(View v) {
				showOrHideLevel2Menu();
			}
		});

		menuAboutButton = (ImageButton) findViewById(R.id.about_button);
		menuAboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(activity, AboutActivity.class);
				activity.startActivity(intent);
			}
		});

		menuShareButton = (ImageButton) findViewById(R.id.share_button);
		menuShareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, R.string.one_click_share);
				// intent.putExtra(Intent.EXTRA_TEXT, "The status update text");
				intent.putExtra(Intent.EXTRA_TEXT,
						getResources().getString(R.string.share_content));
				startActivity(Intent.createChooser(intent, getResources()
						.getString(R.string.one_click_share)));
			}
		});

		
		menuFullScreenButton = (ImageButton)findViewById(R.id.full_screen_button);
		menuFullScreenButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				changeFullScreen(activity);
		
			}
		});
	}
	
	public static void changeFullScreen(Activity activity){
		SharedPreferences settings = activity.getSharedPreferences(
				WelcomeActivity.PREFS_SONG_STORY, Context.MODE_PRIVATE);
		
        //int v1 = activity.getWindow().getAttributes().flags; // full screen is 66816 and not is 65792
        //if(v1 != 66816 ){//非全屏
        
        if(settings.getBoolean(IS_FULL_SCREEN, false)){
        	
        	activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putBoolean(IS_FULL_SCREEN, false);
    		editor.commit();

        }else{
        	
        	activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putBoolean(IS_FULL_SCREEN, true);
    		editor.commit();
        }
	}
	
	public static void setFullScreenOrNot(Activity activity){
		SharedPreferences settings = activity.getSharedPreferences(
				WelcomeActivity.PREFS_SONG_STORY, Context.MODE_PRIVATE);
		
	       if(settings.getBoolean(IS_FULL_SCREEN, false)){
	        	activity.getWindow().setFlags(
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

	        }else{
	        	activity.getWindow().clearFlags(
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

	        }
	}

	@SuppressLint("NewApi")
	public void showOrHideLevel2Menu() {
		if (!areLevel2Showing) {
			relate_level1.setAlpha(1);
			relate_level2.setVisibility(View.VISIBLE);
			MyAnimation.startAnimationsIn(relate_level2, 500);
		} else {
			relate_level1.setAlpha(0.5f);
			relate_level2.setVisibility(View.GONE);
			MyAnimation.startAnimationsOut(relate_level2, 500, 0);
		}
		areLevel2Showing = !areLevel2Showing;
	}

	public void seekBarInit() {
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		duration = musicInfo.getDuration();
		seekBar.setMax((int) duration);
		

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isSeekBarChange = true;
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				intent.putExtra("choice", "seekTo");
				int tProcess = seekBar.getProgress();
				intent.putExtra("progress", tProcess);
				startService(intent);
				theCurrentPosition = tProcess;
				isSeekBarChange = false;
				isPlaying = true;
			}
		});

	}

	@Override
	public void onBackPressed() {
		if (hasTouch == false) {
			 
			 
//			 startActivity(new Intent(StoryDetailActivity.this, StoryListActivity.class));
//			super.onBackPressed();
			
			Intent resultIntent = new Intent();
			resultIntent.putExtra("category_number", songStory.categoryNumber);
			resultIntent.putExtra("song_number", songStory.songNumber);
			setResult(RESULT_OK, resultIntent);
			finish();
			
		} else {
			hasTouch = false;
			mFirstLayout.setVisibility(View.VISIBLE);
			mSecondLayout.setVisibility(View.VISIBLE);
			mThirdLayout.setVisibility(View.VISIBLE);
			mMainContentLayout.setVisibility(View.GONE);
			mySetAnimation(true);
			if (hasGone == true) {
				hasGone = false;
				mTopLayout.setVisibility(View.VISIBLE);
			}
			mUpArrow.clearAnimation();
			mTopLayout.clearAnimation();
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		//stopService(intent);
		

		super.onDestroy();
	}

	public Animation setAnimScale(float toX, float toY, float fromX, float fromY) {
		animationScale = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0f);
		// animationScale.setInterpolator(SongsActivity.this,
		// anim.accelerate_decelerate_interpolator);
		animationScale.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				if (hasGone == true) {
					mTopLayout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (hasGone == false) {
					mTopLayout.setVisibility(View.GONE);
					hasGone = true;
				} else {
					hasGone = false;
				}
				
			}
		});
		animationScale.setDuration(1000);
		animationScale.setFillAfter(true);
		return animationScale;

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

	public Animation animRotateMenu(float fromDegrees, float toDegrees,
			float pivotXValue, float pivotYValue) {
		animationRotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.ABSOLUTE, pivotXValue, Animation.ABSOLUTE,
				pivotYValue);
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

	public Animation animTranslate(float toX, float toY, final int lastX,
			final int lastY, final ImageButton button, long durationMillis) {
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);
		animationTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				button.clearAnimation();

			}
		});
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	public Animation animTranslateTwo(float toX, float toY, int fromX,
			int fromY, long durationMillis) {
		animationTranslate = new TranslateAnimation(fromX, toX, fromY, toY);
		
		
		animationTranslate.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mBottomLayout.clearAnimation();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		animationTranslate.setFillAfter(true);
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}
	
	public Animation animTranslateThree(float toX, float toY, int fromX,
			int fromY, long durationMillis) {
		animationTranslate = new TranslateAnimation(fromX, toX, fromY, toY);
		
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	private void mySetAnimation(boolean isBack) {

		if (isBack) {
			mMainContentLayout.setAnimation(animTranslateThree(width, 0, 0, 0,
					500));
			mFirstLayout.setVisibility(View.VISIBLE);
			mSecondLayout.setVisibility(View.VISIBLE);
			mThirdLayout.setVisibility(View.VISIBLE);
			mMainContentLayout.setVisibility(View.GONE);
			mFirstLayout.setAnimation(animTranslateThree(0, 0, -width, 0, 500));
			mSecondLayout.setAnimation(animTranslateThree(0, 0, -width, 0, 700));
			mThirdLayout.setAnimation(animTranslateThree(0, 0, -width, 0, 1000));

		} else {
			mFirstLayout.setAnimation(animTranslateThree(-width, 0, 0, 0, 1000));
			mSecondLayout.setAnimation(animTranslateThree(-width, 0, 0, 0, 700));
			mThirdLayout.setAnimation(animTranslateThree(-width, 0, 0, 0, 500));
			mFirstLayout.setVisibility(View.GONE);
			mSecondLayout.setVisibility(View.GONE);
			mThirdLayout.setVisibility(View.GONE);
			mMainContentLayout.setVisibility(View.VISIBLE);
			mMainContentLayout.setAnimation(animTranslateThree(0, 0, width, 0,
					1000));
		}

	}

	Handler mHandler = new Handler();
	Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
			lyricView.invalidate();
		}
	};

	class UIUpdateThread implements Runnable {
		long time = 1000;

		@Override
		public void run() {
			while (true) {

				if (isPlaying) {
					if (!hasGone) {
						lyricView.updateIndex(theCurrentPosition);
						mHandler.post(mUpdateResults);
						seekBar.setProgress((int) theCurrentPosition);
					}
					theCurrentPosition += time;
				}
				try {
					Thread.sleep(time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		showOrHideLevel2Menu();
		return true;

	}

	
	




	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(hasTouch){
			mGestureDetector.onTouchEvent(ev);
	
		}
		
		return super.dispatchTouchEvent(ev);
	}







	private class MyGestureListener implements OnGestureListener {
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if (hasTouch) {
				if (e2.getX()-e1.getX()>240) {
					
					hasTouch = false;
					mFirstLayout.setVisibility(View.VISIBLE);
					mSecondLayout.setVisibility(View.VISIBLE);
					mThirdLayout.setVisibility(View.VISIBLE);
					mMainContentLayout.setVisibility(View.GONE);
					mySetAnimation(true);
					if (hasGone == true) {
						hasGone = false;
						mTopLayout.setVisibility(View.VISIBLE);
					}
					mUpArrow.clearAnimation();
					mTopLayout.clearAnimation();
				}
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {

			return true;
		}

	

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

	}

}