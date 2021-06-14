/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * May 29, 2013 5:23:05 PM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.security.descriptor.MMDescriptorUtils;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.ct.workspace.internal.jvmparameters.JVMConstants;
import eu.cessar.ct.workspace.internal.jvmparameters.JVMParameter;

// TODO: Auto-generated Javadoc
/**
 * Class in charge of updating the cessar-ct.ini file
 *
 * @author uidv3687
 *
 *         %created_by: uidw8762 %
 *
 *         %date_created: Mon Apr 14 14:38:33 2014 %
 *
 *         %version: 7 %
 */
public final class CessarIniFileUpdater
{
	// pattern matching the format 4.0.3
	/** The Constant REGEX_PATTERN. */
	private static final String REGEX_PATTERN = "\\d\\.\\d\\.\\d$"; //$NON-NLS-1$

	// string specific to INI file, used to identify if INI file represents a product configuration
	/** The Constant LAUNCHER_LIBRARY_ARG. */
	private static final String LAUNCHER_LIBRARY_ARG = "--launcher.library"; //$NON-NLS-1$

	/** The Constant LAUNCHER_LIBRARY_PATH. */
	private static final String LAUNCHER_LIBRARY_PATH = "org.eclipse.equinox.launcher"; //$NON-NLS-1$

	/** The Constant TEMP_FILE. */
	private static final String TEMP_FILE = "temp.ini"; //$NON-NLS-1$

	/**
	 * Instantiates a new cessar ini file updater.
	 */
	private CessarIniFileUpdater()
	{
		// avoid outside instantiation
	}

	/**
	 * Search the corresponding .ini file and update the AUTOSAR 3x and 4x properties
	 *
	 * @param metaModel3x
	 *        new value
	 * @param metaModel4x
	 *        new value
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	public static void updateMetamodelsOnIniFile(String metaModel3x, String metaModel4x) throws FileNotFoundException
	{
		List<String> files = getIniFiles();

		if (files.size() == 0)
		{
			throw new FileNotFoundException(NLS.bind(Messages.JVMParameters_noINIFileFound,
				Platform.getInstallLocation().getURL().getPath()));
		}
		for (String file: files)
		{
			updateMetamodels(metaModel3x, metaModel4x, file, TEMP_FILE);
		}
	}

	/**
	 * Update JVM parameters on ini file.
	 *
	 * @param jvmParameters
	 *        the JVM parameters
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	public static void updateJVMParametersOnIniFile(Map<String, String> jvmParameters) throws FileNotFoundException
	{
		List<String> files = getIniFiles();

		if (files.size() == 0)
		{
			throw new FileNotFoundException(NLS.bind(Messages.JVMParameters_noINIFileFound,
				Platform.getInstallLocation().getURL().getPath()));
		}

		for (String file: files)
		{
			updateJVMParameters(jvmParameters, file);
		}
	}

	/**
	 * Update JVM parameter on ini file.
	 *
	 * @param jvmParameter
	 *        the jvm parameter
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	public static void updateJVMParameterOnIniFile(JVMParameter jvmParameter) throws FileNotFoundException
	{
		List<String> files = getIniFiles();
		// Test only...
		// List<String> files = getJVMConfigurationFiles();

		if (files.size() == 0)
		{
			throw new FileNotFoundException(NLS.bind(Messages.JVMParameters_noINIFileFound,
				Platform.getInstallLocation().getURL().getPath()));
		}

		for (String file: files)
		{
			updateJVMParameter(jvmParameter, file);
		}
	}

	/**
	 * Gets the INI files under installation dir which contain the <code>LAUNCHER_LIBRARY_ARG</code> and
	 * <code>LAUNCHER_LIBRARY_PATH</code>.
	 *
	 * @return a list of valid INI files
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	private static List<String> getIniFiles() throws FileNotFoundException
	{
		Location location = Platform.getInstallLocation();
		File directory = new File(location.getURL().getFile());

		List<String> fileList = new ArrayList<String>();
		if (directory.isDirectory())
		{
			String[] fileNames = directory.list(new FilenameFilter()
			{

				public boolean accept(File dir, String name)
				{
					return name.endsWith(".ini"); //$NON-NLS-1$
				}
			});

			for (String file: fileNames)
			{
				if (isRelevantIniFile(directory.getAbsolutePath() + File.separator + file))
				{
					fileList.add(directory.getAbsolutePath() + File.separator + file);
				}
			}
		}
		return fileList;
	}

	/**
	 * Gets the jVM configuration files.
	 *
	 * @return the jVM configuration files
	 */
	private static List<String> getJVMConfigurationFiles()
	{
		List<String> fileList = new ArrayList<String>();

		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
		String workspaceLocation = wsRoot.getLocation().toOSString();

		workspaceLocation = workspaceLocation.concat(File.separator);
		workspaceLocation = workspaceLocation.concat("CESSAR-CT.ini"); //$NON-NLS-1$
		fileList.add(workspaceLocation);

		// Add other initialization files if they have to be considered
		// ...

		return fileList;
	}

