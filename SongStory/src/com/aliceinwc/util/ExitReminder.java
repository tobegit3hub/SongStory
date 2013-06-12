package com.aliceinwc.util;

import android.os.Handler;
import android.os.HandlerThread;

public class ExitReminder {

	public boolean isExit = false;

	private Runnable task = new Runnable() {
		@Override
		public void run() {
			isExit = false;
		}
	};

	public void doExitInTwoSecond() {
		isExit = true;
		HandlerThread thread = new HandlerThread("doTask");
		thread.start();
		new Handler(thread.getLooper()).postDelayed(task, 2000);
	}

}
