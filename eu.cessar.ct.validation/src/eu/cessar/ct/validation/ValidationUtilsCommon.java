/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Sep 6, 2013 3:40:04 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.artop.aal.common.resource.AutosarURIFactory;
import org.artop.aal.gautosar.services.splitting.Splitable;
import org.artop.aal.gautosar.services.splitting.SplitableEObjectsProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.service.ConstraintRegistry;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.sphinx.emf.validation.bridge.util.ConstraintUtil;
import org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager;
import org.eclipse.sphinx.emf.validation.preferences.IValidationPreferences;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.osgi.service.prefs.BackingStoreException;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.validation.internal.CessarPluginActivator;
import eu.cessar.ct.validation.internal.MergedValidationMarkerManager;
import eu.cessar.ct.validation.internal.Messages;
import eu.cessar.ct.validation.preferences.EValidationType;
import eu.cessar.ct.validation.preferences.ValidationPreferencesAccessor;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 * Validation utilities shared between the validate action and the validation API.
 *
 * @author uidu2337
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Jul 9 14:15:04 2014 %
 *
 *         %version: 11 %
 */
public final class ValidationUtilsCommon
{
	private ValidationUtilsCommon()
	{
		// hide c-tor
	}

	/**
	 * Go through all the diagnosis and count the errors/warnings.
	 *
	 * @param diagnostics
	 *        the diagnostics list produced by validation
	 * @param parentObj
	 *        the parent objects
	 * @param nrOfStatus
	 *        will accumulate errors in the first element, and warnings in the second element
	 */
	public static void computeInfoFromDiagnosticsList(List<Diagnostic> diagnostics, List<String> parentObj,
		List<Integer> nrOfStatus)
	{
		if (nrOfStatus.isEmpty())
		{
			nrOfStatus.add(0);
			nrOfStatus.add(0);
			nrOfStatus.add(Diagnostic.OK);
		}
		for (Diagnostic diagnostic: diagnostics)
		{
			int nrOfErrors = 0;
			int nrOfWarnings = 0;
			List<Diagnostic> children = diagnostic.getChildren();
			// if diagnosis is OK/Info there is no sense in looking at their
			// children
			if (diagnostic.getSeverity() != Diagnostic.INFO && diagnostic.getSeverity() != Diagnostic.OK)
			{
				for (Diagnostic diag: children)
				{
					if (diag.getSeverity() == Diagnostic.ERROR)
					{
						nrOfErrors++;
						nrOfStatus.set(2, Diagnostic.ERROR);
					}
					else
					{
						if (diag.getSeverity() == Diagnostic.WARNING)
						{
							nrOfWarnings++;
							if (Diagnostic.WARNING > nrOfStatus.get(2))
							{
								nrOfStatus.set(2, Diagnostic.WARNING);
							}
						}
					}
				}
			}
			Integer oldErrors = nrOfStatus.get(0);
			Integer oldWarnings = nrOfStatus.get(1);
			parentObj.add(diagnostic.getMessage() + " " + nrOfErrors + " errors, " + nrOfWarnings //$NON-NLS-1$ //$NON-NLS-2$
				+ " warnings"); //$NON-NLS-1$
			nrOfStatus.set(0, oldErrors + nrOfErrors);
			nrOfStatus.set(1, oldWarnings + nrOfWarnings);
		}
	}

