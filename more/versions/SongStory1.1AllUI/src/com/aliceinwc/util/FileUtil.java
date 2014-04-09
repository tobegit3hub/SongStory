package com.aliceinwc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

import com.aliceinwc.data.SongStory;

import android.os.Environment;

public class FileUtil {
	
	public static String SD_CARD_PATH;
	public static String SONG_STORY_PATH;
	public static String SS_PATH;
	public static String IMAGE_PATH;
	public static String MUSIC_PATH;
	public static String LYRICS_PATH;
	public static String JSON_PATH;
	

	public static void makeAllDirctory(){
	       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){

	    	   SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();	            
	    	   SONG_STORY_PATH = SD_CARD_PATH+"/SongStory/";
	    	   SS_PATH = SONG_STORY_PATH+"/ss/";
	    	   IMAGE_PATH = SONG_STORY_PATH+"/image/";
	    	   MUSIC_PATH = SONG_STORY_PATH+"/music/";
	    	   LYRICS_PATH = SONG_STORY_PATH+"/lyrics/";
	    	   JSON_PATH = SONG_STORY_PATH+"/json/";
	            
	            mkdirIfNotExist(SONG_STORY_PATH);
	            mkdirIfNotExist(SS_PATH);
	            mkdirIfNotExist(IMAGE_PATH);
	            mkdirIfNotExist(MUSIC_PATH);
	            mkdirIfNotExist(LYRICS_PATH);
	            mkdirIfNotExist(JSON_PATH); 	
	       }
		}
	
	
	public static void unzipSsFile(String fileName){
		
	       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
	            

	            try {
	                FileInputStream fis = new FileInputStream(SS_PATH+fileName);
	                ZipInputStream zis = new ZipInputStream(
	                        new BufferedInputStream(fis));
	                ZipEntry entry;
	                String outputPath;

	                while ((entry = zis.getNextEntry()) != null) {

	                    int size;
	                    byte[] buffer = new byte[2048];

	                    //FileOutputStream fos = new FileOutputStream(entry.getName());
	                    
	                    if(entry.getName().toLowerCase().endsWith(".mp3") || entry.getName().toLowerCase().endsWith(".wma")){
	                    	outputPath = MUSIC_PATH;
	                    }else if(entry.getName().toLowerCase().endsWith(".lrc")){
	                    	outputPath = LYRICS_PATH;
	                    }else if(entry.getName().toLowerCase().endsWith(".json")){
	                    	outputPath = JSON_PATH;
	                    }else{
	                    	outputPath = IMAGE_PATH;
	                    }
	                    
	                    FileOutputStream fos = new FileOutputStream(outputPath+ entry.getName());
	                    
	                    BufferedOutputStream bos =
	                            new BufferedOutputStream(fos, buffer.length);

	                    while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
	                        bos.write(buffer, 0, size);
	                    }
	                    bos.flush();
	                    bos.close();
	                }

	                zis.close();
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                
	                // delete the fake or broken ss file 
	                new File(SS_PATH+fileName).delete();
	            }
	            
	            //delete this ss file after unzipping
	            new File(SS_PATH+fileName).delete();
	       }
	       
	}
			       

	
	private static void mkdirIfNotExist(String directory_path){
		File directory = new File(directory_path);
		if(!directory.exists()){
			directory.mkdir();
		}
	}
	
	
	public static ArrayList<SongStory> readJsonFilesAndDelete(){
		
		ArrayList<SongStory> stories = new ArrayList<SongStory>();
		
		if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
			
            String jsonPath = Environment.getExternalStorageDirectory().getPath()+"/SongStory/json/";	 
            File jsonDirectory = new File(jsonPath);

            for(String jsonFileName : jsonDirectory.list(new ExtentionFilenameFilter("json"))){
            	
            	stories.add(readJsonFileToObject(jsonFileName));
            	
            	File jsonFile = new File(jsonPath, jsonFileName);
            	jsonFile.delete();

            }
            
            return stories;
		}
		return stories;
	}
	
	
	public static SongStory readJsonFileToObject(String jsonFileName){
       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
    	   
            String jsonPath = Environment.getExternalStorageDirectory().getPath()+"/SongStory/json/";	 
            
            File jsonFile = new File(jsonPath, jsonFileName);
            BufferedReader bufferedReader;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));
			
	            StringBuilder jsonString = new StringBuilder();
	            String temp = "";
	            while((temp = bufferedReader.readLine()) != null){
	            	jsonString.append(temp);
	            }
	            
	            JSONObject jsonObject = new JSONObject(jsonString.toString());
	            return new SongStory(jsonObject);
            
			} catch (Exception e) {
				e.printStackTrace();
			}
       }
		
		return null;
	}
	

	
	public static String[] getAllSsFiles(){
       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
            String ssPath = Environment.getExternalStorageDirectory().getPath()+"/SongStory/ss/";	 
            
            File ssDirectory = new File(ssPath);
            return ssDirectory.list(new ExtentionFilenameFilter("ss"));
            
       }
		return new String[]{};
	}
	
	public static String[] getAllJsonFiles(){
	       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
	            String jsonPath = Environment.getExternalStorageDirectory().getPath()+"/SongStory/json/";	 
	            
	            File jsonDirectory = new File(jsonPath);
	            return jsonDirectory.list(new ExtentionFilenameFilter("json"));
	            
	       }
			return new String[]{};
		}

}


class ExtentionFilenameFilter implements FilenameFilter{
	
	private String extention;
	
	public ExtentionFilenameFilter(String extention){
		this.extention = extention;
	}

	@Override
	public boolean accept(File dir, String filename) {
		if(filename.toLowerCase().endsWith("."+extention)){
			return true;
		}
		return false;
	}
	
}
