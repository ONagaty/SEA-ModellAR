package eu.cessar.ct.runtime.internal.execution.pluget;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;

import eu.cessar.ct.cid.CIDModels;
import eu.cessar.ct.cid.ICIDGlobalStorage;
import eu.cessar.ct.cid.ICIDProjectStorage;
import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.Delivery;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.AbstractCommandLineRunner;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * Runs a pluget from the command line, with the following arguments:
 * <ul>
 * <li>
 * -project project_name the name of the project, have to be available inside the workspace</li>
 * <li>
 * [-class pluget_class] the name of the class to execute, should be a class that implements ICessarPluget in binary
 * name, eg foo.bar.Pluget (opt)</li>
 * <li>
 * [-pluget pluget_file] the relative path to the deployed pluget (opt)</li>
 * <li>
 * [-plugetID plugetID] the ID of the pluget to be deployed from a CID file. It's obtained like
 * deliveryName.artifactName (opt)</li>
 * <li>
 * [arg1 arg2 arg3] additional arguments to pass to the pluget</li>
 * <br>
 * Note: exactly one of the -class/-pluget/-pluginID has to be given
 * </ul>
 */
@SuppressWarnings("nls")
public class PlugetRunner extends AbstractCommandLineRunner
{
	// arguments
	private static final String ARG_CLASS = "class";
	private static final String ARG_CLASS_NAME = "pluget_class";
	private static final String ARG_CLASS_DESC = "the name of the class to execute, should be a class that implements ICessarPluget in binary name, eg foo.bar.Pluget (optional)";

	private static final String ARG_PLUGET = "pluget";
	private static final String ARG_PLUGET_NAME = "pluget_file";
	private static final String ARG_PLUGET_DESC = "the relative path to the deployed pluget (optional, exactly one of the -class/-pluget have to be given)";

	private static final String ARG_PLUGET_ID = "plugetID";
	private static final String ARG_PLUGET_ID_DESC = "Each pluget from a CID has an ID and is stored within a delivery which have also an ID. The two ID combination shall uniquelly locate the pluget.";
	// error codes
	private static final int EXIT_PLUGET_INVALID = 6;
	// arguments values
	private IFile plugetFile;
	private String className;
	private String plugetId;
	private String[] additionalArgs;
	private URL plugetLocation;
	private ILogger logger = LoggerFactory.getLogger();
	private Artifact projectArtifact;
	private Artifact globalArtifact;
	private Delivery projectDelivery;
	private Delivery globalDelivery;
	private IStatus status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#getOptions()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected Options getOptions()
	{
		Options options = super.getOptions();
		Option className = OptionBuilder.withArgName(ARG_CLASS_NAME).hasArg().withDescription(ARG_CLASS_DESC).create(
			ARG_CLASS);
		Option plugetName = OptionBuilder.withArgName(ARG_PLUGET_NAME).hasArg().withDescription(ARG_PLUGET_DESC).create(
			ARG_PLUGET);
		Option plugetIdName = OptionBuilder.withArgName(null).hasArg().withDescription(ARG_PLUGET_ID_DESC).create(
			ARG_PLUGET_ID);
		options.addOption(className);
		options.addOption(plugetName);
		options.addOption(plugetIdName);
		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#processCmdLineOptions()
	 */
	@Override
	protected IStatus processCmdLineOptions(CommandLine commandLine) throws CoreException, IOException
	{
		String opt;

		status = super.processCmdLineOptions(commandLine);
		if (!status.isOK())
		{
			return status;
		}

		if ((!commandLine.hasOption(ARG_CLASS) && !commandLine.hasOption(ARG_PLUGET) && !commandLine.hasOption(ARG_PLUGET_ID))
			|| (commandLine.hasOption(ARG_CLASS) && commandLine.hasOption(ARG_PLUGET) && commandLine.hasOption(ARG_PLUGET_ID)))
		{
			printUsage();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"Wrong arguments(exactly one of the -class/-pluget argument has to be given)!", null);
			return status;
		}

		// check class name
		opt = commandLine.getOptionValue(ARG_CLASS);
		if (opt != null)
		{
			className = opt;

		}
		opt = commandLine.getOptionValue(ARG_PLUGET_ID);
		if (opt != null)
		{
			plugetId = opt;

		}
		status = checkPlugetStatus(commandLine, status);

		return status;

	}

