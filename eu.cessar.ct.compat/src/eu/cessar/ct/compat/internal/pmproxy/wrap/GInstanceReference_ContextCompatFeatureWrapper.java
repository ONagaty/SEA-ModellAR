/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.wrap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterAttributeWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 12, 2012
 */
public class GInstanceReference_ContextCompatFeatureWrapper extends
	AbstractMultiMasterFeatureWrapper<String> implements IMasterAttributeWrapper<String>
{
	private final GInstanceReferenceValue iRef;

	/**
	 * @param engine
	 */
	public GInstanceReference_ContextCompatFeatureWrapper(IEMFProxyEngine engine,
		GInstanceReferenceValue iRef)
	{
		super(engine);
		this.iRef = iRef;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Class getFeatureClass()
	{
		return String.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public String get(int index)
	{
		GIdentifiable context = getWrappedList().get(index);
		return MetaModelUtils.getAbsoluteQualifiedName(context);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#clear()
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
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
	 */
	public void set(final int index, final Object value)
	{
		updateModel(new Runnable()
		{
			public void run()
			{
				GIdentifiable target = null;
				if (value instanceof String)
				{
					target = (GIdentifiable) EObjectLookupUtils.getEObjectsWithQName(
						engine.getProject(), (String) value).get(0);
				}
				EList<GIdentifiable> list = getWrappedList();
				list.set(index, target);
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
				GIdentifiable target = null;
				if (value instanceof String)
				{
					target = (GIdentifiable) EObjectLookupUtils.getEObjectsWithQName(
						engine.getProject(), (String) value).get(0);
				}
				EList<GIdentifiable> list = getWrappedList();
				list.add(index, target);
			}
		});

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
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#getWrappedList()
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
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(getEngine().getProject());
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

}
