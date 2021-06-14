/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw6834<br/>
 * May 4, 2015 1:56:14 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.execution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.runtime.ui.internal.AutomationConstants;
import eu.cessar.ct.runtime.ui.internal.AutomationUtils;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * Handler for a selected pluget in the Automation Menu (runs the pluget)
 *
 * @author uidw6834
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Tue Jun 23 14:54:34 2015 %
 *
 *         %version: 4 %
 */
@Requirement(
	reqID = "36858")
public class AutomationRunPlugetHandler extends AbstractHandler
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		// get the artifact name of the corresp CommandContributionItemParameter
		String artifactName = (String) event.getParameters().get(AutomationConstants.AUTOMATION_COMMAND_PARAM_ID);

		List<Artifact> liPlugetArtifacts = new ArrayList<Artifact>();

		IProject activeProject = SelectionUtils.getActiveProject(false);
		if (artifactName != null)
		{
			// get the list of pluget artifacts with the given name
			liPlugetArtifacts = AutomationUtils.getPlugetArtifacts(activeProject, artifactName);
		}
		if (liPlugetArtifacts.isEmpty())
		{
			CessarPluginActivator.getDefault().logError(AutomationConstants.NO_ARTIFACT_WITH_GIVEN_NAME);
			return null;
		}
		if (liPlugetArtifacts.size() > 1)
		{
			CessarPluginActivator.getDefault().logWarning(AutomationConstants.MORE_ARTIFACTS_WITH_GIVEN_NAME);
		}
		// get the first artifact found (should be the only one)
		Artifact artifact = liPlugetArtifacts.get(0);

		// get the corresp binding
		IArtifactBinding concreteBinding = artifact.getConcreteBinding();

		// run the pluget
		RunPlugetAutomationWizard.executeWizardForPluget((PlugetBinding) concreteBinding);

		return null;
	}
}
