package com.configuration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class EncryptionUtil {

	static Cipher cipher;
	static SecretKey secretKey;
	

	
	
	public static String encrypt(String plainText, String password) throws Exception {
		byte[] preSharedKey = password.getBytes();  
		byte[] data = plainText.getBytes("UTF-8");
		SecretKey aesKey = new SecretKeySpec(preSharedKey, "AES");  			
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(preSharedKey)); 
		byte[] output = cipher.doFinal(data);  		
		String encryptedString = new String(Hex.encodeHex(output));
		return encryptedString.toUpperCase();
	}
	
	
	
	
	
	/*public static String encrypt(String plainText, SecretKey secretKey)
			throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}
	
	public static String decrypt(String encryptedText, SecretKey secretKey)
			throws Exception {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
	
	public static SecretKey getPassKey() {
		
		KeyGenerator keyGenerator;
		
		try {
			if(secretKey == null) {
				keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(128);
				secretKey = keyGenerator.generateKey();
				return secretKey;
			} else {
				return secretKey;
			}
			
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		
		return secretKey;
	}*/


		
}
