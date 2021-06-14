/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 20.07.2012 17:49:33 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.internal.xmlchecker.impl.XMLCheckerManager;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.wizards.XMLConsistencyCheckWizard;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerManager;
import eu.cessar.req.Requirement;

/**
 * Action that runs the loading/saving consistency check on the AUTOSAR files under selection and presents the final
 * report in a {@link XMLConsistencyCheckWizard}.
 * 
 * @author uidl6870
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#1")
public class StartXMLConsistencyCheckAction implements IObjectActionDelegate
{
	private IStructuredSelection currentSelection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		runWaitForModelLoading(getProject());

		final Set<IFile> filesUnderSelection = new HashSet<IFile>();

		if (isSelectionAtFileLevel(filesUnderSelection))
		{
			invokeManagerAndDisplayReport(filesUnderSelection);
		}
		else
		{
			IStatus status = runCollectFilesFromSelection(filesUnderSelection);

			if (filesUnderSelection.size() == 0)
			{
				handleEmptySelection(status);
			}
			else
			{
				invokeManagerAndDisplayReport(filesUnderSelection);
			}
		}
	}

	/**
	 * @param filesUnderSelection
	 */
	private void invokeManagerAndDisplayReport(final Set<IFile> filesUnderSelection)
	{
		final IXMLCheckerManager manager = new XMLCheckerManager();
		final List<IConsistencyCheckResult> results = new ArrayList<IConsistencyCheckResult>();

		try
		{
			ProgressMonitorDialog monDialog = new ProgressMonitorDialog(getActiveShell());
			monDialog.run(true, true, new IRunnableWithProgress()
			{
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					results.addAll(manager.performConsistencyCheck(new ArrayList<IFile>(filesUnderSelection), monitor));
				}
			});

			boolean canceled = monDialog.getProgressMonitor().isCanceled();
			if (canceled)
			{
				return;
			}
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE OK
		{
			// in future must add some comments to explain why only these
			// actions were taken in the catch block
			CessarPluginActivator.getDefault().logError(e);
			return;
		}

		List<IXMLCheckerInconsistency> inconsistencies = new ArrayList<IXMLCheckerInconsistency>();

		boolean globalIsOk = true;
		for (IConsistencyCheckResult result: results)
		{
			// check whether the operation completed successfully
			boolean isOk = result.getStatus().isOK();
			globalIsOk &= isOk;
			if (!isOk)
			{
				CessarPluginActivator.getDefault().log(result.getStatus());
			}
			else
			{
				List<IXMLCheckerInconsistency> list = (List<IXMLCheckerInconsistency>) result.getInconsistencies();

				inconsistencies.addAll(list);
			}
		}

		if (!globalIsOk)
		{
			String msg = ""; //$NON-NLS-1$
			if (inconsistencies.size() > 0)
			{
				msg = " Pressing OK will display a partial report."; //$NON-NLS-1$
			}

			MessageDialog.openError(getActiveShell(),
				"Consistency check information", //$NON-NLS-1$
				"The consistency check could not be run on some of the files. Please consult the Error Log view for further details." + msg); //$NON-NLS-1$
		}

		if (inconsistencies.size() > 0)
		{
			File file = inconsistencies.get(0).getDetailFromOriginalFile().getFile();
			IFile iFile = ResourceUtils.getIFile(file);
			Assert.isTrue(iFile != null);
			IProject project = iFile.getProject();

			startWizard(project, inconsistencies);
		}
		else
		{
			if (globalIsOk)
			{
				MessageDialog.openInformation(getActiveShell(), "Consistency check information", //$NON-NLS-1$
					"Consistency check completed succesfully."); //$NON-NLS-1$
			}
		}

	}

	/**
	 * @param project
	 * @param inconsistencies
	 */
	private void startWizard(IProject project, List<IXMLCheckerInconsistency> inconsistencies)
	{
		IWizard wizard = new XMLConsistencyCheckWizard(project, inconsistencies);
		WizardDialog dialog = new WizardDialog(null, wizard)
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.wizard.WizardDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
			 */
			@Override
			protected void createButtonsForButtonBar(Composite parent)
			{
				super.createButtonsForButtonBar(parent);

				// replace Finish with OK
				Button button = getButton(IDialogConstants.FINISH_ID);
				button.setText(IDialogConstants.OK_LABEL);
			}
		};

		dialog.create();
		dialog.open();

	}

	/**
	 * @param project
	 */
	private void runWaitForModelLoading(final IProject project)
	{
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getActiveShell());
		try
		{
			dialog.run(true, false, new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					PlatformUtils.waitForModelLoading(project, monitor);
				}
			});
		}
		catch (InvocationTargetException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param status
	 */
	private void handleEmptySelection(IStatus status)
	{
		if (status.isOK())
		{
			MessageDialog.openInformation(getActiveShell(), Messages.xmlChecker_title,
				Messages.noFilesFound_UnderSelection);
		}
		else
		{
			IStatus[] children = status.getChildren();
			for (IStatus child: children)
			{
				CessarPluginActivator.getDefault().logError(child.getException());
			}

			MessageDialog.openError(getActiveShell(), Messages.xmlChecker_title, NLS.bind(
				Messages.error_accesingResource + "Check The Error Log for details", new Object[] {children.length})); //$NON-NLS-1$
		}
	}

	/**
	 * @param filesUnderSelection
	 * @return
	 */
	private IStatus runCollectFilesFromSelection(final Set<IFile> filesUnderSelection)
	{
		final IStatus[] status = new IStatus[1];

		BusyIndicator.showWhile(Display.getCurrent(), new Runnable()
		{
			public void run()
			{
				IStatus stat = SelectionUtils.collectFilesFromSelection(currentSelection, filesUnderSelection,
					getAcceptedContentTypeIds(getProject()));
				status[0] = stat;
			}
		});

		return status[0];
	}

	/**
	 * @return
	 */
	private IProject getProject()
	{
		IResource resource = (IResource) currentSelection.getFirstElement();

		return resource.getProject();
	}

	/**
	 * Returns whether the current selection contains only files. If true, collects the IFiles under the given
	 * <code>filesUnderSelection</code> set
	 * 
	 * @param filesUnderSelection
	 * 
	 * @return
	 */
	private boolean isSelectionAtFileLevel(Set<IFile> filesUnderSelection)
	{
		boolean fileLevelSelection = true;
		Iterator<?> iterator = currentSelection.iterator();
		while (iterator.hasNext())
		{
			IResource resource = (IResource) iterator.next();
			if (resource instanceof IFile)
			{
				filesUnderSelection.add((IFile) resource);
			}
			else
			{
				fileLevelSelection = false;
				break;
			}
		}

		return fileLevelSelection;
	}

	private Shell getActiveShell()
	{
		return Display.getCurrent().getActiveShell();
	}

	private List<String> getAcceptedContentTypeIds(IProject project)
	{
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);

		List<String> contentTypeIds = autosarRelease.getContentTypeIds();
		return contentTypeIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		currentSelection = (IStructuredSelection) selection;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// do nothing
	}

}
