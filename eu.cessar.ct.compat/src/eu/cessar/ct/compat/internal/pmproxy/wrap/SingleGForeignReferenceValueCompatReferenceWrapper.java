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
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractSingleMasterReferenceWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GForeignReferenceDef;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 12, 2012
 * @restored the version without splitable support (uidu2337)
 */
public class SingleGForeignReferenceValueCompatReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private GReferenceValue fRef;
	private final GForeignReferenceDef definition;
	private final List<GContainer> owners;

	public SingleGForeignReferenceValueCompatReferenceWrapper(IEMFProxyEngine engine, GReferenceValue fRef,
		List<GContainer> owners, GForeignReferenceDef definition)
	{
		super(engine);
		this.fRef = fRef;
		this.owners = owners;
		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (!isSetValue())
		{
			return null;
		}
		return getEngine().getSlaveObject(getContext(), fRef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		if (newValue == null || newValue == EStructuralFeature.Internal.DynamicValueHolder.NIL)
		{
			unsetValue();
		}
		else
		{
			if (!(newValue instanceof EMFProxyObjectImpl))
			{
				throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
			}
			EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) newValue;
			IMasterObjectWrapper<?> wrapper = childrenProxy.eGetMasterWrapper();
			List<?> childrenMasters = wrapper.getAllMasterObjects();
			if (childrenMasters.size() > 1)
			{
				throw new PMRuntimeException("Cannot change a split feature"); //$NON-NLS-1$
			}
			final GReferenceValue refValue = (GReferenceValue) childrenMasters.get(0);

			updateModel(new Runnable()
			{

				public void run()
				{
					GContainer owner = owners.get(0);
					refValue.gSetDefinition(definition);
					fRef = refValue;
					owner.gGetReferenceValues().add(fRef);

				}
			});
			// lastAddedOrSet = iRefValue;
			// update both objects
			getEngine().updateSlaveFeature(this);
			// getEngine().updateSlave(getContext(), childrenProxy, refValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return fRef != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		if (fRef != null)
		{
			final GContainer cnt = (GContainer) fRef.eContainer();
			if (cnt == null)
			{
				// nothing to do, should never happen
				fRef = null;
				return;
			}
			updateModel(new Runnable()
			{
				public void run()
				{
					cnt.gGetReferenceValues().remove(fRef);
					fRef = null;
				}
			});
		}
		getEngine().updateSlaveFeature(this);
	}
}
