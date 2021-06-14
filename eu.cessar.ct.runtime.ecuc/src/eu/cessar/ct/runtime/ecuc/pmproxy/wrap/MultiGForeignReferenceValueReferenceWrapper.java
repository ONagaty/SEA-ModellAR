/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 5:51:55 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 */
public class MultiGForeignReferenceValueReferenceWrapper extends AbstractMultiMasterFeatureWrapper<EObject> implements
	IMasterReferenceWrapper
{

	private final EList<GReferenceValue> multiFRefs;
	private final GForeignReferenceDef definition;

	public MultiGForeignReferenceValueReferenceWrapper(IEMFProxyEngine engine, EList<GReferenceValue> multiFRefs,
		GForeignReferenceDef definition)
	{
		super(engine);
		this.multiFRefs = multiFRefs;
		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<EObject> getFeatureClass()
	{
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#getWrappedList()
	 */
	@Override
	protected List<?> getWrappedList()
	{
		return multiFRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		// TODO Auto-generated method stub
		GReferenceValue fRef = multiFRefs.get(index);
		return fRef.gGetValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@Override
	public void clear()
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.clearValues(multiFRefs, definition);
			}
		});
		super.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.remove(index, multiFRefs, definition);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		final Object[] result = {null};
		updateModel(new Runnable()
		{

			public void run()
			{
				if (multiFRefs instanceof DelegatingWithSourceMultiEList)
				{
					Object t = get(sourceIndex);
					remove(sourceIndex);
					add(targetIndex, t);
					result[0] = t;
				}
				else
				{
					result[0] = multiFRefs.move(targetIndex, sourceIndex);
				}
			}
		});
		return result[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, Object newValue)
	{
		if (!(newValue instanceof GIdentifiable))
		{
			throw new PMRuntimeException("Internal error, expected GIdentifiable, got " + newValue); //$NON-NLS-1$
		}
		final GIdentifiable ident = (GIdentifiable) newValue;
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.set(index, ident, multiFRefs, definition, null);
			}
		});
		getEngine().updateSlaveFeature(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, Object newValue)
	{
		if (!(newValue instanceof GIdentifiable))
		{
			throw new PMRuntimeException("Internal error, expected GIdentifiable, got " + newValue); //$NON-NLS-1$
		}
		final GIdentifiable ident = (GIdentifiable) newValue;
		updateModel(new Runnable()
		{

			public void run()
			{
				GReferenceValue value = (GReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(definition).createReferenceValue(
					definition);
				value.gSetValue(ident);
				SplitPMUtils.add(index, value, multiFRefs, definition);
			}
		});
		getEngine().updateSlaveFeature(this);
	}
}
