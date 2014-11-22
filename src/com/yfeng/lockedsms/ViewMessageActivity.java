package com.yfeng.lockedsms;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ViewMessageActivity extends Activity{

	private String decryptedmessage;
	private String originnum;
	private long timestamp;
	
	private TextView messagearea;
	private TextView fromlabel;
	private TextView timelabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewmessage);
		
		//set View objects
		messagearea = (TextView) findViewById(R.id.tv_decryptedmsg);
		fromlabel = (TextView) findViewById(R.id.tv_fromlabel);
		timelabel = (TextView) findViewById(R.id.tv_timelabel);
		
		//get extras from the Intent
		decryptedmessage = getIntent().getStringExtra("decryptedmsg");
		originnum = getIntent().getStringExtra("fromnumber");
		timestamp = getIntent().getLongExtra("timestamp", 333);
		
		//set date
		Date msgdate = new Date(timestamp);
		Format formatter = new SimpleDateFormat("MM-dd-yyyy  hh:mm a");
		String datestring = formatter.format(msgdate);
		
		//set text of the Views
		messagearea.setText(decryptedmessage);
		fromlabel.setText(originnum);
		timelabel.setText(datestring);
		
	}
	
	
	
	public void closeMessage(View v){
		Intent i = new Intent(this, ContactListActivity.class);
		
		Toast.makeText(
				getBaseContext(),
				"Done with message",
				Toast.LENGTH_SHORT).show();
		
		finish();
		startActivity(i);
	}
	
	
}
