/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Allow lazy loading for plugin classes. Must be used when needed to initialize classes contributed through extension
 * points.
 * 
 * @param <T>
 * 
 * @Review uidl6458 - 18.04.2012
 */
public class ExtensionClassWrapper<T>
{
	private String ownerPlugin;
	private String className;

	private boolean initialized; // = false;

	private T instance;
	private IConfigurationElement configElement;
	private String classAttribute;

	/**
	 * Default class constructor
	 * 
	 * @param ownerPlugin
	 *        the id of the plugin where to search for specified class
	 * @param className
	 *        the qualified name of the class to be instantiated
	 */
	public ExtensionClassWrapper(String ownerPlugin, String className)
	{
		this.ownerPlugin = ownerPlugin;
		this.className = className;
	}

	/**
	 * Create an <code>ExtensionClassWrapper</code> wrapped around a configuration element
	 * 
	 * @param configElement
	 *        the configuration element
	 * @param attName
	 *        the attribute from the configuration element that contain the class name
	 */
	public ExtensionClassWrapper(IConfigurationElement configElement, String attName)
	{
		this(configElement, attName, false);
	}

	/**
	 * Create an <code>ExtensionClassWrapper</code> wrapped around a configuration element
	 * 
	 * @param configElement
	 *        the configuration element
	 * @param attName
	 *        the attribute from the configuration element that contain the class name
	 * @param useConfigElement
	 *        if true the {@link IConfigurationElement#createExecutableExtension(String)} will be used to create an
	 *        instance
	 */
	public ExtensionClassWrapper(IConfigurationElement configElement, String attName, boolean useConfigElement)
	{
		if (useConfigElement)
		{
			this.configElement = configElement;
			this.classAttribute = attName;
		}
		else
		{
			this.ownerPlugin = configElement.getContributor().getName();
			this.className = configElement.getAttribute(attName);
		}
	}

	/**
	 * Gets a cached instance of the class creating one if necessary
	 * 
	 * @return The class instance
	 * @throws Throwable
	 */
	// SUPPRESS CHECKSTYLE allow throw
	public T getInstance() throws Throwable
	{
		if (!initialized)
		{
			synchronized (this)
			{
				try
				{
					instance = newInstance();
				}
				finally
				{
					initialized = true;
				}
			}
		}
		if (instance == null)
		{
			throw new Exception(MessageFormat.format("Failed to construct class {0}", className));
		}
		return instance;
	}

	/**
	 * Create and return a new instance of the wrapped object.
	 * 
	 * @return a newly created instance
	 * @throws Throwable
	 */
	// SUPPRESS CHECKSTYLE allow throw
	public T newInstance() throws Throwable
	{
		if (configElement != null)
		{
			return (T) configElement.createExecutableExtension(classAttribute);
		}
		else
		{
			Bundle bundle = Platform.getBundle(ownerPlugin);
			if (bundle == null)
			{
				throw new Exception(MessageFormat.format("Cannot locate contributing plugin: {0}", //$NON-NLS-1$
					ownerPlugin));
			}
			Class<?> factoryClz = bundle.loadClass(className);
			return (T) factoryClz.newInstance();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ExtensionClassWrapper)
		{
			ExtensionClassWrapper<?> classWrapper = (ExtensionClassWrapper<?>) obj;
			if (configElement != null)
			{
				return configElement.equals(classWrapper.configElement)
					&& classAttribute.equals(classWrapper.classAttribute);
			}
			else
			{
				return ownerPlugin.equals(classWrapper.ownerPlugin) && className.equals(classWrapper.className);
			}
		}
		else
		{
			return super.equals(obj);
		}
	}
}
