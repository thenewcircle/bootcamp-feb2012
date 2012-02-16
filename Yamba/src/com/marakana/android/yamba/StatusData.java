package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {
	public static final String TAG = "StatusData";

	// DB constants
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 2;
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

	/** Inserts a Status object into the database. */
	public long insert(Status status) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
		
		// Try to insert into the database, ignoring duplicates
		return db.insertWithOnConflict(TABLE, null, getValues(status),
				SQLiteDatabase.CONFLICT_IGNORE);
	}

	/** Converts Status to ContentValues. */
	public static ContentValues getValues(Status status) {
		ContentValues values = new ContentValues();
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_ID, status.id);
		values.put(C_TEXT, status.text);
		values.put(C_USER, status.user.name);
		return values;
	}

	/** Helper class to open/close the database. */
	class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/** Called only once, first time when the database doesn't exist. */
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("create table %s ("
					+ "%s int primary key, %s int, %s text, %s text)", TABLE,
					C_ID, C_CREATED_AT, C_USER, C_TEXT);

			Log.d(TAG, "onCreate with sql: " + sql);

			db.execSQL(sql);
		}

		/** Called whenever database version is not current. */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Typical SQL would be ALTER TABLE ...
			db.execSQL("drop table is exists " + TABLE);
			onCreate(db);
		}

	}
}
