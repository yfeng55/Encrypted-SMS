package com.yfeng.lockedsms;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;


public class SendMessageActivity extends Activity {
	
	private static final int CAMERA_REQUEST = 1888; 
	
	private EditText msgContent;
	private Button send;
	private Button addphoto;
	private SharedPreferences prefs;
	private String phonenumber;
	private String contactname; 
	private ImageView previewImage;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		setContentView(R.layout.sendmessage);

		prefs = this.getSharedPreferences("com.example.encryptedsms", 0);

		msgContent = (EditText) findViewById(R.id.msgContent);
		send = (Button) findViewById(R.id.Send);
		addphoto = (Button) findViewById(R.id.AddPhoto);
		previewImage = (ImageView) findViewById(R.id.photoPreview);

		
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
		
		// launch camera stuff
		addphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	Log.i("EEE", "clicked the button");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
		
		// encrypt the message and send when click Send button
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				//retrieve the secretkey from sharedprefs
				String secretKeyString = prefs.getString("secretkey", "No Secret Key");
				//Log.i("EEE", secretKeyString);
				
				String msgContentString = msgContent.getText().toString();

				// check for the validity of the user input
				if(phonenumber.length() > 0 && msgContentString.length() > 0 && secretKeyString.length() > 0) {

					// encrypt the message and store in a byte array
					byte[] encryptedMsg = encryptSMS(secretKeyString, msgContentString);

					// convert the byte array to hexadecimal (that represents the byte array's memory address) in order to transmit
					String msgString = byte2hex(encryptedMsg);

					// send the message through SMS
					sendSMS(phonenumber, msgString);
					
					Toast.makeText(
							getBaseContext(),
							"Message Sent",
							Toast.LENGTH_SHORT).show();
					
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
	
	
	//display a preview of the image
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            previewImage.setImageBitmap(photo);
        }  
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
	
	
	
	public static void sendSMS(String recNumString, String encryptedMsg) {
		try{
			// get a SmsManager
			SmsManager smsManager = SmsManager.getDefault();

			// divide the message into 160-character pieces and store in an arraylist
			ArrayList<String> parts = smsManager.divideMessage(encryptedMsg);
			
			// send the message as an arraylist; the parts of the message will be recombined on the receiving end and displayed as a single message
			smsManager.sendMultipartTextMessage(recNumString, null, parts, null, null);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// convert a byte array to hexadecimal
	public static String byte2hex(byte[] b){
		
		String hexstring = "";
		String stmp = "";
		
		for (int n = 0; n < b.length; n++){
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1){
				hexstring += ("0" + stmp);
			}
			else{
				hexstring += stmp;
			}
		}
		return hexstring.toUpperCase();
	}

	// encryption function
	public static byte[] encryptSMS(String secretKeyString, String msgContentString) {
		try {
			byte[] returnArray;

			// generate AES secret key from user input
			SecretKeySpec key = generateKey(secretKeyString);

			// specify the cipher algorithm using AES
			Cipher c = Cipher.getInstance("AES");

			// specify the encryption mode
			c.init(Cipher.ENCRYPT_MODE, key);

			// encrypt
			returnArray = c.doFinal(msgContentString.getBytes());

			return returnArray;

		} catch (Exception e) {
			e.printStackTrace();
			byte[] returnArray = null;
			return returnArray;
		}
	}

	private static SecretKeySpec generateKey(String secretKeyString) throws Exception {
		
		byte[] key = (secretKeyString).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit
		
		// generate secret key from string
		SecretKeySpec secretkey = new SecretKeySpec(key, "AES");
		return secretkey;
	}

	
}







