package com.aliceinwc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class StoryDatabaseHelper extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "SongStory.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_SONG_STORY = "table_song_story";

	private final static String[] TABLE_SONG_STORY_COLUMNS_DEF = new String[] {
			"def", "TEXT", "category_number", "INTEGER DEFAULT 0",
			"song_number", "INTEGER",

	};

	public StoryDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// public StoryDatabaseHelper(Context context, String name, CursorFactory
	// factory, int version) {
	// super(context, name, factory, version);
	// }

	@Override
	public void onCreate(SQLiteDatabase db) {

		createTable(db, TABLE_SONG_STORY, TABLE_SONG_STORY_COLUMNS_DEF);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// if (newVersion >= 2) {
		// if (oldVersion <= 1) {
		// //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
		// //StoryDatabaseHelper.createTable(db, TABLE_NOTICE,
		// TABLE_NOTICE_COLUMNS_DEF);
		// }
		// }
	}

	public static void createTable(final SQLiteDatabase db,
			final String tableName, final String[] columnsDefinition) {
		String queryStr = "CREATE TABLE " + tableName + "(" + BaseColumns._ID
				+ " INTEGER  PRIMARY KEY ,";
		// Add the columns now, Increase by 2
		for (int i = 0; i < (columnsDefinition.length - 1); i += 2) {
			if (i != 0) {
				queryStr += ",";
			}
			queryStr += columnsDefinition[i] + " " + columnsDefinition[i + 1];
		}

		queryStr += ");";

		db.execSQL(queryStr);
	}

}
