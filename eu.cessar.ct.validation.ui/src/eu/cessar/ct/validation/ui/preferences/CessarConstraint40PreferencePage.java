/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 8, 2014 2:30:29 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.sphinx.emf.validation.bridge.util.ConstraintUtil;

import autosar40.util.Autosar40Factory;

/**
 * Preference page for AUTOSAR 4.0 constraints.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Thu Jan 29 10:42:09 2015 %
 *
 *         %version: 2 %
 */
public class CessarConstraint40PreferencePage extends CessarConstraintPreferencePage
{
/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.validation.ui.CessarConstraintPreferencePage#getFilter()
 */
	@Override
	protected String getFilter()
	{
		return ConstraintUtil.getModelFilter(Autosar40Factory.eINSTANCE.createARPackage());
	}
}
