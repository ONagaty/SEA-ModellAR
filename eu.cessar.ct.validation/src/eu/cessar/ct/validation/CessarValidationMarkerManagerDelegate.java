/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Jun 27, 2014 3:03:26 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.validation.internal.MergedValidationMarkerManager;

/**
 * Delegates to {@link ValidationMarkerManager} or {@link MergedValidationMarkerManager}.
 *
 * @author uidu2337
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Jul  9 14:14:44 2014 %
 *
 *         %version: 3 %
 */
public final class CessarValidationMarkerManagerDelegate
{
	private CessarValidationMarkerManagerDelegate()
	{

	}

	/**
	 * Provided just for convenience.
	 *
	 * @see eu.cessar.ct.validation.internal.MergedValidationMarkerManager#getMarkerSourceFile(IProject)
	 */
	@SuppressWarnings("javadoc")
	public static IFile getMarkerSourceFile(IProject project)
	{
		return MergedValidationMarkerManager.getMarkerSourceFile(project);
	}

	/**
	 * Provided just for convenience.
	 *
	 * @see eu.cessar.ct.validation.internal.MergedValidationMarkerManager#isMergedMarkerSourceFile(IFile)
	 */
	@SuppressWarnings("javadoc")
	public static boolean isMergedMarkerSourceFile(IFile file)
	{
		return MergedValidationMarkerManager.isMergedMarkerSourceFile(file);
	}

	/**
	 * Delegates to {@link ValidationMarkerManager} or {@link MergedValidationMarkerManager}.
	 *
	 * @see eu.cessar.ct.validation.internal.MergedValidationMarkerManager#getValidationMarkersList(EObject, int)
	 * @see org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager#getValidationMarkersList(EObject, int)
	 */
	@SuppressWarnings("javadoc")
	public static IMarker[] getValidationMarkersList(EObject eObject, int depth) throws CoreException
	{
		if (SplitableUtils.INSTANCE.isSplitable(eObject))
		{
			return MergedValidationMarkerManager.getValidationMarkersList(eObject, depth);
		}
		else
		{
			return ValidationMarkerManager.getInstance().getValidationMarkersList(eObject, depth);
		}
	}

	/**
	 * Delegates to {@link ValidationMarkerManager} or {@link MergedValidationMarkerManager}.
	 *
	 * @see eu.cessar.ct.validation.internal.MergedValidationMarkerManager#removeMarkers(EObject, int, String)
	 * @see org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager#removeMarkers(EObject, int, String)
	 */
	@SuppressWarnings("javadoc")
	public static void removeMarkers(EObject eObject, int depth, String markerType) throws CoreException
	{
		ValidationMarkerManager.getInstance().removeMarkers(eObject, depth, markerType);
		MergedValidationMarkerManager.removeMarkers(eObject, depth, markerType);
	}

	/**
	 * Delegates to {@link ValidationMarkerManager} or {@link MergedValidationMarkerManager}.
	 *
	 * @see eu.cessar.ct.validation.internal.MergedValidationMarkerManager#handleDiagnostic(Diagnostic)
	 * @see org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager#handleDiagnostic(Diagnostic)
	 */
	@SuppressWarnings("javadoc")
	public static void handleDiagnostic(Diagnostic diag)
	{
		if (diag.getData() == null)
		{
			return;
		}

		List<?> diagnosticData = diag.getData();
		if (diagnosticData.isEmpty() || !(diagnosticData.get(0) instanceof EObject))
		{
			return;
		}
		if (SplitableUtils.INSTANCE.isSplitable((EObject) diagnosticData.get(0)))
		{
			MergedValidationMarkerManager.handleDiagnostic(diag);
		}
		else
		{
			ValidationMarkerManager.getInstance().handleDiagnostic(diag);
		}
	}
}
