/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Feb 3, 2014 2:36:44 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaAttributesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.SeaActiveConfigurationManager;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEAWriteOperationException;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 21 14:53:53 2014 %
 * 
 *         %version: 3 %
 * @param <T>
 */
public abstract class AbstractSeaAttributesHandler<T extends GCommonConfigurationAttributes> extends AbstractSeaHandler
	implements ISeaAttributesHandler<T>
{

	/**
	 * @param seaModel
	 * @param opStore
	 */
	public AbstractSeaAttributesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/**
	 * @param list
	 * @param parent
	 * @param activeContainer
	 * @param isMany
	 * @param definition
	 * @param values
	 */
	protected <V> void doSetValues(EList<V> list, ISEAContainer parent, GContainer activeContainer, boolean isMany,
		T definition, List<V> values)
	{
		boolean isSplit = SeaUtils.isSplit(parent);
		try
		{
			if (SeaUtils.isSplit(parent))
			{
				ISEAConfig configuration = SeaUtils.getConfiguration(parent);
				GModuleConfiguration activeConfiguration = SeaActiveConfigurationManager.INSTANCE.getGlobalActiveConfiguration(configuration);
				if (activeConfiguration == null && activeContainer == null)
				{
					String qualifiedName = SeaUtils.getQualifiedName(parent);
					throw new SEAWriteOperationException(NLS.bind(Messages.NoActiveConfiguration, qualifiedName));
				}
			}

			// validate values
			List<V> acceptedValues = getAcceptedValues(parent, definition, values);
			if (acceptedValues.isEmpty())
			{
				return;
			}

			if (isSplit && activeContainer != null)
			{
				SeaActiveConfigurationManager.INSTANCE.setTemporaryActiveResource(activeContainer.eResource());
			}

			// remove existing values
			list.clear();

			if (isMany)
			{
				list.addAll(acceptedValues);
			}
			else
			{
				if (!values.isEmpty())
				{
					list.add(acceptedValues.get(0));
				}
			}
		}
		finally
		{
			if (isSplit && activeContainer != null)
			{
				SeaActiveConfigurationManager.INSTANCE.setTemporaryActiveResource(null);
			}
		}
	}

}
