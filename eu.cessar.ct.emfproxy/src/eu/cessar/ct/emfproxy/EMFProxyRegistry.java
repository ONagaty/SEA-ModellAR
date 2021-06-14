package eu.cessar.ct.emfproxy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import eu.cessar.ct.emfproxy.internal.CessarPluginActivator;
import eu.cessar.ct.emfproxy.internal.EMFProxyDescriptor;

/**
 * FIXME: Comment this
 * 
 */
public class EMFProxyRegistry
{

	private static final String EXTENSION_ID_EMFPROXY = "eu.cessar.ct.emfproxy"; //$NON-NLS-1$

	public static final EMFProxyRegistry eINSTANCE = new EMFProxyRegistry();

	private List<EMFProxyDescriptor> descriptors;

	/**
	 * Perform the initialization of the registry
	 */
	private void checkInit()
	{
		if (descriptors == null)
		{
			synchronized (this)
			{
				if (descriptors == null)
				{
					descriptors = new ArrayList<EMFProxyDescriptor>();
					IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
						EXTENSION_ID_EMFPROXY);
					for (IConfigurationElement element: elements)
					{
						try
						{
							descriptors.add(new EMFProxyDescriptor(element));
						}
						catch (CoreException e)
						{
							CessarPluginActivator.getDefault().logError(e);
						}
					}
				}
			}
		}
	}

	/**
	 * @param masterURI
	 * @param slaveURI
	 * @param project
	 * @return
	 */
	public IEMFProxyEngine getEMFProxyEngine(String masterURI, String slaveURI, IProject project)
	{
		return getEMFProxyEngine(masterURI, slaveURI, project, true);
	}

	/**
	 * @param masterURI
	 * @param slaveURI
	 * @param project
	 * @param autoCleanup
	 * @return
	 */
	public IEMFProxyEngine getEMFProxyEngine(String masterURI, String slaveURI, IProject project,
		boolean autoCleanup)
	{
		checkInit();
		for (EMFProxyDescriptor descriptor: descriptors)
		{
			if (descriptor.isProxy(masterURI, slaveURI))
			{
				return descriptor.getEngine(project, autoCleanup);
			}
		}
		return null;
	}

}
