package eu.cessar.ct.emfproxy;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.internal.EMFProxyDescriptor;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * FIXME: comment this
 *
 */
public interface IEMFProxyEngine
{

	/**
	 * @return
	 */
	public IProject getProject();

	/**
	 *
	 * @return
	 */
	public EMFProxyDescriptor getEMFProxyDescriptor();

	/**
	 * @return
	 */
	public InternalEObject.EStore getStore();

	/**
	 * @param element
	 * @param key
	 * @return
	 */
	public String getProxyElementAnnotation(EModelElement element, String key);

	/**
	 * Return the cached slave object using the specified context for the master or null if there is no cached value
	 *
	 * @param <T>
	 * @param masterObject
	 * @return
	 */
	public <T> IEMFProxyObject getCachedSlaveObject(Object context, T masterObject);

	/**
	 * Return the slave object for <code>masterObject</code> in the <code>context</code> context. If the context is null
	 * a standard one will be determined using the class resolver
	 *
	 * @param <T>
	 * @param context
	 * @param masterObject
	 * @return
	 */
	public <T> IEMFProxyObject getSlaveObject(Object context, T masterObject);

	/**
	 * Return the slave object for <code>masterObject</code> in the <code>context</code> context. If the context is null
	 * a standard one will be determined using the class resolver. Additional parameters can be delivered to the class
	 * resolver. For the meaning of the additional parameters check individual class resolvers
	 *
	 * @param <T>
	 * @param context
	 * @param masterObject
	 * @param parameters
	 * @return
	 */
	public <T> IEMFProxyObject getSlaveObject(Object context, T masterObject, Map<String, Object> parameters);

	/**
	 * @param slaveObject
	 * @return
	 */
	public <T> List<T> getMasterObjects(EMFProxyObjectImpl slaveObject);

	/**
	 * Return the class resolver associated to the <code>eClass</code> parameter. This will be done by checking the
	 * annotation <code>EMF_PROXY/TYPE</code> assigned to the class. If there is no such annotation or the proxy engine
	 * instance does not contribute with a class resolver for that type, null will be returned.
	 *
	 * @param eClass
	 * @return
	 */
	public IProxyClassResolver<?> getClassResolver(EClass eClass);

	/**
	 * @param slaveObject
	 * @return
	 */
	public IMasterObjectWrapper<?> getMasterObjectWrapper(EMFProxyObjectImpl slaveObject);

	/**
	 * Return the first class resolver for a master object in a particular context optionally using some parameters
	 *
	 * @param <T>
	 * @param context
	 * @param masterObject
	 * @param parameters
	 *
	 * @return The first found class resolver
	 */
	public <T> IProxyClassResolver<T> getFirstClassResolverForMasterObject(Object context, T masterObject,
		Map<String, Object> parameters);

	/**
	 * Return All class resolvers for a master object in a particular context optionally using some parameters
	 *
	 * @param <T>
	 * @param context
	 * @param masterObject
	 * @param parameters
	 *
	 * @return All found class resolvers
	 */
	public <T> List<IProxyClassResolver<T>> getAllClassResolversForMasterObject(Object context, T masterObject,
		Map<String, Object> parameters);

	/**
	 * Return the feature resolver associated to the <code>feature</code> parameter. This will be done by checking the
	 * annotation <code>EMF_PROXY/TYPE</code> assigned to the feature. If there is no such annotation or the proxy
	 * engine instance does not contribute with a class resolver for that type, null will be returned.
	 *
	 * @param feature
	 * @return
	 */
	public IProxyFeatureResolver<?> getFeatureResolver(EStructuralFeature feature);

	/**
	 * @param slaveObject
	 * @param feature
	 * @param createIfMissing
	 * @return
	 */
	public IMasterFeatureWrapper<?> getMasterFeatureWrapper(EMFProxyObjectImpl slaveObject, EStructuralFeature feature,
		boolean createIfMissing);

	/**
	 * Return the operation resolver associated to the <code>feature</code> parameter. This will be done by checking the
	 * annotation <code>EMF_PROXY/TYPE</code> assigned to the operation. If there is no such annotation or the proxy
	 * engine instance does not contribute with a class resolver for that type, null will be returned.
	 *
	 * @param feature
	 * @return
	 */
	public IProxyOperationResolver<?> getOperationResolver(EOperation operation);

	/**
	 * @return
	 */
	public IEMFProxyLogger getLogger();

	/**
	 *
	 */
	public void dispose();

	/**
	 *
	 */
	public void clear();

	/**
	 * @param context
	 * @param emfProxyObjectImpl
	 */
	public void createObjectWrapper(Object context, EMFProxyObjectImpl emfProxyObjectImpl);

	/**
	 * @param <T>
	 * @param context
	 * @param proxyObject
	 * @param masterObject
	 */
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, T masterObject);

	/**
	 * @param <T>
	 * @param context
	 * @param proxyObject
	 * @param masterObject
	 */
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, T masterObject,
		Map<String, Object> parameters);

	/**
	 * @param <T>
	 * @param context
	 * @param proxyObject
	 * @param masterObject
	 * @param parameters
	 */
	public <T> void updateSlave(Object context, EMFProxyObjectImpl proxyObject, Collection<T> masterObject,
		Map<String, Object> parameters);

	/**
	 * @param multiGContainerReferenceWrapper
	 */
	public <T> void updateSlaveFeature(IMasterFeatureWrapper<T> featureWrapper);

	/**
	 * @param emfProxyObjectImpl
	 */
	public void lookupContext(EMFProxyObjectImpl emfProxyObjectImpl);

	/**
	 *
	 * @param name
	 * @return
	 */
	public String getParameterValue(String name);

	/**
	 * @param unloadedResources
	 */
	public void clearCacheForResources(Collection<Resource> unloadedResources);

}
