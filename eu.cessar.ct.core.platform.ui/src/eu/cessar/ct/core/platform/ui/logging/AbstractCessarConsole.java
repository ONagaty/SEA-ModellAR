/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Aug 18, 2010 6:25:22 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.logging;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IOConsole;

/**
 * Abstract implementation of an Eclipse console
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractCessarConsole extends IOConsole
{

	/**
	 * @param name
	 * @param imageDescriptor
	 */
	public AbstractCessarConsole(String name, ImageDescriptor imageDescriptor)
	{
		super(name, imageDescriptor);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param consoleType
	 * @param imageDescriptor
	 * @param autoLifecycle
	 */
	public AbstractCessarConsole(String name, String consoleType, ImageDescriptor imageDescriptor,
		boolean autoLifecycle)
	{
		super(name, consoleType, imageDescriptor, autoLifecycle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param consoleType
	 * @param imageDescriptor
	 * @param encoding
	 * @param autoLifecycle
	 */
	public AbstractCessarConsole(String name, String consoleType, ImageDescriptor imageDescriptor,
		String encoding, boolean autoLifecycle)
	{
		super(name, consoleType, imageDescriptor, encoding, autoLifecycle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param consoleType
	 * @param imageDescriptor
	 */
	public AbstractCessarConsole(String name, String consoleType, ImageDescriptor imageDescriptor)
	{
		super(name, consoleType, imageDescriptor);
		// TODO Auto-generated constructor stub
	}

}
