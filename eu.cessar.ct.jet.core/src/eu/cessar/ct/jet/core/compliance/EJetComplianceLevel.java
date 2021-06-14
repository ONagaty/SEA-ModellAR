/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 1:26:48 PM </copyright>
 */
package eu.cessar.ct.jet.core.compliance;

import eu.cessar.ct.jet.core.internal.compliance.JET2ComplianceSettings;
import eu.cessar.ct.jet.core.internal.compliance.JET3ComplianceSettings;
import eu.cessar.ct.jet.core.internal.compliance.JET4ComplianceSettings;

/**
 * Enumeration of the Jet compliance level
 * 
 */
public enum EJetComplianceLevel
{

	JET2(new JET2ComplianceSettings()), //
	JET3(new JET3ComplianceSettings()), //
	JET4(new JET4ComplianceSettings()), //
	;

	private final IJetComplianceSettings complianceSettings;

	private EJetComplianceLevel(IJetComplianceSettings complianceSettings)
	{
		this.complianceSettings = complianceSettings;
	}

	/**
	 * @return
	 */
	public IJetComplianceSettings getComplianceSettings()
	{
		return complianceSettings;
	}
}
