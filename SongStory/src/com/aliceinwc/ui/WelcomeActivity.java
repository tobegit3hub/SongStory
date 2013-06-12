package com.aliceinwc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.aliceinwc.R;
import com.aliceinwc.util.FileUtil;

public class WelcomeActivity extends Activity {

	public final static String PREFS_SONG_STORY = "prefs_song_story";
	public final static String TOTAL_RUN_TIMES = "total_run_times";
	public final static String IS_CRACK_MODE = "is_crack_mode";

	private final static int DEFAULT_DELAYED_TIME = 685;

	private Activity activity;

	private TextView songStoryText;
	private TextView welcomeStringView;

	private int totalRunTimes;

	private int delayedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		activity = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.welcome_activity);

		totalRunTimes = getAndAddTotalRunTimes();

		initView(activity);

		if (totalRunTimes == 1) {
			Toast.makeText(activity, R.string.first_run_configure,
					Toast.LENGTH_SHORT).show();

			FileUtil.makeAllDirctory();
			// readAssertFiles();
			// StoryListActivity.upzipAllSsFiles();

			delayedTime = DEFAULT_DELAYED_TIME;

		} else {

			if (FileUtil.getAllSsFiles().length != 0) {
				Toast.makeText(activity, R.string.unziping_please_wait,
						Toast.LENGTH_SHORT).show();

				StoryListActivity.upzipAllSsFiles();

				delayedTime = DEFAULT_DELAYED_TIME / 2;

			} else {
				delayedTime = DEFAULT_DELAYED_TIME;
			}

		}

		delayedStartActivity(activity, StoryListActivity.class, delayedTime);

	}

	private int getAndAddTotalRunTimes() {
		SharedPreferences settings = getSharedPreferences(PREFS_SONG_STORY,
				Context.MODE_PRIVATE);

		int previousTotalRunTimes = settings.getInt(TOTAL_RUN_TIMES, 0);
		int newTotalRunTimes = previousTotalRunTimes + 1;

		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(TOTAL_RUN_TIMES, newTotalRunTimes);

		// reset crack
		editor.putBoolean(IS_CRACK_MODE, false);

		editor.commit();

		return newTotalRunTimes;
	}

	private void initView(Activity activity) {

		welcomeStringView = (TextView) activity
				.findViewById(R.id.welcome_string);

		Resources resource = activity.getResources();
		String[] stringArray = resource.getStringArray(R.array.welcome_strings);

		int i = totalRunTimes % stringArray.length;

		welcomeStringView.setText(stringArray[i]);

	}

	// start activity after delayed time
	private void delayedStartActivity(final Activity activity,
			final Class clazz, int delayedTime) {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent intent = new Intent(activity, clazz);
				activity.startActivity(intent);
				activity.finish();
			}
		}, delayedTime);
	}

}
