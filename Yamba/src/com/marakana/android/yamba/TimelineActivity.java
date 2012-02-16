package com.marakana.android.yamba;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TimelineActivity extends ListActivity {
	static final String TAG = "TimelineActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Log.d(TAG, "onCreated");
	}

	
	// --- Options Menu Callbacks ---
	
	/** Called first time to initialize the options menu.*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/** Called each time a menu item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
		case R.id.item_prefs:
			startActivity( new Intent( this, PrefsActivity.class ) );
			return true;
		case R.id.item_refresh:
			startService( new Intent( this, RefreshService.class) );
			return true;
		case R.id.item_status_update:
			startActivity( new Intent( this, StatusActivity.class ) );
			return true;
		}
		return false;
	}

}
