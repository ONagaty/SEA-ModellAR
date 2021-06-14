package eu.cessar.ct.runtime.execution;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * Abstract implementation of a command line runner.
 * 
 */
@SuppressWarnings("nls")
public abstract class AbstractCommandLineRunner implements IApplication
{
	private Options options;

	// arguments
	protected static final String ARG_PROJECT = "project";
	private static final String ARG_PROJECT_NAME = "project_name";
	private static final String ARG_PROJECT_DESC = "the name of the project, have to be available inside the workspace";

	// arguments values
	private IProject project;

	// error codes
	private static final int EXIT_NO_VALUE = -1;
	public static final int EXIT_WRONG_ARG = 1;
	public static final int EXIT_PROJ_NOT_FOUND = 2;
	public static final int EXIT_FILE_NOT_FOUND = 3;
	public static final int EXIT_EXECUTION_EXCEPTION = 4;
	public static final int EXIT_PARSING_FAILED = 5;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception
	{
		Object argumentsObj = context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		if (argumentsObj == null || !argumentsObj.getClass().isArray()
			|| !argumentsObj.getClass().getComponentType().equals(String.class))
		{
			// no arguments given
			printUsage();
			return EXIT_WRONG_ARG;
		}
		String[] args = (String[]) argumentsObj;

		// create the parser
		CommandLineParser parser = new GnuParser();
		CommandLine commandLine;
		try
		{
			// parse the command line arguments, allow unknown arguments, they are normal to exist
			// 3rd parameter, stopAtNonOption specifies whether to continue parsing the arguments if a non option is
			// encountered
			commandLine = parser.parse(getOptions(), args, false);
		}
		catch (ParseException exp)
		{
			CessarPluginActivator.getDefault().logError(exp);
			System.out.println("Cannot parse command line, check the eclipse log for details");
			printUsage();
			return EXIT_PARSING_FAILED;
		}

		int returnStatus = EXIT_NO_VALUE;
		IStatus status = processCmdLineOptions(commandLine);
		if (!status.isOK())
		{
			printStatus(status);
			if (status.getSeverity() == IStatus.ERROR)
			{
				returnStatus = status.getCode();
			}
		}
		if (returnStatus == EXIT_NO_VALUE)
		{
			status = execute();
			if (!status.isOK())
			{
				printStatus(status);
				returnStatus = EXIT_EXECUTION_EXCEPTION;
			}
		}

		if (returnStatus == EXIT_NO_VALUE)
		{
			returnStatus = EXIT_OK;
		}

		return returnStatus;
	}

	/**
	 * @param status
	 */
	public void printStatus(IStatus status)
	{
		int severity = status.getSeverity();
		String severityMsg = "";
		if (severity == IStatus.OK)
		{
			severityMsg = "OK"; //$NON-NLS-1$
		}
		else if (severity == IStatus.ERROR)
		{
			severityMsg = "ERROR"; //$NON-NLS-1$
		}
		else if (severity == IStatus.WARNING)
		{
			severityMsg = "WARNING"; //$NON-NLS-1$
		}
		else if (severity == IStatus.INFO)
		{
			severityMsg = "INFO"; //$NON-NLS-1$
		}
		else if (severity == IStatus.CANCEL)
		{
			severityMsg = "CANCEL"; //$NON-NLS-1$
		}
		System.out.println(severityMsg + "(" + status.getCode() + "): " + status.getMessage());

		Throwable t = status.getException();
		if (t != null)
		{
			t.printStackTrace(System.out);
		}

	}

	/**
	 * @return
	 */
	@SuppressWarnings("static-access")
	protected Options getOptions()
	{
		options = new Options();

		Option projectName = OptionBuilder.withArgName(ARG_PROJECT_NAME).hasArg().withDescription(ARG_PROJECT_DESC).create(
			ARG_PROJECT);

		options.addOption(projectName);
		return options;
	}

	/**
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	protected IStatus processCmdLineOptions(CommandLine commandLine) throws CoreException, IOException
	{
		String projectName;

		if (!commandLine.hasOption(ARG_PROJECT))
		{
			printUsage();
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"Wrong arguments(the project_name argument is missing)!", null);
		}
		projectName = commandLine.getOptionValue(ARG_PROJECT);
		project = locateProject(projectName);
		if (project == null)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_PROJ_NOT_FOUND, "Project "
				+ projectName + " not found!", null);
		}

		return Status.OK_STATUS;
	}

	/**
	 * @return
	 * @throws CoreException
	 */
	protected IStatus execute() throws CoreException
	{
		IStatus status;
		ILogger logger = null;

		try
		{
			logger = LoggerFactory.getLogger();
			logger.attachStream(System.out);
			status = doExecute();
		}
		finally
		{
			if (logger != null)
			{
				logger.detachStream(System.out);
			}
		}
		return status;
	}

	/**
	 * @throws CoreException
	 * 
	 */
	protected abstract IStatus doExecute() throws CoreException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	public IProject getProject()
	{
		return project;
	}

	/**
	 * 
	 * @return
	 */
	protected abstract String getRunnerName();

	/**
	 * 
	 */
	protected void printUsage()
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(getRunnerName(), options);
	}

	/**
	 * Locates a project with the given name, in the workspace.
	 * 
	 * @param projectName
	 *        the name of the project to locate
	 * @return the found project
	 * @throws CoreException
	 */
	protected IProject locateProject(final String projectName) throws CoreException
	{
		// check the number of segments first
		Path prjPath = new Path(projectName);
		IProject project = null;
		assert (prjPath.segmentCount() == 1);

		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project.exists())
		{
			if (!project.isOpen())
			{
				project.open(new NullProgressMonitor());
			}
		}
		else
		{
			project = null;
		}
		return project;
	}

	/**
	 * Locates a file with the given name, in the given project
	 * 
	 * @param project
	 *        the project where the resource is located
	 * @param fileName
	 *        the name of the file
	 * @return the found file
	 * @throws CoreException
	 */
	protected IFile locateFile(final IProject project, final String fileName) throws CoreException
	{
		Path jetPath = new Path(fileName);
		IFile jetIFile = project.getFile(jetPath);
		if (jetIFile.exists())
		{
			return jetIFile;
		}

		// maybe it's a full file name
		File jetFile = new File(fileName);
		if (!jetFile.exists() || !jetFile.isFile())
		{
			return null;
		}
		try
		{
			jetPath = new Path(jetFile.getCanonicalPath());
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getLocalizedMessage(),
				e));
		}
		IResource resource = ResourceUtils.getResourceForLocation(project, jetPath);

		if (resource.getType() == IResource.FILE)
		{
			// file found
			jetIFile = (IFile) resource;
		}
		else
		{
			jetIFile = null;
		}

		return jetIFile;
	}

}
