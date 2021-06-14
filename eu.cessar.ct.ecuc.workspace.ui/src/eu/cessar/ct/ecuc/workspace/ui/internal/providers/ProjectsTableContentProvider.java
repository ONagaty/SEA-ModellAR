package eu.cessar.ct.ecuc.workspace.ui.internal.providers;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.IEcucCore;

public class ProjectsTableContentProvider extends AbstractTableContentProvider
{
	protected static final String HIDDEN_PROJECT_PREFIX = "."; //$NON-NLS-1$
	protected boolean showHiddenProjects = false;

	/**
	 * Return all IProject elements with Cessar nature in the current workspace
	 */
	@Override
	public Object[] getElements(Object inputElement)
	{
		// Allocate return array
		ArrayList<IProject> projects = new ArrayList<IProject>();

		// Verify assumptions: inputElement must be IWorkspaceRoot
		Assert.isTrue(inputElement instanceof IWorkspaceRoot);

		// Build up the array of eligible projects
		for (IProject project: ((IWorkspaceRoot) inputElement).getProjects())
		{
			// Skip projects that are not valid EcuC projects
			if (!IEcucCore.INSTANCE.isValidEcucProject(project))
			{
				continue;
			}

			// Skip hidden projects
			if (!showHiddenProjects && isHidden(project))
			{
				continue;
			}

			projects.add(project);
		}
		return projects.toArray();
	}

	@Override
	public String getColumnText(Object element, int columnIndex)
	{
		if (element instanceof IProject)
		{
			return ((IProject) element).getName();
		}
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		return CessarPluginActivator.getDefault().getImage(CessarPluginActivator.ICON_BSW_PROJECT);
	}

	public void showHiddenProjects(boolean show)
	{
		showHiddenProjects = show;
	}

	public boolean isHidden(IProject project)
	{
		return project.getName().startsWith(HIDDEN_PROJECT_PREFIX);
	}
}
