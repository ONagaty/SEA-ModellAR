/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Jan 22, 2016 1:15:19 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.validation.ui.util.Messages;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.validation.CessarValidationMarkerManagerDelegate;

/**
 * Performs a clean of the problem markers from the validation view.
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class CleanProblemMarkers
{
	private static Collection<IProject> projects = new HashSet<>();

	/**
	 * currently selected item in the application UI
	 */
	private IStructuredSelection currentSelection;
	private boolean cleanProject;

	/**
	 * Performs a clean of the problem markers.
	 *
	 * @param clean
	 *        flag for cleaning the project
	 */
	public void clean(boolean clean)
	{
		cleanProject = clean;

		Object obj = null;
		if (currentSelection != null)
		{
			obj = currentSelection.getFirstElement();

			for (Object selectedObject: currentSelection.toList())
			{
				if (selectedObject instanceof IProject)
				{
					IProject project = (IProject) selectedObject;
					projects.add(project);
				}
			}
		}

		IProject project = MetaModelUtils.getProject(obj);

		if (project != null)
		{
			PlatformUtils.waitForModelLoading(project, null);
		}

		final List<EObject> selectedModelObjects = getSelectedModelObjects();

		if (!selectedModelObjects.isEmpty())
		{
			WorkspaceJob job = new WorkspaceJob(Messages._Job_Clean_Markers)
			{
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
				{
					for (EObject eObject: selectedModelObjects)
					{
						CessarValidationMarkerManagerDelegate.removeMarkers(eObject, EObjectUtil.DEPTH_INFINITE,
							IMarker.PROBLEM);
					}
					return Status.OK_STATUS;
				}
			};

			List<ISchedulingRule> myRules = new ArrayList<>();
			createRules(project, selectedModelObjects, myRules);

			job.setRule(new MultiRule(myRules.toArray(new ISchedulingRule[myRules.size()])));
			job.setPriority(Job.BUILD);
			job.schedule();
		}
	}

	private void createRules(IProject project, final List<EObject> selectedModelObjects, List<ISchedulingRule> myRules)
	{
		for (EObject eObject: selectedModelObjects)
		{
			IFile file = EcorePlatformUtil.getFile(eObject);
			if (file != null)
			{
				addRules(myRules, file);
			}
		}
		IFile markerSourceFile = CessarValidationMarkerManagerDelegate.getMarkerSourceFile(project);
		if (markerSourceFile != null)
		{
			addRules(myRules, markerSourceFile);
		}
	}

	private void addRules(List<ISchedulingRule> myRules, IFile file)
	{
		IResourceRuleFactory ruleFactory = file.getWorkspace().getRuleFactory();
		myRules.add(ruleFactory.modifyRule(file));
		myRules.add(ruleFactory.createRule(file));
	}

	/**
	 * Due to performance overhead, its just called before running the action to init the list of selected model objects
	 *
	 * @return result the list of selected objects
	 */
	protected List<EObject> getSelectedModelObjects()
	{
		IStructuredSelection selection = currentSelection;
		List<IFile> files = new ArrayList<>();
		List<EObject> result = new ArrayList<>();

		for (Object selectedObject: selection.toList())
		{
			if (selectedObject instanceof IProject)
			{
				IProject project = (IProject) selectedObject;
				if (project.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles(project, true));
				}
			}
			else if (selectedObject instanceof IFolder)
			{
				IFolder folder = (IFolder) selectedObject;
				if (folder.isAccessible())
				{
					files.addAll(ExtendedPlatform.getAllFiles(folder));
				}
			}
			else if (selectedObject instanceof IFile)
			{
				IFile file = (IFile) selectedObject;
				if (file.isAccessible())
				{
					files.add(file);
				}
			}
			else if (selectedObject instanceof EObject)
			{
				result.add((EObject) selectedObject);
			}
			else
			{
				Object object = AdapterFactoryEditingDomain.unwrap(selectedObject);
				if (object instanceof EObject)
				{
					result.add((EObject) object);
				}
			}
		}

		result.addAll(getModelRoot(files));
		return result;
	}

	private List<EObject> getModelRoot(List<IFile> files)
	{
		List<EObject> result = new ArrayList<>();
		List<IFile> allFiles = new ArrayList<>();

		if (cleanProject)
		{
			for (IProject project: projects)
			{
				allFiles.addAll(ExtendedPlatform.getAllFiles(project, true));
			}
		}
		else
		{
			allFiles = files;
		}

		for (IFile file: allFiles)
		{
			// Get model from workspace file
			@SuppressWarnings("deprecation")
			EObject modelRoot = EcorePlatformUtil.getModelRoot(file);
			if (modelRoot != null)
			{
				result.add(modelRoot);
			}
		}

		return result;
	}

	/**
	 * @param selection
	 *        the current selection
	 *
	 */
	public void setCurrentSelection(IStructuredSelection selection)
	{
		currentSelection = selection;
	}
}
