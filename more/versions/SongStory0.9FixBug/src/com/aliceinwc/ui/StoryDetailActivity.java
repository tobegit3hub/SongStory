package com.aliceinwc.ui;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
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

public class StoryDetailActivity extends Activity {
	
	private Activity activity;
	
	private ImageButton mPlay;
	private ImageButton mPause;
	private ImageButton mStop;
	private ImageButton mBack;
	private ImageButton mUpArrow;
	private ImageButton mHome;
	
	private ImageButton menuAboutButton;
	private ImageButton menuShareButton;
	
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
	
	
	private SeekBar seekBar;

	public static Lyric mLyric;

	private Intent intent;

	private int height;
	private int width;
	private int layoutHeight;
	
	private long theCurrentPosition = 0;
	private long duration=10*1000;

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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
		setContentView(R.layout.story_detail_activity);
		
		activity = this;
		
		songStory = (SongStory)getIntent().getSerializableExtra("SongStory");

		
		//String path = "/storage/emulated/0/Music/卫兰 - 大哥.mp3";
		//path = Environment.getExternalStorageDirectory().getPath()+"/101.mp3";
		//String path = Environment.getExternalStorageDirectory().getPath()+songStory.
		
		songStoryPath = Environment.getExternalStorageDirectory().getPath()+"/SongStory";
		musicPath = songStoryPath+"/music/";
		lyricsPath = songStoryPath+"/lyrics/";
		imagePath = songStoryPath+"/image/";
		
		
		String path = musicPath + songStory.songFileName;
		//path = Environment.getExternalStorageDirectory().getPath()+"/001.mp3";
		

		
		musicInfo = new MusicInfo(this,path);

		intent = new Intent(StoryDetailActivity.this, MusicPlayService.class);
		intent.putExtra("choice", "play");
		intent.putExtra("path", path);
		startService(intent);
		isPlaying = true;

		buttonInit();
		layoutInit();
		inSideButtonInit();
		menuButtonInit();
		textInit();
		ImageViewInit();

		seekBarInit();
		
