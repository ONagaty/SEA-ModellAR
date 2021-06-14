/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw6834<br/>
 * Apr 29, 2015 3:54:38 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.cid.CIDModels;
import eu.cessar.ct.cid.ICIDGlobalStorage;
import eu.cessar.ct.cid.ICIDProjectStorage;
import eu.cessar.ct.cid.model.Artifact;

/**
 * Automation Utils
 *
 * @author uidw6834
 *
 *         %created_by: uidw6834 %
 *
 *         %date_created: Wed Jun 3 15:46:28 2015 %
 *
 *         %version: 2 %
 */
public final class AutomationUtils
{

	private AutomationUtils()
	{
		// do not instantiate
	}

	/**
	 * Get artifacts of type pluget,both global artifacts and {@link project} artifacts.
	 *
	 * @param project
	 * @return global and project artifacts for this resource
	 */
	public static List<Artifact> getPlugetArtifacts(IProject project)
	{

		List<Artifact> artifacts = new ArrayList<Artifact>();
		// get global artifacts
		List<Artifact> globalArtifacts = getGlobalPlugetArtifacts();

		// get project artifacts
		List<Artifact> projectArtifacts = getProjectPlugetArtifacts(project);

		artifacts.addAll(globalArtifacts);
		artifacts.addAll(projectArtifacts);
		return artifacts;
	}

	/**
	 * Get global artifacts of type pluget
	 *
	 * @return the list of global artifacts
	 */
	public static List<Artifact> getGlobalPlugetArtifacts()
	{
		// get global artifacts
		ICIDGlobalStorage cidGlobalStorage = CIDModels.getCIDGlobalStorage();
		List<Artifact> globalArtifacts = cidGlobalStorage.getArtifacts(AutomationConstants.PLUGET_AR_TYPE);

		return globalArtifacts;
	}

	/**
	 * Get project artifacts of type pluget
	 *
	 * @param project
	 * @return the list of project artifacts from the given resource
	 */
	public static List<Artifact> getProjectPlugetArtifacts(IProject project)
	{
		List<Artifact> projectArtifacts = new ArrayList<Artifact>();
		if (project != null)
		{
			// get project artifacts
			ICIDProjectStorage cidProjectStorage = CIDModels.getCIDProjectStorage(project);
			projectArtifacts = cidProjectStorage.getArtifacts(AutomationConstants.PLUGET_AR_TYPE);
		}

		return projectArtifacts;
	}

	/**
	 *
	 * @param menuID
	 * @return the MenuManager of the Menu with the given ID
	 */
	public static MenuManager getMenuManager(String menuID)
	{
		MenuManager menuManager = null;

		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
		{
			return null;
		}
		ApplicationWindow appWin = null;
		if (activeWorkbenchWindow instanceof ApplicationWindow)
		{
			appWin = (ApplicationWindow) activeWorkbenchWindow;
		}
		else
		{
			return null;
		}
		// get the manager for the whole menu bar
		final IMenuManager manager = appWin.getMenuBarManager();

		if (manager != null)
		{
			// take all the contributions (menus) from the menu bar
			IContributionItem[] items = manager.getItems();

			IContributionItem menuContribItem = null;
			// search for the contribution (menu) with the given id
			for (IContributionItem iContributionItem: items)
			{
				if (menuID.equals(iContributionItem.getId()))
				{
					menuContribItem = iContributionItem;
					break;
				}
			}
			menuManager = (MenuManager) menuContribItem;
		}
		// return the manager for the menu with the given menuID
		return menuManager;
	}

	/**
	 * @param project
	 * @param plugetArtifactName
	 * @return the list of all pluget Artifacts with the given Artifact Name
	 */
	public static List<Artifact> getPlugetArtifacts(IProject project, String plugetArtifactName)
	{
		List<Artifact> artifacts = new ArrayList<Artifact>();
		// get global artifacts
		List<Artifact> globalArtifacts = getGlobalPlugetArtifacts(plugetArtifactName);

		// get project artifacts
		List<Artifact> projectArtifacts = getProjectPlugetArtifacts(project, plugetArtifactName);

		artifacts.addAll(globalArtifacts);
		artifacts.addAll(projectArtifacts);
		return artifacts;
	}

	/**
	 * @param project
	 * @param plugetArtifactName
	 * @return the list of project pluget Artifacts with the given Artifact Name
	 */
	public static List<Artifact> getProjectPlugetArtifacts(IProject project, String plugetArtifactName)
	{
		List<Artifact> projectArtifacts = new ArrayList<Artifact>();
		if (project != null)
		{
			// get project artifacts
			ICIDProjectStorage cidProjectStorage = CIDModels.getCIDProjectStorage(project);
			projectArtifacts = cidProjectStorage.getArtifacts(AutomationConstants.PLUGET_AR_TYPE, plugetArtifactName);
		}
		return projectArtifacts;
	}

	/**
	 * @param plugetArtifactName
	 * @return the list of global pluget Artifacts with the given Artifact Name
	 */
	public static List<Artifact> getGlobalPlugetArtifacts(String plugetArtifactName)
	{
		// get global artifacts
		ICIDGlobalStorage cidGlobalStorage = CIDModels.getCIDGlobalStorage();
		List<Artifact> globalArtifacts = cidGlobalStorage.getArtifacts(AutomationConstants.PLUGET_AR_TYPE,
			plugetArtifactName);

		return globalArtifacts;
	}

	/**
	 * This method will update the layout of a {@link Composite} and also will update the {@link ScrollBar} to be shown
	 * in case it is needed
	 *
	 * @param scrolledComposite
	 */
	public static void updateScrolledCompositeLayout(ScrolledComposite scrolledComposite)
	{
		// Expand both horizontally and vertically
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		// update the layout of the composite
		scrolledComposite.setMinSize(scrolledComposite.getChildren()[0].computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.redraw();
		scrolledComposite.layout(true, true);
	}

}
