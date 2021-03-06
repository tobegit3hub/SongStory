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
	public int storyNumber = 1;
	public String songName = "浣犵瀿鎴戠瀿";
	public String singerName = "闄堟煆瀹�;";
	public String songImagePath = "101.mp3";
	public String storyListSynopsis = "synopsis";
	public String lyrisPath = "101.lrc";
	public String storyTitle1 = "title 1";
	public String storyDetailSynopsis = "synopsis";
	public String StoryContent1 = "content 1";
	public String stroyTitle2 = "title 2";
	public String StoryContent2 = "title 2";
	public String storyTitle3 = "title 3";
	public String StoryContent3 = "content 3";
	public String StroyImagePath1 = "101.jpg";
	public String StoryImagePath2 = "101.jpg";
	
	public SongStory(){
		
	}
	
    // {
    // 鈥漵ong_category鈥�: 0,
    // "song_number" : 1,
    // "song_name" : "鍐嶈浜屼竵鐩�
    // }
    public SongStory(JSONObject json) throws JSONException {

    	def = json.toString();
    	
    	categoryNumber = json.getInt("category_number");
    	storyNumber = json.getInt("song_number");
    	songName = json.getString("song_name");
    	singerName = json.getString("singer_name");
    	songImagePath = json.getString("song_image_path");
    	storyListSynopsis = json.getString("story_list_synopsis");
    	lyrisPath = json.getString("lyris_path");	
    	storyTitle1 = json.getString("story_title_1");
    	storyDetailSynopsis = json.getString("story_detail_synopsis");
    	StoryContent1 = json.getString("story_content_1");
    	stroyTitle2 = json.getString("story_title_2");
    	StoryContent2 = json.getString("story_content_2");
    	storyTitle3 = json.getString("story_title_3");
    	StoryContent3 = json.getString("story_content_3");
    	StroyImagePath1 = json.getString("story_image_path_1");
    	StoryImagePath2 = json.getString("story_image_path_2");
    }
	
	
	
}
