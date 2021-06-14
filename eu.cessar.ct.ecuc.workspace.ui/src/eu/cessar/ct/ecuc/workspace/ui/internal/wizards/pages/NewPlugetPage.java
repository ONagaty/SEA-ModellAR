package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;

/**
 *
 * @author uid95856
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Wed May 20 17:29:52 2015 %
 *
 *         %version: RAUTOSAR~6 %
 */
public class NewPlugetPage extends WizardPage
{
	private static final String NEW_SRC_FOLDER_DEFAULT = Messages.CREATE_NEW_SOURCE_FOLDER;

	private Text textBoxClassName;
	private Text textBoxPackageName;
	private Combo comboSourceFolder;

	private Composite container;
	private IProject selectedProject;

	private boolean createSimpleTemplate = true;

	/**
	 * @param pageName
	 */
	public NewPlugetPage(final String pageName)
	{
		super(pageName);
		setTitle(Messages.CESSAR_CT_PLUGET);
	}

	/**
	 * Returns the state of simple template radio
	 *
	 * @return value
	 */
	public boolean isSimpleTemplate()
	{
		return createSimpleTemplate;
	}

	@Override
	public void createControl(final Composite parent)
	{
		final GridLayout layout = new GridLayout();
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		final Display display = parent.getDisplay();

		final CessarFormToolkit cessarFormToolkit = new CessarFormToolkit(display);

		container = cessarFormToolkit.createPlainComposite(parent, SWT.None);

		container.setLayout(layout);
		layout.numColumns = 2;

		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, Messages.NEW_PLUGET_WIZARD_PAGE);

		// source folders combo

		createSourceFoldersCombo(cessarFormToolkit);

		// package name

		cessarFormToolkit.createCLabel(container, Messages.PACKAGE_NAME, SWT.NONE);

