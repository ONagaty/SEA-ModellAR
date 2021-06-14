/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;

import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.mms.internal.Messages;

/**
 * This class implements the registry for the MM services.
 */
public final class MMSRegistry
{
	/**
	 * The instance of the MMS Registry
	 */
	public static final MMSRegistry INSTANCE = new MMSRegistry();

	private static final String MMS_EXTENSION_ID = CessarPluginActivator.PLUGIN_ID + ".cessarMetaModelService"; //$NON-NLS-1$

	private static final String MMS_ATT_DESCRIPTORID = "descriptorID"; //$NON-NLS-1$

	private static final String MMS_ATT_CLASS = "class"; //$NON-NLS-1$

	private volatile Map<IMetaModelDescriptor, IMetaModelService> services;

	/**
	 * 
	 */
	private MMSRegistry()
	{
		// the default constructor
	}

	/**
	 * Return the MetaModel Services for a particular <code>IMetaModelDescriptor</code>
	 * 
	 * @param mmDescriptor
	 *        a IMetaModelDescriptor, it can by obtained by using the {@link MetaModelDescriptorRegistry} class
	 * @return the corresponding meta model service, null if none found
	 */
	public IMetaModelService getMMService(IMetaModelDescriptor mmDescriptor)
	{
		checkInit();
		return services.get(mmDescriptor);
	}

	/**
	 * Return the MM service for the metamodel associated with a particular EObject
	 * 
	 * @param object
	 * @return
	 * @deprecated Avoid using this method when you have fast access to the project or the IMetaModelDescriptor, use
	 *             {@link #getMMService(EClass)} or {@link #getMMService(IProject)} instead
	 */
	@Deprecated
	public IMetaModelService getMMService(EObject object)
	{
		return getMMService(MetaModelDescriptorRegistry.INSTANCE.getDescriptor(object));
	}

	/**
	 * Get the metamodel services associated with a particular metamodel
	 * 
	 * @param object
	 * @return
	 */
	public IMetaModelService getMMService(EClass clz)
	{
		return getMMService(MetaModelDescriptorRegistry.INSTANCE.getDescriptor(clz));
	}

	/**
	 * Gets the {@link IMetaModelService} associated with a given IProject
	 * 
	 * @param project
	 *        the {@link IProject} for which to retrieve the Meta Model Service
	 * @return the corresponding meta model service, null if none found
	 */
	public IMetaModelService getMMService(IProject project)
	{
		return getMMService(MetaModelUtils.getAutosarRelease(project));
	}

	/**
	 * Get an {@link IGenericFactory} instance corresponding to the AUTOSAR MM to which specified {@link EObject}
	 * belongs to.
	 * 
	 * @param eObject
	 *        the {@link EObject} instance to determine the MM version
	 * @return A concrete {@link IGenericFactory} instance.
	 * @deprecated Avoid using this method when you have fast access to the project, use
	 *             {@link #getGenericFactory(IMetaModelDescriptor)} instead
	 */
	@Deprecated
	public IGenericFactory getGenericFactory(EObject eObject)
	{
		IMetaModelService mmService = getMMService(eObject);
		return mmService.getGenericFactory();
	}

	/**
	 * Gets the {@link IGenericFactory} corresponding to the given {@link IMetaModelDescriptor}.
	 * 
	 * @param descriptor
	 *        the {@link IMetaModelDescriptor} for which to retrieve the {@link IGenericFactory}
	 * @return the Meta Model implementation of the {@link IGenericFactory}
	 */
	public IGenericFactory getGenericFactory(IMetaModelDescriptor descriptor)
	{
		IMetaModelService mmService = getMMService(descriptor);
		return mmService.getGenericFactory();
	}

	/**
	 * Gets the {@link IGenericFactory} corresponding to the given {@link IProject}.
	 * 
	 * @param project
	 *        the {@link IProject} for which to retrieve the {@link IGenericFactory}
	 * @return the Meta Model implementation of the {@link IGenericFactory}
	 */
	public IGenericFactory getGenericFactory(IProject project)
	{
		IMetaModelService mmService = getMMService(project);
		return mmService.getGenericFactory();
	}

	/**
	 * Initialize by reading from plugin registry
	 */
	private void checkInit()
	{
		if (services == null)
		{
			synchronized (INSTANCE)
			{
				if (services == null)
				{
					services = new HashMap<IMetaModelDescriptor, IMetaModelService>();
					IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
						MMS_EXTENSION_ID);
					for (IConfigurationElement cfgElement: elements)
					{
						String descriptor = cfgElement.getAttribute(MMS_ATT_DESCRIPTORID);
						// locate the corresponding MMDescriptor
						IMetaModelDescriptor mmDescriptor = MetaModelDescriptorRegistry.INSTANCE.getDescriptor(descriptor);
						if (mmDescriptor == null)
						{
							// log a warning
							CessarPluginActivator.getDefault().logWarning(Messages.MMSRegistry_Unknown_MM,
								cfgElement.getContributor(), MMS_EXTENSION_ID, descriptor);
							continue;
						}
						if (services.containsKey(mmDescriptor))
						{
							CessarPluginActivator.getDefault().logWarning(Messages.MMSRegistry_MM_Already_Set,
								mmDescriptor);
							continue;
						}
						try
						{
							Object extension = cfgElement.createExecutableExtension(MMS_ATT_CLASS);
							if (!(extension instanceof IMetaModelService))
							{
								CessarPluginActivator.getDefault().logError(
									Messages.MMSRegistry_Invalid_Extension_Class, IMetaModelService.class,
									extension.getClass());
							}
							// everything fine
							services.put(mmDescriptor, (IMetaModelService) extension);
						}
						catch (CoreException e)
						{
							CessarPluginActivator.getDefault().logError(e);
							continue;
						}
					}
				}
			}
		}
	}
}
