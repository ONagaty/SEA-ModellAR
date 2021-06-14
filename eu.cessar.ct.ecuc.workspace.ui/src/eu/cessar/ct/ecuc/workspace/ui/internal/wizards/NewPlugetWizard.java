/**
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;

import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.NewPlugetPage;

/**
 * This is a simple wizard for creating a new pluget.
 *
 * @generated
 */
@SuppressWarnings("javadoc")
public class NewPlugetWizard extends Wizard implements IWorkbenchWizard
{
	private static final String PROJECT_EXPLORER_VIEW_ID = "org.eclipse.ui.navigator.ProjectExplorer"; //$NON-NLS-1$

	private NewPlugetPage newPlugetPage;
	private IProject selectedProject;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		selectedProject = ((IResource) selection.getFirstElement()).getProject();
	}

	@Override
	public void addPages()
	{
		newPlugetPage = new NewPlugetPage(Messages.NEW_PLUGET_PAGE);
		newPlugetPage.setProject(selectedProject);

		addPage(newPlugetPage);
	}

	@Override
	public String getWindowTitle()
	{
		return Messages.NEW_CESSAR_CT_PLUGET;
	}

	@Override
	public boolean performFinish()
	{
		if (selectedProject == null)
		{
			return false;
		}

		try
		{
			createSrcFolderIfNotExists(newPlugetPage.getSourceFolder());

			String filePath = newPlugetPage.getFilePath();

			String folderPath = new Path(filePath.substring(1, filePath.lastIndexOf(Messages.SLASH))).toString();

			IFolder iFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(folderPath));
			if (!iFolder.exists())
			{
				iFolder.create(true, true, null);
			}

			createPlugetFile(filePath);

		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (URISyntaxException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return true;
	}

	private void createPlugetFile(String filePath) throws IOException, URISyntaxException, CoreException
	{
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
			new Path(filePath.substring(1, filePath.length())));

		Bundle bundle = CessarPluginActivator.getDefault().getBundle();

		String templatePath;
		if (newPlugetPage.isSimpleTemplate())
		{
			templatePath = Messages.PATH_TO_TEMPLATE_SIMPLE;
		}
		else
		{
			templatePath = Messages.PATH_TO_TEMPLATE_ADVANCED;
		}

		URL bundleURL = bundle.getEntry(templatePath);
		URL resolvedFileURL;
		resolvedFileURL = FileLocator.toFileURL(bundleURL);

		if (resolvedFileURL != null)
		{
			URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
			File templateFile = new File(resolvedURI);

			String content = getContents(templateFile);

			content = content.replace(Messages.CLASS_NAME, newPlugetPage.getClassName());
			content = content.replace(Messages.PACKAGE_NAME, newPlugetPage.getPackageName());

			InputStream inputStream = new ByteArrayInputStream(content.getBytes());

			iFile.create(inputStream, true, new NullProgressMonitor());

			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart projectExplorer = activePage.findView(PROJECT_EXPLORER_VIEW_ID);
			if (projectExplorer != null && activePage.isPartVisible(projectExplorer))
			{
				projectExplorer.getSite().getSelectionProvider().setSelection(new StructuredSelection(iFile));
			}

			IDE.openEditor(activePage, iFile);
		}
	}

	private String createSrcFolderIfNotExists(String srcFolderName)
	{
		IClasspathEntry srcEntry = null;
		IFolder folder = null;

		try
		{
			if (selectedProject != null && selectedProject.isOpen() && selectedProject.hasNature(JavaCore.NATURE_ID))
			{
				IPath path = new Path(new Path(srcFolderName).lastSegment());
				folder = selectedProject.getFolder(path);
				if (!folder.exists())
				{
					folder.create(true, true, null);
				}

				IJavaProject javaProject = JavaCore.create(selectedProject);

				IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
				IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
				for (IClasspathEntry iClasspathEntry: oldEntries)
				{
					if (iClasspathEntry.getPath().equals(root.getPath()))
					{
						return root.getPath().toPortableString();
					}
				}
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
				srcEntry = JavaCore.newSourceEntry(root.getPath());
				newEntries[oldEntries.length] = srcEntry;
				javaProject.setRawClasspath(newEntries, null);
			}
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		Assert.isNotNull(folder);
		Assert.isTrue(folder.exists());

		Assert.isNotNull(srcEntry);
		String portableString = srcEntry.getPath().toPortableString();

		return portableString;
	}

	/**
	 * Get the contents of a file
	 *
	 * @param aFile
	 * @return
	 */
	static public String getContents(File aFile)
	{

		StringBuilder contents = new StringBuilder();

		try
		{

			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try
			{
				String line = null; // not declared within while loop

				while ((line = input.readLine()) != null)
				{
					contents.append(line);
					contents.append(System.getProperty(Messages.LINE_SEPARATOR));
				}
			}
			finally
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			CessarPluginActivator.getDefault().logError(ex);
		}

		return contents.toString();
	}
}
