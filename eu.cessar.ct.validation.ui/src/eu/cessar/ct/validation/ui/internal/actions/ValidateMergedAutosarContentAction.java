/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 1, 2010 4:00:46 PM </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.req.Requirement;

/**
 * Validate merged AUTOSAR content action implementation.
 *
 * @author uidu2337
 *
 */
@Requirement(
	reqID = "REQ_VALID#UI#4")
public class ValidateMergedAutosarContentAction extends AbstractValidateAutosarContentAction
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
		return ValidationUtilsCommon.getMergedModelObjects(lst);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.internal.actions.AbstractValidateAutosarContentAction#afterValidation()
	 */
	@Override
	protected void afterValidation()
	{
		// delete the merged resource, unhide the real resources?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		boolean enabled = false;
		currentSelection = (IStructuredSelection) selection;
		Iterator<Object> iterator = currentSelection.iterator();
		while (iterator.hasNext())
		{
			Object currentElement = iterator.next();
			// if the selection contains a project or a splittable EObject, the action is enabled.
			if (currentElement instanceof IProject)
			{
				enabled = true;
				break;
			}
			if (currentElement instanceof EObject)
			{
				EClass clz = ((EObject) currentElement).eClass();
				IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(clz);
				boolean splitable = mmService.isSplitable(clz);
				if (splitable)
				{
					enabled = true;
					break;
				}
			}
		}
		action.setEnabled(enabled);
	}
}
