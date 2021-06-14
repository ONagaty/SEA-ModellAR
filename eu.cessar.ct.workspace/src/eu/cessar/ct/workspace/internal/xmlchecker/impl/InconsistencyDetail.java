/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 06.08.2012 10:34:24 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.io.File;

import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.ILocation;

/**
 * @author uidl6870
 * 
 */
public class InconsistencyDetail implements IInconsistencyDetail
{
	private ILocation location;
	private String msg;
	private File file;
	private String schemaLocation;
	private String item;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#setLocation(eu.cessar.ct.workspace.xmlchecker.ILocation)
	 */
	public void setLocation(ILocation location)
	{
		this.location = location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IDetail#getLocation()
	 */
	public ILocation getLocation()
	{
		return location;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return msg;
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message)
	{
		msg = message;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#getFile()
	 */
	public File getFile()
	{
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#setFile(java.io.File)
	 */
	public void setFile(File file)
	{
		this.file = file;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#getSchemaLocation()
	 */
	public String getSchemaLocation()
	{
		return schemaLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#setSchemaLocation(java.lang.String)
	 */
	public void setSchemaLocation(String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#setItem(java.lang.String)
	 */
	public void setItem(String item)
	{
		this.item = item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail#getItem()
	 */
	public String getItem()
	{
		return item;
	}
}
