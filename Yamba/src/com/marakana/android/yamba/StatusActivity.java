package com.marakana.android.yamba;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
    static final int DIALOG_ID = 47;

    private EditText statusMsg;
    private TextView textCount;
    private Toast toast;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
		Log.v(TAG, "onCreate() invoked");
        
        textCount = (TextView) findViewById(R.id.count);
        statusMsg = (EditText) findViewById(R.id.edit_msg);
        statusMsg.addTextChangedListener(this);
        
        toast = Toast.makeText(this, null, Toast.LENGTH_LONG);        
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
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID);
		}

		@Override
		protected Integer doInBackground(String... params) {
			int result = R.string.post_status_success;
			try {
				((YambaApp)getApplication()).getTwitter().setStatus(params[0]);
			} catch (TwitterException e) {
				Log.w(TAG, "Failed to post message", e);
				result = R.string.post_status_fail;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			removeDialog(DIALOG_ID);
			statusMsg.setText("");

			toast.setText(result);
			toast.show();
		}
		
	}

	/** Called when we need to create the dialog. */
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while loading...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
		return dialog;
	}
	
	
	// --- TextWatcher Callbacks ---
	

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


