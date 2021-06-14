/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Aug 9, 2010 7:32:58 PM </copyright>
 */
package eu.cessar.ct.core.mms.commands;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.sphinx.platform.util.ReflectUtil;

/**
 * Utilities class that deal with EMF Command
 * 
 */
public final class CommandUtils
{

	private CommandUtils()
	{
		// do nothing
	}

	/**
	 * Return the child that will be added to a parent by a
	 * {@link CreateChildCommand}
	 * 
	 * @param command
	 * @return
	 */
	public static Object getChildToCreate(CreateChildCommand command)
	{
		try
		{
			return ReflectUtil.getInvisibleFieldValue(command, "child"); //$NON-NLS-1$
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE we throw it
		{
			AssertionFailedException ex = new AssertionFailedException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 * 
	 * Return the owner where a new child will be added
	 * 
	 * @param command
	 * @return
	 */
	public static EObject getOwner(CreateChildCommand command)
	{
		try
		{
			return (EObject) ReflectUtil.getInvisibleFieldValue(command, "owner"); //$NON-NLS-1$
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE re-throws exception
		{
			AssertionFailedException ex = new AssertionFailedException(e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}
}
