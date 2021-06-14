/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidv3687<br/>
 * Feb 25, 2013 1:37:44 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.validation.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostician;
import org.eclipse.sphinx.emf.validation.markers.ValidationMarkerManager;
import org.eclipse.sphinx.emf.validation.ui.Activator;
import org.eclipse.sphinx.emf.validation.ui.util.DiagnosticUI;
import org.eclipse.sphinx.platform.util.PlatformLogUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

import eu.cessar.ct.validation.EMFValidationMessages;
import eu.cessar.ct.validation.ValidationUtilsCommon;
import eu.cessar.ct.validation.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * Utility class that handle the validation action
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Tue Jul  8 18:51:55 2014 %
 * 
 *         %version: 4 %
 */
// CHECKSTYLE:OFF
@Requirement(
	reqID = "REQ_EDIT_PROP#4")
// CHECKSTYLE:ON
public final class ValidateAutosarContentHandler
{
	private ValidateAutosarContentHandler()
	{
		// do nothing
	}

	/**
	 * Run the validation job for the selected element
	 * 
	 * @param selection
	 *        - tree selection
	 * @param validateChildren
	 *        - if true validate children also
	 */
	public static void runValidation(final EObject selection, final boolean validateChildren)
	{
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
		{
			public void run(final IProgressMonitor progressMonitor) throws InvocationTargetException,
				InterruptedException
			{
				try
				{
					final List<Diagnostic> diagnostics = validate(selection, progressMonitor, validateChildren);

					shell.getDisplay().asyncExec(new Runnable()
					{
						public void run()
						{
							if (progressMonitor.isCanceled())
							{
								handleDiagnostic(selection, Diagnostic.CANCEL_INSTANCE);
							}
							else if (!diagnostics.isEmpty())
							{
								handleDiagnosticMulti(selection, diagnostics, true);
							}
						}
					});
				}
				finally
				{
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

	private static void handleDiagnosticMulti(EObject selection, final List<Diagnostic> diagnostics, boolean briefReport)
	{
		Assert.isNotNull(diagnostics);

		// Optionally show validation results in a Pop-up window
		if (briefReport)
		{
			DiagnosticUI.showDiagnostic(diagnostics);
		}

		// On a second and, let's create markers
		WorkspaceJob job = new WorkspaceJob("Handling diagnostic...") //$NON-NLS-1$
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

		IResource r = EcorePlatformUtil.getFile(selection);
		if (r != null)
		{
			IResourceRuleFactory ruleFactory = r.getWorkspace().getRuleFactory();
			myRules.add(ruleFactory.modifyRule(r));
			myRules.add(ruleFactory.createRule(r));
		}

		job.setRule(new MultiRule(myRules.toArray(new ISchedulingRule[myRules.size()])));
		job.setPriority(Job.BUILD);
		job.schedule();
	}

	private static void handleDiagnostic(EObject selection, Diagnostic diagnostic)
	{
		handleDiagnosticMulti(selection, Collections.singletonList(diagnostic), false);

	}

	private static List<Diagnostic> validate(EObject selection, IProgressMonitor progressMonitor,
		boolean validateChildren)
	{
		List<Diagnostic> result = new ArrayList<>();
		ExtendedDiagnostician diagnostician = new ExtendedDiagnostician();

		if (null != selection)
		{
			int count = getNumberOfObject(selection);

			progressMonitor.beginTask("", count); //$NON-NLS-1$
			progressMonitor.setTaskName(EMFEditUIPlugin.INSTANCE.getString("_UI_Validating_message", //$NON-NLS-1$
				new Object[] {diagnostician.getObjectLabel(selection)}));
			diagnostician.setProgressMonitor(progressMonitor);
			int depth = validateChildren ? EObjectUtil.DEPTH_INFINITE : EObjectUtil.DEPTH_ZERO;
			result.add(diagnostician.validate(selection, depth));

			// Check for EMF intrinsic constraints status
			boolean areEMFIntrinsicConstraintsPrefEnabled = ValidationUtilsCommon.areEMFIntrinsicConstraintsEnabled();
			String validationStatusMessage = areEMFIntrinsicConstraintsPrefEnabled ? EMFValidationMessages.EMFIntrinsicConstraintsStatusEnabled
				: EMFValidationMessages.EMFIntrinsicConstraintsStatusDisabled;

			BasicDiagnostic emfDiag = new BasicDiagnostic(EObjectValidator.DIAGNOSTIC_SOURCE, 0,
				validationStatusMessage, new Object[] {diagnostician.getObjectLabel(selection)});

			result.add(emfDiag);

			diagnostician.setProgressMonitor(null);
			progressMonitor.done();

			return result;
		}
		PlatformLogUtil.logAsWarning(Activator.getDefault(), new RuntimeException(
			"Cannot perform validation on empty element selection.")); //$NON-NLS-1$
		return result;

	}

	/**
	 * For progress bar, useful method which return number of Object to validate into model
	 * 
	 * @param eObject
	 * @return number of Object which will be validate
	 */
	private static int getNumberOfObject(EObject eObject)
	{
		int count = 0;
		for (Iterator<?> i = eObject.eAllContents(); i.hasNext(); i.next())
		{
			++count;
		}
		return count;
	}
}