	/**
	 * Check if <code>file</code> contain relevant string for product configuration INI file.
	 *
	 * @param file
	 *        the file
	 * @return true, if is relevant ini file
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	private static boolean isRelevantIniFile(String file) throws FileNotFoundException
	{
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(new FileReader(file));
			String currentLine;
			boolean foundLibraryArg = false;
			while (scanner.hasNextLine())
			{
				currentLine = scanner.nextLine();
				if (foundLibraryArg)
				{
					if (0 <= currentLine.indexOf(LAUNCHER_LIBRARY_PATH))
					{
						return true;
					}
					else if (currentLine.isEmpty())
					{
						continue;
					}
					// the next not empty line after LAUNCHER_LIBRARY_ARG has to be LAUNCHER_LIBRARY_PATH
					return false;
				}
				else
				{
					if (0 <= currentLine.indexOf(LAUNCHER_LIBRARY_ARG))
					{
						foundLibraryArg = true;
					}
				}
			}
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
		return false;
	}

	/**
	 * Update the AUTOSAR 3x and 4x properties from xxx.ini file with values <code>metaModel3x</code> and
	 * <code>metaModel4x</code>
	 *
	 * @param metaModel3x
	 *        new value
	 * @param metaModel4x
	 *        new value
	 * @param origFile
	 *        original file keeping the configuration
	 * @param tempFile
	 *        temporary file keeping the updated configuration
	 * @throws FileNotFoundException
	 *         the file not found exception
	 */
	public static void updateMetamodels(String metaModel3x, String metaModel4x, String origFile, String tempFile)
		throws FileNotFoundException
	{
		BufferedReader br = null;
		BufferedWriter bw = null;

		br = new BufferedReader(new FileReader(origFile));

		File oldFile = new File(origFile);
		String absolutePath = oldFile.getAbsolutePath();

		int lastIndexOf = absolutePath.lastIndexOf("\\"); //$NON-NLS-1$
		String substring = absolutePath.substring(0, lastIndexOf + 1);
		tempFile = substring + tempFile;

		try
		{
			bw = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean foundMM3x = false;
			boolean foundMM4x = false;

			while ((line = br.readLine()) != null)
			{
				if (!line.isEmpty())
				{
					if (line.contains(MMDescriptorUtils.SYS_PROP_MM3X))
					{

						line = line.replaceFirst(REGEX_PATTERN, metaModel3x);

						foundMM3x = true;
					} // for metamodel 4 one of the two versions can be used
					else if (line.contains(MMDescriptorUtils.SYS_PROP_MM40)
						|| line.contains(MMDescriptorUtils.SYS_PROP_MM4X))
					{
						line = line.replaceFirst(REGEX_PATTERN, metaModel4x);

						foundMM4x = true;
					}

					bw.write(line + "\n"); //$NON-NLS-1$
				}
			}

			if (!foundMM3x)
			{
				bw.write("-D" + MMDescriptorUtils.SYS_PROP_MM3X + "=" + metaModel3x + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			if (!foundMM4x)
			{
				bw.write("-D" + MMDescriptorUtils.SYS_PROP_MM4X + "=" + metaModel4x + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			try
			{
				if (bw != null)
				{
					bw.close();
				}
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}

		replaceOldIniFile(origFile, tempFile);
	}

	/**
	 * Replace old ini file.
	 *
	 * @param origFile
	 *        the orig file
	 * @param tempFile
	 *        the temp file
	 */
	private static void replaceOldIniFile(String origFile, String tempFile)
	{
		// Once everything is complete, delete original file..
		File oldFile = new File(origFile);
		oldFile.delete();

		// And rename tmp file's name to original file name
		File newFile = new File(tempFile);
		newFile.renameTo(oldFile);
	}

	/**
	 * Update JVM parameters.
	 *
	 * @param jvmParameters
	 *        the jvm parameters
	 * @param iniFile
	 *        the parameter configuration file
	 */
	private static void updateJVMParameters(Map<String, String> jvmParameters, String iniFile)
	{
		BufferedReader br = null;
		StringBuilder sbTextLines = null;

		try
		{
			br = new BufferedReader(new FileReader(iniFile));
			sbTextLines = new StringBuilder();
			String line = ""; //$NON-NLS-1$

			while ((line = br.readLine()) != null)
			{
				if (!line.isEmpty())
				{
					for (int i = 0; i < JVMConstants.JVMParameterList.length; i++)
					{
						if (line.toUpperCase().contains(JVMConstants.JVMParameterList[i].toUpperCase()))
						{
							line = replaceAllSubstrings(line, JVMConstants.JVMParameterList[i],
								jvmParameters.get(JVMConstants.JVMParameterList[i]));
						}
					}
					if (!line.isEmpty())
					{
						line = line.trim();
						sbTextLines.append(line);
						sbTextLines.append("\n"); //$NON-NLS-1$
					}
				}
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}

		if (sbTextLines != null)
		{
			File oldIniFile = new File(iniFile);
			oldIniFile.delete();

			FileWriter fw = null;
			String fileContent = ""; //$NON-NLS-1$
			try
			{
				fileContent = sbTextLines.toString();
				fw = new FileWriter(iniFile);
				fw.write(fileContent);
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			finally
			{
				try
				{
					fw.close();
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	/**
	 * Update JVM parameter.
	 *
	 * @param jvmParameter
	 *        the jvm parameter
	 * @param iniFile
	 *        the ini file
	 */
	public static void updateJVMParameter(JVMParameter jvmParameter, String iniFile)
	{
		BufferedReader br = null;
		StringBuilder sbTextLines = null;

		try
		{
			br = new BufferedReader(new FileReader(iniFile));
			sbTextLines = new StringBuilder();
			String line = ""; //$NON-NLS-1$

			while ((line = br.readLine()) != null)
			{
				if (!line.isEmpty())
				{
					if (line.toUpperCase().contains(jvmParameter.getParameterName().toUpperCase()))
					{
						line = replaceAllSubstrings(line, jvmParameter.getParameterName(),
							jvmParameter.getFileParameterValue());
					}
					if (!line.isEmpty())
					{
						line = line.trim();
						sbTextLines.append(line);
						sbTextLines.append("\n"); //$NON-NLS-1$
					}
				}
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}

		if (sbTextLines != null)
		{
			File oldIniFile = new File(iniFile);
			oldIniFile.delete();

			FileWriter fw = null;
			String fileContent = ""; //$NON-NLS-1$
			try
			{
				fileContent = sbTextLines.toString();
				fw = new FileWriter(iniFile);
				fw.write(fileContent);
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			finally
			{
				try
				{
					fw.close();
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	/**
	 * Replace substring with a specified string.
	 *
	 * @param enclosingString
	 *        the enclosing string
	 * @param oldString
	 *        the old string
	 * @param newString
	 *        the new string
	 * @return the string result of replacement
	 */
	private static String replaceAllSubstrings(String enclosingString, String oldString, String newString)
	{
		String result = ""; //$NON-NLS-1$

		if ((enclosingString != null) && (oldString != null) && (newString != null))
		{
			String regExp = ".*(" + oldString + ").*"; //$NON-NLS-1$ //$NON-NLS-2$
			result = enclosingString.replaceAll(regExp, newString);
		}

		return result;
	}

}
