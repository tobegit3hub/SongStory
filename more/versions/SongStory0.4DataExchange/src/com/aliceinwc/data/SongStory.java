package com.aliceinwc.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class SongStory implements Serializable{
	
	public String def;

//	public int categoryNumber;
//	public int storyNumber;
//	public String songName;
//	public String singerName;
//	public String songImagePath;
//	public String storyListSynopsis;
//	public String lyrisPath;
//	public String storyTitle1;
//	public String storyDetailSynopsis;
//	public String StoryContent1;
//	public String stroyTitle2;
//	public String StoryContent2;
//	public String storyTitle3;
//	public String StoryContent3;
//	public String StroyImagePath1;
//	public String StoryImagePath2;
	
	public int categoryNumber = 1;
	public int songNumber = 1;
	public String songName = "那些年";
	public String singerName = "黄迪华";
	public String songInformation = "synopsis";
	public String albumImageName = "101_2.jpg";
	public String albumName = "Close Up EP";
	public String songFileName = "101.mp3";
	public String lyricsFileName = "101.lrc";
	public String storyTitle1 = "title 1";
	public String storyContent1 = "content 1";
	public String storyImageName0 = "101_0.jpg";
	public String stroyTitle2 = "title 2";
	public String storyContent2 = "title 2";
	public String storyTitle3 = "title 3";
	public String storyContent3 = "content 3";
	public String storyImageName1 = "101_1.jpg";
	public String storyImageName2 = "101_2.jpg";
	
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
    	songInformation = json.getString("story_list_synopsis");
    	albumName = json.getString("album_name");
    	lyricsFileName = json.getString("lyrics_file_name");	
    	storyTitle1 = json.getString("story_title_1");
    	storyImageName0 = json.getString("story_image_name_0");
    	storyContent1 = json.getString("story_content_1");
    	stroyTitle2 = json.getString("story_title_2");
    	storyContent2 = json.getString("story_content_2");
    	storyTitle3 = json.getString("story_title_3");
    	storyContent3 = json.getString("story_content_3");
    	storyImageName1 = json.getString("story_image_name_1");
    	storyImageName2 = json.getString("story_image_name_2");
    }
	
	
	
}
