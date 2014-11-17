package com.yfeng.lockedsms;
import java.util.ArrayList;

import com.yfeng.util.EncryptUtil;
import com.yfeng.util.PhoneNumUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SendMessageActivity extends Activity {

	private EditText msgContent;
	private Button send;
	private SharedPreferences prefs;
	private String phonenumber;
	private String contactname; 


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmessage);

		prefs = this.getSharedPreferences("com.example.encryptedsms", 0);

		msgContent = (EditText) findViewById(R.id.msgContent);
		send = (Button) findViewById(R.id.Send);

		
		//get phone number and contactname from Intent extras
		phonenumber = getIntent().getStringExtra("phonenumber");
		contactname = getIntent().getStringExtra("contactname");
		
		//update actionbar title with the name or number of the recipient
		ActionBar ab = getActionBar();
		if(contactname.isEmpty()){
			ab.setTitle(PhoneNumUtil.formatPhoneNumber(phonenumber));
		}else{
			ab.setTitle(contactname);
		}
		
		//configure actionbar with a backbutton
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		
		
		// encrypt the message and send when click Send button
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				//retrieve secretkey and convert to char[] array
				char[] password = prefs.getString("secretkey", "No Secret Key").toCharArray();
				
				//retrieve message content from user input
				String msgContentString = msgContent.getText().toString();

				// check for the validity of the user input; key length should be 16 characters as defined by AES-128-bit
				if(phonenumber.length() > 0 && msgContentString.length() > 0 && password.length > 0){

					//generate a salt
					byte[] salt = EncryptUtil.generateNewSalt();
					
					// encrypt the message
					EncryptedMessage encryptedMsg = null;
					try {
						encryptedMsg = EncryptUtil.encryptSMS(msgContentString, password, salt);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// convert the byte arrays to hexadecimal (that represents the byte array's memory address) in order to transmit
					String msgHex = EncryptUtil.byte2hex(encryptedMsg.getCipherText());
					String ivHex = EncryptUtil.byte2hex(encryptedMsg.getIV());
					String saltHex = EncryptUtil.byte2hex(salt);

					// send the message through SMS
					sendSMS(v.getContext(), phonenumber, msgHex, saltHex, ivHex);

					// finish
					finish();

				}else{
					Toast.makeText(
							getBaseContext(),
							"Please enter phone number, secret key and the message. Secret key must be 16 characters!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	private static void sendSMS(Context context, String recNumString, String encryptedMsg, String salt, String iv) {
		try{
			
			String SENT = "SMS_SENT";

			Intent sentIntent = new Intent(SENT);
			sentIntent.putExtra("salt", salt);
			sentIntent.putExtra("iv", iv);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
			
			// get a SmsManager
			SmsManager smsManager = SmsManager.getDefault();

			// divide the message into 160-character pieces and store in an arraylist
			ArrayList<String> parts = smsManager.divideMessage(encryptedMsg);
			ArrayList<PendingIntent> sentPendingIntents = null;
			sentPendingIntents.add(sentPI);
			
			// send the message as an arraylist; the parts of the message will be recombined on the receiving end and displayed as a single message
			smsManager.sendMultipartTextMessage(recNumString, null, parts, sentPendingIntents, null);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
}







