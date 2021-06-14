package com.continental.license.internal.core;

import java.io.ByteArrayOutputStream;

class MacAddress
{

	/** The previous computed mac address */
	private static String _macAddress = null;

	private String _error = null; // The error obtained in stream

	/** A private method to get the command to launch to get a buffer containing mac address */
	private static String[] getMacCommand()
	{

		String[] cmd = null;
		String osName = System.getProperty("os.name");
		if (osName.equals("Linux"))
		{
			cmd = new String[] {"/sbin/ifconfig"};

		}
		else
		{
			cmd = new String[3];

			if (osName.equals("Windows 95"))
			{
				cmd[0] = "command.com";
				cmd[1] = "/C";
				cmd[2] = "ipconfig/all";
			}
			else
			// if (osName.equals("Windows NT"))
			{
				// All other windows.
				cmd[0] = "cmd.exe";
				cmd[1] = "/C";
				cmd[2] = "ipconfig/all";
			}
		} // end windows world.

		return cmd;

	} // end getMacCommand

	MacAddress()
	{

	}

	public String getMacAddress() throws Exception
	{

		if (_macAddress != null)
			return _macAddress;

		String[] cmd = getMacCommand();
		if (cmd == null)
			throw new IllegalArgumentException("Unable to get mac address, on this os : "
				+ System.getProperty("os.name"));

		StreamGobbler errorGobbler = null;
		ByteArrayOutputStream cos = new ByteArrayOutputStream();
		try
		{
			// FileOutputStream fos = new FileOutputStream(args[0]);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			// any error message?
			errorGobbler = new StreamGobbler(proc.getErrorStream());
			// any output?
			MacStreamFilter outputGobbler = new MacStreamFilter(proc.getInputStream()); // , cos);
			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			proc.waitFor();
			outputGobbler.join();
			errorGobbler.join();
			_macAddress = outputGobbler.getMacAddress();

			// On cherche une des chaines caracteristiques...
			// Reader reader = new StreamReader()

			cos.flush();
		}
		catch (Throwable t)
		{
			_error = errorGobbler.getOutputString();
			throw new MacException(_error, t);
		}
		finally
		{
			try
			{
				cos.close();
			}
			catch (Exception e)
			{}
		}

		return _macAddress;
	}

	/** Return the error message if any */
	public String getErrorMessage()
	{

		return _error;
	}

	public class MacException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3493817979904619536L;
		private String _message = null;

		public MacException(String message, Throwable cause)
		{

			super("Unable to get Mac Address on this computer. ", cause);
			_message = message;

		}

		public String getErrorMessage()
		{

			return _message;
		}
	}
}
