/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 26.06.2014 16:03:23
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.decorators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.sphinx.emf.explorer.decorators.BasicExplorerProblemDecorator;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;

/**
 * Lightweight validation decorator used only for splitable objects
 *
 * @author uidl6458
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Thu Jul 3 16:13:59 2014 %
 *
 *         %version: 3 %
 */
public class BasicLightweightMergedValidationDecorator extends BasicExplorerProblemDecorator
{

	/**
	 *
	 */
	public BasicLightweightMergedValidationDecorator()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.sphinx.emf.validation.ui.decorators.BasicLightweightValidationDecorator#decorate(java.lang.Object,
	 * org.eclipse.jface.viewers.IDecoration)
	 */
	@Override
	public void decorate(Object element, IDecoration decoration)
	{
		// decorate only splitable objects
		if (element instanceof EObject && SplitableUtils.INSTANCE.isSplitable((EObject) element))
		{
			super.decorate(element, decoration);
		}
	}

	// @Override
	// protected int computeValidationStatusCode(Object element)
	// {
	//
	// int result = ValidationStatusCode.SEVERITY_OK;
	// IMarker[] markers = new IMarker[0];
	//
	// try
	// {
	//
	// if (element instanceof EObject)
	// {
	// markers = CessarValidationMarkerManagerDelegate.getValidationMarkersList((EObject) element,
	// EObjectUtil.DEPTH_INFINITE);
	// }
	// if (markerManager.isError(markers))
	// {
	// result = ValidationStatusCode.SEVERITY_ERROR;
	// }
	// else if (markerManager.isWarning(markers))
	// {
	// result = ValidationStatusCode.SEVERITY_WARNING;
	// }
	//
	// }
	// catch (CoreException cex)
	// {
	// CessarPluginActivator.getDefault().logError(cex);
	// }
	// return result;
	// }
}
