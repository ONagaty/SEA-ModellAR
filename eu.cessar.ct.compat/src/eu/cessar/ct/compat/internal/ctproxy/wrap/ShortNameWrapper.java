/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 31.07.2012 11:14:54 </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * @author uidl6458
 * 
 */
public class ShortNameWrapper extends AbstractMasterFeatureWrapper<Object> implements
	ISingleMasterFeatureWrapper<Object>
{

	private final GReferrable master;

	/**
	 * @param engine
	 */
	public ShortNameWrapper(IEMFProxyEngine engine, GReferrable master)
	{
		super(engine);
		// TODO Auto-generated constructor stub
		this.master = master;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<Object> getFeatureClass()
	{
		return Object.class;
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
		if (master.gIsSetShortName())
		{
			String result = master.gGetShortName();
			if (result != null)
			{
				return result;
			}
		}
		return EStructuralFeature.Internal.DynamicValueHolder.NIL;
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
				master.gSetShortName(newValue == null ? null : String.valueOf(newValue));
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return master.gIsSetShortName();
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
				master.gUnsetShortName();
			}
		});
	}

}
