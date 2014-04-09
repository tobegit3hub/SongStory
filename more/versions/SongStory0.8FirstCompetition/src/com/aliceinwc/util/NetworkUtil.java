package com.aliceinwc.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil{
	
	public final static String BASE_URL = "http://www.chendihao.cn/SongStoryServer/";
	
	
	public static String httpGetRequest(String url) throws Exception{
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		
		if(httpResponse.getStatusLine().getStatusCode()==200){
			
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
			
		}else{
			return null;
		}

	}
	

	public static boolean isWifiConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        //State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        //useless if not network
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if(networkInfo.isConnected()){
//            return true;
//        } 
//        else {
//            return false;
//        }
        

	}
	

	
	
	public static void downloadSsFile(int categoryNumber, int songNumber){
		Integer[] param = new Integer[2];
		param[0] = categoryNumber;
		param[1] = songNumber;
		new DownloadSsFileTask().execute(param);
	}
	
	   
	   
}
