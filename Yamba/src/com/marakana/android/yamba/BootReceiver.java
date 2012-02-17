package com.marakana.android.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	static final long INTERVAL = 10000; // 10 seconds
	
	/** Called when the intent we subscribed for gets broadcasted. */
	@Override
	public void onReceive(Context context, Intent intent) {
		// Create pending intent, i.e. our operation to submit to the alarm
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				new Intent(context, RefreshService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Setup alarm
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis(), INTERVAL, pendingIntent);

		Log.d("BootReceiver", "onReceive");
	}
}
