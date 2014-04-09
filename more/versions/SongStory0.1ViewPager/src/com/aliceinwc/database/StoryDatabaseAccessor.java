package com.aliceinwc.database;

import java.util.ArrayList;
import java.util.List;

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
        if (context != null) {
            throw new IllegalArgumentException("can not init the dbaccessor twice");
        }
        StoryDatabaseAccessor.context = context;
        databaseHelper = new StoryDatabaseHelper(context);
    }
    
    
    
    public static ArrayList<SongStory> getSongStories(long categoryNumber) throws JSONException {
        ArrayList<SongStory> stories = new ArrayList<SongStory>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.query(StoryDatabaseHelper.TABLE_SONG_STORY, new String[] {
                    "def"
            }, " category_id = ?", new String[] {
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
    
    
//    public static void insertTopics(long categoryId, int type, List<Topic> topics) {
//    	
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            for (Topic t : topics) {
//                ContentValues cvs = new ContentValues();
//                cvs.put("id", t.mId);
//                cvs.put("category_id", categoryId);
//                cvs.put("type", type);
//                cvs.put("def", t.mValue);
//                db.insert(TopicDatabaseHelper.TABLE_TOPIC, null, cvs);
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//        notifyTableChange(TopicDatabaseHelper.TABLE_TOPIC);
//    	
//    }
//    
//    
//    public static void deleteCategory(long categoryId) {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        db.execSQL("delete from " + TopicDatabaseHelper.TABLE_CATEGORY + " where category_id = ?"
//                + categoryId);
//        notifyTableChange(TopicDatabaseHelper.TABLE_CATEGORY);
//    }


    public static void clearCacheDB() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("delete from " + StoryDatabaseHelper.TABLE_SONG_STORY);
        //notifyTableChange(StoryDatabaseHelper.TABLE_SONG_STORY);

    }
}
