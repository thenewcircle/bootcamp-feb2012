package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener {
	static final String TAG = "YambaApp";
	
	public static final String ACTION_NEW_STATUS = "com.marakana.action.NEW_STATUS";
	
	private Twitter twitter;
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		setupRefreshAlarm();
		
		Log.d(TAG, "onCreated");
	}

	/** Returns twitter instance, initializing it if needed. */
	public Twitter getTwitter() {
		if (twitter == null) {
			// System.setProperty("http.proxyHost", "host");
			// System.setProperty("http.proxyPort", "port_number");
			//
			// If proxy requires authentication,
			//
			// System.setProperty("http.proxyUser", "user");
			// System.setProperty("http.proxyPassword", "password");

			// Read preferences
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");
			Log.d(TAG, String.format("%s/%s@%s", username, password, server));

			// Setup twitter object
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
		}
		return twitter;
	}

	/** Called whenever preferences have changed. */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		twitter = null;
	}
	
	static final long INTERVAL = 10000; // 10 seconds

	/** Sets up the alarm. */
	private void setupRefreshAlarm() {
		// Create pending intent, i.e. our operation to submit to the alarm
		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				new Intent(this, RefreshService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Setup alarm
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis(), INTERVAL, pendingIntent);
	}

}
