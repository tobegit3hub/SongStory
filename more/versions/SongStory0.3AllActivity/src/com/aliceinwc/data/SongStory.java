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
	public String songName = "你瞒我瞒";
	public String singerName = "陈柏宇";
	public String songImageName = "101.jpg";
	public String songInformation = "synopsis";
	public String AlbumName = "Close Up EP";
	public String songFileName = "101.mp3";
	public String lyricsFileName = "101.lrc";
	public String storyTitle1 = "title 1";
	public String StoryContent1 = "content 1";
	public String storySynopsisImageName = "101.jpg";
	public String stroyTitle2 = "title 2";
	public String StoryContent2 = "title 2";
	public String storyTitle3 = "title 3";
	public String storyContent3 = "content 3";
	public String storyImageName1 = "101.jpg";
	public String storyImageName2 = "101.jpg";
	
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
    	songImageName = json.getString("song_image_path");
    	songInformation = json.getString("story_list_synopsis");
    	lyricsFileName = json.getString("lyris_path");	
    	storyTitle1 = json.getString("story_title_1");
    	storySynopsisImageName = json.getString("story_detail_synopsis");
    	StoryContent1 = json.getString("story_content_1");
    	stroyTitle2 = json.getString("story_title_2");
    	StoryContent2 = json.getString("story_content_2");
    	storyTitle3 = json.getString("story_title_3");
    	storyContent3 = json.getString("story_content_3");
    	storyImageName1 = json.getString("story_image_path_1");
    	storyImageName2 = json.getString("story_image_path_2");
    }
	
	
	
}