		textBoxPackageName = cessarFormToolkit.createText(container, Messages.EMPTY_STRING, SWT.BORDER | SWT.SINGLE);
		textBoxPackageName.setText(Messages.DEFAULT_PACKAGE);
		textBoxPackageName.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
				// NOTHING
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				validatePage();
			}

		});

		textBoxPackageName.setLayoutData(gridData);

		// class name

		cessarFormToolkit.createCLabel(container, Messages.PLUGET_CLASS_NAME, SWT.NONE);

		textBoxClassName = cessarFormToolkit.createText(container, Messages.EMPTY_STRING, SWT.BORDER | SWT.SINGLE);
		textBoxClassName.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
				// NOTHING
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				validatePage();
			}

		});

		textBoxClassName.setLayoutData(gridData);

		createCodeTemplateRadioButtons(cessarFormToolkit);

		cessarFormToolkit.createCLabel(container, null);

		setControl(container);

		textBoxClassName.setFocus();

		validatePage();
	}

	private void createCodeTemplateRadioButtons(final CessarFormToolkit cessarFormToolkit)
	{
		cessarFormToolkit.createCLabel(container, Messages.CODE_TEMPLATE, SWT.NONE);

		final Button simpleTemplateButton = new Button(container, SWT.RADIO);
		simpleTemplateButton.setText(Messages.SIMPLE);
		simpleTemplateButton.setToolTipText(Messages.SIMPLE_DESCRIPTION);
		simpleTemplateButton.addListener(SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				if (simpleTemplateButton.getSelection())
				{
					createSimpleTemplate = true;
				}
			}
		});

		cessarFormToolkit.createCLabel(container, null);

		final Button advancedTemplateButton = new Button(container, SWT.RADIO);
		advancedTemplateButton.setText(Messages.ADVANCED);
		advancedTemplateButton.setToolTipText(Messages.ADVANCED_DESCRIPTION);
		advancedTemplateButton.addListener(SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				if (advancedTemplateButton.getSelection())
				{
					createSimpleTemplate = false;
				}
			}
		});

		simpleTemplateButton.setSelection(true);
	}

	/**
	 * Creates the combo
	 *
	 * @param cessarFormToolkit
	 * @param container
	 */
	private void createSourceFoldersCombo(final CessarFormToolkit cessarFormToolkit)
	{
		cessarFormToolkit.createCLabel(container, Messages.SOURCE_FOLDER, SWT.NONE);

		comboSourceFolder = new Combo(container, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
		final GridData gridData2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
		comboSourceFolder.setLayoutData(gridData2);

		try
		{
			IPath[] javaProjectSourceDirectories = getJavaProjectSourceDirectories(selectedProject);
			Assert.isNotNull(javaProjectSourceDirectories);

			comboSourceFolder.removeAll();
			for (final IPath iPath: javaProjectSourceDirectories)
			{
				comboSourceFolder.add(iPath.toPortableString());
			}

			final String[] items = comboSourceFolder.getItems();
			final String defaultSrcFolderName = Messages.SLASH + selectedProject.getName() + Messages.SLASH
				+ getDefaultSrcFolderName();
			if (!Arrays.asList(items).contains(defaultSrcFolderName))
			{
				comboSourceFolder.add(NEW_SRC_FOLDER_DEFAULT);
			}
		}
		catch (final JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (final CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		if (comboSourceFolder.getItemCount() > 0)
		{
			comboSourceFolder.select(0);
		}

		comboSourceFolder.addListener(SWT.Selection, new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				validatePage();
			}
		});
	}

	/**
	 * Get all the source folders inside a IProject
	 *
	 * @param project
	 * @return array of source paths
	 * @throws CoreException
	 * @throws JavaModelException
	 */
	private static IPath[] getJavaProjectSourceDirectories(final IProject project) throws JavaModelException,
		CoreException
	{
		final List<IPath> paths = new ArrayList<IPath>();

		if (project != null && project.isOpen() && project.hasNature(JavaCore.NATURE_ID))
		{

			final IJavaProject javaProject = JavaCore.create(project);

			IClasspathEntry[] classpathEntries = null;

			classpathEntries = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < classpathEntries.length; i++)
			{
				final IClasspathEntry entry = classpathEntries[i];

				if (entry.getContentKind() == IPackageFragmentRoot.K_SOURCE)
				{
					final IPath path = entry.getPath();
					paths.add(path);
				}
			}
		}

		return paths.toArray(new IPath[0]);
	}

	/**
	 * Validate page
	 */
	private void validatePage()
	{
		if (!validateSrcFolderCombo())
		{
			return;
		}

		final boolean hasClassName = !textBoxClassName.getText().isEmpty();

		if (!hasClassName)
		{
			setErrorMessage(Messages.ENTER_CLASS_NAME);
			setPageComplete(false);
			return;
		}

		final boolean hasPackageName = !textBoxPackageName.getText().isEmpty();

		if (!hasPackageName)
		{
			setErrorMessage(Messages.ENTER_PACKAGE);
			setPageComplete(false);
			return;
		}

		// class name regex
		final String regexClassName = Messages.CLASS_NAME_REGEX;
		final boolean classNameMatchesExpression = hasClassName && textBoxClassName.getText().matches(regexClassName);

		if (!classNameMatchesExpression)
		{
			setErrorMessage(Messages.ENTER_VALID_CLASSNAME);
			setPageComplete(false);
			return;
		}

		// package name regex
		final String regexPackageName = Messages.PACKAGE_NAME_REGEX;
		final boolean packageNameMatchesExpression = hasPackageName
			&& textBoxPackageName.getText().matches(regexPackageName);

		if (!packageNameMatchesExpression)
		{
			setErrorMessage(Messages.INVALID_PACKAGE);
			setPageComplete(false);
			return;
		}

		final String filePath = getFilePath();

		final boolean fileConflict = !ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath)).exists();

		if (!fileConflict)
		{
			setErrorMessage(Messages.CLASS_ALREADY_EXISTS_IN_FOLDER);
			setPageComplete(false);
			return;
		}

		setPageComplete(true);
		setErrorMessage(null);
	}

	private boolean validateSrcFolderCombo()
	{
		if (comboSourceFolder.getItemCount() == 0)
		{
			setErrorMessage(Messages.NO_SOURCE_FOLDERS);
			setPageComplete(false);
			return false;
		}

		final boolean hasSelection = (comboSourceFolder.getText().length() > 0);

		if (!hasSelection)
		{
			setErrorMessage(Messages.SELECT_SOURCE_FOLDER);
			setPageComplete(false);
			return false;
		}

		return true;
	}

	/**
	 * Get class name
	 *
	 * @return class name
	 */
	public String getClassName()
	{
		if (textBoxClassName.getText().length() > 0)
		{
			return Character.toUpperCase(textBoxClassName.getText().charAt(0))
				+ textBoxClassName.getText().substring(1);
		}
		else
		{
			return Messages.EMPTY_STRING;
		}
	}

	/**
	 * Get package name
	 *
	 * @return package name
	 */
	public String getPackageName()
	{
		return textBoxPackageName.getText();
	}

	/**
	 * Get source folder
	 *
	 * @return source folder
	 */
	public String getSourceFolder()
	{
		String selectedSrcFolder = comboSourceFolder.getText();
		if (selectedSrcFolder.equals(NEW_SRC_FOLDER_DEFAULT))
		{
			selectedSrcFolder = selectedProject + Messages.SLASH + getDefaultSrcFolderName();
		}
		return selectedSrcFolder;
	}

	private static String getDefaultSrcFolderName()
	{
		// JDT default Java preferences
		final IPreferenceStore store = PreferenceConstants.getPreferenceStore();
		String selectedSrcFolder = store.getString(PreferenceConstants.SRCBIN_SRCNAME);

		return selectedSrcFolder;
	}

	/**
	 * @return File path
	 */
	public String getFilePath()
	{
		return getSourceFolder() + Messages.SLASH + getPackageName() + Messages.SLASH + getClassName()
			+ Messages.JAVA_EXT;
	}

	/**
	 * @param project
	 */
	public void setProject(final IProject project)
	{
		selectedProject = project;

	}
}
