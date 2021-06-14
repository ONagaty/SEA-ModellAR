/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractSingleMasterAttributeWrapper;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 12, 2012
 */
public class GInstanceReference_TargetCompatFeatureWrapper extends
	AbstractSingleMasterAttributeWrapper<String>
{

	private final GInstanceReferenceValue iRef;

	/**
	 * @param engine
	 */
	public GInstanceReference_TargetCompatFeatureWrapper(IEMFProxyEngine engine,
		GInstanceReferenceValue iRef)
	{
		super(engine);
		this.iRef = iRef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public String getValue()
	{
		if (iRef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(getEngine().getProject());
			if (mmService != null)
			{
				GIdentifiable instanceRefTarget = mmService.getEcucMMService().getInstanceRefTarget(
					iRef);
				if (instanceRefTarget != null)
				{
					return MetaModelUtils.getAbsoluteQualifiedName(instanceRefTarget);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Class getFeatureClass()
	{
		return String.class;
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
				if (newValue instanceof String || newValue == null)
				{
					GIdentifiable target = null;

					List<EObject> eObjectsWithQName = EObjectLookupUtils.getEObjectsWithQName(
						engine.getProject(), (String) newValue);
					if (!eObjectsWithQName.isEmpty())
					{
						target = (GIdentifiable) eObjectsWithQName.get(0);
					}

					IMetaModelService mmService = null;
					if (iRef != null)
					{
						mmService = MMSRegistry.INSTANCE.getMMService(iRef.eClass());
					}
					if (mmService != null)
					{
						mmService.getEcucMMService().setInstanceRefTarget(iRef, target);
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
		return getValue() != null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		setValue(null);
	}

}
