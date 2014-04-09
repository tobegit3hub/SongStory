package com.aliceinwc.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class RequestUrlTask extends AsyncTask<String, Integer, String>{
	
	private int category_number;
	private int story_number;
	private Context context;
	
	public RequestUrlTask(Context context, int category_number, int story_number){
		this.category_number = category_number;
		this.story_number = story_number;
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		//mProgressDialog.show();
	}
	
	
	@Override
	protected String doInBackground(String... params) {

		try {

			//NetworkUtil.requestSsUrl(category_number, story_number);
			Toast.makeText(context, NetworkUtil.requestSsUrl(1, 1), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		//mProgressDialog.setProgress(progress[0]);
	}


	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

}


