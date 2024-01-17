package com.user.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class Encryptor {

	public static String encrypt(String text, String key) throws Exception {

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, Encryptor.convertStringToKey(key));

		byte[] encryptedTextBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedTextBytes);
	}

	public static String decrypt(String encryptedText, String key) throws Exception {

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, Encryptor.convertStringToKey(key));

		byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

		return new String(decryptedTextBytes, StandardCharsets.UTF_8);
	}

	private static SecretKey convertStringToKey(String keyAsString) {
		byte[] keyBytes = Base64.getDecoder().decode(keyAsString);
		return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
	}

	public static String generateSecretKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		return convertKeyToString(keyGenerator.generateKey());
	}

	private static String convertKeyToString(SecretKey key) {
		byte[] keyBytes = key.getEncoded();
		return Base64.getEncoder().encodeToString(keyBytes);
	}

}
