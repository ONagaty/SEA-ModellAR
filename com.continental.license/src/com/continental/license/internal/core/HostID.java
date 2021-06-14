package com.continental.license.internal.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.crypto.IllegalBlockSizeException;

import com.continental.license.internal.CessarPluginActivator;

public class HostID
{
	public static final String NEW_LICENSE_PREFIX = "#";

	private static class DesPrivateEncrypter extends DesDecrypter
	{

		public DesPrivateEncrypter(String passPhrase) throws javax.crypto.NoSuchPaddingException,
			java.security.NoSuchAlgorithmException, java.security.InvalidKeyException,
			java.security.InvalidAlgorithmParameterException,
			java.security.spec.InvalidKeySpecException
		{

			super(passPhrase);
		}

		public String encrypt(String str) throws javax.crypto.BadPaddingException,
			IllegalBlockSizeException, UnsupportedEncodingException
		{

			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return Base64.encodeBytes(enc, Base64.DONT_BREAK_LINES);
		}
	}

	public static String encodeIntValue(int aHostID)
	// converts 10-bit Host ID value to AA9999 format
	{

		if ((aHostID < 1) || (aHostID > 1023))
			return "AA0000"; // mark as invalid

		int lo4 = aHostID & 15;
		int hi4 = (aHostID >>> 6) & 15;
		int lo3 = aHostID & 7;
		int lm3 = (aHostID >>> 3) & 7;
		int hm3 = (aHostID >>> 4) & 7;
		int hi3 = (aHostID >>> 7) & 7;

		String string = "";

		string += (char) ('A' + lo4);
		string += (char) ('Z' - hi4);
		string += (char) ('9' - lo3);
		string += (char) ('0' + lm3);
		string += (char) ('0' + hm3);
		string += (char) ('9' - hi3);

		return string;
	}

	public static int decodeIntValue(String aString) throws NumberFormatException
	// converts string in AA9999 format to 10-bit Host ID value
	// returns 1..1023 if successful, throws an exception if string is malformed
	{

		int lo4, hi4, lo3, lm3, hm3, hi3, val;

		if (aString.length() != 6)
			throw new NumberFormatException("illegal HostID");

		lo4 = aString.charAt(0) - 'A';
		hi4 = 'Z' - aString.charAt(1);
		lo3 = '9' - aString.charAt(2);
		lm3 = aString.charAt(3) - '0';
		hm3 = aString.charAt(4) - '0';
		hi3 = '9' - aString.charAt(5);

		val = lo3 + (lm3 << 3) + (hi4 << 6);

		if ((lo4 != (val & 15)) || (hm3 != ((val >>> 4) & 7)) || (hi3 != ((val >>> 7) & 7)))
			throw new NumberFormatException("illegal HostID");

		return val;
	}

	public final static int BY_DATE_OF_JAVA_HOME = 0;

	public final static int BY_PATH_OF_JAVA_HOME = 1;

	public final static int BY_DATE_OF_USER_DIR = 2;

	public final static int BY_PATH_OF_USER_DIR = 3;

	public final static int BY_DATE_OF_USER_HOME = 4;

	public final static int BY_PATH_OF_USER_HOME = 5;

	public final static int BY_MAC_ADDR = 6;

	static String getFullID()
	{
		StringBuffer result = new StringBuffer();

		// Add a prefix to identify the new hostid based on the BIOSID
		result.append(NEW_LICENSE_PREFIX);
		
		// Get hostID
		int hid = obtainHostID();
		result.append(encodeIntValue(hid));

		// Get os.name, os.arch and os.version.
		String archParam = "a=" + System.getProperty("os.arch") + "n="
			+ System.getProperty("os.name") + "v=" + System.getProperty("os.version");

		try
		{
			DesPrivateEncrypter dpe = new DesPrivateEncrypter("archParams");
			String enc = dpe.encrypt(archParam);
			result.append("-").append(enc);
		}
		catch (Exception ex)
		{}

		return result.toString();
	}

	/**
	 * Retrieve the HostID. 
	 * - Linux : based on mac adress
	 * - Windows : based on the bios id 
	 * 
	 * @return int, encoded hostID
	 */
	static int obtainHostID()
	{
		int result = -1;
		try
		{
			if (isLinuxPlatform())
			{
				result = getMacAddress();
			}
			else
			{
				result = getEncodedBIOSId();
			}
		}
		catch (Exception e)
		{
			CessarPluginActivator.log(e);
		}

		return result;
	}

	/**
	 * Retrieve the encoded mac adress
	 * 
	 * @return int, encoded mac address
	 * 
	 * @throws Exception
	 */
	private static int getMacAddress() throws Exception
	{
		int result;
		MacAddress mac = new MacAddress();
		int[] values = HashString.hashString(mac.getMacAddress());
		result = ((values[0] + values[1] + values[2]) % 1023) + 1;
		return result;
	}

	private static boolean isLinuxPlatform()
	{
		String osName = System.getProperty("os.name");
		return "Linux".equals(osName);
	}
	
	/**
	 * Retrieve the encoded BIOSId
	 * 
	 * @return int, encoded BIOSId
	 * 
	 * @throws Exception
	 */
	private static int getEncodedBIOSId() throws Exception
	{
		int result;
		int[] values = HashString.hashString(getBIOSId());
		result = ((values[0] + values[1] + values[2]) % 1023) + 1;
		return result;
	}
	
	/**
	 * Obtain old hostID based on MacAdress
	 * 
	 * @return int 
	 */
	static int obtainOldHostID()
	{
		int result = -1;
		try
		{
			result = getMacAddress();
		}
		catch (Exception e)
		{
			CessarPluginActivator.log(e);
		}

		return result;
	}

	/**
	 * Retrieve the host BIOS ID generating and executing on the fly a VB script. 
	 * 
	 * @return String, BIOS identifier
	 * @throws Exception
	 */
	public static String getBIOSId() throws Exception
	{
		String result = "";

		File file = File.createTempFile("temp", ".vbs");
		file.deleteOnExit();
		FileWriter fw = new java.io.FileWriter(file);

		String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
			+ "Set colItems = objWMIService.ExecQuery _ \n"
			+ "   (\"Select * from Win32_BIOS\") \n" + "For Each objItem in colItems \n"
			+ "    Wscript.Echo objItem.SerialNumber \n"
			+ "    exit for  ' do the first cpu only! \n" + "Next \n";

		fw.write(vbs);
		fw.close();
		Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = input.readLine()) != null)
		{
			result += line;
		}
		input.close();

		return result.trim();
	}
}