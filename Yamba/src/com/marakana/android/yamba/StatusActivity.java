package com.marakana.android.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements TextWatcher {
    private static final String TAG = "StatusActivity";
    
    private Twitter twitter;
    private EditText statusMsg;
    private TextView textCount;
    private Toast toast;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Log.v(TAG, "onCreate() invoked");
        
        textCount = (TextView) findViewById(R.id.count);
        statusMsg = (EditText) findViewById(R.id.edit_msg);
        statusMsg.addTextChangedListener(this);
        
        toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        
//        System.setProperty("http.proxyHost", "host");
//        System.setProperty("http.proxyPort", "port_number");
//
//        If proxy requires authentication,
//
//        System.setProperty("http.proxyUser", "user");
//        System.setProperty("http.proxyPassword", "password");
        
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
    }

    /** android:onClick specifies to call onClick() when button is clicked. */
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button_update:
			// User clicked the Update Status button
			Log.v(TAG, "Button clicked");
			String msg = statusMsg.getText().toString();
			Log.v(TAG, "User entered: " + msg);
			statusMsg.setText("");
			
			if (!TextUtils.isEmpty(msg)) {
				// Post the message to the server
				new PostToTwitter().execute(msg);
			}
			
			break;
		default:
			// Unknown button! We should never get here!
		}
	}
	
	private class PostToTwitter extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			int result = R.string.post_status_success;
			try {
				twitter.setStatus(params[0]);
			} catch (TwitterException e) {
				Log.w(TAG, "Failed to post message", e);
				result = R.string.post_status_fail;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			toast.setText(result);
			toast.show();
		}
		
	}

	// TextWatcher Callbacks
	
	public void afterTextChanged(Editable arg0) {		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int available = 140 - statusMsg.getText().length();
		textCount.setText( Integer.toString( available ) );
		
		if( available < 10 ) {
			textCount.setTextColor( Color.RED );
		} else {
			textCount.setTextColor( Color.WHITE );
		}
	}

}


