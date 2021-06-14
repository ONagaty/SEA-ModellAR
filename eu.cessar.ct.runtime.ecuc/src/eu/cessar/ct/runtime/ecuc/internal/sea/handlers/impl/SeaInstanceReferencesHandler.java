/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * May 26, 2013 6:07:54 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAInstanceRefImpl;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.InstanceReferencesEList;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAInstanceReference;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Sea handler for manipulating instance reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 21 14:53:55 2014 %
 * 
 *         %version: 8 %
 */
public class SeaInstanceReferencesHandler extends AbstractSeaReferencesHandler<ISEAInstanceReference>
{

	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaInstanceReferencesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/**
	 * Create an instance reference value having the definition given by the <code>defName</code> short name into the
	 * given parent, considering the specified active configuration
	 * 
	 * @param activeContainer
	 * @param parent
	 * @param defName
	 * @return the created {@link ISEAInstanceReference} object, or <code>null</code> if the object could not be created
	 *         because the definition could not be found
	 */
	public ISEAInstanceReference createSeaInstanceRef(GContainer activeContainer, ISEAContainer parent, String defName)
	{
		checkArgument(defName);
		SeaUtils.checkSplitStatus(parent);

		GConfigReference configReference = getReferenceDefByName(parent, defName);
		if (configReference != null)
		{
			Assert.isTrue(configReference instanceof GInstanceReferenceDef);
			GInstanceReferenceValue value = (GInstanceReferenceValue) getMMService().getGenericFactory().createReferenceValue(
				configReference);

			ISEAInstanceReference seaInstanceRef = new SEAInstanceRefImpl(getSeaModel(), value, getSeaOptionsHolder());

			if (isMany(configReference))
			{
				// TODO: split case
				ISEAList<ISEAInstanceReference> references = getReferences(parent, configReference);
				references.add(seaInstanceRef);
			}
			else
			{
				setReference(activeContainer, parent, configReference, Collections.singletonList(seaInstanceRef));
			}

			return seaInstanceRef;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#getAllowedDefinitionTypes()
	 */
	@Override
	protected List<Class<? extends GCommonConfigurationAttributes>> getAllowedDefinitionTypes()
	{
		List<Class<? extends GCommonConfigurationAttributes>> l = new ArrayList<Class<? extends GCommonConfigurationAttributes>>();
		l.add(GInstanceReferenceDef.class);

		return l;
	}

	@Override
	public ISEAList<ISEAInstanceReference> getReferences(ISEAContainer parent, GConfigReference configReference)
	{
		ESplitableList<GInstanceReferenceValue> splitedReferences = getEcucModel().getSplitedReferences(
			parent.arGetContainers(), GInstanceReferenceValue.class, configReference);

		return new InstanceReferencesEList(parent, splitedReferences, configReference, getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#checkDestinationType(eu.cessar.ct.sdk.sea
	 * .ISEAContainer, java.lang.String, gautosar.gecucparameterdef.GConfigReference, java.util.List)
	 */
	@Override
	public boolean checkDestinationType(ISEAContainer parent, String defName, GConfigReference configReference,
		List<ISEAInstanceReference> values)
	{
		// TODO:Auto-generated method stub
		return true;
	}

	/**
	 * @param iReferenceValue
	 * @param contexts
	 */
	public void setContexts(GInstanceReferenceValue iReferenceValue, List<GIdentifiable> contexts)
	{
		try
		{
			setContexts(iReferenceValue, contexts, !getSeaOptionsHolder().isReadOnly());
		}
		finally
		{
			checkAutoSave();
		}
	}

	/**
	 * @param iReferenceValue
	 * @param target
	 */
	public void setTarget(GInstanceReferenceValue iReferenceValue, GIdentifiable target)
	{
		try
		{
			setTarget(iReferenceValue, target, !getSeaOptionsHolder().isReadOnly());
		}
		finally
		{
			checkAutoSave();
		}

	}

	private void setTarget(final GInstanceReferenceValue iReferenceValue, final GIdentifiable target, boolean readWite)
	{
		if (!readWite)
		{
			doSetTarget(iReferenceValue, target);
		}
		else
		{
			try
			{
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(iReferenceValue);
				updateModel(domain, new Runnable()
				{
					@Override
					public void run()
					{
						doSetTarget(iReferenceValue, target);
					}
				});
			}
			catch (ExecutionException e)
			{
				handleExecutionException(e);
			}
		}
	}

	/**
	 * @param iReferenceValue
	 * @param target
	 */
	private void doSetTarget(GInstanceReferenceValue iReferenceValue, GIdentifiable target)
	{
		IEcucMMService ecucMMService = getMMService().getEcucMMService();
		ecucMMService.setInstanceRefTarget(iReferenceValue, target);
	}

	/**
	 * @param contexts
	 * @param readWrite
	 */
	private void setContexts(final GInstanceReferenceValue iReferenceValue, final List<GIdentifiable> contexts,
		boolean readWrite)
	{
		if (!readWrite)
		{
			doSetContexts(iReferenceValue, contexts);
		}
		else
		{
			try
			{
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(iReferenceValue);
				updateModel(domain, new Runnable()
				{
					@Override
					public void run()
					{
						doSetContexts(iReferenceValue, contexts);
					}
				});
			}
			catch (ExecutionException e)
			{
				handleExecutionException(e);
			}
		}

	}

	/**
	 * @param contexts
	 */
	private void doSetContexts(GInstanceReferenceValue iReferenceValue, List<GIdentifiable> contexts)
	{
		IEcucMMService ecucMMService = getMMService().getEcucMMService();
		ecucMMService.setInstanceRefContext(iReferenceValue, contexts);
	}
}