	private IStatus checkPlugetStatus(CommandLine commandLine, IStatus initialStatus) throws CoreException
	{
		String[] additionalOpt;
		String plugetName;

		status = initialStatus;
		boolean statusFound = false;

		IProject project = getProject();
		plugetName = commandLine.getOptionValue(ARG_PLUGET);
		if (plugetName != null)
		{
			plugetFile = locateFile(project, plugetName);
			if (plugetFile == null)
			{
				status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_FILE_NOT_FOUND, "File "
					+ plugetName + " not found!", null);
				statusFound = true;
			}
			else
			{
				IContentType contentType = plugetFile.getContentDescription().getContentType();
				if (contentType == null || !CessarRuntime.PLUGET_CONTENT_TYPE_ID.equals(contentType.getId()))
				{
					status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_PLUGET_INVALID, "Pluget "
						+ plugetName + " invalid!", null);
					statusFound = true;
				}
			}
		}
		else
		{
			if (plugetId != null)
			{ // Obtain the GlobalRepository
				ICIDGlobalStorage cidGlobalStorage = CIDModels.getCIDGlobalStorage();

				// Obtain the CID's from the project
				ICIDProjectStorage cidProjectStorage = CIDModels.getCIDProjectStorage(project);

				String[] split = plugetId.split("\\.");
				if (!split[0].isEmpty())
				{
					// Obtain the Deliveries from the project
					List<Delivery> projectDeliveries = cidProjectStorage.getDeliveries(split[0]);
					if (projectDeliveries.size() == 1)
					{
						projectDelivery = projectDeliveries.get(0);
						List<Artifact> artifacts = cidProjectStorage.getArtifacts("pluget", projectDelivery.getName(),
							split[1]);
						setPlugetFileFromCID(artifacts, cidGlobalStorage, split[0], split[1]);
					}
					else if ((projectDeliveries.size() == 0))
					{ // Obtain the Deliveries from the GlobalRepository and search the artifact in them

						searchInGlobalRepository(cidGlobalStorage, split[0], split[1]);
					}
					else
					{
						printUsage();
						status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
							"Wrong arguments(The Delivery that you are trying to find exists more than once )!", null);
					}
					if ((projectArtifact != null) && (globalArtifact != null))
					{
						printUsage();
						status = new Status(
							IStatus.ERROR,
							CessarPluginActivator.PLUGIN_ID,
							EXIT_WRONG_ARG,
							"Wrong arguments(The pluget that you are trying to run exists both in the global repository and in the current project)!",
							null);

					}
					else if ((projectArtifact == null) && (globalArtifact == null))
					{
						printUsage();
						status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
							"Wrong arguments(The pluget that you are trying to run doesn't exist)!", null);

					}
				}
			}
		}

		if (!statusFound)
		{
			additionalOpt = commandLine.getArgs();
			if (additionalOpt != null && additionalOpt.length > 0)
			{
				additionalArgs = additionalOpt;
			}
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#doExecute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected IStatus doExecute() throws CoreException
	{
		ICessarTaskManager manager = null;

		if (plugetFile != null)
		{
			manager = ExecutionService.createManager(getProject(), ExecutionService.TASK_TYPE_PLUGET);
			manager.initialize(plugetFile);
		}
		else if (className != null)
		{
			manager = ExecutionService.createManager(getProject(), ExecutionService.TASK_TYPE_PLUGET_CLASS);
			manager.initialize(className);
		}
		else if (plugetLocation != null)
		{
			manager = ExecutionService.createManager(getProject(), ExecutionService.TASK_TYPE_PLUGET_EXTERNAL);
			manager.initialize(plugetLocation);
		}
		status = manager.execute(false, additionalArgs, new NullProgressMonitor());
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCommandLineRunner#getRunnerName()
	 */
	@Override
	protected String getRunnerName()
	{
		return "PlugetRunner";
	}

	/**
	 * Sets the pluget file from the Project
	 * 
	 * @param artifacts
	 * @param split
	 * @param cidGlobalStorage
	 */
	private void setPlugetFileFromCID(List<Artifact> artifacts, ICIDGlobalStorage cidGlobalStorage,
		String deliveryName, String artifactName)
	{
		if (artifacts.size() == 1)
		{
			projectArtifact = artifacts.get(0);
			IArtifactBinding binding = projectArtifact.getConcreteBinding();
			PlugetBinding plugetBinding = (PlugetBinding) binding;
			plugetFile = plugetBinding.getPlugetFile();
		}
		else
		{
			projectArtifact = null;
			searchInGlobalRepository(cidGlobalStorage, deliveryName, artifactName);
		}

	}

	/**
	 * Searches the delivery and the artifact inside the GlobalStorage
	 * 
	 * @param deliveryName
	 * @param artifactName
	 * @param cidGlobalStorage
	 * 
	 */
	private void searchInGlobalRepository(ICIDGlobalStorage cidGlobalStorage, String deliveryName, String artifactName)
	{
		// Obtain the Deliveries from the GlobalRepository
		List<Delivery> globalDeliveries = cidGlobalStorage.getDeliveries(deliveryName);
		if (globalDeliveries.size() == 0)
		{
			printUsage();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"Wrong arguments(The Delivery that you are trying to find doesn't exist )!", null);
		}
		else if (globalDeliveries.size() == 1)
		{
			globalDelivery = globalDeliveries.get(0);
			List<Artifact> artifacts = cidGlobalStorage.getArtifacts("pluget", globalDelivery.getName(), artifactName); //$NON-NLS-1$
			if (artifacts.size() == 1)
			{
				globalArtifact = artifacts.get(0);
				try
				{
					setPlugetLocationFromCID(globalArtifact);
				}
				catch (MalformedURLException e)
				{
					printUsage();
					status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
						"The URL is not valid )!", null);

				}
				catch (URISyntaxException e)
				{
					printUsage();
					status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
						"The location does not exist)!", null);

				}
			}
			else
			{
				globalArtifact = null;
			}

		}
		else
		{
			printUsage();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"Wrong arguments(The Delivery that you are trying to find exists more than once )!", null);
		}

	}

	/**
	 * Searches the artifact inside the deliveries from the GlobalRepository and if it founds it, sets the
	 * plugetLocation to that file
	 * 
	 * @param globalArtifact2
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	private void setPlugetLocationFromCID(Artifact artifact) throws MalformedURLException, URISyntaxException
	{
		IArtifactBinding binding = artifact.getConcreteBinding();
		PlugetBinding plugetBinding = (PlugetBinding) binding;
		try
		{
			plugetLocation = plugetBinding.getPlugetLocation();
		}
		catch (MalformedURLException e)
		{
			printUsage();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"The URL is not valid)!", null);
		}
		catch (URISyntaxException e)
		{
			printUsage();
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, EXIT_WRONG_ARG,
				"The location does not exist)!", null);
		}
	}
}
