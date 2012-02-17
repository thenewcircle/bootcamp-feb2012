package com.marakana.android.yamba;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class YambaWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		onUpdate(context, appWidgetManager,
				appWidgetManager.getAppWidgetIds(new ComponentName(context,
						YambaWidget.class)));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		// Get the data
		Cursor cursor = context.getContentResolver().query(
				StatusProvider.CONTENT_URI, null, null, null,
				StatusProvider.ORDER_BY);

		// Do we have any data
		if (cursor.moveToFirst()) {
			String user = cursor.getString(cursor
					.getColumnIndex(StatusProvider.C_USER));
			String text = cursor.getString(cursor
					.getColumnIndex(StatusProvider.C_TEXT));

			// Loop through all instances of this widget
			for (int appWidgetId : appWidgetIds) {
				RemoteViews views = new RemoteViews(context.getPackageName(),
						R.layout.row);
				views.setTextViewText(R.id.text_user, user);
				views.setTextViewText(R.id.text_text, text);
				views.setTextViewText(R.id.text_created_at, "");
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
	}

}
