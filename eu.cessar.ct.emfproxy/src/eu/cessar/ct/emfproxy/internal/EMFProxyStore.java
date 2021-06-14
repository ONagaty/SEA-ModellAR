/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Nov 30, 2009 12:40:40 PM </copyright>
 */
package eu.cessar.ct.emfproxy.internal;

import static eu.cessar.ct.emfproxy.IEMFProxyConstants.NULL_VALUE;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * @author uidl6458
 *
 */
public class EMFProxyStore implements InternalEObject.EStore
{

	private final IEMFProxyEngine engine;

	/**
	 * Create a new EMF Proxy store for the provided engine
	 *
	 * @param engine
	 */
	public EMFProxyStore(IEMFProxyEngine engine)
	{
		this.engine = engine;
	}

	/**
	 * Create a new {@link InternalProxyConfigurationError} using the message. The arguments will be used to format the
	 * message
	 *
	 * @param message
	 * @param args
	 * @return
	 */
	private InternalProxyConfigurationError getError(String message, Object... args)
	{
		String msg = MessageFormat.format(message, args);
		return new InternalProxyConfigurationError(msg);
	}

	/**
	 *
	 */
	private void throwNotImplemented()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param value
	 */
	private void assertNotNull(Object value, String message, Object... params)
	{
		if (value == null || value == NULL_VALUE)
		{
			throw getError(NLS.bind(message, params));
		}
	}

	/**
	 * @param wrapper
	 * @return
	 */
	private IMultiMasterFeatureWrapper<?> getMultiWrapper(IMasterFeatureWrapper<?> wrapper)
	{
		if (!(wrapper instanceof IMultiMasterFeatureWrapper<?>))
		{
			throw getError("Expected multi wrapper but got {0}", wrapper); //$NON-NLS-1$
		}
		return (IMultiMasterFeatureWrapper<?>) wrapper;
	}

