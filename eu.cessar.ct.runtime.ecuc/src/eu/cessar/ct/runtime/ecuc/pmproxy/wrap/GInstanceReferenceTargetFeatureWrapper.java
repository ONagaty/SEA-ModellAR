/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 9:36:02 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 */
public class GInstanceReferenceTargetFeatureWrapper extends AbstractSingleMasterReferenceWrapper
{

	private final GInstanceReferenceValue iRef;

	/**
	 * @param engine
	 */
	public GInstanceReferenceTargetFeatureWrapper(IEMFProxyEngine engine,
		GInstanceReferenceValue iRef)
	{
		super(engine);
		// TODO Auto-generated constructor stub
		this.iRef = iRef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (iRef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(iRef);
			if (mmService != null)
			{
				return mmService.getEcucMMService().getInstanceRefTarget(iRef);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return getValue() != null;
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
				if (newValue instanceof GIdentifiable || newValue == null)
				{
					IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(iRef);
					if (mmService != null)
					{
						mmService.getEcucMMService().setInstanceRefTarget(iRef,
							(GIdentifiable) newValue);
					}
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		setValue(null);
	}
}
