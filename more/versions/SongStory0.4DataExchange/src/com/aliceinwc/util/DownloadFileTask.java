package com.aliceinwc.util;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;


public class DownloadFileTask extends AsyncTask<String, Integer, String>{
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		//mProgressDialog.show();
	}
	
	
	@Override
	protected String doInBackground(String... params) {

		try {
	        //String urlPath = "http://127.0.0.1/SongStoryServer/ss/101.ss";
	        //String urlPath = "http://110.64.91.18/SongStoryServer/ss/cgbt.txt";
	        String urlPath ="http://g.hiphotos.baidu.com/baike/s%3D220/sign=5127173ab899a9013f355c342d950a58/0d338744ebf81a4c1f1827e7d72a6059252da6f2.jpg";
	        
	        URL url = new URL(urlPath);
	        URLConnection urlConnection = url.openConnection();
	        
	        BufferedInputStream input = new BufferedInputStream(urlConnection.getInputStream());
	        OutputStream output = new FileOutputStream("/sdcard/download.ss");

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
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
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


