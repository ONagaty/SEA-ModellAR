/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw6647<br/>
 * May 27, 2015 5:12:21 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.ui.internal.Messages;

/**
 * @author uidw6647
 *
 */
public class ExportPlugetWizard extends Wizard implements IExportWizard
{

	/**
	 *
	 */
	private static final String CLASS_EXT = ".class"; //$NON-NLS-1$
	/**
	 *
	 */
	private static final String JAVA_EXT = ".java"; //$NON-NLS-1$
	/**
	 *
	 */
	private static final String EXPORT_PLUGET_TEMP_FOLDER = "ExportPlugetTemp"; //$NON-NLS-1$
	private IProject thisProject;
	private ExportPlugetPage page;
	private String plugetName;

	/**
	 * @param project
	 * @param plugetFileName
	 */
	public ExportPlugetWizard(IProject project, String plugetFileName)
	{
		thisProject = project;
		plugetName = plugetFileName;
		setWindowTitle(Messages.EXPORT_PLUGET_WIZARD_SHELL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages()
	{
		super.addPages();
		page = new ExportPlugetPage(thisProject, plugetName, Messages.EXPORT_PLUGET_WIZARD_TITLE);
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */

	@Override
	public boolean performFinish()
	{
		String destinationName = page.getDestinationName();
		CessarPluginActivator.getDefault().getDialogSettings().put(
			ExportPlugetPage.EXPORT_PLUGET_LASTNAME + plugetName, destinationName);
		try
		{
			File destFile = new File(destinationName);

			if (destFile.exists() && !destFile.delete())
			{
				MessageDialog.openError(getShell(),
					"Error", "File " + destinationName + " is using and cannot be overwritten "); //$NON-NLS-1$
				return false;
			}

			getContainer().run(
				true,
				true,
				new PlugetCreationWithProgreess(destinationName, page.getJars(), page.getSourceFolders(),
					page.getVersion()));
		}
		catch (InvocationTargetException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return true;
	}

	private class PlugetCreationWithProgreess implements IRunnableWithProgress
	{

		private String destinationName;
		private List<String> jars;
		private List<String> src;
		private String version;

		public PlugetCreationWithProgreess(String destinationName, List<String> jars, List<String> src, String version)
		{
			this.src = src;
			this.destinationName = destinationName;
			this.jars = jars;
			this.version = version;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
		{
			monitor.beginTask(Messages.EXPORT_PLUGET_WIZARD_TITLE, 100);

			try
			{
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CREATE_TMP_DIR);
				// Create Temporary folder to prepare the pluget creation
				File file = File.createTempFile(EXPORT_PLUGET_TEMP_FOLDER, ""); //$NON-NLS-1$
				file.delete();
				file.mkdir();

				// Copy compiled classes from project
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CPY_PLUGET_CLASS);
				copyCompiledSources(file, src);
				monitor.worked(6);

				// Copy other files and jar files to be added to the pluget
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CPY_FILES_JARS);
				copyOrExtract(file, jars, monitor);
				monitor.worked(80);
				// Delete manifes file from extracted jars
				File possibleManifest = new File(file.toString() + "\\META-INF"); //$NON-NLS-1$
				if (possibleManifest.exists())
				{
					FileUtils.deleteDirectory(possibleManifest);
				}

				// Create Manifest file for pluget
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CREATE_PLUGET);
				Manifest manifest = new Manifest();
				manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0"); //$NON-NLS-1$
				manifest.getMainAttributes().put(Attributes.Name.SPECIFICATION_VERSION, version);
				// Create plugetFile
				JarOutputStream target = new JarOutputStream(new FileOutputStream(destinationName), manifest);
				add(file, target, file.getPath());

				target.close();
				// Delete temporary directory
				monitor.worked(90);
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_DEL_TMP_DIR);
				monitor.worked(100);
				FileUtils.deleteDirectory(file);
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			catch (JavaModelException e1)
			{
				CessarPluginActivator.getDefault().logError(e1);
			}

			monitor.done();

		}
	}

	private void copyCompiledSources(File destDir, List<String> sources) throws JavaModelException, IOException
	{
		IPath outputLocation = JavaCore.create(thisProject).getOutputLocation();
		String osString = thisProject.getFolder(outputLocation.lastSegment()).getLocation().toOSString();
		for (String string: sources)
		{
			File src = new File(string);
			File[] listFiles = src.listFiles();
			copyRecusivlyFiles(destDir, string, osString, listFiles);
		}
	}

	// Copy only class files that come from the specified source folder
	private void copyRecusivlyFiles(File destDir, final String string, String osString, File[] listFiles)
		throws IOException
	{
		for (final File file: listFiles)
		{
			String relative = file.getAbsolutePath().toString().replace(string, ""); //$NON-NLS-1$
			if (relative.endsWith(JAVA_EXT))
			{
				relative = relative.substring(0, relative.length() - 5) + CLASS_EXT;
			}

			File classFile = new File(osString + relative);
			if (classFile.exists())
			{
				if (classFile.isDirectory())
				{
					String nextFolder = "\\" + classFile.getName(); //$NON-NLS-1$
					File[] recursiveList = file.listFiles();
					File nexDest = new File(destDir.getAbsolutePath() + nextFolder);
					copyRecusivlyFiles(nexDest, string + nextFolder, osString + nextFolder, recursiveList);
				}
				else
				{
					FileUtils.copyFileToDirectory(classFile, destDir);
					File directory = new File(osString);
					// (relative.replace(CLASS_EXT, "") + "*" + CLASS_EXT)
					File[] listFiles2 = directory.listFiles(new FilenameFilter()
					{

						@Override
						public boolean accept(File dir, String name)
						{
							String filter = file.getAbsolutePath().toString().replace(string + "\\", ""); //$NON-NLS-1$ //$NON-NLS-2$"
							filter = filter.replace(JAVA_EXT, ""); //$NON-NLS-1$
							if (name.startsWith(filter + "$") && name.endsWith(CLASS_EXT)) //$NON-NLS-1$
							{
								return true;
							}
							return false;
						}
					});
					for (File file2: listFiles2)
					{
						FileUtils.copyFileToDirectory(file2, destDir);
					}

				}
			}
		}
	}

	private void copyOrExtract(File destFile, List<String> jars, IProgressMonitor monitor) throws IOException
	{

		for (String string: jars)
		{
			File file = new File(string);
			if (string.endsWith(ExportPlugetPage.JAR.substring(1)))
			{
				monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_EXTRACTING + file.getName());
				extractJar(file, destFile, monitor);
			}
			else
			{
				if (file.isDirectory())
				{
					monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CPY_DIR + file.getName());
					FileUtils.copyDirectoryToDirectory(file, destFile);
				}
				else
				{
					monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_CPY_FILE + file.getName());
					FileUtils.copyFileToDirectory(file, destFile);
				}
			}
		}
	}

	/**
	 * Extracts a jar
	 *
	 * @param jarFile
	 * @param destFile
	 * @param monitor
	 * @throws IOException
	 */
	private void extractJar(File jarFile, File destFile, IProgressMonitor monitor) throws IOException
	{
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements())
		{
			java.util.jar.JarEntry file = entries.nextElement();
			monitor.subTask(Messages.EXPORT_PLUGET_WIZARD_EXTRACTING + file.getName());

			java.io.File f = new java.io.File(destFile + java.io.File.separator + file.getName());

			if (file.isDirectory())
			{ // if its a directory, create it
				f.mkdir();
				continue;
			}
			else
			{

				f.getParentFile().mkdirs();
			}

			java.io.InputStream is = jar.getInputStream(file); // get the input stream
			java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
			while (is.available() > 0)
			{ // write contents of 'is' to 'fos'
				fos.write(is.read());
			}
			fos.close();
			is.close();
		}
		jar.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// Nothing to do here

	}

	/**
	 * Adds file to jar
	 *
	 * @param source
	 * @param target
	 * @param trim
	 * @throws IOException
	 */
	private void add(File source, JarOutputStream target, String trim) throws IOException
	{
		BufferedInputStream in = null;
		try
		{
			if (source.isDirectory())
			{
				String name = source.getPath().replace(trim, "").replace("\\", "/"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				if (!name.isEmpty())
				{
					if (name.startsWith("/")) //$NON-NLS-1$
					{
						name = name.substring(1, name.length());
					}
					if (!name.endsWith("/")) //$NON-NLS-1$
					{
						name += "/"; //$NON-NLS-1$
					}
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile: source.listFiles())
				{
					add(nestedFile, target, trim);
				}
				return;
			}

			String replace = source.getPath().replace(trim + "\\", "").replace("\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			JarEntry entry = new JarEntry(replace);
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true)
			{
				int count = in.read(buffer);
				if (count == -1)
				{
					break;
				}
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		}
		finally
		{
			if (in != null)
			{
				in.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
	 */
	@Override
	public boolean needsProgressMonitor()
	{
		return true;
	}

}
