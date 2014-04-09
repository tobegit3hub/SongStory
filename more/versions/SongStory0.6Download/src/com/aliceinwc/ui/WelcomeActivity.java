package com.aliceinwc.ui;

import com.aliceinwc.R;

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

public class WelcomeActivity extends Activity {

	public final static String PREFS_SONG_STORY = "prefs_song_story";
	public final static String TOTAL_RUN_TIMES = "total_run_times";

	private final static int DURATION_TIME = 185;

	private Activity activity;

	private TextView welcomeStringView;

	private int totalRunTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		activity = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.welcome_activity);

		welcomeStringView = (TextView) findViewById(R.id.welcome_string);

		SharedPreferences settings = getSharedPreferences(PREFS_SONG_STORY,
				Context.MODE_PRIVATE);

		totalRunTimes = settings.getInt(TOTAL_RUN_TIMES, 0);
		totalRunTimes++;

		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(TOTAL_RUN_TIMES, totalRunTimes);
		editor.commit();

		Resources resource = getResources();
		String[] stringArray = resource.getStringArray(R.array.welcome_strings);

		int i = totalRunTimes % stringArray.length;

		welcomeStringView.setText(stringArray[i]);

		

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent intent = new Intent(activity, StoryListActivity.class);
				activity.startActivity(intent);
				activity.finish();
			}
		}, DURATION_TIME);

	}

}
