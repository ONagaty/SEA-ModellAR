/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 4:22:27 PM </copyright>
 */
package eu.cessar.ct.jet.core.internal.compliance;

import eu.cessar.ct.jet.core.compliance.IJetComplianceSettings;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractComplianceSettings implements IJetComplianceSettings
{

	private boolean extendedSkeleton;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compliance.IJetComplianceSettings#useExtendedSkeleton()
	 */
	public boolean useExtendedSkeleton()
	{
		return extendedSkeleton;
	}

	/**
	 * @param value
	 * @return
	 */
	protected void setUseExtendedSkeleton(boolean value)
	{
		extendedSkeleton = value;
	}

}
