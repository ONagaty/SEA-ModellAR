/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 10, 2014 5:20:31 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.sphinx.emf.validation.bridge.util.ConstraintUtil;

import autosar21.util.Autosar21Factory;

/**
 * Preference page for AUTOSAR 2.1 constraints.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Mon Jan 12 11:48:37 2015 %
 *
 *         %version: 1 %
 */
public class CessarConstraint21PreferencePage extends CessarConstraintPreferencePage
{
	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.ui.CessarConstraintPreferencePage#getFilter()
	 */
	@Override
	protected String getFilter()
	{
		return ConstraintUtil.getModelFilter(Autosar21Factory.eINSTANCE.createARPackage());
	}
}
