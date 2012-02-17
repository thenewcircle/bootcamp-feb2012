package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		// Find the webview and load the url
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl("file:///android_asset/about.html");
	}

}
