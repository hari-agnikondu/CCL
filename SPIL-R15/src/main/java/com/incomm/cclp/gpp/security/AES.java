/**
 * The default package for all Encryption Classes
 */
package com.incomm.cclp.gpp.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Hex;

/**
 * A Simple class to perform AES 256 Encryption and Decryption functions using an AES 256-bit Key
 * 
 * @author Vishy Iyer (viyer@incomm.com)
 * 
 */
@SuppressWarnings("unused")
public class AES {

	private AES() {
		// constructor
	}

	/**
	 * The AES Algorithm
	 */
	private static final String ALGORITHM = "AES";

	/**
	 * The Key Strength in bits. For any key strength above 128-bits, ensure that the contents of the
	 * UnlimitedJCEPolicy.zip are extracted to the JRE/lib/security folder
	 */
	private static final int KEY_STRENGTH = 256;

	/**
	 * All the hexadecimal digits
	 */
	private static String digits = "0123456789ABCDEF";

	private static String keyString = "478CCB8898D9F3A58BCD420677D8E27EC4C380B202A01B604E7389BDF1AA0D0D";

	/**
	 * This function returns a Hex String representation of an encoded AES 256 Bit Key
	 * 
	 * @return
	 * @throws APISecurityException
	 */
	public static String generateAESKey() throws APISecurityException {

		String aesKey = null;

		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(KEY_STRENGTH); // for example
			SecretKey secretKey = keyGen.generateKey();

			aesKey = byteArrayToHexString(secretKey.getEncoded());

		} catch (Exception exp) {
			throw new APISecurityException(exp);
		}

		return aesKey;

	}

	/**
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 */
	public static String encrypt(String plainText, String keyString) throws APISecurityException {

		String encryptedText = null;
		Key key = null;

		try {

			byte[] encodedKey = hexStringToByteArray(keyString);

			key = new SecretKeySpec(encodedKey, ALGORITHM);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));

			encryptedText = new String(Hex.encode(ciphertext));
		} catch (Exception exp) {
			throw new APISecurityException(exp);
		}

		return encryptedText;

	}

	/**
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 */
	public static String decrypt(String encryptedText, String keyString) throws APISecurityException {

		String plainText = null;
		Key key = null;

		try {

			byte[] encodedKey = hexStringToByteArray(keyString);

			key = new SecretKeySpec(encodedKey, ALGORITHM);

			Cipher chiper = Cipher.getInstance(ALGORITHM);
			chiper.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedBytes = hexStringToByteArray(encryptedText);
			byte[] decValue = chiper.doFinal(encryptedBytes);
			plainText = new String(decValue);

		} catch (Exception exp) {
			throw new APISecurityException(exp);
		}

		return plainText;

	}

	/**
	 * Helper function to convert a hex string to a byte array
	 * 
	 * @param s - The input Hex String to be converted to a byte array
	 * @return - The resultant byte array
	 */
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Helper function to convert a byte array to a Hex String
	 * 
	 * @param ba - The input byte array to be converted to a hex string
	 * @return - The resultant Hex String
	 */
	private static String byteArrayToHexString(byte[] ba) {
		StringBuilder sb = new StringBuilder();
		int off = 0;
		int len = ba.length;

		for (int i = off; i < off + len; i++) {
			int b = (int) ba[i];
			char c = digits.charAt((b >> 4) & 0xf);
			sb.append(c);
			c = digits.charAt(b & 0xf);
			sb.append((char) c);
		}
		return sb.toString();

	}

	/**
	 * Padding required to ensure multiplicity of 8 Typically this function is not required; but may be needed to pad
	 * strings in case of byte-endian issues.
	 * 
	 * @param text
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static String padString(String text, int size) throws APISecurityException {
		StringBuilder myText = new StringBuilder(text);

		int padSize = size - (text.length() % size);

		if (padSize < size)
			for (int i = 0; i < padSize; i++)
				myText.append(" ");

		return String.valueOf(myText);
	}

	/*
	 * public static void main(String args[]) throws APISecurityException { String plainText = "mpGYxjgKbGDcUt69";
	 * 
	 * System.out.println(encrypt(plainText,keyString));
	 * System.out.println(decrypt(encrypt(plainText,keyString),keyString)); }
	 */

}
