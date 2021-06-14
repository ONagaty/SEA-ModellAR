package com.continental.license.internal.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Cette classe permet de chiffrer et déchiffrer une chaine de caractères à l'aide d'un mot de
 * passe. Cette classe s'utilise ainsi :
 * 
 * <pre>
 * SecretKey key = KeyGenerator.getInstance(&quot;DES&quot;).generateKey();
 * 
 * // Create encrypter/decrypter class
 * DesEncrypter encrypter = new DesEncrypter(key);
 * 
 * // Encrypt
 * String encrypted = encrypter.encrypt(RULE_FILENAME);
 * 
 * //...
 * 
 * // Decrypt
 * String decrypted = encrypter.decrypt(encrypted);
 * </pre>
 * 
 * @author sebz
 * @version $Id: DesDecrypter.java,v 1.1 2005-08-05 09:49:59 olivier Exp $
 */
public class DesDecrypter
{
	protected Cipher ecipher;
	protected Cipher dcipher;

	// 8-byte Salt
	protected byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
		(byte) 0x35, (byte) 0xE3, (byte) 0x03};

	// Iteration count
	int iterationCount = 19;

	public DesDecrypter(String passPhrase) throws javax.crypto.NoSuchPaddingException,
		java.security.NoSuchAlgorithmException, java.security.InvalidKeyException,
		java.security.InvalidAlgorithmParameterException,
		java.security.spec.InvalidKeySpecException
	{

		// Create the key
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

		ecipher = Cipher.getInstance(key.getAlgorithm());
		dcipher = Cipher.getInstance(key.getAlgorithm());

		// Prepare the parameter to the ciphers
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

		// Create the ciphers
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}

	public String decrypt(String str) throws javax.crypto.BadPaddingException,
		IllegalBlockSizeException, UnsupportedEncodingException, IOException
	{

		// Decode base64 to get bytes
		byte[] dec = Base64.decode(str, Base64.DONT_BREAK_LINES);

		// Decrypt
		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, "UTF8");
	}
}