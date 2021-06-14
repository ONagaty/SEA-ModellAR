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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.PlainMasterObjectWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GForeignReferenceCompatObjectWrapper extends PlainMasterObjectWrapper<GReferenceValue>
{

	/**
	 * @param data
	 */
	public GForeignReferenceCompatObjectWrapper(IEMFProxyEngine engine, GReferenceValue data)
	{
		super(engine, GReferenceValue.class, data);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		GReferenceValue value = getMasterObject();
		if (value == null)
		{
			return null;
		}
		else
		{
			return value.eContainer();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		if (getMasterObject() == null)
		{
			// nothing to do
			return;
		}
		// IEcucModel ecucModel =
		// IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
		// first entry is existing parent, second is new parent list
		@SuppressWarnings("unchecked")
		final EList<GConfigReferenceValue>[] removeFromList = new EList[1];
		@SuppressWarnings("unchecked")
		final EList<GConfigReferenceValue>[] addToList = new EList[1];

		if (parentWrapper == null)
		{
			addToList[0] = null;
		}
		else
		{
			List<?> masterObjects = parentWrapper.getAllMasterObjects();
			if (masterObjects.size() > 1)
			{
				throw new PMRuntimeException("Cannot alter a splited container"); //$NON-NLS-1$
			}
			GContainer parent = (GContainer) masterObjects.get(0);
			addToList[0] = parent.gGetReferenceValues();

		}

		// set the remove from list
		if (getMasterObject().eContainer() != null)
		{
			removeFromList[0] = ((GContainer) getMasterObject().eContainer()).gGetReferenceValues();
		}
		if (addToList[0] == removeFromList[0])
		{
			// nothing to do, both null or represent the same list
			return;
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				if (removeFromList[0] != null)
				{
					// remove children from current parent
					removeFromList[0].remove(getMasterObject());
				}
				if (addToList[0] != null)
				{
					addToList[0].add(getMasterObject());

				}
			}

		});
		// we have to re-run the query
		getEngine().updateSlave(getContext(), getProxyObject(), getMasterObject());
		if (parentWrapper != null && parentFeature != null)
		{
			IMasterFeatureWrapper<?> parentFeatureWrapper = parentWrapper.getProxyObject().eGetCachedFeatureWrapper(
				parentFeature);
			if (parentFeatureWrapper != null)
			{
				getEngine().updateSlaveFeature(parentFeatureWrapper);
			}
		}

	}

}
