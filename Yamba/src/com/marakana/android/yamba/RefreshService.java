package com.marakana.android.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";
	int count = 0;

	/** Default constructor. */
	public RefreshService() {
		super(TAG);
		Log.d(TAG, "Created: " + this.toString());
	}

	/** Called each time service is started. Runs on a separate worker thread. */
	@Override
	protected void onHandleIntent(Intent intent) {
		// Gets 20 most recent statuses from us and our friends in the last 24
		// hours
		List<Status> timeline = ((YambaApp) getApplication()).getTwitter()
				.getFriendsTimeline();

		long id;
		// Iterate of timeline
		for (Status status : timeline) {
			Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
			id = ((YambaApp) getApplication()).statusData.insert(status);
			if (id != -1)
				count++;
		}

		Log.d(TAG, "onHandleIntent");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Create a toast with new statuses
		Toast.makeText(getApplication(),
				"You have " + count + " new statuses!", Toast.LENGTH_LONG)
				.show();
	}
}
