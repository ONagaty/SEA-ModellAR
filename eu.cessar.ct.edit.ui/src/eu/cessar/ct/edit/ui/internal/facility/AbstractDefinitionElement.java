/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 17, 2010 4:31:52 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author uidl7321
 * 
 */
public abstract class AbstractDefinitionElement implements IDefinitionElement
{
	private String id;

	/**
	 * 
	 * @param extension
	 */
	public AbstractDefinitionElement(IConfigurationElement extension)
	{
		id = extension.getAttribute(FacilityConstants.ATT_ID);
		if (id == null)
		{
			id = ""; //$NON-NLS-1$
		}
	}

	public String getId()
	{
		return id;
	}

}