	/**
	 * Checks whether validation constraints have been specified in the AUTOSAR preferences. If not, no validation will
	 * be performed.
	 *
	 * @param filter
	 *        AUTOSAR validation constraint filter
	 * @return {@code true} if there are validation constraints matching the filter, {@code false otherwise}
	 */
	public static boolean hasValidationConstraintsPreferences(final String filter)
	{
		boolean prefsSet = true;
		int excludedNo = 0;

		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("org.eclipse.emf.validation"); //$NON-NLS-1$
		if (prefs != null)
		{
			try
			{
				String[] keys = prefs.keys();
				if (keys != null)
				{
					for (int i = 0; i < keys.length; i++)
					{
						if (keys[i].contains(filter))
						{
							excludedNo++;
						}
					}
				}
			}
			catch (BackingStoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		if (excludedNo > 0)
		{
			int noOfValidators = 0;
			Collection<IConstraintDescriptor> allDescriptors = ConstraintRegistry.getInstance().getAllDescriptors();
			if (allDescriptors.size() > 0)
			{
				for (IConstraintDescriptor iConstraintDescriptor: allDescriptors)
				{
					if (null != iConstraintDescriptor.getId())
					{
						if (iConstraintDescriptor.getId().contains(filter))
						{
							noOfValidators++;
						}
					}
				}
				if (excludedNo == noOfValidators)
				{
					prefsSet = false;
				}
			}
		}
		return prefsSet;
	}

	/**
	 * Compute the merged model as a list of {@link EObject}s starting from some passed objects ({@link IProject},
	 * {@link IFolder}, {@link IFile}, {@link EObject}).
	 *
	 * @param objects
	 *        the passed objects
	 * @return the merged model as a list of {@link EObject}s.
	 */
	public static List<EObject> getMergedModelObjects(Collection<Object> objects)
	{
		return getModelObjects(objects, true);
	}

	/**
	 * Decides if the action should be enabled for this {@link EObject}.
	 *
	 * @param obj
	 *        the {@link EObject}
	 * @return true <b>iff</b> {@link obj} is an instance of {@link Splitable} or it is a splitable {@link EObject}.
	 */
	private static boolean isSplitableObject(EObject obj)
	{
		if (SplitableUtils.INSTANCE.isSplitable(obj))
		{
			return true;
		}
		return SplitableUtils.INSTANCE.isSplitable(obj);
	}

	/**
	 * Compute the merged model from {@link objects}.
	 *
	 * @param objects
	 *        for which to compute the merged model
	 * @return the merged model
	 */
	private static List<EObject> mergedModel(final List<EObject> objects)
	{
		// fail fast for empty input cases
		if (objects.isEmpty())
		{
			return Collections.emptyList();
		}
		SplitableEObjectsProvider splitableEObjectsProvider = SplitableUtils.INSTANCE.getSplitableProvider();
		final Collection<EObject> splitablesForObjects = splitableEObjectsProvider.splitablesFor(objects);
		if (splitablesForObjects.isEmpty())
		{
			return Collections.emptyList();
		}

		// Compute the "top-level" objects for the input
		final int splitablesSize = splitablesForObjects.size();
		final List<EObject> uniqueObjects = new ArrayList<>(splitablesSize);
		final SetMultimap<String, EClass> uniqueNameEClassPairs = LinkedHashMultimap.create(splitablesSize, 1);
		for (EObject obj: splitablesForObjects)
		{
			if (obj instanceof GAUTOSAR)
			{
				// GAUTOSAR encountered, merged model of the entire project must
				// be created, so simply return this instance of the root.
				return Lists.newArrayList(obj);
			}
			else if (isSplitableObject(obj))
			{
				// otherwise, if the current object is a Splitable or a splitable EObject,
				final String qName = AutosarURIFactory.getAbsoluteQualifiedName(obj);
				final EClass eClass = obj.eClass();
				// decide whether to add it based on the uniqueness of its <AQN, EClass> pair.
				if (!uniqueNameEClassPairs.containsEntry(qName, eClass))
				{
					uniqueNameEClassPairs.put(qName, eClass);
					uniqueObjects.add(obj);
				}
			}
		}

		return uniqueObjects.isEmpty() ? Collections.<EObject> emptyList() : uniqueObjects;
	}

	/**
	 * Due to performance overhead, its just called before running the action to initialize the list of selected model
	 * objects.
	 *
	 * @param objects
	 *        the objects for which to compute the corresponding model objects list
	 * @return the (non-merged) model objects list
	 */
	public static List<EObject> getModelObjects(Collection<Object> objects)
	{
		return getModelObjects(objects, false);
	}

	/**
	 * Due to performance overhead, its just called before running the action to initialize the list of selected model
	 * objects.
	 *
	 * @param objects
	 *        the current selection
	 * @param merged
	 *        indicates whether to compute the merged model
	 * @return the model objects list
	 */
	private static List<EObject> getModelObjects(Collection<Object> objects, boolean merged)
	{
		boolean nullObjects = false;
		List<EObject> result = new ArrayList<>();
		List<IFile> files = new ArrayList<>();
		for (Object object: objects)
		{
			if (object instanceof IProject)
			{
				IProject project = (IProject) object;
				if (project.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles((IProject) object, true));
				}
			}
			else if (object instanceof IFolder)
			{
				IFolder folder = (IFolder) object;
				if (folder.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles((IFolder) object));
				}
			}
			else if (object instanceof IFile)
			{
				IFile file = (IFile) object;
				if (file.isAccessible())
				{
					files.add((IFile) object);
				}
			}
			else if (object instanceof EObject)
			{
				result.add((EObject) object);
			}
			else if (null == object)
			{
				nullObjects = true;
			}
		}
		loadModelRoot(result, files);

		if (nullObjects)
		{
			result.add(null);
		}

		return merged ? mergedModel(result) : fragmentModel(result);
	}

