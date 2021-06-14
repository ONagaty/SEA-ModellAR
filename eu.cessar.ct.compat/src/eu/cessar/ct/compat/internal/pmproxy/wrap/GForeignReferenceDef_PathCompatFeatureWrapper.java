/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterAttributeWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractSingleMasterAttributeWrapper;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 *
 * @Review uidl7321 - Apr 11, 2012
 *
 */

public class GForeignReferenceDef_PathCompatFeatureWrapper extends AbstractSingleMasterAttributeWrapper<String>
	implements IMasterAttributeWrapper<String>
{

	private final GReferenceValue refValue;

	/**
	 * @param engine
	 * @param refValue
	 */
	public GForeignReferenceDef_PathCompatFeatureWrapper(IEMFProxyEngine engine, GReferenceValue refValue)
	{
		super(engine);
		this.refValue = refValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<String> getFeatureClass()
	{
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public String getValue()
	{
		if (refValue != null)
		{

			GReferrable refTarget = refValue.gGetValue();
			if (refTarget != null)
			{
				return MetaModelUtils.getAbsoluteQualifiedName(refTarget);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
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
					GIdentifiable refTarget = null;

					List<EObject> eObjectsWithQName = EObjectLookupUtils.getEObjectsWithQName(engine.getProject(),
						(String) newValue);
					if (!eObjectsWithQName.isEmpty())
					{
						refTarget = (GIdentifiable) eObjectsWithQName.get(0);
					}

					refValue.gSetValue(refTarget);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return getValue() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		setValue(null);
	}

}
