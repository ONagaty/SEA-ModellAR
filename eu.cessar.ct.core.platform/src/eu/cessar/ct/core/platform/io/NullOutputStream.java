/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Aug 23, 2010 11:04:11 AM </copyright>
 */
package eu.cessar.ct.core.platform.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * An OutputStream that ignore everything that will be wrote to it.
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 12.04.2012
 */
public class NullOutputStream extends OutputStream
{

	/**
	 * 
	 */
	public NullOutputStream()
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException
	{
		// do nothing
	}

}
