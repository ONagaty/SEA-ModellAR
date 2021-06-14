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

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class MultiGInstanceReferenceValueReferenceWrapper extends AbstractMultiMasterFeatureWrapper<EObject> implements
	IMasterReferenceWrapper
{

	private final EList<GInstanceReferenceValue> multiIRefs;
	private final GInstanceReferenceDef definition;

	public MultiGInstanceReferenceValueReferenceWrapper(IEMFProxyEngine engine,
		EList<GInstanceReferenceValue> multiIRefs, GInstanceReferenceDef definition)
	{
		super(engine);
		this.multiIRefs = multiIRefs;
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
		return multiIRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		// TODO Auto-generated method stub
		GInstanceReferenceValue iRef = multiIRefs.get(index);
		return getEngine().getSlaveObject(getContext(), iRef);
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
				SplitPMUtils.clearValues(multiIRefs, definition);
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
				SplitPMUtils.remove(index, multiIRefs, definition);
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
				if (multiIRefs instanceof DelegatingWithSourceMultiEList)
				{
					Object t = get(sourceIndex);
					remove(sourceIndex);
					add(targetIndex, t);
					result[0] = t;
				}
				else
				{
					result[0] = multiIRefs.move(targetIndex, sourceIndex);
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
	public void set(final int index, Object value)
	{
		if (!(value instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + value); //$NON-NLS-1$
		}
		EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) value;
		IMasterObjectWrapper<?> wrapper = ((EMFProxyObjectImpl) value).eGetMasterWrapper();
		List<?> childrenMasters = wrapper.getAllMasterObjects();
		if (childrenMasters.size() > 1)
		{
			throw new PMRuntimeException("Cannot change a split feature"); //$NON-NLS-1$
		}
		final GInstanceReferenceValue iRefValue = (GInstanceReferenceValue) childrenMasters.get(0);

		final GInstanceReferenceDef[] newTargetDef = new GInstanceReferenceDef[1];
		if (PMProxyUtils.haveRefinementSupport(engine))
		{
			IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
			GModuleDef targetModuleDef = ecucModel.getModuleDef(definition);
			if (targetModuleDef != ecucModel.getModuleDef(iRefValue.gGetDefinition()))
			{
				// the definition need to be changed
				newTargetDef[0] = (GInstanceReferenceDef) ecucModel.getRefinedReferenceDefFamily(targetModuleDef,
					iRefValue.gGetDefinition());
			}
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				if (newTargetDef[0] != null)
				{
					iRefValue.gSetDefinition(newTargetDef[0]);
				}
				SplitPMUtils.set(index, iRefValue, multiIRefs, definition, null);
			}
		});
		// lastAddedOrSet = iRefValue;
		// update both objects
		getEngine().updateSlaveFeature(this);
		getEngine().updateSlave(getContext(), childrenProxy, iRefValue);
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
		IMasterObjectWrapper<?> wrapper = ((EMFProxyObjectImpl) value).eGetMasterWrapper();
		List<?> childrenMasters = wrapper.getAllMasterObjects();
		if (childrenMasters.size() > 1)
		{
			throw new PMRuntimeException("Cannot change a split feature"); //$NON-NLS-1$
		}
		final GInstanceReferenceValue iRefValue = (GInstanceReferenceValue) childrenMasters.get(0);

		final GInstanceReferenceDef[] newTargetDef = new GInstanceReferenceDef[1];
		if (PMProxyUtils.haveRefinementSupport(engine))
		{
			IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
			GModuleDef targetModuleDef = ecucModel.getModuleDef(definition);
			if (targetModuleDef != ecucModel.getModuleDef(iRefValue.gGetDefinition()))
			{
				// the definition need to be changed
				newTargetDef[0] = (GInstanceReferenceDef) ecucModel.getRefinedReferenceDefFamily(targetModuleDef,
					iRefValue.gGetDefinition());
			}
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				if (newTargetDef[0] != null)
				{
					iRefValue.gSetDefinition(newTargetDef[0]);
				}
				SplitPMUtils.add(index, iRefValue, multiIRefs, definition);
			}
		});
		// lastAddedOrSet = iRefValue;
		// update both objects
		getEngine().updateSlaveFeature(this);
		getEngine().updateSlave(getContext(), childrenProxy, iRefValue);
	}
}
