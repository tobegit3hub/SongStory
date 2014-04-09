package com.songstory;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicPlayService extends Service {

	private MediaPlayer mp = new MediaPlayer();;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
		mp.release();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String choice = intent.getStringExtra("choice");
		String path = intent.getStringExtra("path");
		if (choice.equals("play")) {
			try {				
				mp.setDataSource(path);
				mp.prepare();				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.start();
			
		} else if (choice.equals("pause")) {
			mp.pause();
		} else if (choice.equals("shake")) {
			if (mp != null) {
				mp.stop();
				mp.reset();
			}
			try {
				mp.setDataSource(path);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.start();
		} else if (choice.equals("GoOn")) {
			mp.start();
		} else if (choice.equals("seekTo")) {
			int mProgress = intent.getIntExtra("progress", 0);
//			Log.e("====ddd===", "" + mProgress);
			mp.seekTo(mProgress);
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
