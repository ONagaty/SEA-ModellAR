package com.continental.license.internal.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

// Inner class to deal with streams.
class StreamGobbler extends Thread
{
	private InputStream is;
	private ByteArrayOutputStream os;

	public StreamGobbler(InputStream is)
	{

		this.is = is;
		this.os = new ByteArrayOutputStream();
	}

	public String getOutputString()
	{

		return os.toString();
	}

	public void run()
	{

		try
		{
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if (pw != null)
					pw.println(line);
			}

			if (pw != null)
				pw.flush();

		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
