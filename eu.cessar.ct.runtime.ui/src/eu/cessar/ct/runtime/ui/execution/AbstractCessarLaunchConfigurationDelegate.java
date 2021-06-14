package eu.cessar.ct.runtime.ui.execution;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.launching.EclipseApplicationLaunchConfiguration;
import org.eclipse.pde.launching.IPDELauncherConstants;

import eu.cessar.ct.runtime.CessarRuntimeUtils;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;

/**
 * An abstract launch delegate for launching CESSAR-CT specific applications.
 *
 */
public abstract class AbstractCessarLaunchConfigurationDelegate extends EclipseApplicationLaunchConfiguration
{
	private static final String OSGI_CLASS_PATH = "osgi.frameworkClassPath"; //$NON-NLS-1$
	private static final String OSGI_HOOK_INCLUDE = "osgi.hook.configurators.include"; //$NON-NLS-1$
	private static final String CONFIG_INI = "config.ini"; //$NON-NLS-1$

	//private static final String BUNDLE_EQUINOX_LAUNCHER = "org.eclipse.equinox.launcher"; //$NON-NLS-1$

	/**
	 *
	 * @return attributes for the launcher
	 */
	protected abstract String getApplicationAttribute();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration#preLaunchCheck(org.eclipse.debug.core.
	 * ILaunchConfiguration, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void preLaunchCheck(final ILaunchConfiguration configuration, final ILaunch launch,
		final IProgressMonitor monitor) throws CoreException
	{
		// save the workspace before launching
		monitor.beginTask("Saving workspace", 1); //$NON-NLS-1$
		ResourcesPlugin.getWorkspace().save(true, new SubProgressMonitor(monitor, 1));
		super.preLaunchCheck(configuration, launch, monitor);
	}

