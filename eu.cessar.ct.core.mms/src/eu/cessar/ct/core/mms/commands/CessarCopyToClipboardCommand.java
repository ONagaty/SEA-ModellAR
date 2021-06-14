/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt6343 Mar 16, 2011 11:03:07 AM </copyright>
 */
package eu.cessar.ct.core.mms.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * A Cessar-specific implementation of {@link CopyToClipboardCommand}
 * 
 */
public class CessarCopyToClipboardCommand extends CopyToClipboardCommand
{

	private Command lastExecutedCopyCommand;

	/**
	 * Creates a {@link CessarCopyToClipboardCommand} using the given editing
	 * domain and the collection of objects as input.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} on which the command operates
	 * @param collection
	 *        the collection of objects used as input for the command
	 */
	public CessarCopyToClipboardCommand(EditingDomain domain, Collection<?> collection)
	{
		super(domain, collection);
	}

	/**
	 * Creates a {@link CessarCopyToClipboardCommand} using the given editing
	 * domain and the collection of objects as input.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} on which the command operates
	 * @param collection
	 *        the collection of objects used as input for the command
	 * @return the newly created command
	 */
	public static Command create(EditingDomain domain, final Collection<?> collection)
	{
		return new CessarCopyToClipboardCommand(domain, collection);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CopyToClipboardCommand#doExecute()
	 */
	@Override
	public void doExecute()
	{
		// avoid executing the same copy command more than once, it is redundant
		// and also has side-effects in case the selected element has attributes
		// of FeatureMap type
		if (copyCommand == lastExecutedCopyCommand)
		{
			return;
		}

		copyCommand.execute();

		oldClipboard = domain.getClipboard();
		domain.setClipboard(new ArrayList<Object>(copyCommand.getResult()));
		lastExecutedCopyCommand = copyCommand;

		// domain.setClipboard(new ArrayList<Object>(sourceObjects));

		CessarPasteFromClipboardCommand.setDelegateCommand(null);
		CessarPasteFromClipboardCommand.saveFeatures(sourceObjects, copyCommand.getResult());
	}

}
