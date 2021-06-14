package eu.cessar.ct.core.internal.platform.ui.ant;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ant.internal.ui.AntUtil;
import org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchConfigurationMessages;
import org.eclipse.ant.internal.ui.model.AntElementNode;
import org.eclipse.ant.launching.IAntLaunchConstants;
import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsUtil;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.req.Requirement;

/**
 * Launch shortcut for CESSAR-CT Ant files.
 *
 */

@SuppressWarnings("restriction")
@Requirement(
	reqID = "26579")
public class AntLaunchShortcut extends org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchShortcut
{
	/**
	 *
	 */
	private boolean rebuild = false;
	private boolean backgroundMode = true;
	private IStatus terminationStatus;

	private static final String EU_CESSAR_CT_PREF_NODE = "eu.cessar.ct.runtime";

	private static final String PREFFERED_LAUNCH_DELEGATE = "eu.cessar.ct.core.platform.ui.ant.CustomAntLaunchDelegate"; //$NON-NLS-1$

	private static final String NAMESPACE = EU_CESSAR_CT_PREF_NODE;

	private static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	private static final String KEY_OUTPUT_CUSTOM = "gen.output.custom"; //$NON-NLS-1$

	private static final String KEY_OUTPUT_CUSTOM_FOLDER = "gen.output.custom.folder"; //$NON-NLS-1$

	private static final String OUTPUT_FOLDER_DEFAULT = "generated"; //$NON-NLS-1$

	/**
	 * If true, the rebuild flag will be delivered to the laucher
	 *
	 * @param value
	 */
	public void setRebuild(boolean value)
	{
		rebuild = value;
	}

	/**
	 * This is not part of the API
	 *
	 * @param backgroundMode
	 */
	public void testSetBackgroundMode(boolean backgroundMode)
	{
		this.backgroundMode = backgroundMode;
	}

	public IStatus getTerminationStatus()
	{
		return terminationStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ant.internal.ui.launchConfigurations.AntLaunchShortcut#launch(org.eclipse.core.runtime.IPath,
	 * org.eclipse.core.resources.IProject, java.lang.String, java.lang.String)
	 */
	@Override
	public void launch(IPath filePath, IProject project, String mode, String targetAttribute)
	{

		ILaunchConfiguration configuration = null;
		IFile backingfile = null;
		if (project != null)
		{
			// need to get the full location of a workspace file to compare
			// against the resolved
			// config location attribute
			backingfile = project.getFile(filePath.removeFirstSegments(1));
		}
		// check if a configuration is already created
		List<ILaunchConfiguration> configs = collectConfigurations(
			(backingfile != null && backingfile.exists() ? backingfile.getLocation() : filePath), targetAttribute,
			rebuild);
		if (configs.isEmpty())
		{
			// create a default configuration. We use the ant implementation and
			// we will customize
			// the result
			configuration = createDefaultLaunchConfiguration(filePath, (project != null && project.exists() ? project
				: null));
			try
			{
				if (targetAttribute != null
					&& !targetAttribute.equals(configuration.getAttribute(IAntLaunchConstants.ATTR_ANT_TARGETS, ""))) //$NON-NLS-1$
				{
					String projectName = configuration.getAttribute(
						IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
					String newName = getNewLaunchConfigurationName(filePath, projectName, targetAttribute);
					ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
					copy.rename(newName);
					copy.setAttribute(IAntLaunchConstants.ATTR_ANT_TARGETS, targetAttribute);
					// copy.setAttribute(PREFFERED_LAUNCH_DELEGATE,
					// "eu.cessar.ct.core.platform.ui.ant.CustomAntLaunchDelegate");
					configuration = copy.doSave();
				}
				// update the configuration
				configuration = updateLaunchConfiguration(configuration, filePath, project);
			}
			catch (CoreException exception)
			{
				reportError(MessageFormat.format(AntLaunchConfigurationMessages.AntLaunchShortcut_Exception_launching,
					new Object[] {filePath.toFile().getName()}), exception);
				return;
			}
		}
		else if (configs.size() == 1)
		{
			configuration = configs.get(0);
		}
		else
		{
			// more then one configuration available, let the user choose
			configuration = chooseConfig(configs);
			if (configuration == null)
			{
				// fail gracefully if the user cancels choosing a configuration
				return;
			}
		}
		if (configuration != null)
		{
			try
			{
				// if the call is made from Step Handler, the launch must be done in foreground
				// (according to the "backgroundMode" flag, which is set on false in this case).
				// If we deal with an already existing configuration which has the
				// IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND attribute
				// set on true, we must override it according to the "backgroundMode" flag. See bug
				// 4546
				Map<String, Object> attributes = configuration.getAttributes();
				Object object = attributes.get("org.eclipse.ui.externaltools.ATTR_ANT_PROPERTIES");

				if (object instanceof Map<?, ?>)
				{
					Map<String, String> map = (HashMap<String, String>) object;
					String customOutputFolder = getCustomOutputFolder(project);
					map.put("general.output.dir", customOutputFolder);

				}

				ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
				workingCopy.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, backgroundMode);

				workingCopy.doSave();
			}
			catch (CoreException ex)
			{
				reportError(MessageFormat.format(AntLaunchConfigurationMessages.AntLaunchShortcut_Exception_launching,
					new Object[] {filePath.toFile().getName()}), ex);
				return;
			}
			setPreferedDelegate(configuration, mode);
			DebugUITools.launch(configuration, mode);
			extractExitStatus(configuration, mode);
		}
		else
		{
			// status = new Status(IStatus.ERROR, IAntUIConstants.PLUGIN_ID, 0,
			// message, throwable);

			antFileNotFound();
		}
	}

