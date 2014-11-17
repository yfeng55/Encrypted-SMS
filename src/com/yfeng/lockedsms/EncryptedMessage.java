package com.yfeng.lockedsms;

public class EncryptedMessage {
	
	private byte[] iv;
    private byte[] ciphertext;
	
	
	public EncryptedMessage(byte[] iv, byte[] ciphertext) {
        this.iv = iv;
        this.ciphertext = ciphertext;
    }
	
	public EncryptedMessage(){
		
	}
	
	
	public byte[] getIV(){
		return this.iv;
	}
	
	public byte[] getCipherText(){
		return this.ciphertext;
	}
	
	public void setIV(byte[] iv){
		this.iv = iv;
	}
	
	public void setCipherText(byte[] ciphertext){
		this.ciphertext = ciphertext;
	}
	
	
}
