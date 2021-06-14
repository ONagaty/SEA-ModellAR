package eu.cessar.ct.emfproxy.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;

/**
 * 
 */
public class EMFProxyDescriptor
{
	private static final String ELEM_CLASS_RESOLVER = "ClassResolver"; //$NON-NLS-1$

	private static final String ELEM_FEATURE_RESOLVER = "FeatureResolver"; //$NON-NLS-1$

	private static final String ELEM_OPERATION_RESOLVER = "OperationResolver"; //$NON-NLS-1$

	private static final String ELEM_PARAMETERS = "Parameters"; //$NON-NLS-1$

	private static final String ELEM_PARAMETER = "Parameter"; //$NON-NLS-1$

	private static final String ATTR_NAME = "name"; //$NON-NLS-1$

	private static final String ATTR_VALUE = "value"; //$NON-NLS-1$

	private static final String ATTR_SLAVE_URI = "slaveURI"; //$NON-NLS-1$

	private static final String ATTR_MASTER_URI = "masterURI"; //$NON-NLS-1$

	private static final String ATTR_ID = "id"; //$NON-NLS-1$

	private static final String ATTR_CLASS = "class"; //$NON-NLS-1$

	private Pattern masterURIPattern;

	private Pattern slaveURIPattern;

	private String masterURI;

	private String slaveURI;

	private String ID;

	private Map<String, ProxyExtensionClassWrapper<IProxyClassResolver<?>>> classResolvers;

	private Map<String, ProxyExtensionClassWrapper<IProxyFeatureResolver<?>>> featureResolvers;

	private Map<String, ProxyExtensionClassWrapper<IProxyOperationResolver<?>>> operationResolvers;

	private Map<IProject, IEMFProxyEngine> engines;

	private Map<String, String> parameters;

	public EMFProxyDescriptor(IConfigurationElement configElement) throws CoreException
	{
		ID = configElement.getAttribute(ATTR_ID);
		String contributingPlugin = configElement.getContributor().getName();

		masterURI = configElement.getAttribute(ATTR_MASTER_URI);
		throwError(masterURI == null, "Master URI cannot be null"); //$NON-NLS-1$
		masterURIPattern = Pattern.compile(masterURI);

		slaveURI = configElement.getAttribute(ATTR_SLAVE_URI);
		throwError(slaveURI == null, "Slave URI cannot be null"); //$NON-NLS-1$
		slaveURIPattern = Pattern.compile(slaveURI);

		// get the childrens and collect the class, references and attribute
		// resolvers

		IConfigurationElement[] children = configElement.getChildren(ELEM_CLASS_RESOLVER);
		classResolvers = new HashMap<String, ProxyExtensionClassWrapper<IProxyClassResolver<?>>>();
		for (IConfigurationElement cfg: children)
		{
			classResolvers.put(
				cfg.getAttribute(ATTR_ID),
				new ProxyExtensionClassWrapper<IProxyClassResolver<?>>(contributingPlugin,
					cfg.getAttribute(ATTR_CLASS)));
		}

		children = configElement.getChildren(ELEM_FEATURE_RESOLVER);
		featureResolvers = new HashMap<String, ProxyExtensionClassWrapper<IProxyFeatureResolver<?>>>();
		for (IConfigurationElement cfg: children)
		{
			featureResolvers.put(
				cfg.getAttribute(ATTR_ID),
				new ProxyExtensionClassWrapper<IProxyFeatureResolver<?>>(contributingPlugin,
					cfg.getAttribute(ATTR_CLASS)));
		}

		children = configElement.getChildren(ELEM_OPERATION_RESOLVER);
		operationResolvers = new HashMap<String, ProxyExtensionClassWrapper<IProxyOperationResolver<?>>>();
		for (IConfigurationElement cfg: children)
		{
			operationResolvers.put(
				cfg.getAttribute(ATTR_ID),
				new ProxyExtensionClassWrapper<IProxyOperationResolver<?>>(contributingPlugin,
					cfg.getAttribute(ATTR_CLASS)));
		}

		children = configElement.getChildren(ELEM_PARAMETERS);
		parameters = new HashMap<String, String>();
		for (IConfigurationElement cfg: children)
		{
			IConfigurationElement[] children2 = cfg.getChildren(ELEM_PARAMETER);
			for (IConfigurationElement cfg2: children2)
			{
				parameters.put(cfg2.getAttribute(ATTR_NAME), cfg2.getAttribute(ATTR_VALUE));
			}
		}

		engines = new WeakHashMap<IProject, IEMFProxyEngine>();

	}

	/**
	 * @param proceed
	 * @param message
	 * @throws CoreException
	 */
	private void throwError(boolean proceed, String message) throws CoreException
	{
		if (proceed)
		{
			message = "There was an error while initializing proxy " + ID + "\n" + message; //$NON-NLS-1$//$NON-NLS-2$
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(IStatus.ERROR,
				message));
		}
	}

	/**
	 * @return the masterURI
	 */
	public String getMasterURI()
	{
		return masterURI;
	}

	/**
	 * @return the slaveURI
	 */
	public String getSlaveURI()
	{
		return slaveURI;
	}

	/**
	 * @return the iD
	 */
	public String getID()
	{
		return ID;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.internal.IEMFProxyFactoryDescriptor#isProxy(java.lang.String, java.lang.String)
	 */
	public boolean isProxy(String masterURI, String slaveURI)
	{
		return masterURIPattern.matcher(masterURI).matches()
			&& slaveURIPattern.matcher(slaveURI).matches();
	}

	public boolean isProxyForSlaveURI(String slaveURI)
	{
		return slaveURIPattern.matcher(slaveURI).matches();
	}

	public boolean isProxyForMasterURI(String masterURI)
	{
		return masterURIPattern.matcher(masterURI).matches();
	}

	/**
	 * @param project
	 */
	/*package*/void removeEngine(IProject project)
	{
		engines.remove(project);
	}

	/**
	 * @param project
	 * @return
	 */
	public IEMFProxyEngine getEngine(IProject project, boolean autoCleanup)
	{
		if (!engines.containsKey(project))
		{
			synchronized (this)
			{
				if (!engines.containsKey(project))
				{
					// the constructor will also take care to attach the engine
					// with the current thread
					EMFProxyEngine engine = new EMFProxyEngine(project, this, autoCleanup);
					engines.put(project, engine);
					return engine;
				}
				else
				{
					IEMFProxyEngine engine = engines.get(project);
					EMFProxyEngine.addEngineForCurrentThread(engine);
					return engine;
				}
			}
		}
		else
		{
			IEMFProxyEngine engine = engines.get(project);
			EMFProxyEngine.addEngineForCurrentThread(engine);
			return engine;
		}
	}

	/**
	 * @return
	 */
	public Map<String, ProxyExtensionClassWrapper<IProxyClassResolver<?>>> getClassResolvers()
	{
		return classResolvers;
	}

	/**
	 * @return
	 */
	public Map<String, ProxyExtensionClassWrapper<IProxyFeatureResolver<?>>> getFeatureResolvers()
	{
		return featureResolvers;
	}

	/**
	 * @return
	 */
	public Map<String, ProxyExtensionClassWrapper<IProxyOperationResolver<?>>> getOperationResolvers()
	{
		return operationResolvers;
	}

	/**
	 * @param name
	 * @return
	 */
	public String getParameterValue(String name)
	{
		return parameters.get(name);
	}
}