	@SuppressWarnings("unchecked")
	private void setPreferedDelegate(ILaunchConfiguration configuration, String mode)
	{
		try
		{
			ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
			Set modes = copy.getModes();
			modes.add(mode);

			ILaunchDelegate delegate = copy.getPreferredDelegate(modes);
			if (delegate == null)
			{
				// Map map = new HashMap();
				// map.put(mode,
				// "org.mosart.ecuconfig.codegen.launch.CustomAntLaunchDelegate");
				// copy.setAttribute(LaunchConfiguration.ATTR_PREFERRED_LAUNCHERS,
				// map);
				copy.setPreferredLaunchDelegate(modes, PREFFERED_LAUNCH_DELEGATE);
			}
			configuration = copy.doSave();
		}
		catch (CoreException ex)
		{
			reportError("Exception ocured while setting custom ANT launched delegate", ex); //$NON-NLS-1$
		}
	}

	private void extractExitStatus(ILaunchConfiguration configuration, String mode)
	{
		try
		{
			ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
			Set modes = copy.getModes();
			modes.add(mode);

			ILaunchDelegate delegate = null;

			ILaunchDelegate[] delegates = configuration.getType().getDelegates(modes);
			Map myDelegates = configuration.getAttribute(LaunchConfiguration.ATTR_PREFERRED_LAUNCHERS, (Map) null);
			if (delegates != null)
			{
				String id = (String) myDelegates.get(modes.toString());
				for (ILaunchDelegate del: delegates)
				{
					if (del.getId().equals(id))
					{
						delegate = del;
						break;
					}
				}
			}

			if (delegate != null)
			{
				ILaunchConfigurationDelegate delegate2 = delegate.getDelegate();
				if (delegate2 instanceof CustomAntLaunchDelegate)
				{
					terminationStatus = ((CustomAntLaunchDelegate) delegate2).getExitStatus();
				}
			}
		}
		catch (CoreException ex)
		{
			reportError("Exception ocured while setting custom ANT launched delegate", ex); //$NON-NLS-1$
			terminationStatus = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
				"Exception ocured while setting custom ANT launched delegate", ex); //$NON-NLS-1$
		}
	}

	/**
	 * Collect the launch configuration that are compatible with the specified filepath, targetname and rebuild
	 * parameters
	 *
	 * @param filepath
	 *        path to ant file
	 * @param targetname
	 *        the name of the target to be executed
	 * @param rebuild
	 *        true if a rebuild is requested, false otherwise
	 * @return the list of launch configurations, never null
	 */
	private static List<ILaunchConfiguration> collectConfigurations(IPath filepath, String targetname, boolean rebuild)
	{
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		// the launch configuration type that we are interested in
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IAntLaunchConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE);
		if (type != null)
		{
			try
			{
				// the available configurations of the type Ant
				ILaunchConfiguration[] configs = manager.getLaunchConfigurations(type);
				List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>();
				String targetattr = null;
				String[] targets = null;
				IPath location = null;
				for (int i = 0; i < configs.length; i++)
				{
					if (!configs[i].exists())
					{
						continue;
					}
					try
					{
						location = ExternalToolsUtil.getLocation(configs[i]);
						if (location != null && location.equals(filepath))
						{
							targetattr = configs[i].getAttribute(IAntLaunchConstants.ATTR_ANT_TARGETS, ""); //$NON-NLS-1$
							targets = AntUtil.parseString(targetattr, ","); //$NON-NLS-1$
							Map<String, String> attrs = configs[i].getAttribute(
								IAntLaunchConstants.ATTR_ANT_PROPERTIES, (Map<String, String>) null);
							if (attrs == null && rebuild)
							{
								// not ok, skip this one
								continue;
							}
							if (attrs != null)
							{
								boolean attrRebuild = Boolean.valueOf(attrs.get("rebuild")); //$NON-NLS-1$
								if (attrRebuild != rebuild)
								{
									// not ok, skip this one
									continue;
								}

							}
							if (targets.length == 0)
							{
								if (targetattr.equals(targetname) || targetname == null)
								{
									list.add(configs[i]);
								}
							}
							else
							{
								if (Arrays.asList(targets).contains(targetname))
								{
									list.add(configs[i]);
								}
							}
						}
					}
					catch (CoreException e)
					{
						// ignore it
					}
				}

				return list;
			}
			catch (CoreException e)
			{
				// ignore it
			}
		}
		return new ArrayList<ILaunchConfiguration>();
	}

	/**
	 * Update the launch configuration with Cessar specific entries
	 *
	 * @param configuration
	 *        the original launch configuration
	 * @param filePath
	 *        the path to the ant file
	 * @param project
	 *        the project where the ant is located
	 * @return the updated configuration, never null
	 * @throws CoreException
	 *         when configuration cannot be modified
	 */
	protected ILaunchConfiguration updateLaunchConfiguration(ILaunchConfiguration configuration, IPath filePath,
		IProject project) throws CoreException
	{
		ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();

		// set working directory
		copy.setAttribute(IExternalToolConstants.ATTR_WORKING_DIRECTORY, "${workspace_loc:/" //$NON-NLS-1$
			+ project.getName() + "}"); //$NON-NLS-1$

		copy.setAttribute(IExternalToolConstants.ATTR_BUILD_SCOPE, "${none}"); //$NON-NLS-1$

		// set properties
		Map<String, String> properties = copy.getAttribute(IAntLaunchConstants.ATTR_ANT_PROPERTIES,
			(Map<String, String>) null);
		if (properties == null)
		{
			properties = new HashMap<String, String>();
		}

		ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (compatibilityMode == ECompatibilityMode.NONE)
		{
			properties.put("general.output.dir", getCustomOutputFolder(project)); //$NON-NLS-1$
			//		properties.put("project.dir", "${workspace_loc:/" + project.getName() + "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			properties.put("project.name", project.getName()); //$NON-NLS-1$
		}
		else
		{
			properties.put("general.output.dir", getCustomOutputFolder(project)); //$NON-NLS-1$
			properties.put("CESSAR_PROJECT_NAME", project.getName()); //$NON-NLS-1$
			properties.put("project.dir", "${workspace_loc:/" + project.getName() + "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		}

		if (rebuild)
		{
			properties.put("rebuild", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			// change also the name of the configuration
			StringBuilder sb = new StringBuilder();
			sb.append(project.getName());
			sb.append(" "); //$NON-NLS-1$
			sb.append(filePath.lastSegment());
			sb.append(" "); //$NON-NLS-1$
			sb.append("(rebuild)"); //$NON-NLS-1$
			copy.rename(DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName(sb.toString()));
		}

		copy.setAttribute(IAntLaunchConstants.ATTR_ANT_PROPERTIES, properties);

		// set JRE attributes
		copy.setAttribute(IAntLaunchConstants.ATTR_DEFAULT_VM_INSTALL, false);
		copy.removeAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME);
		copy.removeAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE);
		copy.removeAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME);

		// the mapped resources informations are not put correctly, fix this
		IFile file = AntUtil.getFileForLocation(
			ResourcesPlugin.getWorkspace().getRoot().getFile(filePath).getLocation().toString(), null);
		copy.setMappedResources(new IResource[] {file});
		copy.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, backgroundMode);

		return copy.doSave();
	}

	private static String getCustomOutputFolder(IProject project)
	{
		boolean enabled = false;
		String outputfolder = "generated";
		IEclipsePreferences projectPreferences = null;
		if (project != null)
		{
			ProjectScope projectScope = new ProjectScope(project);
			projectPreferences = projectScope.getNode(EU_CESSAR_CT_PREF_NODE);
			enabled = projectPreferences.getBoolean(KEY_PROJECT_SPECIFIC, false);
		}

		if (enabled)
		{
			return projectPreferences.get(KEY_OUTPUT_CUSTOM_FOLDER, OUTPUT_FOLDER_DEFAULT);
		}

		else
		{
			IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(EU_CESSAR_CT_PREF_NODE);

			if (preference.getBoolean(KEY_OUTPUT_CUSTOM, enabled))
			{
				return preference.get(KEY_OUTPUT_CUSTOM_FOLDER, OUTPUT_FOLDER_DEFAULT);
			}
			else
			{
				return outputfolder;
			}
		}
	}

	/**
	 * Locates a launchable entity in the given selection and launches an application in the specified mode. This launch
	 * configuration shortcut is responsible for progress reporting as well as error handling, in the event that a
	 * launchable entity cannot be found, or launching fails.
	 *
	 * @param selection
	 *        workbench selection
	 * @param mode
	 *        one of the launch modes defined by the launch manager
	 * @see org.eclipse.debug.core.ILaunchManager
	 */
	@Override
	public void launch(ISelection selection, String mode)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object object = structuredSelection.getFirstElement();
			if (object instanceof IAdaptable)
			{
				IResource resource = (IResource) ((IAdaptable) object).getAdapter(IResource.class);
				if (resource != null && "ant".equalsIgnoreCase(resource.getFileExtension())) //$NON-NLS-1$
				{
					IFile file = (IFile) resource;
					launch(file.getFullPath(), file.getProject(), mode, null);
					return;
				}
				else if (object instanceof AntElementNode)
				{
					launch((AntElementNode) object, mode);
					return;
				}
			}
		}
		antFileNotFound();
	}

	/**
	 * Inform the user that an ant file was not found to run.
	 */
	private void antFileNotFound()
	{
		reportError("Cannot find a Cessar ant file to run", null); //$NON-NLS-1$
		terminationStatus = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
			"Cannot find a Cessar ant file to run"); //$NON-NLS-1$
	}
}
