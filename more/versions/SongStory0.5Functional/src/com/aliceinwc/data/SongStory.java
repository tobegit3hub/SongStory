package com.aliceinwc.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SongStory implements Serializable{
	
	public String def;
	
	public int categoryNumber = 1;
	public int songNumber = 1;
	public String songName = "那些年";
	public String singerName = "黄迪华";
	public String songInformation = "synopsis";
	public String albumImageName = "001_album.jpg";
	public String albumName = "Close Up EP";
	public String songFileName = "001.mp3";
	public String lyricsFileName = "001.lrc";
	public String storyTitle1 = "title 1";
	public String storyContent1 = "content 1";
	public String storyImageName0 = "001_0.jpg";
	public String storyTitle2 = "title 2";
	public String storyContent2 = "title 2";
	public String storyTitle3 = "title 3";
	public String storyContent3 = "content 3";
	public String storyImageName1 = "001_1.jpg";
	public String storyImageName2 = "001_2.jpg";
	
	public SongStory(){
		
	}
	
    // {
    // ”song_category“ : 0,
    // "song_number" : 1,
    // "song_name" : "再见二丁目"
    // }
    public SongStory(JSONObject json) throws JSONException {

    	def = json.toString();
    	
    	categoryNumber = json.getInt("category_number");
    	songNumber = json.getInt("song_number");
    	songName = json.getString("song_name");
    	singerName = json.getString("singer_name");
    	songInformation = json.getString("song_information");
    	albumImageName = json.getString("album_image_name");
    	albumName = json.getString("album_name");
    	songFileName = json.getString("song_file_name");
    	lyricsFileName = json.getString("lyrics_file_name");	
    	storyTitle1 = json.getString("story_title_1");
    	storyContent1 = json.getString("story_content_1");
    	storyImageName0 = json.getString("story_image_name_0");
    	storyTitle2 = json.getString("story_title_2");
    	storyContent2 = json.getString("story_content_2");
    	storyTitle3 = json.getString("story_title_3");
    	storyContent3 = json.getString("story_content_3");
    	storyImageName1 = json.getString("story_image_name_1");
    	storyImageName2 = json.getString("story_image_name_2");
    }
	
	
	
}
