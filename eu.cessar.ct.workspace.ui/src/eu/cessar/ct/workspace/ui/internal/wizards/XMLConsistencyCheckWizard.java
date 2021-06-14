/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 17.08.2012 13:23:35 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.artop.aal.common.preferences.IAutosarPreferenceConstants;
import org.artop.aal.common.preferences.IAutosarPreferences;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import eu.cessar.ct.sdk.utils.ProjectUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.xmlchecker.ReportPage;
import eu.cessar.ct.workspace.ui.internal.xmlchecker.TroubleshootingPage;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.req.Requirement;

/**
 * Wizard presenting a troubleshooting page and the final report with the identified loading/saving XML inconsistencies
 * 
 * @author uidl6870
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#3")
public class XMLConsistencyCheckWizard extends Wizard
{
	private TroubleshootingPage troubleshootingPage;
	private ReportPage reportPage;

	private final List<IXMLCheckerInconsistency> inconsistencies;
	private final IProject project;

	private final static String LOAD_PREFS_NAMESPACE = "org.artop.aal.common"; //$NON-NLS-1$

	public XMLConsistencyCheckWizard(IProject project, List<IXMLCheckerInconsistency> inconsistencies)
	{
		this.project = project;
		setNeedsProgressMonitor(true);
		this.inconsistencies = inconsistencies;
		setWindowTitle("Loading/saving consistency check wizard"); //$NON-NLS-1$
		setHelpAvailable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish()
	{
		// once the Next button has been pressed, enable OK button
		return troubleshootingPage.nextPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		boolean validateWithXsd = troubleshootingPage.isValidateWithXsdSelected();

		if (validateWithXsd)
		{
			triggerXsdValidation();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages()
	{
		troubleshootingPage = new TroubleshootingPage("Troubleshooting", project, inconsistencies); //$NON-NLS-1$
		addPage(troubleshootingPage);

		reportPage = new ReportPage("Consistency check report", inconsistencies); //$NON-NLS-1$
		addPage(reportPage);
	}

	/**
	 * Temporary enables validation with XSD preference and disables the caching of the XML files, then triggers a
	 * reload of the project
	 */
	private void triggerXsdValidation()
	{
		Boolean isXsdValidationEnabled = IAutosarPreferences.XSD_VALIDATION_ON_LOAD.get();

		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(LOAD_PREFS_NAMESPACE);
		boolean isCacheFilesEnabled = node.getBoolean(IAutosarPreferenceConstants.PREF_USE_BINARY_RESOURCE, false);

		try
		{
			// disable caching
			if (isCacheFilesEnabled)
			{
				node.putBoolean(IAutosarPreferenceConstants.PREF_USE_BINARY_RESOURCE, false);
			}

			// enable XSD validation during subsequent reload
			if (isXsdValidationEnabled == null || !isXsdValidationEnabled)
			{
				IAutosarPreferences.XSD_VALIDATION_ON_LOAD.set(true);
			}

			runReloadProject();
		}
		finally
		{
			// restore initial values
			IAutosarPreferences.XSD_VALIDATION_ON_LOAD.set(isXsdValidationEnabled);
			node.putBoolean(IAutosarPreferenceConstants.PREF_USE_BINARY_RESOURCE, isCacheFilesEnabled);
		}
	}

	/**
	 * 
	 */
	private void runReloadProject()
	{
		try
		{
			getContainer().run(true, true, new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					monitor.beginTask("", 1000); //$NON-NLS-1$
					ProjectUtils.unloadProject(project, new SubProgressMonitor(monitor, 200));
					ProjectUtils.loadProject(project, new SubProgressMonitor(monitor, 800));
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

}
