package eu.cessar.ct.validation.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sphinx.emf.validation.bridge.util.ConstraintUtil;
import org.eclipse.sphinx.emf.validation.stats.ValidationPerformanceStats;
import org.eclipse.sphinx.emf.validation.ui.actions.BasicValidateAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.ct.validation.ui.internal.CessarDiagnosticDialog;
import eu.cessar.ct.validation.ui.internal.CessarPluginActivator;
import eu.cessar.ct.validation.ui.internal.actions.CleanProblemMarkersAction;
import eu.cessar.req.Requirement;

/**
 * Abstract class for validation operations (file-based and merged).
 *
 * @author uidu2337
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Wed Feb 4 15:15:22 2015 %
 *
 *         %version: 1.1.1 %
 */
public abstract class AbstractValidateAutosarContent extends BasicValidateAction
{
	/**
	 * The current selected object.
	 */
	protected IStructuredSelection currentSelection;

	Shell parentShell;

	/**
	 *
	 */
	public AbstractValidateAutosarContent()
	{
		super();
	}

	/**
	 * @param parentObjects
	 * @return
	 */
	private static String computeMessage(List<String> parentObjects)
	{
		StringBuilder result = new StringBuilder();
		// result.append("The elements on which the validation was done are: \n");
		for (String parent: parentObjects)
		{
			result.append(parent);
			result.append(";\n"); //$NON-NLS-1$
		}
		int lastIndexOf = result.lastIndexOf(";") != -1 ? result.lastIndexOf(";") : result.length() - 1; //$NON-NLS-1$ //$NON-NLS-2$

		return result.toString().substring(0, lastIndexOf);

	}

	/**
	 * Compute the model objects based on the supplied list of objects.
	 *
	 * @param objects
	 *        the list of objects (@{link IProject}, @{link IFile}, @{link IFolder}, @{link EObject}).
	 * @return the model objects
	 */
	protected abstract List<EObject> getModelObjects(List<Object> objects);

	/**
	 * @param action
	 */
	public void run(@SuppressWarnings("unused") IAction action)
	{
		parentShell = getParentShell();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();

		final Shell shell;
		if (activeWorkbenchWindow == null)
		{
			shell = parentShell;

		}
		else
		{
			shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		}

		// SUPPRESS CHECKSTYLE Avoid Checkstyle errors
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
		{
			public void run(final IProgressMonitor progressMonitor) throws InvocationTargetException,
				InterruptedException
			{
				try
				{
					List<Object> selectedObjects = getSelectedObjects(progressMonitor);

					beforeValidation();
					// MessageDialog

					final List<EObject> selectedModelObjects = getModelObjects(selectedObjects);
					final String[] message = {null};
					if (selectedModelObjects.size() == 0)
					{
						message[0] = "No model object(s) found."; //$NON-NLS-1$
					}
					else if (!ValidationUtilsCommon.hasValidationConstraintsPreferences(ConstraintUtil.getModelFilter(selectedModelObjects.get(0))))
					{
						message[0] = "No validation has been performed because no constraints have been specified in the AUTOSAR preference"; //$NON-NLS-1$
					}
					if (message[0] != null)
					{
						parentShell.getDisplay().syncExec(new Runnable()
						{
							@Override
							public void run()
							{
								MessageDialog.openInformation(parentShell, "Validation Information", message[0]); //$NON-NLS-1$
							}
						});
						return;
					}
					handleDiagnostics(selectedModelObjects, shell, progressMonitor);
				}
				finally
				{
					afterValidation();
					progressMonitor.done();
				}
			}

		};

		// This runs the operation, and shows progress.
		// (It appears to be a bad thing to fork this onto another thread.)
		try
		{
			new ProgressMonitorDialog(shell).run(true, true, new WorkspaceModifyDelegatingOperation(
				runnableWithProgress));
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
	 * @return currentSelection
	 */
	protected IStructuredSelection getCurrentSelection()
	{
		return currentSelection;
	}

	/**
	 * Perform any additional tasks needed before validation.
	 */
	@Requirement(
		reqID = "235543")
	protected void beforeValidation()
	{
		// Clean the validation view
		IStructuredSelection structuredSelection = getCurrentSelection();
		structuredSelection.getFirstElement();

		CleanProblemMarkersAction cleanAction = new CleanProblemMarkersAction();
		cleanAction.selectionChanged(null, structuredSelection);
		cleanAction.cleanProject(true);
		cleanAction.run(null);
	}

	/**
	 * Perform any additional tasks needed after validation.
	 */
	abstract protected void afterValidation();

	/**
	 * Get parent shell.
	 *
	 * @return parent shell
	 */
	abstract protected Shell getParentShell();

	/**
	 * Return the selected objects.
	 *
	 * @param progressMonitor
	 * @return the selected objects
	 */
	protected abstract List<Object> getSelectedObjects(final IProgressMonitor progressMonitor);

	/**
	 * @param selectedModelObjects1
	 * @param shell1
	 * @param progressMonitor
	 */
	private void handleDiagnostics(final List<EObject> selectedModelObjects1, final Shell shell1,
		final IProgressMonitor progressMonitor)
	{
		ValidationPerformanceStats.INSTANCE.openContext("Validation of " //$NON-NLS-1$
			+ selectedModelObjects1.get(0));

		final List<Diagnostic> diagnostics = validateMulti(selectedModelObjects1, progressMonitor);

		shell1.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				if (progressMonitor.isCanceled())
				{
					handleDiagnostic(selectedModelObjects1, Diagnostic.CANCEL_INSTANCE);
				}
				else if (diagnostics != null)
				{
					if (diagnostics.size() == 0)
					{

						MessageDialog.openInformation(parentShell,
							"Validation Information", "Validation completed successfully"); //$NON-NLS-1$ //$NON-NLS-2$
						return;
					}
					handleDiagnosticMulti(selectedModelObjects1, diagnostics, true);
				}

				try
				{
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
						"eu.cessar.ct.validation.ui.views.validationView"); //$NON-NLS-1$
				}
				catch (PartInitException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
			}
		});

