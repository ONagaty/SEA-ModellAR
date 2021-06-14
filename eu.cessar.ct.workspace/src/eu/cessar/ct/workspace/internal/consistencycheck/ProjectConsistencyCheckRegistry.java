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
package eu.cessar.ct.workspace.internal.consistencycheck;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import eu.cessar.ct.workspace.WorkspaceConstants;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheck;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckRegistry;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.consistencycheck.contrib.ProjectConsistencyCheckElemDef;
import eu.cessar.ct.workspace.internal.consistencycheck.impl.ProjectConsistencyCheck;

/**
 * Implementation of {@link IProjectConsistencyCheckRegistry}.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 16:16:34 2014 %
 * 
 *         %version: 3 %
 */
public class ProjectConsistencyCheckRegistry implements IProjectConsistencyCheckRegistry
{
	private volatile boolean fullyInitialized;

	// private IProject project;

	private List<ProjectConsistencyCheckElemDef> consistencyCheckElemDefs;

	private List<IProjectConsistencyCheck> consistencyChecks;

	/**
	 * 
	 */
	public ProjectConsistencyCheckRegistry()
	{

	}

	/**
	 * Check if initialization is needed and call doInit if necessary
	 */
	private void checkInit()
	{
		if (!fullyInitialized)
		{
			synchronized (ProjectConsistencyCheckRegistry.class)
			{
				if (!fullyInitialized)
				{
					try
					{
						doInit();
					}
					finally
					{
						fullyInitialized = true;
					}
				}
			}
		}

	}

	/**
	 * 
	 */
	private void doInit()
	{
		consistencyCheckElemDefs = new ArrayList<ProjectConsistencyCheckElemDef>();

		// locate the extension point
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			WorkspaceConstants.EXTENSION_CONSISTENCY_CHECK);

		if (extensionPoint != null)
		{

			// read contributions
			IConfigurationElement[] configurationElements = extensionPoint.getConfigurationElements();
			for (IConfigurationElement configElement: configurationElements)
			{
				if (configElement.getName().equals(WorkspaceConstants.ELEMENT_CONSISTENCY_CHECK))
				{
					consistencyCheckElemDefs.add(new ProjectConsistencyCheckElemDef(configElement));
				}
			}

			createConsistencyChecks();
		}

	}

	/**
	 * 
	 */
	private void createConsistencyChecks()
	{
		consistencyChecks = new ArrayList<IProjectConsistencyCheck>();

		for (ProjectConsistencyCheckElemDef elemDef: consistencyCheckElemDefs)
		{
			IProjectConsistencyCheck check = new ProjectConsistencyCheck(elemDef);
			consistencyChecks.add(check);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckRegistry#getRegisteredConsistencyChecks()
	 */
	@Override
	public List<IProjectConsistencyCheck> getRegisteredConsistencyChecks()
	{
		checkInit();
		return consistencyChecks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckRegistry#getRegisteredConsistencyCheck(java.lang
	 * .String)
	 */
	@Override
	public IProjectConsistencyCheck getRegisteredConsistencyCheck(String id)
	{
		checkInit();
		for (IProjectConsistencyCheck check: consistencyChecks)
		{
			if (check.getId().equals(id))
			{
				return check;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckRegistry#getConsistencyCheckers()
	 */
	@Override
	public List<IProjectConsistencyChecker> getConsistencyCheckers()
	{
		checkInit();
		List<IProjectConsistencyChecker> checkerList = new ArrayList<IProjectConsistencyChecker>();

		for (IProjectConsistencyCheck check: consistencyChecks)
		{
			IProjectConsistencyChecker checker = check.getConsistencyChecker();
			if (checker != null)
			{
				checkerList.add(checker);
			}
		}
		return checkerList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckRegistry#getConsistencyChecker(java.lang.String)
	 */
	@Override
	public IProjectConsistencyChecker getConsistencyChecker(String id)
	{

		checkInit();

		IProjectConsistencyCheck check = getRegisteredConsistencyCheck(id);
		if (check != null)
		{
			return check.getConsistencyChecker();
		}

		return null;
	}
}
