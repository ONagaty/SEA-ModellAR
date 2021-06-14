/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.04.2013 11:53:14
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GinfrastructurePackage;

/**
 * Base implementation of a SEA object capable of holding ECUC container(s)
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Jun 19 15:09:33 2015 %
 * 
 *         %version: 7 %
 */
public abstract class AbstractSEAContainerParent extends AbstractSEAObject implements ISEAContainerParent
{
	/**
	 * @param seaModel
	 * @param optionsHolder
	 */
	public AbstractSEAContainerParent(ISEAModel seaModel, ISeaOptions optionsHolder)
	{
		super(seaModel, optionsHolder);
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#getContainer(java.lang.String)
 */
	@Override
	public ISEAContainer getContainer(String defName)
	{
		return getSeaContainersHandler().getContainer(this, defName);
	}

	private ISeaContainersHandler getSeaContainersHandler()
	{
		return SeaHandlersManager.getSeaContainersHandler(getSEAModel(), getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#getContainers(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAContainer> getContainers(String defName)
	{
		return getSeaContainersHandler().getContainers(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#searchForContainers(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAContainer> searchForContainers(String pathFragment)
	{
		return getSeaContainersHandler().searchForContainers(this, pathFragment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#getShortName()
	 */
	@Override
	public String getShortName()
	{
		return getWrappedElement().gGetShortName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSEAContainerParent#setShortName(java.lang.String)
	 */
	@Override
	public ISEAContainerParent setShortName(final String shortName)
	{
		getSeaContainersHandler().setShortName(this, shortName);
		// try
		// {
		// setShortName(shortName, !getSeaOptionsHolder().isReadOnly());
		// }
		// catch (ExecutionException e)
		// {
		// handleExecutionException(e);
		// }
		// finally
		// {
		// checkAutoSave();
		// }

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#unSetShortName()
	 */
	@Override
	public ISEAContainerParent unSetShortName()
	{
		return setShortName(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#isSetShortName()
	 */
	@Override
	public boolean isSetShortName()
	{
		return getWrappedElement().eIsSet(GinfrastructurePackage.eINSTANCE.getGReferrable_GShortName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#isSetContainer(java.lang.String)
	 */
	@Override
	public boolean isSetContainer(String defName)
	{
		return getSeaContainersHandler().isSetContainer(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#unSetContainer(java.lang.String)
	 */
	@Override
	public ISEAContainerParent unSetContainer(String defName)
	{
		getSeaContainersHandler().unSetContainer(this, defName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAContainer(java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(String defName)
	{
		return createContainer(defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAContainer(java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(String defName, String shortName)
	{
		return createContainer(defName, shortName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAContainer(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createContainer(String defName, String shortName, boolean deriveNameFromSuggestion)
	{
		return getSeaContainersHandler().createSEAContainer(this, defName, shortName, deriveNameFromSuggestion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAChoiceContainer(java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName)
	{
		return createChoiceContainer(defName, chosenDefName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAChoiceContainer(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName, String shortName)
	{
		return createChoiceContainer(defName, chosenDefName, shortName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#createSEAChoiceContainer(java.lang.String, java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName, String shortName,
		boolean deriveNameFromSuggestion)
	{
		return getSeaContainersHandler().createSEAChoiceContainer(this, defName, chosenDefName, shortName,
			deriveNameFromSuggestion);
	}

	/**
	 * Sorts the EObjects based on the resource URI
	 * 
	 * @param eObjects
	 */
	protected void sort(List<? extends EObject> eObjects)
	{
		Collections.sort(eObjects, new Comparator<EObject>()
		{
			@Override
			public int compare(EObject o1, EObject o2)
			{
				Resource resource1 = o1.eResource();
				Resource resource2 = o2.eResource();

				if (resource1 != null && resource2 != null)
				{
					URI uri1 = resource1.getURI();
					URI uri2 = resource2.getURI();

					return uri1.toString().compareTo(uri2.toString());
				}
				else if (resource1 == null && resource2 == null)
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
		});
	}

	/**
	 * @return the definition of this Ecuc element
	 */
	protected abstract GIdentifiable getDefinition();

	/**
	 * 
	 * @return the wrapped Ecuc element
	 */
	protected abstract GIdentifiable getWrappedElement();

}