	/**
	 * @param result
	 * @return
	 */
	private static List<EObject> fragmentModel(List<EObject> objects)
	{
		// fail fast for empty input cases
		if (objects.isEmpty())
		{
			return Collections.emptyList();
		}

		List<EObject> result = new LinkedList<>();
		for (EObject eObject: objects)
		{
			if (isSplitableObject(eObject))
			{
				result.addAll(SplitableUtils.INSTANCE.getAllFragments((Splitable) eObject));
			}
			else
			{
				result.add(eObject);
			}
		}

		return result;
	}

	/**
	 * Add model root for the files in the list.
	 *
	 * @param result
	 *        the list of {@code EObject}s that should be populated
	 * @param files
	 *        the list of files
	 */
	private static void loadModelRoot(List<EObject> result, List<IFile> files)
	{
		if (!files.isEmpty())
		{
			// If selected object is a file, get the mapped model root
			for (IFile file: files)
			{

				Resource resource = EcorePlatformUtil.loadResource(file, EcoreResourceUtil.getDefaultLoadOptions());

				if (resource == null)
				{
					continue;
				}

				URI uri = resource.getURI();

				EObject modelRoot = EcorePlatformUtil.getEObject(uri);

				if (modelRoot == null)
				{
					modelRoot = EcorePlatformUtil.loadEObject(uri);
				}

				if (modelRoot == null && resource.getContents().size() > 0)
				{
					modelRoot = resource.getContents().get(0);
				}

				if (modelRoot != null)
				{
					result.add(modelRoot);
				}
			}
		}
	}

	/**
	 * Update the Problems view with the validation diagnostics.
	 *
	 * @param selectedModelObjects
	 *        the objects on which validation has been performed
	 * @param diagnostics
	 *        the list of validation diagnostics
	 */
	public static void updateProblemMarkersDummyResource(final List<EObject> selectedModelObjects,
		final List<Diagnostic> diagnostics)
	{
		final IProject project = MetaModelUtils.getProjectForObjects(selectedModelObjects);
		IFile ctDummyMarkersFile = MergedValidationMarkerManager.getMarkerSourceFile(project);

		WorkspaceJob job = new WorkspaceJob(Messages.API_updating_markers)
		{
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
			{
				for (Diagnostic diag: diagnostics)
				{
					MergedValidationMarkerManager.handleDiagnostic(diag);
				}
				return Status.OK_STATUS;
			}

		};

		List<ISchedulingRule> myRules = new ArrayList<>();
		IResource r = ctDummyMarkersFile;
		if (null != r)
		{
			IResourceRuleFactory ruleFactory = r.getWorkspace().getRuleFactory();
			myRules.add(ruleFactory.modifyRule(r));
			myRules.add(ruleFactory.createRule(r));
		}

		job.setRule(new MultiRule(myRules.toArray(new ISchedulingRule[myRules.size()])));
		job.setPriority(Job.BUILD);
		job.schedule();
	}

