/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 17, 2010 4:41:06 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.sdk.utils.EcucUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

/**
 * @author aurel_avramescu
 * 
 */
public abstract class AbstractSyncCleanAction implements IObjectActionDelegate
{

	protected IWorkbenchPart targetPart;
	protected IAction action;
	protected IProject project;
	protected IFile file;
	protected List<IFile> files = new ArrayList<IFile>();
	protected int isFileOrProject = 0;
	protected List<GModuleConfiguration> configurations = new ArrayList<GModuleConfiguration>(0);
	protected boolean isValidSelection = true;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		this.targetPart = targetPart;
		this.action = action;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		IStructuredSelection sel = (IStructuredSelection) selection;
		configurations = new ArrayList<GModuleConfiguration>(0);
		files = new ArrayList<IFile>();
		if (sel.getFirstElement() instanceof IFile)
		{
			file = (IFile) sel.getFirstElement();
			isFileOrProject = 1;
			for (Object selectedObject: sel.toList())
			{
				IFile temp = (IFile) selectedObject;
				if (!temp.getProject().equals(file.getProject()))
				{
					isValidSelection = false;
					break;
				}
				isValidSelection = true;
				files.add(temp);
			}
		}

		if (sel.getFirstElement() instanceof IProject)
		{
			project = (IProject) sel.getFirstElement();
			isFileOrProject = 2;
		}

		if (sel.getFirstElement() instanceof GModuleConfiguration)
		{
			GModuleConfiguration moduleConf = (GModuleConfiguration) sel.getFirstElement();
			file = EcorePlatformUtil.getFile(moduleConf);
			project = file.getProject();
			for (Object selectedObject: sel.toList())
			{
				if (!file.getProject().equals(
					EcorePlatformUtil.getFile((GModuleConfiguration) selectedObject).getProject()))
				{
					isValidSelection = false;
					break;
				}
				isValidSelection = true;
				configurations.add((GModuleConfiguration) selectedObject);

			}

		}

	}

	/**
	 * 
	 */
	protected void extractModulesFromProject()
	{
		loadProject(project);
		Collection<GModuleDef> modulesDef = EcucUtils.getAvailableModules(project);
		for (GModuleDef moduleDef: modulesDef)
		{
			Collection<GModuleConfiguration> modulesConf = EcucUtils.getAvailableInstances(project,
				moduleDef);
			List<String> shortNames = new ArrayList<String>(10);
			for (GModuleConfiguration moduleConf: modulesConf)
			{
				if (!shortNames.contains(moduleConf.gGetShortName()))
				{
					if (file == null)
					{
						file = EcorePlatformUtil.getFile(moduleConf);
					}
					configurations.add(moduleConf);
					shortNames.add(moduleConf.gGetShortName());
				}
			}

		}
	}

	/**
	 * 
	 */
	protected void extractModulesFromFile()
	{
		project = file.getProject();
		loadProject(project);
		Collection<GModuleDef> modulesDef = EcucUtils.getAvailableModules(project);
		for (GModuleDef moduleDef: modulesDef)
		{
			Collection<GModuleConfiguration> modulesConf = EcucUtils.getAvailableInstances(project,
				moduleDef);
			List<String> shortNames = new ArrayList<String>(10);
			for (GModuleConfiguration moduleConf: modulesConf)
			{
				if (!shortNames.contains(moduleConf.gGetShortName())
					&& moduleConf.eResource() != null
					&& files.contains(EcorePlatformUtil.getFile(moduleConf)))
				{
					configurations.add(moduleConf);
					shortNames.add(moduleConf.gGetShortName());
				}
			}

		}
	}

	private void loadProject(final IProject project)
	{
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
		{
			public void run(final IProgressMonitor monitor) throws InvocationTargetException
			{
				monitor.beginTask(NLS.bind(Messages.Progress_LoadingProject, project.getName()),
					IProgressMonitor.UNKNOWN);
				try
				{
					PlatformUtils.waitForModelLoading(project, null);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				monitor.done();
			}
		};
		try
		{
			new ProgressMonitorDialog(targetPart.getSite().getShell()).run(true, true,
				new WorkspaceModifyDelegatingOperation(runnableWithProgress));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
