package com.yfeng.lockedsms;
import com.yfeng.lockedsms.R;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class ContactListActivity extends FragmentActivity {
	
	private EditText numberentry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contactlist);
		
		numberentry = (EditText) findViewById(R.id.et_numberentry);
		
		//don't show keyboard when first loaded
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		//load fragment to the fragment_content FrameLayout
		FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_content);
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content, new ContactListFragment());
            ft.commit();
        }
        
        
        //listener for the "Done" key on the keyboard
		numberentry.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Log.i("EEE", "Enter pressed");
					// get the EditText values
					String phonenumber = numberentry.getText().toString();

					if (PhoneNumUtil.isValidNumber(phonenumber) == true) {
						// start SendMessageActivity and pass it the
						Intent i = new Intent(v.getContext(),
								SendMessageActivity.class);
						i.putExtra("phonenumber", phonenumber);
						i.putExtra("contactname", "");
						startActivity(i);
					} else {
						Toast.makeText(getBaseContext(),
								"Please enter a valid phone number",
								Toast.LENGTH_SHORT).show();
					}
				}
				return false;
			}
		});
        
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.settings:
			Intent j = new Intent(this, SetupActivity.class);
			startActivity(j);
			return true;
		
		case R.id.help:
			Intent i = new Intent(this, HelpActivity.class);
			startActivity(i);
			return true;
		
		case R.id.rate:
			
			Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
			Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
			goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			
			try {
			  startActivity(goToMarket);
			} catch (ActivityNotFoundException e) {
			  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
			}
			
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	
	
}









