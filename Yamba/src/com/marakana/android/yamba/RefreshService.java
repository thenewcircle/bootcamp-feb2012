package com.marakana.android.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";

	/** Default constructor. */
	public RefreshService() {
		super(TAG);
		Log.d(TAG, "Created: " + this.toString());
	}

	/** Called each time service is started. Runs on a separate worker thread. */
	@Override
	protected void onHandleIntent(Intent intent) {
		List<Status> timeline = ((YambaApp) getApplication()).getTwitter()
				.getFriendsTimeline();

		// Iterate of timeline
		for (Status status : timeline) {
			Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
			
		}

		Log.d(TAG, "onHandleIntent");
	}
}
