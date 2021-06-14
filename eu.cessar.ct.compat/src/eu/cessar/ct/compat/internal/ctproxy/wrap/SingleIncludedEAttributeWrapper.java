/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 8, 2011 9:00:49 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleIncludedEAttributeWrapper extends AbstractIncludedWrapper implements
	ISingleMasterFeatureWrapper<Object>
{

	private final IDataTypeConvertor<Object, Object> convertor;

	/**
	 * @param engine
	 * @param master
	 * @param lvl1MasterFeature
	 * @param lvl1ExpectedType
	 * @param lvl2MasterFeature
	 * @param lvl2ExpectedType
	 * @param finalMasterFeature
	 */
	public SingleIncludedEAttributeWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature lvl2MasterFeature, EClass lvl2ExpectedType,
		EStructuralFeature finalMasterFeature, IDataTypeConvertor<Object, Object> convertor)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, lvl2MasterFeature,
			lvl2ExpectedType, finalMasterFeature);
		// TODO Auto-generated constructor stub
		this.convertor = convertor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
	{
		Object nil = EStructuralFeature.Internal.DynamicValueHolder.NIL;

		if (getMaster().eIsSet(getLvl1MasterFeature()))
		{
			EObject children = (EObject) getMaster().eGet(getLvl1MasterFeature());
			if (getLvl1ExpectedType().isInstance(children))
			{
				if (getLvl2MasterFeature() != null)
				{
					children = (EObject) (children).eGet(getLvl2MasterFeature());
					if (!getLvl2ExpectedType().isInstance(children))
					{
						return nil;
					}
				}
				// found the children
				if (children.eIsSet(getFinalMasterFeature()))
				{
					Object result = children.eGet(getFinalMasterFeature());
					if (convertor != null)
					{
						return convertor.convertToSlaveValue(result);
					}
					else
					{
						return result;
					}
				}
			}
		}
		return nil;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(final Object newValue)
	{
		if (newValue == null)
		{
			unsetValue();
		}
		else
		{
			updateModel(new Runnable()
			{

				public void run()
				{
					if (!getMaster().eIsSet(getLvl1MasterFeature()))
					{
						EObject children = EcoreUtil.create(getLvl1ExpectedType());
						getMaster().eSet(getLvl1MasterFeature(), children);
					}
					if (getMaster().eIsSet(getLvl1MasterFeature()))
					{
						EObject children = (EObject) getMaster().eGet(getLvl1MasterFeature());
						if (getLvl1ExpectedType().isInstance(children))
						{
							if (getLvl2MasterFeature() != null)
							{
								if (!children.eIsSet(getLvl2MasterFeature()))
								{
									EObject children2 = EcoreUtil.create(getLvl2ExpectedType());
									children.eSet(getLvl2MasterFeature(), children2);
								}
								children = (EObject) children.eGet(getLvl2MasterFeature());
								if (!getLvl2ExpectedType().isInstance(children))
								{
									return;
								}
							}
							if (convertor != null)
							{
								children.eSet(getFinalMasterFeature(),
									convertor.convertToMasterValue(newValue));
							}
							else
							{
								children.eSet(getFinalMasterFeature(), newValue);
							}
						}
					}
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		if (getMaster().eIsSet(getLvl1MasterFeature()))
		{
			EObject children = (EObject) getMaster().eGet(getLvl1MasterFeature());
			if (getLvl1ExpectedType().isInstance(children))
			{
				if (getLvl2MasterFeature() != null)
				{
					children = (EObject) (children).eGet(getLvl2MasterFeature());
					if (!getLvl2ExpectedType().isInstance(children))
					{
						return false;
					}
				}
				// found the children
				return children.eIsSet(getFinalMasterFeature());
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		if (getMaster().eIsSet(getLvl1MasterFeature()))
		{
			final EObject[] children = {(EObject) getMaster().eGet(getLvl1MasterFeature())};
			if (getLvl1ExpectedType().isInstance(children[0]))
			{
				if (getLvl2MasterFeature() != null)
				{
					children[0] = (EObject) (children[0]).eGet(getLvl2MasterFeature());
					if (!getLvl2ExpectedType().isInstance(children[0]))
					{
						return;
					}
				}
				// found the children
				if (children[0].eIsSet(getFinalMasterFeature()))
				{
					updateModel(new Runnable()
					{

						public void run()
						{
							children[0].eUnset(getFinalMasterFeature());
						}
					});
				}
			}
		}
	}

}
