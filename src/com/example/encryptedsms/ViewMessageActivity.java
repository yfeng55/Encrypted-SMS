package com.example.encryptedsms;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class ViewMessageActivity extends Activity{

	private String decryptedmessage;
	private TextView messagearea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewmessage);
		
		messagearea = (TextView) findViewById(R.id.tv_decryptedmsg);
		
		decryptedmessage = getIntent().getStringExtra("decryptedmsg");
		
		messagearea.setText(decryptedmessage);
		
	}

	
	
	
}
