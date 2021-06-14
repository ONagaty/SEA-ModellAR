/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 1, 2010 4:00:46 PM </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.req.Requirement;

/**
 * Validate AUTOSAR content action implementation
 *
 * @author uidl6870
 *
 */
@Requirement(
	reqID = "REQ_VALID#UI#1")
public class ValidateAutosarContentAction extends AbstractValidateAutosarContentAction
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.validation.ui.internal.actions.AbstractValidateAutosarContentAction.getModelObjects(List<Object>)
	 */
	@Override
	public List<EObject> getModelObjects(List<Object> lst)
	{
		return ValidationUtilsCommon.getModelObjects(lst);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.internal.actions.AbstractValidateAutosarContentAction#afterValidation()
	 */
	@Override
	protected void afterValidation()
	{
		// nothing needed
	}

}
