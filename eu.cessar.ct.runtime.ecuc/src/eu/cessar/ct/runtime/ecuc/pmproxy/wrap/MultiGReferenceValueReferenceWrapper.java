/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 5:51:55 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.GContainerDefClassResolver;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 *
 */
public class MultiGReferenceValueReferenceWrapper extends AbstractMultiMasterFeatureWrapper<EObject> implements
IMasterReferenceWrapper
{

	private final EList<GReferenceValue> multiFRefs;
	private final GConfigReference definition;
	private final IEcucModel ecucModel;

	/**
	 * @param engine
	 * @param multiFRefs
	 * @param definition
	 */
	public MultiGReferenceValueReferenceWrapper(IEMFProxyEngine engine, EList<GReferenceValue> multiFRefs,
		GConfigReference definition)
	{
		super(engine);
		this.multiFRefs = multiFRefs;
		this.definition = definition;
		ecucModel = IEcucCore.INSTANCE.getEcucModel(engine.getProject());
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
		GReferenceValue fRef = multiFRefs.get(index);
		GReferrable value = fRef.gGetValue();
		if (value != null)
		{
			Map<String, Object> parameters = null;
			Object context = null;
			if (!(value instanceof GContainer) || value.eIsProxy())
			{
				parameters = new HashMap<String, Object>();
				parameters.put(GContainerDefClassResolver.PARAM_MASTER_CLASS, GContainer.class);
				parameters.put(GContainerDefClassResolver.PARAM_PM_FEATURE, getWrappedFeature());
				parameters.put(GContainerDefClassResolver.PARAM_PM_OWNER, getProxyObject());
			}
			else
			{
				// the context is the module def where the container is defined
				GContainerDef def = ecucModel.getRefinedContainerDef(definition, (GContainer) value);
				if (def != null)
				{
					context = ecucModel.getModuleDef(def);
				}
			}
			return getEngine().getSlaveObject(context, value, parameters);

		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object slaveValue)
	{
		if (slaveValue instanceof EMFProxyObjectImpl)
		{
			EMFProxyObjectImpl slaveProxy = (EMFProxyObjectImpl) slaveValue;
			List<?> allMasterObjects = slaveProxy.eGetMasterWrapper().getAllMasterObjects();
			int refSize = multiFRefs.size();
			int masterSize = allMasterObjects.size();
			for (int i = 0; i < refSize; i++)
			{
				GReferrable gReferenceValue = multiFRefs.get(i).gGetValue();
				for (int j = 0; j < masterSize; j++)
				{
					if (gReferenceValue == allMasterObjects.get(j))
					{
						return i;
					}
				}
			}
		}
		return -1;
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
		if (!(newValue instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
		}
		List<?> masterObjects = ((EMFProxyObjectImpl) newValue).eGetMasterWrapper().getAllMasterObjects();
		final GContainer container = (GContainer) masterObjects.get(0);
		updateModel(new Runnable()
		{

			public void run()
			{
				SplitPMUtils.set(index, container, multiFRefs, definition, null);
				// multiFRefs.get(index).gSetValue(container);
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
		if (!(newValue instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
		}
		List<?> masterObjects = ((EMFProxyObjectImpl) newValue).eGetMasterWrapper().getAllMasterObjects();
		final GContainer container = (GContainer) masterObjects.get(0);
		updateModel(new Runnable()
		{

			public void run()
			{
				GReferenceValue value = (GReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(
					getEngine().getProject()).createReferenceValue(definition);
				value.gSetValue(container);
				SplitPMUtils.add(index, value, multiFRefs, definition);
			}
		});
		getEngine().updateSlaveFeature(this);
	}

}
