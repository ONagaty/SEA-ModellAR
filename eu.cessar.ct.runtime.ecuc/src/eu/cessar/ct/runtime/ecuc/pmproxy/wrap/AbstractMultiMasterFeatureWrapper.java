/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 4:30:01 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.lang.reflect.Array;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.platform.util.ReflectionUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.ASMNames;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.Constants;
import eu.cessar.ct.runtime.execution.IExecutionLoader;

/**
 * 
 */
public abstract class AbstractMultiMasterFeatureWrapper<T> extends AbstractMasterFeatureWrapper<T>
	implements IMultiMasterFeatureWrapper<T>
{

	/**
	 * @param engine
	 */
	public AbstractMultiMasterFeatureWrapper(IEMFProxyEngine engine)
	{
		super(engine);
	}

	/**
	 * @return
	 */
	protected abstract List<?> getWrappedList();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#size()
	 */
	public int size()
	{
		List<?> wrappedList = getWrappedList();
		if (wrappedList != null)
		{
			return getWrappedList().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#isEmpty()
	 */
	public boolean isEmpty()
	{
		return getWrappedList().isEmpty();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	public int indexOf(Object slaveValue)
	{
		for (int i = 0; i < size(); i++)
		{
			T other = get(i);
			if (slaveValue == other)
			{
				return i;
			}
			else if (slaveValue == null)
			{
				// do nothing, "other" is not null otherwise the previous test
				// will not fail
			}
			else if (slaveValue.equals(other))
			{
				return i;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object slaveValue)
	{
		for (int i = size() - 1; i >= 0; i--)
		{
			T other = get(i);
			if (slaveValue == other)
			{
				return i;
			}
			else if (slaveValue == null)
			{
				// do nothing, "other" is not null otherwise the previous test
				// will not fail
			}
			else if (slaveValue.equals(other))
			{
				return i;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray()
	 */
	public Object[] toArray()
	{
		int size = size();
		EStructuralFeature wrappedFeature = getWrappedFeature();
		if (wrappedFeature instanceof EReference)
		{
			EReference wrappedReference = (EReference) wrappedFeature;
			EClass eReferenceType = wrappedReference.getEReferenceType();
			// get the class name
			String classCName = ASMNames.getEClassInterfaceCName(Constants.DOT, eReferenceType);
			// get the class
			IProject project = getEngine().getProject();
			if (project != null)
			{
				IExecutionLoader activeExecutionLoader = CessarRuntime.getExecutionSupport().getActiveExecutionLoader(
					project);
				if (activeExecutionLoader != null)
				{
					Class<?> instanceClass;
					try
					{
						instanceClass = Class.forName(classCName, true,
							activeExecutionLoader.getClassLoader());
					}
					catch (ClassNotFoundException e)
					{
						return toArray((Object[]) Array.newInstance(Object.class, size), size);
					}

					return toArray((Object[]) Array.newInstance(instanceClass, size), size);
				}
			}
		}
		if (wrappedFeature instanceof EAttribute)
		{
			EAttribute wrappedAttribute = (EAttribute) wrappedFeature;
			Class<?> instanceClass = wrappedAttribute.getEAttributeType().getInstanceClass();
			if (instanceClass.isPrimitive())
			{
				instanceClass = ReflectionUtils.getWrapperClass(instanceClass);
			}
			return toArray((Object[]) Array.newInstance(instanceClass, size), size);
		}
		return toArray((Object[]) Array.newInstance(Object.class, size), size);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#toArray(S[])
	 */
	public <S> S[] toArray(S[] array)
	{
		return toArray(array, size());
	}

	/**
	 * @param newInstance
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <S> S[] toArray(S[] array, int cachedSize)
	{
		if (array.length < cachedSize)
		{
			S[] newArray = (S[]) Array.newInstance(array.getClass().getComponentType(), cachedSize);
			array = newArray;
		}
		for (int i = 0; i < cachedSize; i++)
		{
			array[i] = (S) get(i);
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	public void clear()
	{
		// clean up also the cache present into the IEMFProxyObject
		getProxyObject().eSetCachedFeatureWrapper(getWrappedFeature(), null);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		int hashCode = 1;
		Object[] array = toArray();
		for (Object object: array)
		{
			hashCode = 31 * hashCode + (object == null ? 0 : object.hashCode());
		}
		return hashCode;
	}
}
