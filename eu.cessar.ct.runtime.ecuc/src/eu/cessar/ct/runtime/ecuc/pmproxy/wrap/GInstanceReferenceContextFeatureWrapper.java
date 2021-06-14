/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 9:36:02 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 */
public class GInstanceReferenceContextFeatureWrapper extends
	AbstractMultiMasterFeatureWrapper<EObject> implements IMasterReferenceWrapper
{

	private final GInstanceReferenceValue iRef;

	/**
	 * @param engine
	 */
	public GInstanceReferenceContextFeatureWrapper(IEMFProxyEngine engine,
		GInstanceReferenceValue iRef)
	{
		super(engine);
		// TODO Auto-generated constructor stub
		this.iRef = iRef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<EObject> getFeatureClass()
	{
		return EObject.class;
	}

	/**
	 * @return
	 */
	@Override
	protected EList<GIdentifiable> getWrappedList()
	{
		if (iRef == null)
		{
			return new BasicEList.UnmodifiableEList<GIdentifiable>(0, new GIdentifiable[0]);
		}
		else
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(iRef);
			if (mmService != null)
			{
				return mmService.getEcucMMService().getInstanceRefContext(iRef);
			}
			else
			{
				return new BasicEList.UnmodifiableEList<GIdentifiable>(0, new GIdentifiable[0]);
			}

		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		return getWrappedList().get(index);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@Override
	public void clear()
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GIdentifiable> list = getWrappedList();
				if (!list.isEmpty())
				{
					list.clear();
				}
			}
		});
		super.clear();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
	 */
	public void remove(final int index)
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GIdentifiable> list = getWrappedList();
				list.remove(index);
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
	 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		final Object[] result = {null};
		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GIdentifiable> list = getWrappedList();
				result[0] = list.move(targetIndex, sourceIndex);
			}
		});
		return result[0];
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GIdentifiable> list = getWrappedList();
				list.set(index, (GIdentifiable) value);
			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
	 */
	public void add(final int index, final Object value)
	{
		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GIdentifiable> list = getWrappedList();
				list.add(index, (GIdentifiable) value);
			}
		});
	}

}