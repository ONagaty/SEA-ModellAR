/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Oct 27, 2010 5:32:23 PM </copyright>
 */
package org.autosartools.ecuconfig.pdk.core;

import org.eclipse.emf.common.command.AbstractCommand;

/**
 * Change command used in a pluget to perform model changes.
 * 
 * @author uidt2045
 * @Review uidl7321 - Apr 3, 2012
 * 
 */
public abstract class PDKChangeCommand extends AbstractCommand
{

	@Override
	public boolean canExecute()
	{
		return true;
	}

	public void redo()
	{
		// do nothing
	}

}