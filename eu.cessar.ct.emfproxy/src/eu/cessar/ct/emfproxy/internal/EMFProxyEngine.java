package eu.cessar.ct.emfproxy.internal;

import java.util.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject.EStore;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.emfproxy.IEMFProxyCache;
import eu.cessar.ct.emfproxy.IEMFProxyCache.ICacheValue;
import eu.cessar.ct.emfproxy.IEMFProxyCache.IFilter;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IEMFProxyLogger;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.IExecutionLoader;
import eu.cessar.ct.runtime.execution.IExecutionSupportListener;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 *
 */
public class EMFProxyEngine implements IEMFProxyEngine, IEMFProxyConstants
{
	// need a synchronized map
	private static final Map<Thread, List<IEMFProxyEngine>> THREAD_ENGINE_MAPPING = new Hashtable<>();

	private final IProject project;
	private final EMFProxyDescriptor descriptor;
	private EMFProxyStore store;

	private IEMFProxyLogger logger = new ConsoleLogger();
	private Map<Object, IEMFProxyCache<IEMFProxyObject, Object>> cache;
	private IExecutionSupportListener execListener = new IExecutionSupportListener()
	{

		public void executionSupportReleased(IProject prj, ICessarTaskManager<?> manager,
			IExecutionLoader executionLoader)
		{
			if (executionLoader == null && prj == project)
			{
				try
				{
					dispose();
				}
				finally
				{
					CessarRuntime.getExecutionSupport().removeListener(this);
				}
			}
		}

		public void executionSupportAquired(IProject prj, ICessarTaskManager<?> manager,
			IExecutionLoader executionLoader)
		{
			// do nothing
		}
	};

	/**
	 * @param project
	 * @param descriptor
	 * @param autoCleanup
	 */
	public EMFProxyEngine(IProject project, EMFProxyDescriptor descriptor, boolean autoCleanup)
	{
		this.project = project;
		this.descriptor = descriptor;
		cache = new HashMap<>();
		if (autoCleanup)
		{
			CessarRuntime.getExecutionSupport().addListener(execListener);
		}
		checkInit();
	}

	/**
	 * Get the proxy engine that is associated with the current thread, could be null.
	 *
	 * @param slaveURI
	 *
	 * @return The proxy engine, if found
	 */
	public static IEMFProxyEngine getEngineForCurrentThread(String slaveURI)
	{
		List<IEMFProxyEngine> engineList = THREAD_ENGINE_MAPPING.get(Thread.currentThread());
		if (engineList != null)
		{
			for (IEMFProxyEngine engine: engineList)
			{
				EMFProxyDescriptor emfProxyDescriptor = engine.getEMFProxyDescriptor();
				if (emfProxyDescriptor != null && emfProxyDescriptor.isProxyForSlaveURI(slaveURI))
				{
					return engine;
				}
			}
		}
		return null;
	}

	/**
	 * @param engine
	 */
	public static void addEngineForCurrentThread(IEMFProxyEngine engine)
	{
		((EMFProxyEngine) engine).checkInit();
	}

