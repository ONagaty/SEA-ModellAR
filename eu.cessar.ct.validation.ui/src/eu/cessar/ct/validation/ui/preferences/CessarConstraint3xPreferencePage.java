/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Dec 10, 2014 3:13:48 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.sphinx.emf.validation.bridge.util.ConstraintUtil;

import autosar3x.util.Autosar3xFactory;

/**
 * Preference page for AUTOSAR 3.x constraints.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Mon Jan 12 11:48:37 2015 %
 *
 *         %version: 1 %
 */
public class CessarConstraint3xPreferencePage extends CessarConstraintPreferencePage
{
/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.validation.ui.CessarConstraintPreferencePage#getFilter()
 */
	@Override
	protected String getFilter()
	{
		return ConstraintUtil.getModelFilter(Autosar3xFactory.eINSTANCE.createARPackage());
	}
}