		ValidationPerformanceStats.INSTANCE.closeAndLogCurrentContext();
	}

	@Override
	protected void handleDiagnosticMulti(List<EObject> selectedModelObjects, final List<Diagnostic> diagnostics,
		boolean showBriefReport)
	{
		Assert.isNotNull(diagnostics);

		ValidationPerformanceStats.INSTANCE.startNewEvent(
			ValidationPerformanceStats.ValidationEvent.EVENT_UPDATE_PROBLEM_MARKERS, "UpdateMarkers"); //$NON-NLS-1$
		// Optionally show validation results in a Pop-up window
		if (showBriefReport)
		{
			// this is the area that has been modified in order to customize the
			// output of the Dialog
			List<String> parentObjects = new ArrayList<>();
			List<Integer> nrOfStatus = new ArrayList<>();

			ValidationUtilsCommon.computeInfoFromDiagnosticsList(diagnostics, parentObjects, nrOfStatus);
			if (nrOfStatus.get(2) != Diagnostic.ERROR && nrOfStatus.get(2) != Diagnostic.WARNING)
			{
				String title = EMFEditUIPlugin.INSTANCE.getString("_UI_ValidationResults_title"); //$NON-NLS-1$
				String message = EMFEditUIPlugin.INSTANCE.getString("_UI_ValidationOK_message"); //$NON-NLS-1$

				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title,
					message);
			}

			CessarDiagnosticDialog diagnosticDialog = new CessarDiagnosticDialog(parentShell,
				"Validation Summary", "The elements on which the validation was done are: \n", //$NON-NLS-1$ //$NON-NLS-2$
				diagnostics, Diagnostic.OK | Diagnostic.INFO | Diagnostic.WARNING | Diagnostic.ERROR, nrOfStatus);

			diagnosticDialog.create();
			diagnosticDialog.populateElements(computeMessage(parentObjects));

			diagnosticDialog.open();

		}

		boolean merged = false;
		if (!selectedModelObjects.isEmpty())
		{
			merged = SplitableUtils.INSTANCE.isSplitable(selectedModelObjects.get(0));
		}
		if (merged)
		{
			ValidationUtilsCommon.updateProblemMarkersDummyResource(selectedModelObjects, diagnostics);
		}
		else
		{
			ValidationUtilsCommon.updateProblemMarkers(selectedModelObjects, diagnostics);
		}

		ValidationPerformanceStats.INSTANCE.endEvent(
			ValidationPerformanceStats.ValidationEvent.EVENT_UPDATE_PROBLEM_MARKERS, "UpdateMarkers"); //$NON-NLS-1$
	}

}
