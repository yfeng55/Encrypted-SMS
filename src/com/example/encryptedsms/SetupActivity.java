package com.example.encryptedsms;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class SetupActivity extends Activity{
	
	private EditText secretkey_field;
	private SharedPreferences prefs;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usersetup);
		
		//obtain shared preferences
		prefs = this.getSharedPreferences("com.example.encryptedsms", 0);
		
		secretkey_field = (EditText) findViewById(R.id.et_secretKey);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	
	public void submitKey(View v){
		String secretkey = secretkey_field.getText().toString();
		
		//create a new editor for the prefs object
		editor = prefs.edit();
		editor.putString("secretkey", secretkey);
		editor.commit();
		
		//after registering, start the next activity
		Intent i = new Intent(this, SendMessageActivity.class);
		startActivity(i);

		
	}
	
}