package com.aliceinwc.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SongStory {
	

	public int number;
	public int category;
	public String songName;
	public String singerName;
	public String songImagePath;
	public String storySynopsis;
	public String lyrisPath;
	
	public String storyTitle1;
	public String stroyTitle2;
	public String storyTitle3;
	public String StoryContent1;
	public String StoryContent2;
	public String StoryContent3;
	
	public String StroyImagePath1;
	public String StoryImagePath2;
	
	
	
	
	public SongStory(){
		
		
	}
	
    // {
    // ”txt“:"恭喜你。。。。。。。。。。。。",
    // "hotid":123456,
    // "name":"米吧名字"
    // }
    public SongStory(JSONObject json) throws JSONException {
//        mValue = json.toString();
//        mType = Notice.NOTICE_TYPE_MANAGER_SUCCESS;
//
//        mTopicName = json.getString("name");
//        JSONObject moreData = json.getJSONObject("moredata");
//        mTopicId = moreData.getLong("hotid");
//        mContent = moreData.getString("txt");
    }
	
	
	
}
