/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 29, 2013 9:53:45 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GContainerDef;

/**
 * A list used by the Sea API, to operate on containers of a given definition.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:55 2015 %
 * 
 *         %version: 7 %
 */
public class SEAContainersEList extends AbstractSeaSingleEList<ISEAContainer, GContainer>
{
	private final GContainerDef definition;

	/**
	 * @param parent
	 * @param store
	 * @param list
	 * @param definition
	 */
	public SEAContainersEList(ISEAContainerParent parent, ISeaOptions store, ESplitableList<GContainer> list,
		GContainerDef definition)
	{
		super(list, parent, store);
		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		return getBackingStoreList().remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doClear()
	 */
	@Override
	protected void doClear()
	{
		getBackingStoreList().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(ISEAContainer e)
	{
		checkArgument(e);

		if (!isAccepted(e))
		{
			getErrorHandler().definitionNotAllowed(getParent(), Collections.singletonList(e));
			return false;
		}

		if (!EcucMetaModelUtils.isMulti(definition))
		{
			clear();
		}

		List<GContainer> containers = e.arGetContainers();
		if (containers.size() == 1)
		{
			return getBackingStoreList().add(containers.get(0));
		}

		return false;
	}

	private boolean isAccepted(ISEAContainer e)
	{
		return e.arGetDefinition() == definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public ISEAContainer get(int index)
	{
		GContainer container = getBackingStoreList().get(index);
		return getSEAModel().getContainer(container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size()
	{
		return getBackingStoreList().size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#getEditingDomain()
	 */
	@Override
	protected TransactionalEditingDomain getEditingDomain()
	{
		TransactionalEditingDomain domain = WorkspaceEditingDomainUtil.getEditingDomain(definition);
		return domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#getMMService()
	 */
	@Override
	protected IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(definition.eClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#add(java.lang.Object)
	 */
	@Override
	public boolean add(ISEAContainer e)
	{
		SeaUtils.checkSplitStatus(getParent());
		return super.add(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, ISEAContainer e)
	{
		SeaUtils.checkSplitStatus(getParent());
		super.add(index, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends ISEAContainer> c)
	{
		SeaUtils.checkSplitStatus(getParent());
		return super.addAll(c);
	}

	@Override
	public ISEAContainer doMove(int newPosition, int oldPosition)
	{
		throw new UnsupportedOperationException("The operation is currently not supported!"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#set(int, java.lang.Object)
	 */
	@Override
	public ISEAContainer set(int index, ISEAContainer e)
	{
		SeaUtils.checkSplitStatus(getParent());
		return super.set(index, e);
	}

}
