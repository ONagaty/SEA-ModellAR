/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 26, 2015 3:04:00 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.NamedParamConfContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.NamedParamConfContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:55 2015 %
 * 
 *         %version: 2 %
 */
public class SEAContainersMultiEList extends AbstractSeaMultiEList<ISEAContainer, GContainer>
{
	private GParamConfContainerDef definition;
	private List<EList<? super GContainer>> parentLists;

	/**
	 * @param childContainerDef
	 * @param parent
	 * @param parentLists
	 * @param optionsHolder
	 */
	public SEAContainersMultiEList(GParamConfContainerDef childContainerDef, ISEAContainerParent parent,
		List<EList<? super GContainer>> parentLists, ISeaOptions optionsHolder)
	{
		super(parent, optionsHolder);
		definition = childContainerDef;
		this.parentLists = parentLists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaMultiEList#getList()
	 */
	@Override
	protected List<ESplitableList<GContainer>> getList()
	{
		List<ESplitableList<GContainer>> list = new ArrayList<ESplitableList<GContainer>>();

		List<SplitedEntry<GContainer>> splitedContainers = null;
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getSEAModel().getProject());

		IModelChangeStampProvider changeStampProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(getSEAModel().getProject());

		ISEAContainerParent parent = getParent();
		if (getParent() instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configurations = ((ISEAConfig) parent).arGetConfigurations();
			splitedContainers = ecucModel.getSplitedContainersForModule(configurations, definition);
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) parent).arGetContainers();
			splitedContainers = ecucModel.getSplitedContainersForContainer(containers, definition);
		}

		for (SplitedEntry<GContainer> splitedEntry: splitedContainers)
		{
			ESplitableList<GContainer> splitableList = splitedEntry.getSplitableList();
			String name = splitedEntry.getName();

			if (splitableList.size() > 1)
			{
				NamedParamConfContainerMultiEList l = new NamedParamConfContainerMultiEList(definition, name,
					parentLists, changeStampProvider);
				list.add(l);
			}
			else
			{

				GContainer container = splitableList.get(0);
				EList<GContainer> containers = null;

				EObject eContainer = container.eContainer();
				if (eContainer instanceof GContainer)
				{
					containers = ((GContainer) eContainer).gGetSubContainers();
				}
				else if (eContainer instanceof GModuleConfiguration)
				{
					containers = ((GModuleConfiguration) eContainer).gGetContainers();
				}

				NamedParamConfContainerSubEList l = new NamedParamConfContainerSubEList(definition, name, containers,
					container.eResource(), changeStampProvider);
				list.add(l);
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaMultiEList#get(int)
	 */
	@Override
	public ISEAContainer get(int arg0)
	{
		ESplitableList<GContainer> eSplitableList = getList().get(arg0);
		GContainer container = eSplitableList.get(0);

		return getSEAModel().getContainer(container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doRemove(int)
	 */
	@Override
	protected Object doRemove(int index)
	{
		ISEAContainer iseaContainer = get(index);

		ESplitableList<GContainer> splitableList = getList().get(index);
		splitableList.clear();

		return iseaContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doClear()
	 */
	@Override
	protected void doClear()
	{
		List<ESplitableList<GContainer>> list = getList();
		for (ESplitableList<GContainer> splitableList: list)
		{
			splitableList.clear();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doAdd(java.lang.Object)
	 */
	@Override
	protected boolean doAdd(ISEAContainer e)
	{
		EList<? super GContainer> activeParentList = null;

		Resource activeResource = getActiveResource();
		if (activeResource != null)
		{
			activeParentList = getParentList(activeResource);
		}
		else
		{
			// no active resource specified
			if (getOptionsHolder().reuseFragment())
			{
				activeParentList = getParentListToBeUsed();
				if (activeParentList != null)
				{
					activeResource = getResource(activeParentList);
				}
			}
		}

		if (activeResource == null || activeParentList == null)
		{
			handleNoActiveResource();
		}

		if (!EcucMetaModelUtils.isMulti(definition))
		{
			clear();
		}

		IModelChangeStampProvider changeStampProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(getSEAModel().getProject());
		ParamConfContainerSubEList subEList = new ParamConfContainerSubEList(e.arGetDefinition(), activeParentList,
			activeResource, changeStampProvider);

		GContainer container = e.arGetContainers().get(0);
		boolean res = subEList.add(container);

		return res;
	}

	/**
	 * @param activeParentList
	 * @return
	 */
	private EList<? super GContainer> getParentListToBeUsed()
	{
		EList<? super GContainer> activeParentList = null;
		// try reusing the fragments that already holds containers of the same definition if any
		List<EList<? super GContainer>> parentListsWithChildrenOfDefinition = new ArrayList<EList<? super GContainer>>();

		for (EList<? super GContainer> parentList: parentLists)
		{
			boolean hasChildrenOfDefinition = false;
			for (Object object: parentList)
			{
				if (object instanceof ESplitableList<?>)
				{
					for (Object obj: (ESplitableList<?>) object)
					{
						if (obj instanceof GContainer)
						{
							if (((GContainer) obj).gGetDefinition().gGetShortName().equals(definition.gGetShortName()))
							{
								parentListsWithChildrenOfDefinition.add(parentList);
								hasChildrenOfDefinition = true;

								break;
							}
						}
					}

					if (hasChildrenOfDefinition)
					{
						break;
					}
				}
			}
		}

		if (parentListsWithChildrenOfDefinition.size() == 1)
		{
			activeParentList = parentListsWithChildrenOfDefinition.get(0);
		}

		return activeParentList;
	}

	/**
	 * @param activeResource
	 * @return
	 */
	private EList<? super GContainer> getParentList(Resource activeResource)
	{
		ISEAContainerParent parent = getParent();
		if (parent instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configurations = ((ISEAConfig) parent).arGetConfigurations();
			for (GModuleConfiguration configuration: configurations)
			{
				if (configuration.eResource() == activeResource)
				{
					return configuration.gGetContainers();
				}
			}
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) parent).arGetContainers();
			for (GContainer container: containers)
			{
				if (container.eResource() == activeResource)
				{
					return container.gGetSubContainers();
				}
			}
		}

		return null;
	}

	private Resource getResource(EList<? super GContainer> parentList)
	{
		ISEAContainerParent parent = getParent();
		if (parent instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configurations = ((ISEAConfig) parent).arGetConfigurations();
			for (GModuleConfiguration configuration: configurations)
			{
				if (configuration.gGetContainers() == parentList)
				{
					return configuration.eResource();
				}
			}
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) parent).arGetContainers();
			for (GContainer container: containers)
			{
				if (container.gGetSubContainers() == parentList)
				{
					return container.eResource();
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#checkSplitAndNoActiveResource()
	 */
	@Override
	protected void checkSplitAndNoActiveResource()
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#updateSettingsForSplitList()
	 */
	@Override
	protected void updateSettingsForSplitList()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#resetActiveResourceForSplitList()
	 */
	@Override
	protected void resetActiveResourceForSplitList()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.util.AbstractSeaEList#doMove(int, int)
	 */
	@Override
	protected ISEAContainer doMove(int newPosition, int oldPosition)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
