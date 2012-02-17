package com.marakana.android.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	/** Called when the intent we subscribed for gets broadcasted. */
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, RefreshService.class));
		Log.d("BootReceiver", "onReceive");
	}
}
