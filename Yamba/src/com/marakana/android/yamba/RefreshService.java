package com.marakana.android.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";
		
	/** Default constructor. */
	public RefreshService() {
		super(TAG);
		Log.d(TAG, "Created");
	}

	/** Called each time service is started. Runs on a separate worker thread. */
	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "onHandleIntent");
	}
}
