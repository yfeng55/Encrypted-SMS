package com.yfeng.lockedsms;

import android.util.Log;


public class PhoneNumUtil {
	
	public static boolean isValidNumber(String phoneNum){
		
		//validate phone numbers of format "1234567890"
        if (phoneNum.matches("\\d{10}")) return true;
        
        //validating phone number with -, . or spaces
        else if(phoneNum.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        
        //validating phone number with extension length from 3 to 5
        else if(phoneNum.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        
        //validating phone number where area code is in braces ()
        else if(phoneNum.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        
        //return false if nothing matches the input
        else return false;
		
	}
	
	public static String formatPhoneNumber(String phoneNum){
		String formattednumber = phoneNum.replaceAll("[^\\d]", "");
		formattednumber = String.valueOf(formattednumber).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
		return formattednumber;
	}
	
	
	
}
