package com.marakana.android.yamba;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String TAG = "TimelineActivity";
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT,
			StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text,
			R.id.text_created_at };

	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get data
		cursor = ((YambaApp) getApplication()).statusData.query();
		startManagingCursor(cursor);

		// Setup adapter
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		setListAdapter(adapter);

		Log.d(TAG, "onCreated");
	}

	/**
	 * Responsible for binding a particular row of data to a particular row in
	 * the list view.
	 */
	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		/** Convert the timestamp from DB to relative time in the UI. */
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			// Is this call binding the CreatedAt data?
			if (view.getId() == R.id.text_created_at) {
				long timestamp = cursor.getLong(columnIndex);
				CharSequence relativeTime = DateUtils
						.getRelativeTimeSpanString(timestamp);
				((TextView) view).setText(relativeTime);
				return true;
			} else {
				return false;
			}
		}
	};

	// --- Options Menu Callbacks ---

	/** Called first time to initialize the options menu. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/** Called each time a menu item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_prefs:
			startActivity(new Intent(this, PrefsActivity.class));
			return true;
		case R.id.item_refresh:
			startService(new Intent(this, RefreshService.class));
			return true;
		case R.id.item_status_update:
			startActivity(new Intent(this, StatusActivity.class));
			return true;
		}
		return false;
	}

}