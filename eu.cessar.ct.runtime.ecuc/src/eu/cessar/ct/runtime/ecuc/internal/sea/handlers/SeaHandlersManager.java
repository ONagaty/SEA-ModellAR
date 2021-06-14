/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.05.2013 10:48:47
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaAttributesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaConfigsHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaContainersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaForeignReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaInstanceReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaParametersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.SeaSimpleReferencesHandler;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;

/**
 * Manager providing access to the Sea handlers, based on a {@link ISEAModel} and a {@link ISeaOptionsStore} objects.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Aug 23 17:36:59 2013 %
 * 
 *         %version: 2 %
 */
public final class SeaHandlersManager
{
	static
	{
		// just to reach 100% code coverage
		new SeaHandlersManager();
	}

	private SeaHandlersManager()
	{
		// hide
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating configuration(s)
	 */
	public static SeaConfigsHandler getSeaConfigsHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaConfigsHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating container(s)
	 */
	public static SeaContainersHandler getSeaContainersHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaContainersHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating {@link GCommonConfigurationAttributes}
	 */
	public static SeaAttributesHandler getSeaAttributesHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaAttributesHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating parameter values
	 */
	public static SeaParametersHandler getSeaParametersHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaParametersHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating foreign reference values
	 */
	public static SeaForeignReferencesHandler getSeaForeignReferencesHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaForeignReferencesHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating simple reference values
	 */
	public static SeaSimpleReferencesHandler getSeaSimpleReferencesHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaSimpleReferencesHandler(seaModel, store);
	}

	/**
	 * @param seaModel
	 * @param store
	 * @return Sea handler for manipulating instance reference values
	 */
	public static SeaInstanceReferencesHandler getSeaInstanceReferencesHandler(ISEAModel seaModel, ISeaOptions store)
	{
		return new SeaInstanceReferencesHandler(seaModel, store);
	}

}
