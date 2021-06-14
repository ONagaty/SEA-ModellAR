/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 12:49:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * Encapsulate access to a feature that could exist in multiple EObjects at the
 * same time
 * 
 */
public class NonsplitedSingleEAttributeAccessor implements INonsplitedSingleEAttributeAccessor
{

	private final List<? extends EObject> objects;
	private final EAttribute typeEAttribute;
	private final INonsplitedSingleEAttributeAccessListener listener;

	public NonsplitedSingleEAttributeAccessor(List<? extends EObject> objects,
		EAttribute typeEAttribute, INonsplitedSingleEAttributeAccessListener listener)
	{
		Assert.isNotNull(typeEAttribute);
		this.objects = objects;
		this.typeEAttribute = typeEAttribute;
		this.listener = listener;
		checkValidAttributes(typeEAttribute);
	}

	/**
	 * @param attribute
	 * @param obj
	 * @return
	 */
	private RuntimeException getInconsistenceError(EAttribute attribute, EObject obj)
	{
		return new IllegalArgumentException("The attribute " + attribute.getName() //$NON-NLS-1$
			+ " have a invalid splitted value.\nOwner object is " + obj); //$NON-NLS-1$
	}

	/**
	 * @param lastResult
	 * @param newResult
	 * @return
	 */
	private boolean resultEquals(Object lastResult, Object newResult)
	{
		if (lastResult == newResult)
		{
			return true;
		}
		if (lastResult == null || newResult == null)
		{
			return false;
		}
		return lastResult.equals(newResult);
	}

	/**
	 * @param attribute
	 */
	private void checkValidAttributes(EAttribute attribute)
	{
		Object lastResult = null;
		boolean lastIsSet = true;
		for (int i = 0; i < objects.size(); i++)
		{
			EObject obj = objects.get(i);
			if (i == 0)
			{
				lastIsSet = obj.eIsSet(attribute);
				if (lastIsSet)
				{
					lastResult = obj.eGet(attribute);
				}
			}
			else
			{
				boolean isSet = obj.eIsSet(attribute);
				if (isSet != lastIsSet)
				{
					throw getInconsistenceError(attribute, obj);
				}
				else
				{
					Object newResult = obj.eGet(attribute);
					if (!resultEquals(lastResult, newResult))
					{
						throw getInconsistenceError(attribute, obj);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#getValue()
	 */
	public Object getValue()
	{
		Object result;
		if (objects.size() == 0)
		{
			result = null;
		}
		else
		{
			result = objects.get(0).eGet(typeEAttribute);
		}
		if (listener != null)
		{
			listener.notifySingleAttrGet(typeEAttribute, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#setValue(java.lang.Object)
	 */
	public void setValue(Object value)
	{
		if (listener != null)
		{
			listener.preNotifySingleAttrSet(typeEAttribute, value);
		}
		EObject[] array = objects.toArray(new EObject[0]);
		for (EObject eObj: array)
		{
			eObj.eSet(typeEAttribute, value);
		}
		if (listener != null)
		{
			listener.postNotifySingleAttrSet(typeEAttribute, value);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#isSet()
	 */
	public boolean isSet()
	{
		boolean result = objects.get(0).eIsSet(typeEAttribute);
		if (listener != null)
		{
			listener.notifySingleAttrIsSet(typeEAttribute, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor#unset()
	 */
	public void unset()
	{
		if (listener != null)
		{
			listener.preNotifySingleAttrUnset(typeEAttribute);
		}
		EObject[] array = objects.toArray(new EObject[0]);
		for (EObject eObj: array)
		{
			eObj.eUnset(typeEAttribute);
		}
		if (listener != null)
		{
			listener.postNotifySingleAttrUnset(typeEAttribute);
		}
	}

}
