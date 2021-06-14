package eu.cessar.ct.core.security.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Cette classe intercepte les numero de mac adresse */
class MacStreamFilter extends Thread
{
	private InputStream is;

	private String _macAddress = null;

	public MacStreamFilter(InputStream is)
	{

		this.is = is;
	}

	public String getMacAddress()
	{

		return _macAddress;
	}

	/**
	 * This method search in line, the string : 99:99:99:99:99:99 or 99-99-99-99-99-99 where 9 is
	 * any digit.
	 * 
	 * @param line
	 *        the string to analyze.
	 */
	private String _extractMacAddress(String line)
	{

		int nbDigit = 0;
		boolean mustFindDigit = true;
		boolean mustFindSep = false;
		StringBuffer macAdr = new StringBuffer();
		int macAdrLenExpected = 17;

		char[] content = line.toCharArray();

		for (int i = 0; (i < content.length) && (macAdr.length() != macAdrLenExpected); i++)
		{
			char c = Character.toUpperCase(content[i]);
			if (mustFindDigit)
			{
				if (Character.digit(c, 16) != -1)
				{
					// This is a digit.
					macAdr.append(c);
					nbDigit++;
					if (nbDigit == 2)
					{
						// must find now a separator, or mac adress is found.
						nbDigit = 0;
						mustFindDigit = false;
						mustFindSep = true;
					}
				}
				else
				{
					// This is not a digit... reset all
					nbDigit = 0;
					macAdr.delete(0, macAdr.length());
				}
			}
			else if (mustFindSep)
			{
				if ((c != '-') && (c != ':'))
				{
					// separator is not found
					// Reset all.

					macAdr.delete(0, macAdr.length());
				}
				else
				{
					macAdr.append(c);
				}

				// Anyway, digit must be found now.

				nbDigit = 0;
				mustFindDigit = true;
				mustFindSep = false;

			}
		}

		if (macAdr.length() == macAdrLenExpected)
			return macAdr.toString();

		return null;

	}

	public void run()
	{

		try
		{
			// PrintWriter pw = null;
			// if (os != null)
			// pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			String currentMacAdr = null;
			while ((line = br.readLine()) != null)
			{
				// Try to extract mac line.
				currentMacAdr = _extractMacAddress(line);
				// If a mac adress was found, keep the lowest one.
				if (currentMacAdr != null)
				{
					// Is this adress lowest than _macAdress...
					if ((_macAddress == null)
						|| (currentMacAdr.compareToIgnoreCase(_macAddress) < 0))
						_macAddress = currentMacAdr;
				}
			}

		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
