package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusProvider extends ContentProvider {
	static final String TAG = "StatusProvider";

	public static final Uri CONTENT_URI = Uri
			.parse("content://com.marakana.android.yamba.provider/status");

	// DB constants
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 2;
	public static final String TABLE = "statuses";
	public static final String C_CREATED_AT = "yamba_created_at";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_USER = "yamba_user";
	public static final String C_TEXT = "yamba_text";

	static final String ORDER_BY = C_CREATED_AT + " DESC";
	
	DbHelper dbHelper;

	// --- ContentProvider Callbacks ---
	
	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(this.getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		if (id == -1) {
			return null;
		} else {
			return Uri.withAppendedPath(CONTENT_URI, Long.toString(id) );
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String orderBy) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.query(TABLE, projection, selection, selectionArgs, null, null, orderBy);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	
	// --- DbHelper ---
	
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
