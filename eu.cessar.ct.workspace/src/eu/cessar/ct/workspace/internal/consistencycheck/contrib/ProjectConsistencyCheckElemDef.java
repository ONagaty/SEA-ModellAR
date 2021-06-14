/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 21, 2014 6:03:24 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.contrib;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.workspace.WorkspaceConstants;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * Wrapper for a contribution to a consistency check, in the plug-in registry.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 16:16:35 2014 %
 * 
 *         %version: 4 %
 */
public class ProjectConsistencyCheckElemDef
{
	private String id;
	private String name;
	private ExtensionClassWrapper<IProjectConsistencyChecker> checkerClassWrapper;
	private IProjectConsistencyChecker consistencyChecker;

	/**
	 * Constructor of this wrapper class corresponding to the given configuration element.
	 * 
	 * @param extension
	 *        the given configuration element from the plugin registry
	 */
	public ProjectConsistencyCheckElemDef(IConfigurationElement extension)
	{
		id = readContribution(extension, WorkspaceConstants.ATT_ID);
		name = readContribution(extension, WorkspaceConstants.ATT_NAME);

		checkerClassWrapper = new ExtensionClassWrapper<IProjectConsistencyChecker>(extension,
			WorkspaceConstants.ATT_CHECKER_CLASS);
	}

	/**
	 * Reads the contribution from the plugin registry of the given configuration element.
	 * 
	 * @param extension
	 *        the given extension to read
	 * @param attribute
	 *        the attribute of the given <code>extension</code>
	 * @return the value of the given <code>attribute</code>
	 */
	protected static String readContribution(IConfigurationElement extension, String attribute)
	{
		String contribution = extension.getAttribute(attribute);
		if (contribution == null)
		{
			CessarPluginActivator.getDefault().logWarning(Messages.extension_missing_element, attribute,
				extension.getName());
			return ""; //$NON-NLS-1$
		}
		return contribution;
	}

	/**
	 * Returns the id attribute of this element contributed in the plugin registry.
	 * 
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Returns the name attribute of this element contributed in the plugin registry.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Creates and/or returns an instance of the attribute {@link WorkspaceConstants.ATT_CHECKER_CLASS},associated in
	 * the plug-in registry .
	 * 
	 * @return an instance of the checker class or <code>null</code>, if an error occurs
	 */
	public IProjectConsistencyChecker createCheckerClass()
	{
		if (consistencyChecker != null)
		{
			return consistencyChecker;
		}
		try
		{
			consistencyChecker = checkerClassWrapper.newInstance();
			return consistencyChecker;
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}
}
