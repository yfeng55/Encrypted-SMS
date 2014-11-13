package com.yfeng.lockedsms;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.yfeng.lockedsms.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayMessageActivity extends Activity{

	EditText secretKey;
	TextView senderNum;
	TextView encryptedMsg;
	TextView decryptedMsg;
	Button submit;
	Button cancel;
	String originNum = "";
	String msgContent = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receivemessage);

		ActionBar ab = getActionBar();
		ab.setTitle("New Message");
		
		senderNum = (TextView) findViewById(R.id.senderNum);
//		encryptedMsg = (TextView) findViewById(R.id.encryptedMsg);
//		decryptedMsg = (TextView) findViewById(R.id.decryptedMsg);
		secretKey = (EditText) findViewById(R.id.secretKey);
		submit = (Button) findViewById(R.id.submit);
//		cancel = (Button) findViewById(R.id.cancel);

		// get the Intent extra
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			// get the sender phone number from extra
			originNum = extras.getString("originNum");

			// get the encrypted message body from extra
			msgContent = extras.getString("msgContent");

			// set the text fields in the UI
			senderNum.setText(originNum);
//			encryptedMsg.setText(msgContent);
		} else {

			// if the Intent is null, there should be something wrong
			Toast.makeText(getBaseContext(), "Error Occurs!",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		
		// when click on the submit button decrypt the message body
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// user input the AES secret key
				String secretKeyString = secretKey.getText().toString();

				// key length should be 16 characters as defined by AES-128-bit
				if (secretKeyString.length() > 0
						&& secretKeyString.length() == 16) {

					try {
						// convert the encrypted String message body to a byte
						// array
						byte[] msg = hex2byte(msgContent.getBytes());

						// decrypt the byte array
						byte[] result = decryptSMS(secretKey.getText()
								.toString(), msg);

						// set the text view for the decrypted message
						// decryptedMsg.setText(new String(result));
						
						String decryptedmsg = new String(result);
						
						Intent i = new Intent(v.getContext(), ViewMessageActivity.class);
						i.putExtra("decryptedmsg", decryptedmsg);
						
						startActivity(i);

					}catch(Exception e){

						// in the case of message corrupted or invalid key
						// decryption cannot be carried out
						Toast.makeText(getBaseContext(),
								"Invalid password",
								Toast.LENGTH_SHORT).show();
					}
					
				} else {
					Toast.makeText(getBaseContext(),
							"You must enter a 16-character password",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		
	}

	
	// utility function: convert hex array to byte array
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("hello");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	// decryption function
	public static byte[] decryptSMS(String secretKeyString, byte[] encryptedMsg)
			throws Exception {

		// generate AES secret key from the user input secret key
		SecretKeySpec key = generateKey(secretKeyString);

		// get the cipher algorithm for AES
		Cipher c = Cipher.getInstance("AES");

		// specify the decryption mode
		c.init(Cipher.DECRYPT_MODE, (java.security.Key) key);

		// decrypt the message
		byte[] decValue = c.doFinal(encryptedMsg);

		return decValue;
	}

	private static SecretKeySpec generateKey(String secretKeyString) throws Exception {

		// generate AES secret key from a String
		SecretKeySpec key = new SecretKeySpec(secretKeyString.getBytes(), "AES");
		return key;
	}
	
	

}










