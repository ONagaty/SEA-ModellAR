package eu.cessar.ct.core.internal.platform.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewFolderResourceWizard;

import eu.cessar.ct.core.platform.PlatformConstants;

public class CessarPerspective implements IPerspectiveFactory
{
	//	private static final String NAVIGATOR_RESOURCES = "org.eclipse.ui.views.ResourceNavigator"; //$NON-NLS-1$

	//	private static final String NAVIGATOR_PACKAGE = "org.eclipse.jdt.ui.PackageExplorer"; //$NON-NLS-1$

	private static final String NAVIGATOR_PROJECT = "org.eclipse.ui.navigator.ProjectExplorer"; //$NON-NLS-1$

	private static final String VIEW_CONSOLE = "org.eclipse.ui.console.ConsoleView"; //$NON-NLS-1$

	private static final String VIEW_PROPERTIES = IPageLayout.ID_PROP_SHEET;

	private static final String VIEW_PROBLEMS = IPageLayout.ID_PROBLEM_VIEW;

	private static final String VIEW_ERROR = "org.eclipse.pde.runtime.LogView"; //$NON-NLS-1$

	protected void addShortcuts(final IPageLayout layout)
	{
		layout.addNewWizardShortcut(BasicNewFolderResourceWizard.WIZARD_ID);
		layout.addNewWizardShortcut(BasicNewFileResourceWizard.WIZARD_ID);

		layout.addPerspectiveShortcut(PlatformConstants.CESSAR_CT_PERSPECTIVE);
	}

	// interface methods of IPerspectiveFactory
	// /////////////////////////////////////////

	public void createInitialLayout(final IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();

		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.26f, editorArea); //$NON-NLS-1$
		left.addView(NAVIGATOR_PROJECT);
		// left.addView(NAVIGATOR_RESOURCES);
		// left.addView(NAVIGATOR_PACKAGE);

		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.70f, editorArea); //$NON-NLS-1$
		bottom.addView(VIEW_PROPERTIES);
		bottom.addView(VIEW_PROBLEMS);
		bottom.addView(VIEW_CONSOLE);
		if (Platform.inDevelopmentMode())
		{
			bottom.addView(VIEW_ERROR);
		}

		// IPlaceholderFolderLayout right = layout.createPlaceholderFolder(
		//			"right", IPageLayout.RIGHT, 0.15f, editorArea); //$NON-NLS-1$
		// right.addPlaceholder(IPageLayout.ID_OUTLINE);
		addShortcuts(layout);
	}

}
