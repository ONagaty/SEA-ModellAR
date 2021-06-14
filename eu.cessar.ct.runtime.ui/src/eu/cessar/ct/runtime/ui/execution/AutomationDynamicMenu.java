/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw6834<br/>
 * May 4, 2015 1:24:20 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Property;
import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.runtime.ui.internal.AutomationConstants;
import eu.cessar.ct.runtime.ui.internal.AutomationUtils;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;

/**
 * Dynamically creates the Automation Menu
 *
 * @author uidw6834
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Tue Jun 23 14:54:35 2015 %
 *
 *         %version: 3 %
 */
@Requirement(
	reqID = "36858")
public class AutomationDynamicMenu extends CompoundContributionItem
{
	/**
	 * The list of subMenus (ContributionItems)
	 */
	List<CommandContributionItem> list;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Requirement(
		reqID = "47227")
	@Override
	protected IContributionItem[] getContributionItems()
	{

		CommandContributionItem item = null;

		list = new ArrayList<CommandContributionItem>();

		// list of pluget artifacts
		List<Artifact> liPlugetArtifacts = new ArrayList<Artifact>();

		// take the selected project
		IProject activeProject = SelectionUtils.getActiveProject(false);

		// if no proj is selected or it does not have cessar nature
		try
		{
			if (activeProject == null || !activeProject.hasNature(PlatformConstants.CESSAR_NATURE))
			{
				// should put in the menu the global artifacts only
				liPlugetArtifacts = AutomationUtils.getGlobalPlugetArtifacts();
			}
			else
			{
				// otherwise, put the artifacts both from project and from global storage
				liPlugetArtifacts = AutomationUtils.getPlugetArtifacts(activeProject);

			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		// for each pluget artifact
		for (Artifact plugetArtifact: liPlugetArtifacts)
		{
			// take the list of all properties named "automation" for this artifact
			List<Property> properties = plugetArtifact.getProperties(AutomationConstants.AUTOMATION_PROPERTY);

			// if the automation property is set to true
			if (properties.size() > 0 && Boolean.TRUE.toString().equals(properties.get(0).getValue()))
			{
				// create a contribution (subMenu) in the Automation menu
				item = createSubMenu(plugetArtifact);
				item.setVisible(true);
				list.add(item);
			}
		}

		// return the list of contributions (subMenus)
		return list.toArray(new IContributionItem[list.size()]);
	}

	/**
	 * Creates a contribution (subMenu) for the given plugetArtifact
	 *
	 * @param plugetArtifact
	 * @return
	 */
	private static CommandContributionItem createSubMenu(Artifact plugetArtifact)
	{
		CommandContributionItem item;
		CommandContributionItemParameter p = null;

		p = new CommandContributionItemParameter(PlatformUI.getWorkbench(), plugetArtifact.getName(),
			AutomationConstants.AUTOMATION_COMMAND_ID, SWT.PUSH);

		p.label = plugetArtifact.getName();

		// map of parameters for the command handler (AutomationRunPlugetHandler)
		Map<String, String> commandParams = new HashMap<String, String>();
		commandParams.put(AutomationConstants.AUTOMATION_COMMAND_PARAM_ID, plugetArtifact.getName());
		p.parameters = commandParams;

		// create the ContributionItem with the created ContribItemParameter
		item = new CommandContributionItem(p);

		return item;
	}

}
