package com.aliceinwc.database;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import com.aliceinwc.data.SongStory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class StoryDatabaseAccessor {

    private static Context context;

    private static StoryDatabaseHelper databaseHelper;
    
    
    public static void init(Context context) {
        if (StoryDatabaseAccessor.context != null) {
            throw new IllegalArgumentException("can not init the dbaccessor twice");
        }
        StoryDatabaseAccessor.context = context;
        databaseHelper = new StoryDatabaseHelper(context);
    }
    
    
    
    public static ArrayList<SongStory> getSongStories(int categoryNumber) throws JSONException {
        ArrayList<SongStory> stories = new ArrayList<SongStory>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.query(StoryDatabaseHelper.TABLE_SONG_STORY, new String[] {
                    "def"
            }, " category_number = ?", new String[] {
                    String.valueOf(categoryNumber)
            }, null, null, null);
            
            if (c != null && c.moveToFirst()) {
                do {
                	stories.add(new SongStory(new JSONObject(c.getString(0))));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return stories;
    }
    
    public static SongStory getSongStory(int categoryNumber, int songNumber) throws JSONException {
    	SongStory songStory = null;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.query(StoryDatabaseHelper.TABLE_SONG_STORY, new String[] {
                    "def"
            }, " category_number = ? and song_number = ?", new String[] {
                    String.valueOf(categoryNumber), String.valueOf(songNumber)
            }, null, null, null);
            
            if (c != null && c.moveToFirst()) {
//                do {
                	songStory = new SongStory(new JSONObject(c.getString(0)));
//                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return songStory;
    }
    
    
    public static void insertSongStory(SongStory songStory){
    	SQLiteDatabase db = databaseHelper.getWritableDatabase();
    	
    	db.beginTransaction();
    	try {

          ContentValues cvs = new ContentValues();
          cvs.put("def", songStory.def);
          cvs.put("category_number", songStory.categoryNumber);
          cvs.put("song_number", songStory.songNumber);

          db.insert(StoryDatabaseHelper.TABLE_SONG_STORY, null, cvs);

          db.setTransactionSuccessful();
    	} finally {
    		db.endTransaction();
    	}    	

    }
    

//    
//    public static void deleteCategory(long categoryId) {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        db.execSQL("delete from " + TopicDatabaseHelper.TABLE_CATEGORY + " where category_id = ?"
//                + categoryId);
//        notifyTableChange(TopicDatabaseHelper.TABLE_CATEGORY);
//    }


    public static void clearDatabase() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("delete from " + StoryDatabaseHelper.TABLE_SONG_STORY);
        //notifyTableChange(StoryDatabaseHelper.TABLE_SONG_STORY);

    }
}
