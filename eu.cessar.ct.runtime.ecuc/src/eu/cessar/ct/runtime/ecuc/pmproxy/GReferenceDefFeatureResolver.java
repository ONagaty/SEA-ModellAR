/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 7, 2009 3:44:40 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGReferenceValueReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGReferenceValueReferenceWrapper;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;

/**
 * 
 */
public class GReferenceDefFeatureResolver extends GForeignReferenceDefFeatureResolver
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.GForeignReferenceDefFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine, org.eclipse.emf.ecore.EReference, org.eclipse.emf.common.util.EList)
	 */
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine, EReference reference,
		EList<GReferenceValue> references, List<GContainer> owners, GConfigReference definition)
	{
		if (reference.isMany())
		{
			return new MultiGReferenceValueReferenceWrapper(engine, references, definition);
		}
		else
		{
			if (references.size() >= 1)
			{
				GReferenceValue fRef = references.get(0);
				if (references.size() > 1)
				{
					engine.getLogger().logMultiplicityWarning(reference, references.size());
				}
				return new SingleGReferenceValueReferenceWrapper(engine, references, fRef, owners, definition);
			}
			else
			{
				return new SingleGReferenceValueReferenceWrapper(engine, references, null, owners, definition);
			}
		}
	}
}
