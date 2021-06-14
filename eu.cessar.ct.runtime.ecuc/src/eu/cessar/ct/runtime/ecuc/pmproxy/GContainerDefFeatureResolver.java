/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 15, 2010 11:15:52 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GModuleConfigurationObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGContainerReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGContainerReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.util.ChoiceContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ChoiceContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 *
 */
public class GContainerDefFeatureResolver extends AbstractEReferenceFeatureResolver
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EReference)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{

		String uri = engine.getProxyElementAnnotation(reference.getEReferenceType(), ATTR_URI);
		InternalProxyConfigurationError.assertTrue(uri != null);

		IEcucModel model = getEcucModel(engine);
		IModelChangeStampProvider changeProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(engine.getProject());
		List<EObject> defs = EObjectLookupUtils.getEObjectsWithQName(getEcucModel(engine).getResourcesWithModuleDefs(),
			uri);
		InternalProxyConfigurationError.assertTrue(defs.size() == 1);
		// should not be null
		InternalProxyConfigurationError.assertTrue(defs.get(0) instanceof GContainerDef);
		// proxyObject is mapped to GModuleConfiguration or GContainer
		GContainerDef def = (GContainerDef) defs.get(0);
		boolean isChoice = def instanceof GChoiceContainerDef;
		GModuleDef requiredModuleDef = model.getModuleDef(def);
		IMasterObjectWrapper<?> masterWrapper = proxyObject.eGetMasterWrapper();

		// Map<String, ESplitableList<GContainer>> children;
		List<SplitedEntry<GContainer>> children;

		EList<GContainer> singularParentList = null;
		GContainerDef realDef = def;
		// keep the parents list
		List<GIdentifiable> parentEList = new ArrayList<GIdentifiable>();
		if (masterWrapper instanceof GModuleConfigurationObjectWrapper)
		{
			GModuleConfigurationObjectWrapper wrapper = (GModuleConfigurationObjectWrapper) masterWrapper;
			List<GModuleConfiguration> masterList = wrapper.getMasterObject();
			// instantiate the list in case the children list is empty
			parentEList.addAll(masterList);

			if (PMProxyUtils.haveRefinementSupport(engine) && requiredModuleDef != masterList.get(0).gGetDefinition())
			{
				// refined implementation
				realDef = model.getRefinedContainerDefFamily(masterList.get(0).gGetDefinition(), def);
			}
			children = model.getSplitedContainersForModule(masterList, realDef);
			if (masterList.size() == 1)
			{
				singularParentList = masterList.get(0).gGetContainers();
			}
			if (!reference.isMany() && children.size() == 0)
			{
				// children = new HashMap<String, ESplitableList<GContainer>>();
				children = new ArrayList<SplitedEntry<GContainer>>();
				ESplitableList<GContainer> containers;
				if (isChoice)
				{
					if (masterList.size() > 1)
					{
						ChoiceContainerMultiEList result = new ChoiceContainerMultiEList((GChoiceContainerDef) realDef,
							changeProvider);
						for (GModuleConfiguration cfg: masterList)
						{
							result.addParentList(cfg.gGetContainers(), cfg.eResource());
						}
						containers = result;
					}
					else
					{
						containers = new ChoiceContainerSubEList((GChoiceContainerDef) realDef,
							masterList.get(0).gGetContainers(), masterList.get(0).eResource(), changeProvider);
					}
				}
				else
				{
					if (masterList.size() > 1)
					{
						ParamConfContainerMultiEList result = new ParamConfContainerMultiEList(
							(GParamConfContainerDef) realDef, changeProvider);
						for (GModuleConfiguration cfg: masterList)
						{
							result.addParentList(cfg.gGetContainers(), cfg.eResource());
						}
						containers = result;
					}
					else
					{
						containers = new ParamConfContainerSubEList((GParamConfContainerDef) realDef,
							masterList.get(0).gGetContainers(), masterList.get(0).eResource(), changeProvider);
					}
				}
				SplitedEntry<GContainer> namedContainersList = new SplitedEntry<GContainer>();
				namedContainersList.setName(""); //$NON-NLS-1$
				namedContainersList.setQualifiedName(""); //$NON-NLS-1$
				namedContainersList.setSplitableList(containers);
				children.add(namedContainersList);
				//				children.put("", containers); //$NON-NLS-1$
			}
		}
		else
		{
			// (masterWrapper instanceof GContainerObjectWrapper)
			GContainerObjectWrapper wrapper = (GContainerObjectWrapper) masterWrapper;
			List<GContainer> masterList = wrapper.getMasterObject();
			GModuleDef currentModuleDef = model.getModuleDef(masterList.get(0).gGetDefinition());
			if (PMProxyUtils.haveRefinementSupport(engine) && requiredModuleDef != currentModuleDef)
			{
				realDef = model.getRefinedContainerDefFamily(currentModuleDef, def);
			}
			children = model.getSplitedContainersForContainer(masterList, realDef);
			// instantiate the parent list in case the children list is empty
			parentEList.addAll(masterList);
			if (masterList.size() == 1)
			{
				singularParentList = masterList.get(0).gGetSubContainers();
			}
			if (!reference.isMany() && children.size() == 0)
			{
				children = new ArrayList<SplitedEntry<GContainer>>();
				ESplitableList<GContainer> containers;
				if (isChoice)
				{
					if (masterList.size() > 1)
					{
						ChoiceContainerMultiEList result = new ChoiceContainerMultiEList((GChoiceContainerDef) realDef,
							changeProvider);
						for (GContainer cfg: masterList)
						{
							result.addParentList(cfg.gGetSubContainers(), cfg.eResource());
						}
						containers = result;
					}
					else
					{
						containers = new ChoiceContainerSubEList((GChoiceContainerDef) realDef,
							masterList.get(0).gGetSubContainers(), masterList.get(0).eResource(), changeProvider);
					}
				}
				else
				{
					if (masterList.size() > 1)
					{
						ParamConfContainerMultiEList result = new ParamConfContainerMultiEList(
							(GParamConfContainerDef) realDef, changeProvider);
						for (GContainer cfg: masterList)
						{
							result.addParentList(cfg.gGetSubContainers(), cfg.eResource());
						}
						containers = result;
					}
					else
					{
						containers = new ParamConfContainerSubEList((GParamConfContainerDef) realDef,
							masterList.get(0).gGetSubContainers(), masterList.get(0).eResource(), changeProvider);
					}
				}
				SplitedEntry<GContainer> namedContainersList = new SplitedEntry<GContainer>();
				namedContainersList.setName(""); //$NON-NLS-1$
				namedContainersList.setQualifiedName(""); //$NON-NLS-1$
				namedContainersList.setSplitableList(containers);
				children.add(namedContainersList);
				//				children.put("", containers); //$NON-NLS-1$
			}
		}

		if (reference.isMany())
		{

			List<ESplitableList<GContainer>> containers = new ArrayList<ESplitableList<GContainer>>();
			List<EList<? super GContainer>> list = new ArrayList<EList<? super GContainer>>();

			for (SplitedEntry<GContainer> splitedEntry: children)
			{
				ESplitableList<GContainer> splitableList = splitedEntry.getSplitableList();
				containers.add(splitedEntry.getSplitableList());

				if (splitableList instanceof DelegatingWithSourceMultiEList)
				{
					DelegatingWithSourceMultiEList delegatingSplitableList = (DelegatingWithSourceMultiEList) splitableList;
					delegatingSplitableList.setReadOnly(false);
					List sources = delegatingSplitableList.getSources();
					if (!sources.isEmpty())
					{
						delegatingSplitableList.setActiveSource(sources.get(0));
					}
				}
				list.add(splitableList);
			}
			if (children.isEmpty())
			{
				// in case the children is empty, the parentEList is needed
				return new MultiGContainerReferenceWrapper(engine, realDef, containers, singularParentList, list,
					parentEList);
			}

			else
			{
				return new MultiGContainerReferenceWrapper(engine, realDef, containers, singularParentList, list);
			}
		}
		else
		{
			// multiplicity single

			Assert.isTrue(children.size() > 0);
			ESplitableList<GContainer> containers;
			// get the first one, it will allways be
			// containers = children.values().iterator().next();
			containers = children.get(0).getSplitableList();
			if (children.size() > 1)
			{
				engine.getLogger().logMultiplicityWarning(reference, children.size());
			}

			// for splitable-case create the multi parent list that will have only one list

			List<EList<? super GContainer>> list = new ArrayList<EList<? super GContainer>>();

			if (containers instanceof DelegatingWithSourceMultiEList)
			{
				DelegatingWithSourceMultiEList delegatingSplitableList = (DelegatingWithSourceMultiEList) containers;
				delegatingSplitableList.setReadOnly(false);
				List sources = delegatingSplitableList.getSources();
				if (!sources.isEmpty())
				{
					delegatingSplitableList.setActiveSource(sources.get(0));
				}
			}
			list.add(containers);
			SplitedEntry<GContainer> splitedEntry = children.get(0);
			boolean single = children.size() == 1 && splitedEntry.getSplitableList().size() == 0;

			if (children.isEmpty() || single)
			{ // in case the children is empty, the parentEList is needed
				return new SingleGContainerReferenceWrapper(engine, containers, realDef, singularParentList, list,
					parentEList);
			}
			else
			{
				return new SingleGContainerReferenceWrapper(engine, containers, realDef, singularParentList, list);
			}
		}
	}
}
