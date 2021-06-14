/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 26, 2010 12:37:33 PM </copyright>
 */
package eu.cessar.ct.validation.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.validation.markers.IValidationMarker;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.concurrent.AbstractAsyncJob;
import eu.cessar.ct.core.platform.concurrent.AbstractAsyncWorkExecManager;
import eu.cessar.ct.core.platform.concurrent.ICallback;
import eu.cessar.ct.core.platform.util.SafeRunnable;
import eu.cessar.ct.validation.CessarValidationMarkerManagerDelegate;
import eu.cessar.ct.validation.FeatureBoundDiagnostic;
import eu.cessar.ct.validation.IValidationManager;
import eu.cessar.ct.validation.ValidationModeDiagnostic;
import eu.cessar.ct.validation.internal.jobs.SystemValidationJob;
import eu.cessar.ct.validation.preferences.CessarValidationConstants;
import eu.cessar.ct.validation.preferences.EValidationType;
import eu.cessar.ct.validation.preferences.ValidationPreferencesAccessor;

/**
 *
 *
 */
public class ValidationManagerImpl extends AbstractAsyncWorkExecManager<EObject, Diagnostic> implements
IValidationManager
{

	/** delay in milliseconds */
	private static final long JOB_DELAY = 1000;

	private static final String JOB_NAME = "System Validation Job"; //$NON-NLS-1$

	/**
	 *
	 */
	public ValidationManagerImpl()
	{
		super(JOB_DELAY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.AbstractAsyncWorkExecManager#createJob()
	 */
	@Override
	protected AbstractAsyncJob<EObject, Diagnostic> createJob()
	{
		AbstractAsyncJob<EObject, Diagnostic> job = new SystemValidationJob(JOB_NAME, this);
		job.setSystem(true);
		return job;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.IAsyncWorkExecManager#performWork(java.lang.Object,
	 * eu.cessar.ct.validation.ICallback)
	 */
	@Override
	public synchronized void performWork(final EObject target, final ICallback<Diagnostic> callBack)
	{
		final IProject project = MetaModelUtils.getProject(target);
		EValidationType validationType = ValidationPreferencesAccessor.getValidationType(project);
		if (validationType != null)
		{
			switch (validationType)
			{
				case NONE:
					// should return a warning: validation disabled
					SafeRunner.run(new SafeRunnable()
					{
						public void run() throws Exception
						{
							ValidationModeDiagnostic warningDiagnostic = new ValidationModeDiagnostic(IStatus.WARNING,
								MetaModelUtils.getAbsoluteQualifiedName(target), IStatus.WARNING,
								CessarValidationConstants.WARNING_NONE_FLAG, new Object[] {target});
							BasicDiagnostic result = new BasicDiagnostic();

							result.add(warningDiagnostic);
							callBack.workDone(result);
						}
					});
					break;
				case ON_DEMAND:
					// does nothing, it simply shows the validation errors that
					// already exists. It adds a warning that explains that
					// the validation flag is ON_DEMAND

					SafeRunner.run(new SafeRunnable()
					{
						public void run() throws Exception
						{

							TransactionalEditingDomain editingDomain = MetaModelUtils.getEditingDomain(project);
							BasicDiagnostic result = new BasicDiagnostic();
							// add the warning diagnostic

							ValidationModeDiagnostic warningDiagnostic = new ValidationModeDiagnostic(IStatus.WARNING,
								MetaModelUtils.getAbsoluteQualifiedName(target), IStatus.WARNING,
								CessarValidationConstants.WARNING_ON_DEMAND_FLAG, new Object[] {target});
							result.add(warningDiagnostic);

							// add the diagnostics that already exist
							for (Diagnostic diag: computeOldValidationInfo(editingDomain, target))
							{
								result.add(diag);
							}
							callBack.workDone(result);
						}

					});

					break;
				default:
					super.performWork(target, callBack);
					break;
			}
		}
	}

	/**
	 * Computes all the markers that exists for the given EObject, and creates diagnosis for them
	 *
	 * @param obj
	 * @return
	 */
	private List<Diagnostic> computeOldValidationInfo(EditingDomain editingDomain, EObject obj)
	{
		try
		{
			IMarker[] validationMarkersList = CessarValidationMarkerManagerDelegate.getValidationMarkersList(obj,
				EObjectUtil.DEPTH_ONE);
			List<Diagnostic> result = new ArrayList<>();

			for (IMarker marker: validationMarkersList)
			{
				String uriAttribute = marker.getAttribute(EValidator.URI_ATTRIBUTE, ""); //$NON-NLS-1$
				Object feature = marker.getAttribute(IValidationMarker.FEATURES_ATTRIBUTE);

				URI uri = URI.createURI(uriAttribute);
				EObject eObject = getEObjectFromURI(editingDomain, uri, true);

				Diagnostic diagnostic;
				if (feature != null)
				{
					// it should be an extended diagnostic
					List<String> listOfFeatures = computeFeaturesList(feature);

					diagnostic = new FeatureBoundDiagnostic(
						marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR ? Diagnostic.ERROR
							: Diagnostic.WARNING,
						marker.getAttribute(IMarker.SOURCE_ID, ""), //$NON-NLS-1$
						listOfFeatures,
						marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR ? Diagnostic.ERROR
							: Diagnostic.WARNING, marker.getAttribute(IMarker.MESSAGE, ""), new Object[] {eObject}); //$NON-NLS-1$
				}
				else
				{
					diagnostic = new BasicDiagnostic(
						marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR ? Diagnostic.ERROR
							: Diagnostic.WARNING,
						marker.getAttribute(IMarker.SOURCE_ID, ""), //$NON-NLS-1$
						marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR ? Diagnostic.ERROR
							: Diagnostic.WARNING, marker.getAttribute(IMarker.MESSAGE, ""), new Object[] {eObject}); //$NON-NLS-1$
				}
				result.add(diagnostic);
			}

			return result;
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * @return the eObject corresponding to the given URI
	 */
	private EObject getEObjectFromURI(EditingDomain domain, URI uri, boolean loadOnDemand)
	{
		if (uri != null)
		{
			return domain.getResourceSet().getEObject(uri, loadOnDemand);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Creates a list of strings with the values that exist in the given object
	 *
	 * @param feature
	 * @return
	 */
	private List<String> computeFeaturesList(Object feature)
	{
		List<String> result = new ArrayList<>();

		if (feature instanceof List<?>)
		{
			for (Object f: (List<?>) feature)
			{
				if (f instanceof String)
				{
					result.add((String) f);
				}
			}
		}
		else
		{
			if (feature instanceof String)
			{
				result.add((String) feature);
			}
		}
		return result;
	}

}
