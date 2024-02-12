package com.user.util;
import java.security.SecureRandom;
import java.util.Random;
public class PasswordGenerator {
	 private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
	    private static final String DIGITS = "0123456789";
	    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[{]};:'\",<.>/?";
	  private static final String ALL_CHARACTERS =
	            LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + DIGITS + SPECIAL_CHARACTERS;
	  private static final Random random = new SecureRandom();
	public static String password() {
		 StringBuilder password = new StringBuilder();

	        for (int i = 0; i < 6; i++) {
	            int randomIndex = random.nextInt(ALL_CHARACTERS.length());
	            char randomChar = ALL_CHARACTERS.charAt(randomIndex);
	            password.append(randomChar);
	        }

	        return password.toString();
	}

}
