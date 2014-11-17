package com.yfeng.util;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.yfeng.lockedsms.EncryptedMessage;

public class EncryptUtil {

	// convert a byte array to hexadecimal
	public static String byte2hex(byte[] b) {

		String hexstring = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hexstring += ("0" + stmp);
			} else {
				hexstring += stmp;
			}
		}
		return hexstring.toUpperCase();
	}

	// encryption function
	public static EncryptedMessage encryptSMS(String message, char[] password,
			byte[] salt) throws Exception {

		SecretKey secret = generateKey(password, salt);

		// encrypt the message
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();

		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] ciphertext = cipher.doFinal(message.getBytes("UTF-8"));

		// set values in EncryptedMessage object and return
		EncryptedMessage msg = new EncryptedMessage();
		msg.setCipherText(ciphertext);
		msg.setIV(iv);

		return msg;

	}

	public static SecretKey generateKey(char[] password, byte[] salt)
			throws Exception {

		// generate secret key from password and salt
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);

		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		return secret;
	}

	public static byte[] generateNewSalt() {
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[8];
		sr.nextBytes(salt);

		return salt;
	}

}








