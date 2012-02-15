package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application {
	static final String TAG = "YambaApp";
	private Twitter twitter;

	@Override
	public void onCreate() {
		super.onCreate();
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
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
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

}
