package com.aliceinwc.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil{
	
	public final static String BASE_URL = "http://www.chendihao.cn";
	
	
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
	
	//public static String httpPostRequest(String url)
	
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
	

	
	public static String requestSsUrl(int category_number, int story_number){
		
		String url = NetworkUtil.BASE_URL+"/SongStoryServer/response.php?category_number="+category_number+"&story_number="+story_number;
		
		try {
			String responseString = NetworkUtil.httpGetRequest("http://www.baidu.com");
			return responseString;
//			JSONObject jsonObject = new JSONObject(responseString);
//			if(jsonObject.getBoolean("is_exist")){
//				String ssUrl = jsonObject.getString("ss_url");
//
//				return ssUrl;
//			}else{
//				return "";
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	
	
	
	   
	   
}