	/**
	 * Update the Problems view with the validation diagnostics.
	 *
	 * @param selectedModelObjects
	 *        the objects on which validation has been performed
	 * @param diagnostics
	 *        the list of validation diagnostics
	 */
	public static void updateProblemMarkers(List<EObject> selectedModelObjects, final List<Diagnostic> diagnostics)
	{
		WorkspaceJob job = new WorkspaceJob(Messages.API_updating_markers)
		{
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
			{
				for (Diagnostic diag: diagnostics)
				{
					ValidationMarkerManager.getInstance().handleDiagnostic(diag);
				}
				return Status.OK_STATUS;
			}

		};

		List<ISchedulingRule> myRules = new ArrayList<>();
		for (EObject eObject: selectedModelObjects)
		{
			IResource r = EcorePlatformUtil.getFile(eObject);
			if (null != r)
			{
				IResourceRuleFactory ruleFactory = r.getWorkspace().getRuleFactory();
				myRules.add(ruleFactory.modifyRule(r));
				myRules.add(ruleFactory.createRule(r));
			}
		}

		job.setRule(new MultiRule(myRules.toArray(new ISchedulingRule[myRules.size()])));
		job.setPriority(Job.BUILD);
		job.schedule();
	}

	/**
	 * Combine a number of {@code Diagnostic}s into a {@code BasicDiagnostic}.
	 *
	 * @param diags
	 *        the {@code Diagnostic} collection
	 * @return the combined {@code BasicDiagnostic}
	 */
	public static BasicDiagnostic combinedDiagnostic(Collection<Diagnostic> diags)
	{
		BasicDiagnostic start = new BasicDiagnostic(Messages.API_validation_results, Diagnostic.OK, null, null);

		for (Diagnostic diag: diags)
		{
			start.add(diag);
		}
		return start;
	}

	/**
	 * Decides whether validation should be started.
	 *
	 * @param targets
	 *        the list of {@code EObject}s
	 * @return {@code true} if should validate, {@code false} otherwise
	 */
	public static boolean shouldValidate(List<EObject> targets)
	{
		if (targets == null || targets.isEmpty())
		{
			return false;
		}

		// be sure that the constraints are loaded
		ModelValidationService.getInstance().loadXmlConstraintDeclarations();

		boolean hasConstraintsPrefs = false;
		for (EObject target: targets)
		{
			if (target == null)
			{
				continue;
			}

			hasConstraintsPrefs = hasConstraintsPrefs
				|| hasValidationConstraintsPreferences(ConstraintUtil.getModelFilter(target));
			if (hasConstraintsPrefs)
			{
				break;
			}
		}

		if (!hasConstraintsPrefs)
		{
			return false;
		}

		IProject project = MetaModelUtils.getProject(targets.get(0));

		EValidationType validationType = ValidationPreferencesAccessor.getValidationType(project);

		return (null != validationType);
	}

	/**
	 * Return the value of the preference to check EMF default rules.
	 *
	 * @return
	 */
	@SuppressWarnings("static-method")
	public static boolean areEMFIntrinsicConstraintsEnabled()
	{
		boolean emfIntrinsicConstraints = false;

		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(
			org.eclipse.sphinx.emf.validation.Activator.PLUGIN_ID);
		if (prefs != null)
		{
			emfIntrinsicConstraints = prefs.getBoolean(IValidationPreferences.PREF_ENABLE_EMF_DEFAULT_RULES,
				IValidationPreferences.PREF_ENABLE_EMF_DEFAULT_RULES_DEFAULT);
		}
		return emfIntrinsicConstraints;
	}

}
