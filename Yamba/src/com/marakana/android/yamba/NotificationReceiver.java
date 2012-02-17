package com.marakana.android.yamba;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	static final String TAG = "NotificationReceiver";

	public static final int NOTIFICATION_ID = 47;

	@Override
	public void onReceive(Context context, Intent intent) {

		// Check preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (prefs.getBoolean("vibrate", false)) {
			// Use Vibrator
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(1000); // 1 second
			Log.d(TAG, "Vibrating...");
		}

		// Create pending intent for when notification is clicked
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, TimelineActivity.class),
				PendingIntent.FLAG_ONE_SHOT);

		// Create notification
		Notification notification = new Notification.Builder(context)
				.setContentTitle("New Status")
				.setTicker("You have a new status!")
				.setSmallIcon(android.R.drawable.stat_notify_chat)
				.setContentIntent(pendingIntent).getNotification();

		// Send the notification
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
}
