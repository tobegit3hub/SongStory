package com.aliceinwc.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.aliceinwc.R;
import com.aliceinwc.ui.StoryListActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Message;

public class DownloadSsFileTask extends AsyncTask<Integer, Integer, Boolean> {
	
	private boolean isSuccess = false;
	

	private Notification notification;
    private NotificationManager notificationManager;
    

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		//mProgressDialog.show();
		
		notification = StoryListActivity.notification;
		notificationManager = StoryListActivity.nm;
		
		notificationManager.notify(0, notification);
		notification.contentView.setProgressBar(R.id.task_progress, 100, 0, false);
	}
	


	@Override
	protected Boolean doInBackground(Integer... params) {
		
		int categoryNumber = params[0];
		int song_number = params[1];
		
		String requestUrl = NetworkUtil.BASE_URL+"response.php?category_number="+categoryNumber+"&song_number="+song_number;
		
		int progress = 0;

		
		try {
			String responseString = NetworkUtil.httpGetRequest(requestUrl);

			JSONObject jsonObject = new JSONObject(responseString);
			if(jsonObject.getBoolean("is_exist")){
				
				// let ui thread show "downloading..."
				Message msg = new Message();
				msg.what = StoryListActivity.MESSAGE_DOWNLOAD_PROCESSING;
				StoryListActivity.handler.sendMessage(msg);
				
				
				String ssUrl = jsonObject.getString("ss_url");

				
		        URL downloadUrl = new URL(ssUrl);
		        URLConnection urlConnection = downloadUrl.openConnection();
		        
		        BufferedInputStream input = new BufferedInputStream(urlConnection.getInputStream());

		        String downloadFileName = ""+categoryNumber+song_number+".ss";
		        if(song_number <= 9){
		        	downloadFileName = ""+categoryNumber+"0"+song_number+".ss";
		        }
		        OutputStream output = new FileOutputStream(FileUtil.SS_PATH+downloadFileName);

	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            long fileLength = urlConnection.getInputStream().available();
	            float downloadSize = 0;
	            
	            while ((count = input.read(data)) != -1) {
	                total += count;

	                downloadSize += count;
	                progress = (int)(downloadSize* 100/fileLength);
	                
	                output.write(data, 0, count);
	                
	            }

	            output.flush();
	            output.close();
	            input.close();
	            
	            
                notification.contentView.setProgressBar(R.id.task_progress, 100, progress, false);
                notificationManager.notify(0, notification);
	            
	            isSuccess = true;
				return true;
			}else{
				isSuccess = false;
				return false;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		isSuccess = false;
		return false;

	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		//mProgressDialog.setProgress(progress[0]);
	}



	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
		Message msg = new Message();
		if(isSuccess){
			msg.what = StoryListActivity.MESSAGE_DOWNLOAD_COMPLETE;
		}else{
			msg.what = StoryListActivity.MESSAGE_DOWNLOAD_FAIL;
		}
		
		StoryListActivity.handler.sendMessage(msg);

		
		 notificationManager.cancel(0);

		
	}



}
