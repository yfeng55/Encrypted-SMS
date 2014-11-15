package com.yfeng.lockedsms;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SetupActivity extends Activity{
	
	private EditText secretkey_field;
	private TextView existingkeylabel;
	private TextView existingkey;
	private SharedPreferences prefs;
	private Editor editor;
	private String secretkey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usersetup);
		prefs = this.getSharedPreferences("com.example.encryptedsms", 0);
		
		secretkey_field = (EditText) findViewById(R.id.et_secretKey);
		existingkeylabel = (TextView) findViewById(R.id.tv_existingkeylabel);
		existingkey = (TextView) findViewById(R.id.tv_existingkey);
		
		//set actionbar title
		ActionBar ab = getActionBar();
		ab.setTitle("Set Secret Key");
		
		
		//display the existing key if it exists
		secretkey = prefs.getString("secretkey", "");
		if (!secretkey.isEmpty()) {
			existingkeylabel.setText("Existing Key:");
			existingkey.setText(secretkey);
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	
	public void submitKey(View v){
		String secretkey = secretkey_field.getText().toString();
		
		if(isValidKey(secretkey) == true){
			//create a new editor for the prefs object
			editor = prefs.edit();
			editor.putString("secretkey", secretkey);
			editor.commit();
			
			//after registering, start the next activity
			Intent i = new Intent(this, ContactListActivity.class);
			startActivity(i);
		}else{
			Log.i("SetupActivity", "invalid secretkey entered");
			Toast.makeText(
					getBaseContext(),
					"Please enter a valid secretkey. A secret key must be 16 characters with no whitespaces.",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	private boolean isValidKey(String secretkey){
		
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(secretkey);
		boolean hasWhitespace = matcher.find();
		
		//validate that the secretkey is 16 characters with no spaces
		if(secretkey.length()==16 && hasWhitespace == false){
			return true;
		}else{
			return false;
		}
		
	}
	
	
}