	/**
	 *
	 */
	private void checkInit()
	{
		boolean needMapping = false;
		List<IEMFProxyEngine> engineList = THREAD_ENGINE_MAPPING.get(Thread.currentThread());
		if (engineList == null)
		{
			needMapping = true;
		}
		else
		{
			needMapping = !engineList.contains(this);
		}
		if (needMapping)
		{
			synchronized (EMFProxyEngine.class)
			{
				// need another read because someone else might changed in the
				// mean time.
				engineList = THREAD_ENGINE_MAPPING.get(Thread.currentThread());
				if (engineList == null)
				{
					needMapping = true;
				}
				else
				{
					needMapping = !engineList.contains(this);
				}
				if (needMapping)
				{
					if (engineList == null)
					{
						engineList = new ArrayList<>(5);
					}
					else
					{
						engineList = new ArrayList<>(engineList);
					}
					engineList.add(this);
					THREAD_ENGINE_MAPPING.put(Thread.currentThread(), engineList);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getEMFProxyDescriptor()
	 */
	public EMFProxyDescriptor getEMFProxyDescriptor()
	{
		return descriptor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getStore()
	 */
	public EStore getStore()
	{
		if (store == null)
		{
			synchronized (this)
			{
				if (store == null)
				{
					store = new EMFProxyStore(this);
				}
			}
		}
		return store;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getParameterValue(java.lang.String)
	 */
	public String getParameterValue(String name)
	{
		return descriptor.getParameterValue(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getFirstClassResolverForMasterObject(java.lang.Object,
	 * java.lang.Object, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public <T> IProxyClassResolver<T> getFirstClassResolverForMasterObject(Object context, T masterObject,
		Map<String, Object> parameters)
	{
		Collection<ProxyExtensionClassWrapper<IProxyClassResolver<?>>> classResolversWrappers = descriptor.getClassResolvers().values();
		for (ProxyExtensionClassWrapper<IProxyClassResolver<?>> wrapper: classResolversWrappers)
		{
			IProxyClassResolver<?> instance = wrapper.getInstance();
			if (instance.isValidMasterObject(this, context, masterObject, parameters))
			{
				return (IProxyClassResolver<T>) instance;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getAllClassResolversForMasterObject(java.lang.Object,
	 * java.lang.Object, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<IProxyClassResolver<T>> getAllClassResolversForMasterObject(Object context, T masterObject,
		Map<String, Object> parameters)
	{
		List<IProxyClassResolver<T>> resolvers = new ArrayList<>();
		Collection<ProxyExtensionClassWrapper<IProxyClassResolver<?>>> classResolversWrappers = descriptor.getClassResolvers().values();
		for (ProxyExtensionClassWrapper<IProxyClassResolver<?>> wrapper: classResolversWrappers)
		{
			IProxyClassResolver<?> instance = wrapper.getInstance();
			if (instance.isValidMasterObject(this, context, masterObject, parameters))
			{
				resolvers.add((IProxyClassResolver<T>) instance);
			}
		}
		return resolvers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getCachedSlaveObject(java.lang.Object, java.lang.Object)
	 */
	public <T> IEMFProxyObject getCachedSlaveObject(Object context, T masterObject)
	{
		checkInit();
		return getCache(context).getSlaveObject(masterObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getSlaveObject(java.lang.Object, java.lang.Object)
	 */
	public <T> IEMFProxyObject getSlaveObject(Object context, T masterObject)
	{
		return getSlaveObject(context, masterObject, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getSlaveObject(java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public <T> IEMFProxyObject getSlaveObject(Object context, T masterObject, Map<String, Object> parameters)
	{
		checkInit();
		IEMFProxyObject slave = null;
		if (parameters == null)
		{
			// if parameters are not set, it's ok to look first into the cache
			if (context != null)
			{
				slave = getCache(context).getSlaveObject(masterObject);
			}
		}
		if (slave != null)
		{
			return slave;
		}
		else
		{
			synchronized (masterObject)
			{
				// look again in the cache, maybe another thread just checked in
				// something
				if (parameters == null)
				{
					// if parameters are not set, it's ok to look first into the
					// cache
					if (context != null)
					{
						slave = getCache(context).getSlaveObject(masterObject);
					}
				}
				if (slave != null)
				{
					return slave;
				}
				List<IProxyClassResolver<T>> resolvers = getAllClassResolversForMasterObject(context, masterObject,
					parameters);
				InternalProxyConfigurationError.assertTrue(!resolvers.isEmpty(),
					"No IProxyClassResolver for master object:" + masterObject); //$NON-NLS-1$

				for (IProxyClassResolver<T> resolver: resolvers)
				{
					boolean masterChanged = false;
					T resolvedMaster = resolver.resolveMaster(this, context, masterObject);
					if (resolvedMaster != null && resolvedMaster != masterObject)
					{
						// use the resolvedMaster, later we will also check the
						// cache
						masterChanged = true;
						masterObject = resolvedMaster;
					}

					EClass eClass = resolver.getEClass(this, context, masterObject, parameters);
					if (eClass != null)
					{
						IEMFProxyObject result = null;
						try
						{
							result = (IEMFProxyObject) EcoreUtil.create(eClass);
						}
						catch (ClassFormatError exception)
						{
							if (exception.getMessage().contains("Duplicate field name&signature in class file")) //$NON-NLS-1$
							{
								CessarPluginActivator.getDefault().logError(
									"A java.lang.ClassFormatError occurred when creating the Presentation Model. Please check for duplicate short names or differences only in case sensitive"); //$NON-NLS-1$

							}

						}

						if (context == null || masterChanged)
						{
							if (context == null)
							{
								context = resolver.getStandardContext(this, result);
							}

							// if the delivered context is null this doesn't mean that
							// the
							// object is not cached
							// now that we have a valid context look again inside the
							// cache,
							// maybe there is something there.

							slave = getCache(context).getSlaveObject(masterObject);
							if (slave != null)
							{
								return slave;
							}
						}

						IMasterObjectWrapper<? extends T> wrapper = resolver.getWrapper(this, context, result,
							masterObject, parameters);

						if (result instanceof EMFProxyObjectImpl)
						{
							EMFProxyObjectImpl proxyResult = (EMFProxyObjectImpl) result;
							proxyResult.eSetProxyClassResolver(resolver);
							proxyResult.eSetMasterWrapper(wrapper);
							proxyResult.eSetContext(context);
						}
						resolver.postCreateSlave(this, context, masterObject, result);
						getCache(context).addToCache(result, wrapper.getAllMasterObjects());

						return result;
					}
				}
				throw new InternalProxyConfigurationError("No EClass for master object:" + masterObject); //$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getMasterObjects(eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public <T> List<T> getMasterObjects(EMFProxyObjectImpl slaveObject)
	{
		checkInit();
		Assert.isNotNull(slaveObject);
		@SuppressWarnings("unchecked")
		IMasterObjectWrapper<T> wrapper = (IMasterObjectWrapper<T>) slaveObject.eGetMasterWrapper();
		if (wrapper == null)
		{
			return Collections.emptyList();
		}
		else
		{
			List<T> result = new ArrayList<>();
			result.addAll(wrapper.getAllMasterObjects());
			return result;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#createObjectWrapper(java.lang.Object,
	 * eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public void createObjectWrapper(Object context, EMFProxyObjectImpl proxyObject)
	{
		checkInit();
		if (context == null)
		{
			context = DEFAULT_CONTEXT;
		}
		IProxyClassResolver<?> resolver = proxyObject.eGetProxyClassResolver();
		if (resolver == null)
		{
			InternalProxyConfigurationError.assertTrue(false,
				"No IProxyClassResolver for slave object:" + proxyObject.getClass().getName()); //$NON-NLS-1$
		}

		IMasterObjectWrapper<?> wrapper = resolver.createWrapper(this, context, proxyObject);
		proxyObject.eSetMasterWrapper(wrapper);
		getCache(context).addToCache(proxyObject, wrapper.getAllMasterObjects());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#lookupContext(eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public void lookupContext(EMFProxyObjectImpl emfProxyObjectImpl)
	{
		IProxyClassResolver<?> proxyClassResolver = emfProxyObjectImpl.eGetProxyClassResolver();
		if (proxyClassResolver != null)
		{
			emfProxyObjectImpl.eSetContext(proxyClassResolver.getStandardContext(this, emfProxyObjectImpl));
		}
		else
		{
			emfProxyObjectImpl.eSetContext(IEMFProxyConstants.DEFAULT_CONTEXT);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#updateSlave(java.lang.Object,
	 * eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, java.lang.Object)
	 */
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, T masterObject)
	{
		if (masterObject == null)
		{
			updateSlave(context, proxyObject, Collections.emptyList(), null);
		}
		else
		{
			updateSlave(context, proxyObject, Collections.singletonList(masterObject), null);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#updateSlave(java.lang.Object,
	 * eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, java.lang.Object, java.util.Map)
	 */
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, T masterObject,
		java.util.Map<String, Object> parameters)
	{
		if (masterObject == null)
		{
			updateSlave(context, proxyObject, Collections.emptyList(), parameters);
		}
		else
		{
			updateSlave(context, proxyObject, Collections.singletonList(masterObject), parameters);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#updateSlave(java.lang.Object,
	 * eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, java.util.Collection, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, Collection<T> masterObjects,
		Map<String, Object> parameters)
	{
		checkInit();
		if (context == null)
		{
			context = DEFAULT_CONTEXT;
		}
		T masterObject = null;
		if (!masterObjects.isEmpty())
		{
			masterObject = masterObjects.iterator().next();
		}
		// System.err.println("about to remove");
		//
		Collection<T> allMasters = new HashSet<>(masterObjects);
		allMasters.addAll((Collection<T>) getMasterObjects(proxyObject));

		getCache(context).removeMastersFromCache(allMasters);

		IProxyClassResolver<T> resolver = (IProxyClassResolver<T>) proxyObject.eGetProxyClassResolver();
		InternalProxyConfigurationError.assertTrue(resolver != null,
			"No IProxyClassResolver for slave object:" + proxyObject); //$NON-NLS-1$

		IMasterObjectWrapper<? extends T> wrapper = resolver.getWrapper(this, context, proxyObject, masterObject,
			parameters);
		proxyObject.eSetMasterWrapper(wrapper);
		getCache(context).addToCache(proxyObject, wrapper.getAllMasterObjects());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#updateSlaveFeature(eu.cessar.ct.emfproxy.IMasterFeatureWrapper)
	 */
	public <T> void updateSlaveFeature(IMasterFeatureWrapper<T> featureWrapper)
	{
		checkInit();
		EMFProxyObjectImpl proxyObject = featureWrapper.getProxyObject();
		EStructuralFeature feature = featureWrapper.getWrappedFeature();
		if (proxyObject == null || feature == null)
		{
			// nothing to do
			return;
		}
		@SuppressWarnings("unchecked")
		IProxyFeatureResolver<T> resolver = (IProxyFeatureResolver<T>) getFeatureResolver(feature);

		InternalProxyConfigurationError.assertTrue(resolver != null,
			"No IProxyFeatureResolver for feature :" + feature); //$NON-NLS-1$
		IMasterFeatureWrapper<T> newWrapper = resolver.getFeatureWrapper(this, proxyObject, feature);
		proxyObject.eSetCachedFeatureWrapper(feature, newWrapper);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getProxyElementAnnotation(org.eclipse.emf.ecore.EModelElement,
	 * java.lang.String)
	 */
	public String getProxyElementAnnotation(EModelElement element, String key)
	{
		EAnnotation ann = element.getEAnnotation(IEMFProxyConstants.PROXY_ANN_NAME);
		if (ann != null)
		{
			EMap<String, String> map = ann.getDetails();
			if (map != null)
			{
				return map.get(key);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getClassResolver(org.eclipse.emf.ecore.EClass)
	 */
	public IProxyClassResolver<?> getClassResolver(EClass eClass)
	{
		String id = getProxyElementAnnotation(eClass, IEMFProxyConstants.ANN_KEY_TYPE);
		if (id != null)
		{
			Map<String, ProxyExtensionClassWrapper<IProxyClassResolver<?>>> resolvers = descriptor.getClassResolvers();
			if (resolvers.containsKey(id))
			{
				return resolvers.get(id).getInstance();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getMasterObjectWrapper(eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<?> getMasterObjectWrapper(EMFProxyObjectImpl slaveObject)
	{
		return slaveObject.eGetMasterWrapper();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getFeatureResolver(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IProxyFeatureResolver<?> getFeatureResolver(EStructuralFeature feature)
	{
		String id = getProxyElementAnnotation(feature, IEMFProxyConstants.ANN_KEY_TYPE);
		if (id != null)
		{
			Map<String, ProxyExtensionClassWrapper<IProxyFeatureResolver<?>>> resolvers = descriptor.getFeatureResolvers();
			ProxyExtensionClassWrapper<IProxyFeatureResolver<?>> wrapper = resolvers.get(id);
			if (wrapper != null)
			{
				return wrapper.getInstance();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getMasterFeatureWrapper(eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<?> getMasterFeatureWrapper(EMFProxyObjectImpl slaveObject, EStructuralFeature feature,
		boolean createIfMissing)
	{
		IMasterFeatureWrapper<?> wrapper = slaveObject.eGetCachedFeatureWrapper(feature);
		if (wrapper != null)
		{
			return wrapper;
		}
		else if (createIfMissing)
		{
			IProxyFeatureResolver<?> resolver = getFeatureResolver(feature);
			InternalProxyConfigurationError.assertTrue(resolver != null,
				"No IProxyFeatureResolver for feature :" + feature); //$NON-NLS-1$
			IMasterFeatureWrapper<?> newWrapper = resolver.getFeatureWrapper(this, slaveObject, feature);
			slaveObject.eSetCachedFeatureWrapper(feature, newWrapper);
			return newWrapper;
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getOperationResolver(org.eclipse.emf.ecore.EOperation)
	 */
	public IProxyOperationResolver<?> getOperationResolver(EOperation operation)
	{
		String id = getProxyElementAnnotation(operation, IEMFProxyConstants.ANN_KEY_TYPE);
		if (id != null)
		{
			Map<String, ProxyExtensionClassWrapper<IProxyOperationResolver<?>>> resolvers = descriptor.getOperationResolvers();
			if (resolvers.containsKey(id))
			{
				return resolvers.get(id).getInstance();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getLogger()
	 */
	public IEMFProxyLogger getLogger()
	{
		return logger;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#dispose()
	 */
	public void dispose()
	{
		clear();
		cache = null;
		store = null;
		descriptor.removeEngine(project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#clear()
	 */
	public void clear()
	{
		if (cache != null)
		{
			for (Object context: cache.keySet())
			{
				cache.get(context).clearCache();
			}
			cache.clear();
		}

		synchronized (EMFProxyEngine.class)
		{
			Set<Thread> threads = new HashSet<>(THREAD_ENGINE_MAPPING.keySet());
			Iterator<Thread> threadIt = threads.iterator();
			while (threadIt.hasNext())
			{
				Thread th = threadIt.next();
				List<IEMFProxyEngine> list = THREAD_ENGINE_MAPPING.get(th);
				List<IEMFProxyEngine> remaining = null;
				if (th.isAlive() || list != null)
				{
					remaining = new ArrayList<>();
					Iterator<IEMFProxyEngine> engineIt = list.iterator();
					while (engineIt.hasNext())
					{
						IEMFProxyEngine proxyEngine = engineIt.next();
						if (!(proxyEngine == this))
						{
							remaining.add(proxyEngine);
						}
					}
					if (remaining.size() == 0)
					{
						remaining = null;
					}
				}
				if (remaining == null)
				{
					THREAD_ENGINE_MAPPING.remove(th);
				}
				else
				{
					// remaining and list != null at this point
					if (remaining.size() != list.size())
					{
						THREAD_ENGINE_MAPPING.put(th, remaining);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#getCache()
	 */
	private IEMFProxyCache<IEMFProxyObject, Object> getCache(Object context)
	{
		if (context == null)
		{
			context = DEFAULT_CONTEXT;
		}
		IEMFProxyCache<IEMFProxyObject, Object> contextCache = cache.get(context);
		if (contextCache != null)
		{
			return contextCache;
		}
		else
		{
			synchronized (context)
			{
				contextCache = cache.get(context);
				if (contextCache != null)
				{
					return contextCache;
				}
				contextCache = new EMFProxyCache<>();
				cache.put(context, contextCache);
				return contextCache;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IEMFProxyEngine#clearCacheForResources(java.util.Collection)
	 */
	public synchronized void clearCacheForResources(final Collection<Resource> unloadedResources)
	{
		if (cache != null)
		{
			for (IEMFProxyCache<IEMFProxyObject, Object> cacheInstance: cache.values())
			{
				cacheInstance.clearCache(new IFilter<IEMFProxyObject, Object>()
				{
					public boolean accept(Object master, ICacheValue<IEMFProxyObject> slaveValue)
					{
						if (master instanceof EObject && slaveValue != null)
						{
							Resource eMasterRes = slaveValue.getMasterResource();
							if (eMasterRes != null && unloadedResources.contains(eMasterRes))
							{
								return true;
							}
						}
						return false;
					}

					public void aboutToRemove(Object masterValue, ICacheValue<IEMFProxyObject> slaveValue)
					{
						if (masterValue instanceof EObject && slaveValue != null)
						{
							EMFProxyObjectImpl slave = (EMFProxyObjectImpl) slaveValue.getSlave();
							if (slave != null)
							{
								EMFProxyObjectImpl eContainer = (EMFProxyObjectImpl) slave.eContainer();
								EStructuralFeature cntFeature = slave.eContainingFeature();
								if (eContainer != null && cntFeature != null)
								{
									eContainer.eSetCachedFeatureWrapper(cntFeature, null);
								}
							}
						}
					}
				});
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Engine for " + getProject().getName() + " [" + hashCode() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
