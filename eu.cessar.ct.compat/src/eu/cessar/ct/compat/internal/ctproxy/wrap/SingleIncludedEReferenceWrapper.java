/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 8, 2011 9:01:05 AM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class SingleIncludedEReferenceWrapper extends AbstractIncludedWrapper implements
	ISingleMasterFeatureWrapper<Object>
{

	/**
	 * @param engine
	 * @param master
	 * @param lvl1MasterFeature
	 * @param lvl1ExpectedType
	 * @param lvl2MasterFeature
	 * @param lvl2ExpectedType
	 * @param finalMasterFeature
	 */
	public SingleIncludedEReferenceWrapper(IEMFProxyEngine engine, EObject master,
		EStructuralFeature lvl1MasterFeature, EClass lvl1ExpectedType,
		EStructuralFeature lvl2MasterFeature, EClass lvl2ExpectedType,
		EStructuralFeature finalMasterFeature)
	{
		super(engine, master, lvl1MasterFeature, lvl1ExpectedType, lvl2MasterFeature,
			lvl2ExpectedType, finalMasterFeature);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public Object getValue()
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
						return null;
					}
				}
				// found the children
				if (children.eIsSet(getFinalMasterFeature()))
				{
					children = (EObject) children.eGet(getFinalMasterFeature());
					return engine.getCachedSlaveObject(getContext(), children);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(final Object newValue)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				EObject children;
				if (!getMaster().eIsSet(getLvl1MasterFeature()) && newValue != null)
				{
					// create in level 1
					children = EcoreUtil.create(getLvl1ExpectedType());
					getMaster().eSet(getLvl1MasterFeature(), children);
				}
				if (getMaster().eIsSet(getLvl1MasterFeature()))
				{
					children = (EObject) getMaster().eGet(getLvl1MasterFeature());
					if (getLvl1ExpectedType().isInstance(children))
					{
						if (getLvl2MasterFeature() != null)
						{
							if (!children.eIsSet(getLvl2MasterFeature()) && newValue != null)
							{
								EObject newChildren = EcoreUtil.create(getLvl2ExpectedType());
								children.eSet(getLvl2MasterFeature(), newChildren);
							}
							children = (EObject) children.eGet(getLvl2MasterFeature());
							if (!getLvl2ExpectedType().isInstance(children))
							{
								return;
							}
						}
						// found the children
						if (newValue == null)
						{
							children.eUnset(getFinalMasterFeature());
						}
						else
						{
							EObject masterTarget = (EObject) engine.getMasterObjects(
								(EMFProxyObjectImpl) newValue).get(0);
							children.eSet(getFinalMasterFeature(), masterTarget);
						}
					}
				}
			}
		});
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
					// unset it
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