	/**
	 * @param configuration
	 * @throws CoreException
	 */
	protected void customizeLaunchConfiguration(ILaunchConfiguration configuration) throws CoreException
	{

		ILaunchConfigurationWorkingCopy wc = configuration.getWorkingCopy();
		fillRCPLaunchConfiguration(wc);

		String existingVMArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""); //$NON-NLS-1$
		if ("".equals(existingVMArgs)) //$NON-NLS-1$
		{

			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, CessarRuntimeUtils.getVMArgs());
		}
		wc.doSave();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration#clear(org.eclipse.debug.core.ILaunchConfiguration
	 * , org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void clear(final ILaunchConfiguration configuration, final IProgressMonitor monitor) throws CoreException
	{
		// do not attempt to clear the configuration area or the workspace
	}

	/**
	 * @param wc
	 * @throws CoreException
	 */
	protected void fillRCPLaunchConfiguration(final ILaunchConfigurationWorkingCopy wc) throws CoreException
	{
		wc.setAttribute(IPDELauncherConstants.USE_PRODUCT, false);
		wc.setAttribute(IPDELauncherConstants.APPLICATION, getApplicationAttribute());
		wc.setAttribute(IPDELauncherConstants.LOCATION,
			ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
		if (Platform.inDevelopmentMode())
		{
			// TODO: un-comment and fix compilation errors
			// String bootstrapEntries = getBootstrapEntries();
			// wc.setAttribute(IPDELauncherConstants.BOOTSTRAP_ENTRIES, bootstrapEntries);
		}
	}

	/**
	 * Compose the entries that must be used as boot class path. This is required when CESSAR is executed in development
	 * mode only and must contain all jar files of the <code>org.eclipse.equinox.launcher</code>
	 *
	 * @return bootstrapEntries
	 * @throws CoreException
	 */
	// @SuppressWarnings("restriction")
	// private String getBootstrapEntries() throws CoreException
	// {
	// Bundle bundle = Platform.getBundle(BUNDLE_EQUINOX_LAUNCHER);
	// if (bundle == null)
	// {
	// throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
	//				"Cannot find plugin " + BUNDLE_EQUINOX_LAUNCHER)); //$NON-NLS-1$
	// }
	//		URL entry = bundle.getEntry("/"); //$NON-NLS-1$
	// String bundleLocation;
	// try
	// {
	//			bundleLocation = FileLocator.toFileURL(entry).toString().substring("file:/".length()); //$NON-NLS-1$
	// }
	// catch (IOException e)
	// {
	// throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
	//				"Cannot get Equinox launcher local URL", e)); //$NON-NLS-1$
	// }
	// String[] classPath = null;
	// if (bundle instanceof AbstractBundle)
	// {
	// try
	// {
	// classPath = ((AbstractBundle) bundle).getBundleData().getClassPath();
	// }
	// catch (BundleException e)
	// {
	// throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
	//					"Cannot get Equinox launcher classpath", e)); //$NON-NLS-1$
	// }
	// }
	// else
	// {
	// throw new CoreException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
	//				"Internal error: Equinox bundle it's not an instanceof AbstractBundle, is " //$NON-NLS-1$
	// + bundle.getClass().getName()));
	// }
	// StringBuilder bootEntries = new StringBuilder();
	// for (String cp: classPath)
	// {
	// bootEntries.append(bundleLocation);
	// bootEntries.append(cp);
	//			bootEntries.append(","); //$NON-NLS-1$
	// }
	// return bootEntries.substring(0, bootEntries.length() - 1);
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration#getProgramArguments(org.eclipse.debug.core.
	 * ILaunchConfiguration)
	 */
	@Override
	public String[] getProgramArguments(ILaunchConfiguration configuration) throws CoreException
	{
		List<String> programArgs = new ArrayList<String>();

		String[] args = super.getProgramArguments(configuration);
		int devLocation = -1;
		for (int i = 0; i < args.length; i++)
		{
			if ("-dev".equals(args[i])) //$NON-NLS-1$
			{
				devLocation = i + 1;
			}
			programArgs.add(args[i]);
		}

		if (Platform.inDevelopmentMode() && devLocation >= 0 && devLocation < programArgs.size())
		{
			// use the parent dev location when executed in development mode
			programArgs.set(devLocation, CessarPluginActivator.getDefault().getContext().getProperty("osgi.dev")); //$NON-NLS-1$
		}

		if (!Platform.inDevelopmentMode())
		{
			// update the config.ini with additional information
			updateConfigIni(configuration);
		}

		return programArgs.toArray(new String[programArgs.size()]);
	}

	/**
	 *
	 * @param configuration
	 */
	protected void updateConfigIni(ILaunchConfiguration configuration)
	{
		File configDir = getConfigDir(configuration);
		Properties launchProperties = loadConfigIni(configDir);
		try
		{
			URL eclipseConfigUrl = new URL(Platform.getConfigurationLocation().getURL(), CONFIG_INI);
			InputStream stream = eclipseConfigUrl.openStream();
			try
			{
				Properties eclipseProperties = loadConfigIni(stream);
				// put necessary entries from eclipseProperties to
				// lauchProperties
				copyProperty(eclipseProperties, launchProperties, OSGI_HOOK_INCLUDE);
				copyProperty(eclipseProperties, launchProperties, OSGI_CLASS_PATH);
				saveConfigIni(configDir, launchProperties);
			}
			finally
			{
				closeStream(stream);
			}

		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param eclipseProperties
	 * @param launchProperties
	 * @param osgiHookInclude
	 */
	private static void copyProperty(Properties sourceProperties, Properties targetProperties, String key)
	{
		if (sourceProperties.containsKey(key))
		{
			targetProperties.put(key, sourceProperties.get(key));
		}
	}

	/**
	 * @param configDir
	 * @return
	 */
	private static Properties loadConfigIni(File configDir)
	{
		try
		{
			FileInputStream fIn = new FileInputStream(new File(configDir, CONFIG_INI));
			try
			{
				return loadConfigIni(fIn);
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			finally
			{
				closeStream(fIn);
			}
		}
		catch (FileNotFoundException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	private static Properties loadConfigIni(InputStream stream) throws IOException
	{
		Properties result = new Properties();
		result.load(stream);
		return result;
	}

	/**
	 * @param configDir
	 * @param properties
	 */
	protected static void saveConfigIni(File configDir, Properties properties)
	{
		try
		{
			FileOutputStream fOut = new FileOutputStream(new File(configDir, CONFIG_INI));
			try
			{
				properties.store(fOut, "CESSAR Configuration File"); //$NON-NLS-1$
				fOut.flush();
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			finally
			{
				closeStream(fOut);
			}

		}
		catch (FileNotFoundException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	/**
	 * @param stream
	 */
	private static void closeStream(Closeable stream)
	{
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration#getVMArguments(org.eclipse.debug.core.
	 * ILaunchConfiguration)
	 */
	@Override
	public String[] getVMArguments(ILaunchConfiguration configuration) throws CoreException
	{
		String[] vmArguments = super.getVMArguments(configuration);
		if (vmArguments.length > 0)
		{
			return vmArguments;
		}
		// possible dead code
		List<String> vmArgs = new ArrayList<String>();
		for (String arg: vmArguments)
		{
			vmArgs.add(arg);
		}
		String[] specificVmArgs = CessarRuntimeUtils.getVMArgs().split("\n"); //$NON-NLS-1$
		for (String arg: specificVmArgs)
		{
			String newArg = arg.replace("\n", "").replace("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			vmArgs.add(newArg);
		}
		return vmArgs.toArray(new String[vmArgs.size()]);
	}
}
