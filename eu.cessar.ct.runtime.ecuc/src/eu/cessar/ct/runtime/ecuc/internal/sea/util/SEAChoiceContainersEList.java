/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 26.07.2013 13:52:12
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEAWriteOperationException;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;

/**
 * A list used by the Sea API, to operate on choice containers of a given definition.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:54 2015 %
 * 
 *         %version: 7 %
 */
public class SEAChoiceContainersEList extends AbstractSeaSingleEList<ISEAContainer, GContainer>
{
	private final GChoiceContainerDef definition;

	/**
	 * @param parent
	 * @param optionsHolder
	 * @param definition
	 */
	public SEAChoiceContainersEList(ISEAContainerParent parent, ISeaOptions optionsHolder,
		GChoiceContainerDef definition)
	{
		super(null, parent, optionsHolder);

		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#checkSplitStatus()
	 */
	@Override
	protected void checkSplitAndNoActiveResource()
	{
		if (getMasterList().size() > 1)
		{
			throw new SEAWriteOperationException(Messages.NoActiveConfiguration);
		}
	}

	private List<ESplitableList<GContainer>> getElements()
	{
		IEcucModel ecucModel = getECUCModel();

		List<SplitedEntry<GContainer>> children = null;
		if (getParent() instanceof ISEAConfig)
		{
			children = ecucModel.getSplitedContainersForModule(getMasterList((ISEAConfig) getParent()), definition);
		}
		else
		{
			children = ecucModel.getSplitedContainersForContainer(getMasterList((ISEAContainer) getParent()),
				definition);
		}

		List<ESplitableList<GContainer>> containers = new ArrayList<ESplitableList<GContainer>>();
		for (SplitedEntry<GContainer> splitedEntry: children)
		{
			containers.add(splitedEntry.getSplitableList());
		}

		return containers;
	}

	private List<EObject> getMasterList()
	{
		List<EObject> masterList = new ArrayList<EObject>();

		ISEAContainerParent containerParent = getParent();
		if (containerParent instanceof ISEAConfig)
		{
			masterList.addAll(getMasterList((ISEAConfig) containerParent));
		}
		else
		{
			masterList.addAll(getMasterList((ISEAContainer) containerParent));
		}

		return masterList;
	}

	private List<GModuleConfiguration> getMasterList(ISEAConfig seaConfig)
	{
		List<GModuleConfiguration> masterList = new ArrayList<GModuleConfiguration>();

		List<GModuleConfiguration> arConfigurations = seaConfig.arGetConfigurations();

		String qualifiedName = ModelUtils.getAbsoluteQualifiedName(arConfigurations.get(0));
		masterList.addAll(getECUCModel().getModuleCfg(qualifiedName));

		return masterList;
	}

	private List<GContainer> getMasterList(ISEAContainer seaContainer)
	{
		List<GContainer> masterList = new ArrayList<GContainer>();

		ESplitableList<GContainer> splitedSiblingContainers = getECUCModel().getSplitedSiblingContainers(
			seaContainer.arGetContainers().get(0));
		masterList.addAll(splitedSiblingContainers);

		return masterList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doClear()
	 */
	@Override
	protected void doClear()
	{
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(ISEAContainer e)
	{
		throw new UnsupportedOperationException();
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
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public ISEAContainer get(int index)
	{
		List<ESplitableList<GContainer>> elements = getElements();
		if (index > elements.size())
		{
			throw new IndexOutOfBoundsException(
				NLS.bind("Index: {0}, size: {1}", new Object[] {index, elements.size()})); //$NON-NLS-1$
		}

		ESplitableList<GContainer> splitableList = elements.get(index);
		return getSEAModel().getContainer(splitableList.get(0));

	}

	private IEcucModel getECUCModel()
	{
		return IEcucCore.INSTANCE.getEcucModel(definition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size()
	{
		return getElements().size();
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
