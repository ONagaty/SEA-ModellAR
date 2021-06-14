/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleDirectEAttributeWrapper extends AbstractDirectEAttributeWrapper implements
	ISingleMasterFeatureWrapper<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	/**
	 * @param engine
	 * @param masterFeature
	 * @param master
	 * @param convertor
	 */
	public SingleDirectEAttributeWrapper(IEMFProxyEngine engine, EObject master,
		EAttribute eAttribute, IDataTypeConvertor<Object, Object> convertor)
	{
		super(engine, master, eAttribute, convertor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
	{
		// if (!master.eIsSet(eAttribute))
		// {
		// return EStructuralFeature.Internal.DynamicValueHolder.NIL;
		// }
		Object masterValue = master.eGet(eAttribute);
		if (masterValue == null || (!(masterValue instanceof Enum) && !master.eIsSet(eAttribute)))
		{
			return EStructuralFeature.Internal.DynamicValueHolder.NIL;
		}
		if (convertor != null)
		{
			return convertor.convertToSlaveValue(masterValue);
		}
		return masterValue;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		// newValue is from slave
		if (convertor != null)
		{
			newValue = convertor.convertToMasterValue(newValue);
		}

		final Object masterValue = newValue;
		updateModel(new Runnable()
		{

			public void run()
			{
				if (masterValue != null)
				{
					master.eSet(eAttribute, masterValue);
				}
				else
				{
					unsetValue();
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return master.eIsSet(eAttribute);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{

		updateModel(new Runnable()
		{

			public void run()
			{
				master.eUnset(eAttribute);
			}
		});
	}
}