		mthread = new Thread(new UIUpdateThread());
		mthread.start();

	}
	
	private void ImageViewInit(){
		mSpecialEdition = (ImageView) findViewById(R.id.specialedition);
		mPoster = (ImageView) findViewById(R.id.poster); 
		
		mSpecialEdition.setImageBitmap(BitmapFactory.decodeFile(imagePath+"/"+songStory.albumImageName));
		mPoster.setImageBitmap(BitmapFactory.decodeFile(imagePath+"/"+songStory.storyImageName0));
		
	}

	private void textInit() {
		//mContent = (TextView) findViewById(R.id.detail_detail_content);
		lyricView = (LyricView) findViewById(R.id.lycView);

		mLyric = new Lyric(new File(lyricsPath, songStory.lyricsFileName));
		//mLyric = new Lyric(new File(Environment.getExternalStorageDirectory().getPath()+"/001.lrc"));
		

		lyricView.setmLyric(mLyric);
		lyricView.setSentencelist(mLyric.list);
		lyricView.setNotCurrentPaintColor(Color.GRAY);
		lyricView.setCurrentPaintColor(Color.WHITE);
		lyricView.setLrcTextSize(25);
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
		
//		mDetailDetailTitle.setText(songStory.storyTitle1);
//		mDetailDetailContent.setText(songStory.storyTitle1);
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

		mFirstLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (hasTouch == false) {
					hasTouch = true;
					mySetAnimation(false);
					
					//mContent.setText(songStory.storyContent1);
					//mContent.setText("��ȷ�����Ǹ���ֻ���г������������İ��ĵ����������������𣿡����ߵĵ���ͷ�����԰������Ҹ��𣿡�������˵��ʲô����ֻ��������Ժ��Ϻ������Ѵ��к�������˵ʲô�����˿̣���������������ֵ�̫�����˺��ߵĵ���ͷ�������Ц�������������һ������û��ͣ��\r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n ");
				
					mDetailDetailTitle.setText(songStory.storyTitle1);
					mDetailDetailContent.setText(songStory.storyContent1);

					
					storyImageView1.setVisibility(View.VISIBLE);
					storyImageView2.setVisibility(View.VISIBLE);
					storyImageView1.setImageBitmap(BitmapFactory.decodeFile(FileUtil.IMAGE_PATH+songStory.storyImageName1));
					storyImageView2.setImageBitmap(BitmapFactory.decodeFile(FileUtil.IMAGE_PATH+songStory.storyImageName2));
					
					Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/lanting_black_simple.ttf");
					
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
				if(isPlaying == true){
					intent.putExtra("choice", "pause");
					isPlaying = !isPlaying;
				}else{
					isPlaying = !isPlaying;
					intent.putExtra("choice", "GoOn");
//					mthread.start();
				}
				startService(intent);

			}
		});
		mStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				stopService(intent);
			}
		});
		mPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
		
		
		menuAboutButton = (ImageButton)findViewById(R.id.about);
		menuAboutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(activity, AboutActivity.class);
				activity.startActivity(intent);
			}
		});

		
		menuShareButton = (ImageButton)findViewById(R.id.share);
		menuShareButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			    Intent intent = new Intent(Intent.ACTION_SEND);
			    intent.setType("text/plain");
			    intent.putExtra(Intent.EXTRA_SUBJECT, R.string.one_click_share);
			    //intent.putExtra(Intent.EXTRA_TEXT, "The status update text");
			    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content));
			    startActivity(Intent.createChooser(intent, getResources().getString(R.string.one_click_share)));
			}
		});
		
	}
	
	public void showOrHideLevel2Menu(){
		if (!areLevel2Showing) {
			relate_level1.setAlpha(1);
			relate_level2.setVisibility(View.VISIBLE);
			MyAnimation.startAnimationsIn(relate_level2, 500);
		} else {
			relate_level1.setAlpha( 0.5f);
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
//			Intent intent = new Intent();
//			intent.setClass(SongsActivity.this, MainActivity.class);
//			startActivity(intent);
			super.onBackPressed();
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
		}
		
	}

	@Override
	protected void onPause() {

		super.onPause();
	}
	
	



	@Override
	protected void onDestroy() {
		stopService(intent);
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
		animationScale.setFillAfter(false);
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

		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	private void mySetAnimation(boolean isBack) {

		if (isBack) {
			mMainContentLayout.setAnimation(animTranslateTwo(width, 0, 0, 0,
					500));
			mFirstLayout.setVisibility(View.VISIBLE);
			mSecondLayout.setVisibility(View.VISIBLE);
			mThirdLayout.setVisibility(View.VISIBLE);
			mMainContentLayout.setVisibility(View.GONE);
			mFirstLayout.setAnimation(animTranslateTwo(0, 0, -width, 0, 500));
			mSecondLayout.setAnimation(animTranslateTwo(0, 0, -width, 0, 700));
			mThirdLayout.setAnimation(animTranslateTwo(0, 0, -width, 0, 1000));

		} else {
			mFirstLayout.setAnimation(animTranslateTwo(-width, 0, 0, 0, 1000));
			mSecondLayout.setAnimation(animTranslateTwo(-width, 0, 0, 0, 700));
			mThirdLayout.setAnimation(animTranslateTwo(-width, 0, 0, 0, 500));
			mFirstLayout.setVisibility(View.GONE);
			mSecondLayout.setVisibility(View.GONE);
			mThirdLayout.setVisibility(View.GONE);
			mMainContentLayout.setVisibility(View.VISIBLE);
			mMainContentLayout.setAnimation(animTranslateTwo(0, 0, width, 0,
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
		
		//return super.onPrepareOptionsMenu(menu);
	}
}


