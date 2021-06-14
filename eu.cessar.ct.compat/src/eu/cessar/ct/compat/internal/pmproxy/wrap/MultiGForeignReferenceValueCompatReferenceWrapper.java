/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GForeignReferenceDef;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 12, 2012
 * @restored version without splitable support (uidu2337)
 */
public class MultiGForeignReferenceValueCompatReferenceWrapper extends AbstractMultiMasterFeatureWrapper<EObject>
	implements IMasterReferenceWrapper
{
	private final EList<GReferenceValue> multiFRefs;
	private final GForeignReferenceDef definition;

	/**
	 * @param engine
	 * @param references
	 * @param def
	 */
	public MultiGForeignReferenceValueCompatReferenceWrapper(IEMFProxyEngine engine, EList<GReferenceValue> multiFRefs,
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
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		GReferenceValue gReferenceValue = multiFRefs.get(index);
		return getEngine().getSlaveObject(getContext(), gReferenceValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#clear()
	 */
	@Override
	public void clear()
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				multiFRefs.clear();
			}
		});
		super.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, Object value)
	{
		if (!(value instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + value); //$NON-NLS-1$
		}
		EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) value;
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
				refValue.gSetDefinition(definition);
				multiFRefs.set(index, refValue);
			}
		});
		// lastAddedOrSet = refValue;
		// update both objects
		getEngine().updateSlaveFeature(this);
		// getEngine().updateSlave(getContext(), childrenProxy, refValue);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, Object value)
	{
		if (!(value instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + value); //$NON-NLS-1$
		}
		EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) value;
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
				refValue.gSetDefinition(definition);
				multiFRefs.add(index, refValue);
			}
		});
		// lastAddedOrSet = refValue;
		// update both objects
		getEngine().updateSlaveFeature(this);
		// getEngine().updateSlave(getContext(), childrenProxy, refValue);

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
				multiFRefs.remove(index);
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
				result[0] = multiFRefs.move(targetIndex, sourceIndex);
			}
		});
		return result[0];
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

}
