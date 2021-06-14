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
public class JET2ComplianceSettings extends AbstractComplianceSettings
{

	private static final String[] IMPORTS = new String[] {//
	// "java.util.*",//
	// "org.autosartools.ecuconfig.codegen.core.*",//
	// "ecuc.*",//
	// "ecuc.autosar.*",//
	// "org.autosartools.armodel.r20.templates.genericstructure.infrastructure.autosar.IContainerDef",//
	};

	/**
	 * 
	 */
	public JET2ComplianceSettings()
	{
		setUseExtendedSkeleton(false);
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
