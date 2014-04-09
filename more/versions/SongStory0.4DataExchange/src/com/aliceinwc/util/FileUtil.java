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

	
	public static void unzipSsFile(String fileName){
		
	       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){

	            String sdCardPath = Environment.getExternalStorageDirectory().getPath();	            
	            String songStoryPath = sdCardPath+"/SongStory/";
	            String ssPath = songStoryPath+"/ss/";
	            String imagePath = songStoryPath+"/image/";
	            String musicPath = songStoryPath+"/music/";
	            String lyrisPath = songStoryPath+"/lyris/";
	            String jsonPath = songStoryPath+"/json/";
	            

	            try {
	                FileInputStream fis = new FileInputStream(ssPath+fileName);
	                ZipInputStream zis = new ZipInputStream(
	                        new BufferedInputStream(fis));
	                ZipEntry entry;
	                String outputPath;

	                while ((entry = zis.getNextEntry()) != null) {

	                    int size;
	                    byte[] buffer = new byte[2048];

	                    //FileOutputStream fos = new FileOutputStream(entry.getName());
	                    
	                    if(entry.getName().toLowerCase().endsWith(".mp3") || entry.getName().toLowerCase().endsWith(".wma")){
	                    	outputPath = musicPath;
	                    }else if(entry.getName().toLowerCase().endsWith(".lrc")){
	                    	outputPath = lyrisPath;
	                    }else if(entry.getName().toLowerCase().endsWith(".json")){
	                    	outputPath = jsonPath;
	                    }else{
	                    	outputPath = imagePath;
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
	            }
	            
	            //delete this ss file after unzipping
	            new File(ssPath+fileName).delete();
	       }
	       
	}
			       
	public static void mkdirAllDirctory(){
       if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){

            String sdCardPath = Environment.getExternalStorageDirectory().getPath();	            
            String songStoryPath = sdCardPath+"/SongStory/";
            String ssPath = songStoryPath+"/ss/";
            String imagePath = songStoryPath+"/image/";
            String musicPath = songStoryPath+"/music/";
            String lyrisPath = songStoryPath+"/lyrics/";
            String jsonPath = songStoryPath+"/json/";
            
            mkdirIfNotExist(songStoryPath);
            mkdirIfNotExist(ssPath);
            mkdirIfNotExist(imagePath);
            mkdirIfNotExist(musicPath);
            mkdirIfNotExist(lyrisPath);
            mkdirIfNotExist(jsonPath); 	
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
            	
            	stories.add(readJsonToObject(jsonFileName));
            	
            	File jsonFile = new File(jsonPath, jsonFileName);
            	jsonFile.delete();

            }
            
            return stories;
		}
		return stories;
	}
	
	
	public static SongStory readJsonToObject(String jsonFileName){
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
