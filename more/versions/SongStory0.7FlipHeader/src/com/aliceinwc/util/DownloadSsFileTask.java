package com.aliceinwc.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.os.AsyncTask;

public class DownloadSsFileTask extends AsyncTask<Integer, Integer, Boolean> {
	
	private boolean isSuccess = false;

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		//mProgressDialog.show();
	}
	


	@Override
	protected Boolean doInBackground(Integer... params) {
		
		int categoryNumber = params[0];
		int song_number = params[1];
		
		String requestUrl = NetworkUtil.BASE_URL+"response.php?category_number="+categoryNumber+"&song_number="+song_number;
		
		try {
			String responseString = NetworkUtil.httpGetRequest(requestUrl);

			JSONObject jsonObject = new JSONObject(responseString);
			if(jsonObject.getBoolean("is_exist")){
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
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                //publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	            }

	            output.flush();
	            output.close();
	            input.close();
	            
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
		
		
		 
	}



}
