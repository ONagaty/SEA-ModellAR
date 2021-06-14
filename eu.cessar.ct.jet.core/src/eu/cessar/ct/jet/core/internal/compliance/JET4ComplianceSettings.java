/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jul 29, 2010 4:22:10 PM </copyright>
 */
package eu.cessar.ct.jet.core.internal.compliance;

/**
 * @author uidl6458
 *
 */
public class JET4ComplianceSettings extends AbstractComplianceSettings
{

	private static final String[] IMPORTS = new String[] {"java.io.IOException", //$NON-NLS-1$
		"eu.cessar.ct.sdk.logging.LoggerFactory"}; //$NON-NLS-1$

	/**
	 *
	 */
	public JET4ComplianceSettings()
	{
		setUseExtendedSkeleton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.jet.core.compliance.IJetComplianceSettings#getSkeletonImports()
	 */
	public String[] getSkeletonImports()
	{
		return IMPORTS;
	}
}
