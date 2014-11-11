package com.example.encryptedsms;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		numberentry = (EditText) findViewById(R.id.et_numberentry);
		
		
		FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_content);

        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content, new ContactListFragment());
            ft.commit();
        }
        
        
        numberentry.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    
                	Log.i("EEE","Enter pressed");
                	//get the EditText values
                    String phonenumber = numberentry.getText().toString();
                    
                    if(PhoneNumUtil.isValidNumber(phonenumber)==true){
                    	//start SendMessageActivity and pass it the 
                        Intent i = new Intent(v.getContext(), SendMessageActivity.class);
                        i.putExtra("phonenumber", phonenumber);
            			startActivity(i);
                    }else{
                    	Toast.makeText(
    							getBaseContext(),
    							"Please enter a valid phone number",
    							Toast.LENGTH_SHORT).show();
                    }
                    
                }    
                return false;
            }
        });
        
	}
	
	

	
}
