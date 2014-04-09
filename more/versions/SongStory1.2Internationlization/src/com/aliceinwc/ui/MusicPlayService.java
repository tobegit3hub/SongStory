package com.aliceinwc.ui;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicPlayService extends Service implements OnCompletionListener {

	private static MediaPlayer mp = new MediaPlayer();
	String path;
	

	public static int getCurrentPosition(){
		return mp.getCurrentPosition();
	}
	
	public static void resetCurrentPosition(){
		mp.seekTo(0);
	}

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
		path = intent.getStringExtra("path");
		
		boolean isContinue = intent.getBooleanExtra("continue", false);
		
		if (choice.equals("play")) {
			try {			
				mp.setLooping(true);
				
				if(isContinue){
					
				}else{
					mp.reset();
					mp.setDataSource(path);
					mp.prepare();	
					mp.start();

				}

							
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.setOnCompletionListener(this);
			
		} else if (choice.equals("pause")) {
			mp.pause();
		} else if (choice.equals("shake")) {
			if (mp != null) {
				mp.stop();
				mp.reset();
				mp.start();
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
			mp.setOnCompletionListener(this);
		} else if (choice.equals("GoOn")) {
			mp.start();
		} else if (choice.equals("seekTo")) {
			int mProgress = intent.getIntExtra("progress", 0);
			mp.seekTo(mProgress);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
		try {
			mp.reset();
			mp.setDataSource(path);
			mp.prepare();	
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.setOnCompletionListener(this);
		
	}

}
