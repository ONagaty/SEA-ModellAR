/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 04.04.2013 16:39:01
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaInstanceReferencesHandler;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Implementation of the wrapper over an ECUC Containers, used by the SEA
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 24 15:24:56 2014 %
 * 
 *         %version: 11 %
 */
public class SEAContainerImpl extends AbstractSEAContainer
{
	private GContainer wrappedContainer;

	/**
	 * @param seaModel
	 * @param toWrap
	 * @param optionsHolder
	 */
	public SEAContainerImpl(ISEAModel seaModel, GContainer toWrap, ISeaOptions optionsHolder)
	{
		super(seaModel, optionsHolder);
		wrappedContainer = toWrap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		List<GContainer> containers = arGetContainers();
		if (containers.size() > 0)
		{
			return ModelUtils.getAbsoluteQualifiedName(containers.get(0));
		}

		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#arGetContainers()
	 */
	@Override
	public List<GContainer> arGetContainers()
	{
		if (wrappedContainer.eResource() == null)
		{
			return Collections.singletonList(wrappedContainer);
		}

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(wrappedContainer);
		ESplitableList<GContainer> containers = ecucModel.getSplitedSiblingContainers(wrappedContainer);

		List<GContainer> l = new ArrayList<GContainer>();
		l.addAll(containers);

		return Collections.unmodifiableList(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#arGetDefinition()
	 */
	@Override
	public GParamConfContainerDef arGetDefinition()
	{
		// safe to cast, as we create wrappers only for containers of GParamConfContainerDef definition
		return (GParamConfContainerDef) getWrappedElement().gGetDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSEAContainerParent#getDefinition()
	 */
	@Override
	protected GParamConfContainerDef getDefinition()
	{
		return arGetDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.impl.AbstractSEAContainerParent#getWrappedElement()
	 */
	@Override
	protected GContainer getWrappedElement()
	{
		return wrappedContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.impl.AbstractSEAContainerParent#getParent()
	 */
	@Override
	public ISEAContainerParent getParent()
	{
		List<GContainer> containers = arGetContainers();
		if (containers.size() > 0)
		{
			GContainer container = containers.get(0);
			EObject eContainer = container.eContainer();

			if (eContainer instanceof GModuleConfiguration)
			{
				return getSEAModel().getConfiguration((GModuleConfiguration) eContainer);
			}
			else if (eContainer instanceof GContainer)
			{
				EObject parent = eContainer;
				if (((GContainer) eContainer).gGetDefinition() instanceof GChoiceContainerDef)
				{
					parent = parent.eContainer();
				}

				if (parent instanceof GModuleConfiguration)
				{
					return getSEAModel().getConfiguration((GModuleConfiguration) parent);
				}
				else
				{
					return getSEAModel().getContainer((GContainer) parent);
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#pmGetContainer()
	 */
	@Override
	public IPMContainer pmGetContainer()
	{
		List<GContainer> containers = arGetContainers();
		IPMContainer pmContainer = PMUtils.getPMContainer(containers.get(0));

		return pmContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createSEAInstanceRef(java.lang.String)
	 */
	@Override
	public ISEAInstanceReference createInstanceReference(String defName)
	{
		return createInstanceReference(null, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainer#createInstanceReference(gautosar.gecucdescription.GContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAInstanceReference createInstanceReference(GContainer activeContainer, String defName)
	{
		SeaInstanceReferencesHandler handler = SeaHandlersManager.getSeaInstanceReferencesHandler(getSEAModel(),
			getSeaOptionsHolder());
		return handler.createSeaInstanceRef(activeContainer, this, defName);
	}
}
