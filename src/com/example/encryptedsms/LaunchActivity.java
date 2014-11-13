package com.example.encryptedsms;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class LaunchActivity extends Activity{

	private SharedPreferences prefs;
	private String secretkey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// obtain shared preferences
		prefs = this.getSharedPreferences("com.example.encryptedsms", 0);
		secretkey = prefs.getString("secretkey", "");

		if (!secretkey.isEmpty()) {
			Intent i = new Intent(this, ContactListActivity.class);
			startActivity(i);
		}else{
			Intent i = new Intent(this, SetupActivity.class);
			startActivity(i);
		}
				
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	
}
