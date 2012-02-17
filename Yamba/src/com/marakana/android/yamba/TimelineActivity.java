package com.marakana.android.yamba;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	static final String TAG = "TimelineActivity";
	static final String[] FROM = { StatusProvider.C_USER,
			StatusProvider.C_TEXT, StatusProvider.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text,
			R.id.text_created_at };
	static final IntentFilter FILTER = new IntentFilter(
			YambaApp.ACTION_NEW_STATUS);

	static final int STATUS_LOADER = 47;

	Cursor cursor;
	SimpleCursorAdapter adapter;
	TimelineReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Needed for the incremental progress bar in Action Bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

		// Initialize the loader
		getLoaderManager().initLoader(STATUS_LOADER, null, this);

		// Setup adapter
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		setListAdapter(adapter);

		// Instantiate receiver
		receiver = new TimelineReceiver();

		Log.d(TAG, "onCreated");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Register TimelineReceiver
		registerReceiver(receiver, FILTER);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister TimelineReceiver
		unregisterReceiver(receiver);
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
		case R.id.item_purge:
			int rows = getContentResolver().delete(StatusProvider.CONTENT_URI, "1", null);
			// Refresh the view
			sendBroadcast(new Intent(YambaApp.ACTION_NEW_STATUS));
			Toast.makeText(this, "Purged "+rows+" records", Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/**
	 * TimelineReceiver responsible for catching ACTION_NEW_STATUS and updating
	 * the list.
	 */
	private class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
	        setProgressBarIndeterminateVisibility(true);

			// Refresh the view
			getLoaderManager().restartLoader(STATUS_LOADER, null, TimelineActivity.this);
			Log.d(TAG, "TimelineReceiver: onReceive");
		}
	}

	// --- LoaderManager.LoaderCallbacks<Cursor> ---

	/** Called to crate the loader firs time. */ 
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Start progress bar here
        setProgressBarIndeterminateVisibility(true);
		return new CursorLoader(this, StatusProvider.CONTENT_URI, null, null,
				null, StatusProvider.ORDER_BY);
	}

	/** Called when loader is ready - we got the data. */
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        setProgressBarIndeterminateVisibility(false);
		adapter.swapCursor(cursor);
	}

	/** Called when loader is no longer available. */
	public void onLoaderReset(Loader<Cursor> loader) {
        setProgressBarIndeterminateVisibility(false);
		adapter.swapCursor(null);
	}

}