	/**
	 * @param wrapper
	 * @return
	 */
	private ISingleMasterFeatureWrapper<?> getSingleWrapper(IMasterFeatureWrapper<?> wrapper)
	{
		if (!(wrapper instanceof ISingleMasterFeatureWrapper<?>))
		{
			throw getError("Expected single wrapper but got {0}", wrapper); //$NON-NLS-1$
		}
		return (ISingleMasterFeatureWrapper<?>) wrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#add(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, int, java.lang.Object)
	 */
	public void add(InternalEObject object, EStructuralFeature feature, int index, Object value)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		getMultiWrapper(wrapper).add(index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#clear(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void clear(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		getMultiWrapper(wrapper).clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#contains(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public boolean contains(InternalEObject object, EStructuralFeature feature, Object slaveValue)
	{
		return indexOf(object, feature, slaveValue) != -1;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#create(org.eclipse.emf.ecore.EClass)
	 *
	 * @deprecated do not use
	 */
	@Deprecated
	public EObject create(EClass eClass)
	{
		throwNotImplemented();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#get(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, int)
	 */
	public Object get(InternalEObject object, EStructuralFeature feature, int index)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		if (index != NO_INDEX)
		{
			return getMultiWrapper(wrapper).get(index);
		}
		else
		{
			return getSingleWrapper(wrapper).getValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#getContainer(org.eclipse.emf.ecore.InternalEObject)
	 */
	public InternalEObject getContainer(InternalEObject object)
	{
		EMFProxyObjectImpl proxyObject = (EMFProxyObjectImpl) object;
		return proxyObject.eGetMasterWrapper().getSlaveContainer(proxyObject);
		// EMFProxyObjectImpl proxyObject = (EMFProxyObjectImpl) object;
		// Object masterContainer =
		// proxyObject.eGetMasterWrapper().getMasterContainer();
		// if (masterContainer == null)
		// {
		// return null;
		// }
		// else
		// {
		// // if the parent support multi context use the same context as the
		// // children, otherwise let the engine decide
		// IProxyClassResolver<Object> classResolver =
		// engine.getClassResolverForMasterObject(
		// IEMFProxyConstants.DEFAULT_CONTEXT, masterContainer, null);
		// if (classResolver.supportMultiContext(engine))
		// {
		// return engine.getSlaveObject(proxyObject.eGetContext(),
		// masterContainer);
		// }
		// else
		// {
		// return engine.getSlaveObject(null, masterContainer);
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#getContainingFeature(org.eclipse.emf.ecore.InternalEObject)
	 */
	public EStructuralFeature getContainingFeature(InternalEObject object)
	{
		EMFProxyObjectImpl childObject = (EMFProxyObjectImpl) object;
		EMFProxyObjectImpl parentObject = (EMFProxyObjectImpl) getContainer(object);
		return childObject.eGetProxyClassResolver().getContainingFeature(engine, parentObject, childObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#hashCode(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public int hashCode(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return wrapper.getHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#indexOf(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public int indexOf(InternalEObject object, EStructuralFeature feature, Object slaveValue)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		if (!wrapper.getFeatureClass().isInstance(slaveValue))
		{
			return -1;
		}
		return getMultiWrapper(wrapper).indexOf(slaveValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#isEmpty(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean isEmpty(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return getMultiWrapper(wrapper).isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#isSet(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean isSet(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		if (feature.isMany())
		{
			return !getMultiWrapper(wrapper).isEmpty();
		}
		else
		{

			return getSingleWrapper(wrapper).isSetValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#lastIndexOf(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public int lastIndexOf(InternalEObject object, EStructuralFeature feature, Object slaveValue)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		if (!wrapper.getFeatureClass().isInstance(slaveValue))
		{
			return -1;
		}
		return getMultiWrapper(wrapper).lastIndexOf(slaveValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#move(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, int, int)
	 */
	public Object move(InternalEObject object, EStructuralFeature feature, int targetIndex, int sourceIndex)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return getMultiWrapper(wrapper).move(targetIndex, sourceIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#remove(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, int)
	 */
	public Object remove(InternalEObject object, EStructuralFeature feature, int index)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		Object lastValue = getMultiWrapper(wrapper).get(index);
		getMultiWrapper(wrapper).remove(index);
		return lastValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#set(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, int, java.lang.Object)
	 */
	public Object set(InternalEObject object, EStructuralFeature feature, int index, Object value)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		Object oldValue = get(object, feature, index);
		if (feature.isMany())
		{
			getMultiWrapper(wrapper).set(index, value);
		}
		else
		{

			getSingleWrapper(wrapper).setValue(value);
		}
		return oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#size(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public int size(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return getMultiWrapper(wrapper).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#toArray(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public Object[] toArray(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return getMultiWrapper(wrapper).toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#toArray(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature, T[])
	 */
	public <T> T[] toArray(InternalEObject object, EStructuralFeature feature, T[] array)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		return getMultiWrapper(wrapper).toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.InternalEObject.EStore#unset(org.eclipse.emf.ecore.InternalEObject,
	 * org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void unset(InternalEObject object, EStructuralFeature feature)
	{
		IMasterFeatureWrapper<?> wrapper = lookupFeatureWrapper(object, feature);
		if (feature.isMany())
		{
			getMultiWrapper(wrapper).clear();
		}
		else
		{
			getSingleWrapper(wrapper).unsetValue();
		}
	}

	/**
	 * @param object
	 * @param feature
	 * @return
	 */
	private IMasterFeatureWrapper<?> lookupFeatureWrapper(InternalEObject object, EStructuralFeature feature)
	{
		EMFProxyObjectImpl proxyObject = (EMFProxyObjectImpl) object;
		// be sure that the proxy object is already initialized
		synchronized (proxyObject)
		{
			proxyObject.eDoInit();
			IMasterFeatureWrapper<?> wrapper = proxyObject.eGetCachedFeatureWrapper(feature);
			if (wrapper == null || !wrapper.haveLiveValues())
			{
				IProxyFeatureResolver<?> featureResolver = engine.getFeatureResolver(feature);
				assertNotNull(featureResolver, "Cannot locate feature resolver for feature: {0}", //$NON-NLS-1$
					feature.getEContainingClass().getName() + "." + feature.getName()); //$NON-NLS-1$
				wrapper = featureResolver.getFeatureWrapper(engine, proxyObject, feature);
				proxyObject.eSetCachedFeatureWrapper(feature, wrapper);
			}
			return wrapper;
		}
	}

}
