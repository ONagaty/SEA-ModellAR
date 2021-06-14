/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An output stream that deliver the output to multiple other outputs. It can be
 * used for example to output information at the same time in a file and on the
 * console.
 * 
 * @Review uidl6458 - 12.04.2012
 * 
 */
public class MultiplexedOutputStream extends FilterOutputStream
{

	private List<OutputStream> streams = new ArrayList<OutputStream>();

	/**
	 * @param out
	 */
	public MultiplexedOutputStream(OutputStream out)
	{
		super(out);
		streams.add(out);
	}

	/**
	 * @param out
	 */
	public MultiplexedOutputStream(List<OutputStream> out)
	{
		super(out.get(0));
		streams.addAll(out);
	}

	/**
	 * @param out
	 */
	public void addOutputStream(OutputStream out)
	{
		streams.add(out);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		Iterator<OutputStream> iterator = streams.iterator();
		while (iterator.hasNext())
		{
			OutputStream out = iterator.next();
			out.write(b);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] data, int offset, int length) throws IOException
	{
		Iterator<OutputStream> iterator = streams.iterator();
		while (iterator.hasNext())
		{
			OutputStream out = iterator.next();
			out.write(data, offset, length);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#flush()
	 */
	@Override
	public void flush() throws IOException
	{
		Iterator<OutputStream> iterator = streams.iterator();
		while (iterator.hasNext())
		{
			OutputStream out = iterator.next();
			out.flush();
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException
	{
		Iterator<OutputStream> iterator = streams.iterator();
		while (iterator.hasNext())
		{
			OutputStream out = iterator.next();
			out.close();
		}
	}
}