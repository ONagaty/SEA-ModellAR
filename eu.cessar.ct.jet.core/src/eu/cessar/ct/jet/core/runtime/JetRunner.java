package eu.cessar.ct.jet.core.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;

import eu.cessar.ct.jet.core.JETCoreConstants;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.AbstractCommandLineRunner;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * 
 * Executes jets from the command line, with the following arguments:
 * <ul>
 * <li>
 * -project project_name the name of the project, have to be available inside
 * the workspace</li>
 * <li>
 * [-all] if given, all jets will be generated</li>
 * <li>
 * [-output path/to/output/folder] path to the output folder (optional).
 * Relative or absolute path are accepted</li>
 * <li>[jet1 jet2 ...] a list of jet files to generate. No dependency checks
 * will be performed</li>
 * </ul>
 * 
 * 
 */
@SuppressWarnings("nls")
public class JetRunner extends AbstractCommandLineRunner
{
	// arguments
	private static final String ARG_ALL_JETS = "all";
	private static final String ARG_ALL_JETS_DESC = "generate all jets";

	private static final String ARG_JET = "jet";
	private static final String ARG_JET_NAME = "jet1 jet2 ...";
	private static final String ARG_JET_DESC = "list of jet files to generate. No dependency checks will be performed";

	private static final String ARG_OUTPUT = "output";
	private static final String ARG_OUTPUT_NAME = "path/to/output/folder";
	private static final String ARG_OUTPUT_DESC = "path to the output folder (optional).Relative or absolute path are accepted";

	// arguments values
	private IFolder outputFolder;
	private List<IFile> compiledJetFiles;

	// key from the map passed to the jet manager
	private static final String KEY_OUTPUT_FOLDER = "output.folder";

	/**
	 * @return
	 */
	@Override
	@SuppressWarnings("static-access")
	protected Options getOptions()
	{
		Options options = super.getOptions();
		Option outputFolderName = OptionBuilder.withArgName(ARG_OUTPUT_NAME).hasArg().withDescription(
			ARG_OUTPUT_DESC).create(ARG_OUTPUT);
		Option jetNames = OptionBuilder.withArgName(ARG_JET_NAME).hasArgs().withDescription(
			ARG_JET_DESC).create(ARG_JET);

		options.addOption(outputFolderName);
		options.addOption(ARG_ALL_JETS, false, ARG_ALL_JETS_DESC);
		options.addOption(jetNames);

		return options;
	}

	/**
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	@Override
	protected IStatus processCmdLineOptions(CommandLine commandLine) throws CoreException,
		IOException
	{
		String outputFolderName;
		String[] jetNames;

		IStatus status = super.processCmdLineOptions(commandLine);
		if (!status.isOK())
		{
			return status;
		}

		IProject project = getProject();
		if ((!commandLine.hasOption(ARG_JET) && !commandLine.hasOption(ARG_ALL_JETS))
			|| (commandLine.hasOption(ARG_JET) && commandLine.hasOption(ARG_ALL_JETS)))
		{
			printUsage();
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"Wrong arguments(exactly one of the -jet/-all argument has to be given)!", null);
		}

		outputFolderName = commandLine.getOptionValue(ARG_OUTPUT);
		if (outputFolderName != null)
		{
			outputFolder = project.getFolder(outputFolderName);
		}

		if (commandLine.hasOption(ARG_ALL_JETS))
		{
			compiledJetFiles = new ArrayList<IFile>();
			// retrieve all the jet files from the project

			List<IFile> allJets = new ArrayList<IFile>();
			JETCoreUtils.collectJETFiles(new Object[] {project}, allJets);
			JETCoreUtils.collectCompiledJETFiles(new Object[] {project}, compiledJetFiles);
			for (IFile jet: allJets)
			{
				IFile jarFile = JETCoreUtils.getJarFile(jet);
				if (jarFile == null || !compiledJetFiles.contains(jarFile))
				{
					status = new Status(IStatus.WARNING, CessarPluginActivator.PLUGIN_ID, EXIT_FILE_NOT_FOUND,
						"JAR not found for:" + jet.getName(), null);
				}
			}
		}
		else if (commandLine.hasOption(ARG_JET))
		{
			IFile foundJet = null;
			jetNames = commandLine.getOptionValues(ARG_JET);
			if (jetNames == null)
			{
				// doesn't have -jet, nor -all
				printUsage();
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
					"Wrong arguments (Exactly one of the -jet/-all argument has to be given)!",
					null);
			}
			compiledJetFiles = new ArrayList<IFile>();
			for (int i = 0; i < jetNames.length; i++)
			{
				int index = jetNames[i].lastIndexOf(".");
				if (index == -1)
				{
					return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_FILE_NOT_FOUND,
						"File " + jetNames[i] + " not found!", null);
				}
				String extension = jetNames[i].substring(index + 1);
				if (extension.equals("jar") || extension.equals("dbgjar"))
				{
					foundJet = locateFile(project, jetNames[i]);
				}
				else
				{
					String jarFileName = JETCoreUtils.getJarFileName(jetNames[i]);
					foundJet = locateFile(project, jarFileName);
					if (foundJet != null && !JETCoreUtils.isJetJarFile(foundJet))
					{
						foundJet = null;
					}
				}
				if (foundJet != null)
				{
					compiledJetFiles.add(foundJet);
				}
				else
				{
					status = new Status(IStatus.WARNING, CessarPluginActivator.PLUGIN_ID, EXIT_FILE_NOT_FOUND,
						"JAR not found for: " + jetNames[i], null);
				}
			}
		}

		return status;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IStatus doExecute() throws CoreException
	{
		IStatus status = null;
		ICessarTaskManager manager;
		// wait for build
		ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
			new NullProgressMonitor());
		IJobManager jobMan = Job.getJobManager();
		boolean doWait = true;
		while (doWait)
		{
			try
			{
				jobMan.join(ResourcesPlugin.FAMILY_AUTO_REFRESH, new NullProgressMonitor());
				jobMan.join(ResourcesPlugin.FAMILY_AUTO_BUILD, new NullProgressMonitor());
				jobMan.join(ResourcesPlugin.FAMILY_MANUAL_BUILD, new NullProgressMonitor());
				doWait = false;
			}
			catch (InterruptedException ie)
			{
				// ignore
			}
		}

		// populate map
		Map<String, Object> map = new HashMap<String, Object>();
		if (outputFolder != null)
		{
			map.put(KEY_OUTPUT_FOLDER, outputFolder);
		}
		manager = ExecutionService.createManager(getProject(), JETCoreConstants.JET, map);
		if (manager != null)
		{
			manager.initialize(compiledJetFiles);
			status = manager.execute(false, null, new NullProgressMonitor());
		}

		return status;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#getRunnerName()
	 */
	@Override
	protected String getRunnerName()
	{
		return "JetRunner";
	}
}
