/**
 * The default package for all Encryption Classes
 */
package com.incomm.cclp.fsapi.token.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.crypto.codec.Hex;

import com.incomm.cclp.fsapi.token.security.exception.APISecurityException;


/**
 * A Simple class to perform AES 256 Encryption and Decryption functions using
 * an AES 256-bit Key
 * 
 * @author Vishy Iyer (viyer@incomm.com)
 * 
 */
@SuppressWarnings("unused")
public class AES {

	/**
	 * The AES Algorithm
	 */
	private static final String ALGORITHM = "AES";

	/**
	 * The Key Strength in bits. For any key strength above 128-bits, ensure
	 * that the contents of the UnlimitedJCEPolicy.zip are extracted to the
	 * JRE/lib/security folder
	 */
	private static final int KEY_STRENGTH = 256;

	/**
	 * All the hexadecimal digits
	 */
	private static String digits = "0123456789ABCDEF";

	/**
	 * This function returns a Hex String representation of an encoded AES 256
	 * Bit Key
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
	public static String encrypt(String plainText, String keyString)
			throws APISecurityException {

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
	public static String decrypt(String encryptedText, String keyString)
			throws APISecurityException {

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
	 * @param s
	 *            - The input Hex String to be converted to a byte array
	 * @return - The resultant byte array
	 */
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Helper function to convert a byte array to a Hex String
	 * 
	 * @param ba
	 *            - The input byte array to be converted to a hex string
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
	 * Padding required to ensure multiplicity of 8 Typically this function is
	 * not required; but may be needed to pad strings in case of byte-endian
	 * issues.
	 * 
	 * @param text
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static String padString(String text, int size) {
		String myText = text;

		int padSize = size - (text.length() % size);

		if (padSize < size)
			for (int i = 0; i < padSize; i++)
				myText += " ";

		return myText;
	}

	/**
	 * The one and only main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws APISecurityException {

		// Generate the Hex representation of a 256-bit AES Key
		String aesKey = generateAESKey();
		//# AES Key
		String fsapiKey = "478CCB8898D9F3A58BCD420677D8E27EC4C380B202A01B604E7389BDF1AA0D0D";
		System.out.println("Generated AES Key [" + aesKey + "]");

		// The plain text message to decrypt
		String plainText = "mode=encrypt&correlationID=1221212";

		String encryptedText = encrypt(plainText, fsapiKey);

		System.out.println("Encrypted Text : [" + encryptedText + "]");
		/*//System.out.println("Decrypted Text : ["
				+ decrypt("84305e4696ab0055b3a454a142e93c37ef10d0222da8b64b124a80035618638182d140e61c62d64a2dfc798828ddbd4c2ffd7242d35b18cf2f0ad483eda79379c8b55c53ceb601689a5815359aa4dfa6d59f9379f1d0df8c88418cde71b93277c38952a438f58d229bb9c6fb0af441689fd0e1e85cccbb90ad38b6636e4690a152d5afa992264306ef7e21730eeadf52997f3eb32f302f850cf8544de300f9c345a0b8f92a74615aed6d35dd399ba34d", 
						"478CCB8898D9F3A58BCD420677D8E27EC4C380B202A01B604E7389BDF1AA0D0D") + "]");*/

	}

}
