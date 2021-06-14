/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Oct 26, 2010 12:46:15 PM </copyright>
 */
package org.autosartools.general.core.data;

import org.autosartools.general.core.project.CommonConstants;
import org.autosartools.general.core.project.IAutosarProject;

/**
 * @author uidt2045
 * @Review uidl7321 - Apr 3, 2012 Wrapper for an ArPackage.
 */
public class ARPackageWrapper
{

	private IAutosarProject model;
	private String arPackageName;
	private String parentName;

	/**
	 * 
	 * Creates a new wrapper.
	 * 
	 * @param model
	 *        the Autosar project
	 * @param arPackageName
	 *        the ArPackage name
	 * @param parentName
	 *        the parent name
	 */
	public ARPackageWrapper(IAutosarProject model, String arPackageName, String parentName)
	{
		this.model = model;
		this.arPackageName = arPackageName;
		this.parentName = parentName;
	}

	/**
	 * Returns whether the package is a root one.
	 * 
	 * @return <code>true</code>, if the wrapped package is a root one,
	 *         <code>false</code>, otherwise.
	 */
	public boolean isMainARPackage()
	{
		return CommonConstants.ROOT_PACKAGES.equals(parentName);
	}

	/**
	 * Returns the Autosar project.
	 * 
	 * @return the project
	 */
	public IAutosarProject getModel()
	{
		return model;
	}

	/**
	 * Returns the ArPackage's name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return arPackageName;
	}

	/**
	 * Returns the parent package's name.
	 * 
	 * @return the parent's name
	 */
	public String getParentName()
	{
		return parentName;
	}

	/**
	 * Returns the parent package or the Autosar project.
	 * 
	 * @return the parent
	 */
	public Object getParent()
	{
		Object result = null;

		if (!CommonConstants.ROOT_PACKAGES.equals(getParentName()))
		{
			result = getModel().getARPackageWrapper(getParentName());
		}
		else
		{
			result = getModel();
		}

		return result;
	}

	/**
	 * 
	 * Retuns the fully qualified name of the wrapped package.
	 * 
	 * @return the full path
	 */
	public String getFullPath()
	{
		String result = getName();

		if (!CommonConstants.ROOT_PACKAGES.equals(getParentName()))
		{
			result = getParentName() + CommonConstants.PATH_SEPARATOR + getName();
		}

		return result;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
