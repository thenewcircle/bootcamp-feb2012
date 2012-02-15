package com.marakana.android.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {
	public static final String TAG = "StatusData";
	
	// DB constants
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "statuses";
	public static final String C_CREATED_AT = "yamba_created_at";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_USER = "yamba_user";
	public static final String C_TEXT = "yamba_text";
	
	DbHelper dbHelper;
	
	/** Constructor */
	public StatusData(Context context) {
		dbHelper = new DbHelper(context);
	}
	
	/** Helper class to open/close the database. */
	class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/** Called only once, first time when the database doesn't exist. */
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("create table %s (" +
					"%s int primary key, %s int, %s text, %s text)",
					TABLE, C_ID, C_CREATED_AT, C_USER, C_TEXT);
			
			Log.d(TAG, "onCreate with sql: "+sql);
			
			db.execSQL( sql );
		}

		/** Called whenever database version is not current. */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Typical SQL would be ALTER TABLE ...
			db.execSQL("drop table is exists "+TABLE);
			onCreate(db);
		}
		
	}
}
