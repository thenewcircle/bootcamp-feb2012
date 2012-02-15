package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener {
	static final String TAG = "YambaApp";
	private Twitter twitter;
	private SharedPreferences prefs;
	StatusData statusData;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		statusData = new StatusData(this);

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

}
