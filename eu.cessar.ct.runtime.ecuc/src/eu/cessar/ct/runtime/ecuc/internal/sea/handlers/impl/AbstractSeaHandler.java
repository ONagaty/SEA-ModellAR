/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 09.05.2013 16:51:29
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.UnmodifiableSEAEList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEAWriteOperationException;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * Base implementation of all Sea handlers
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Nov  5 15:57:23 2014 %
 * 
 *         %version: 6 %
 */
public abstract class AbstractSeaHandler
{
	private final ISeaOptions opStore;
	private final IProject project;
	private ISEAModel seaModel;

	/**
	 * @param seaModel
	 * @param project
	 * @param opStore
	 */
	public AbstractSeaHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		this.seaModel = seaModel;
		project = seaModel.getProject();
		this.opStore = opStore;
	}

	/**
	 * @return SEA options store
	 */
	protected ISeaOptions getSeaOptionsHolder()
	{
		return opStore;
	}

	/**
	 * @return the used SEA model
	 */
	protected ISEAModel getSeaModel()
	{
		return seaModel;
	}

	/**
	 * @return the meta-model service
	 */
	protected IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(project);
	}

	/**
	 * @return the Ecuc model
	 */
	protected IEcucModel getEcucModel()
	{
		return IEcucCore.INSTANCE.getEcucModel(project);
	}

	/**
	 * @param list
	 * @return whether given list is allows alteration
	 */
	protected static boolean isModifiable(EList<?> list)
	{
		return !list.getClass().getName().equals(UnmodifiableSEAEList.class.getName());
	}

	/**
	 * Executes the runnable in a write transaction.
	 * 
	 * @param domain
	 * 
	 * @param runnable
	 * @throws ExecutionException
	 */
	protected static void updateModel(TransactionalEditingDomain domain, Runnable runnable) throws ExecutionException
	{
		WorkspaceTransactionUtil.executeInWriteTransaction(domain, runnable, "update model"); //$NON-NLS-1$
	}

	/**
	 * Throws a runtime exception wrapping the cause of the given execution exception.
	 * 
	 * @param e
	 *        execution exception whose cause will be wrapped
	 */
	protected static void handleExecutionException(ExecutionException e)
	{
		throw new SEAWriteOperationException(e.getCause());

	}

	/**
	 * @param definition
	 * @return whether given definition allows having more than one instance
	 */
	protected static boolean isMany(GParamConfMultiplicity definition)
	{
		return EcucMetaModelUtils.isMulti(definition);
	}

	/**
	 * If the <code>definitions</code> list has one element (as expected), the element is simply returned. <br>
	 * If the list is empty or has several elements, the error handler is invoked and asked for a default definition.
	 * 
	 * @param parent
	 * 
	 * @param definitions
	 *        definitions that matched the <code>defName</code>, could be empty
	 * @param defName
	 *        definition name to be passed to the handler if the case
	 * @return the module definition to be used or <code>null</code>
	 */
	protected GARObject selectDefinition(ISEAObject parent, List<? extends GARObject> definitions, String defName)
	{
		GARObject definition = null;
		int modulesCount = definitions.size();
		if (modulesCount == 0)
		{
			definition = handleNoDefinition(parent, defName);
		}
		if (modulesCount == 1)
		{
			definition = definitions.get(0);
		}
		if (modulesCount > 1)
		{
			definition = handleMultipleDefinitions(parent, defName, definitions);
		}

		return definition;
	}

	/**
	 * Checks the passed argument for <code>null</code> or empty string. If so, throws an unchecked exception.
	 * 
	 * @param arg
	 *        string to be checked
	 */
	protected static void checkArgument(String arg)
	{
		assertNotNull(arg);
		assertNotEmptyString(arg);
	}

	/**
	 * Checks whether provided argument is <code>null</code> and if so, throws an unchecked exception.
	 * 
	 * @param arg
	 *        object to be checked for <code>null</code>
	 */
	protected static void assertNotNull(Object arg)
	{
		if (arg == null)
		{
			throw new IllegalArgumentException(Messages.Invalid_null_argument);
		}
	}

	/**
	 * @param arg
	 */
	protected static void assertNotEmptyString(String arg)
	{
		if ("".equals(arg)) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("Invalid (empty String) argument!"); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the error handler
	 * 
	 * @param obj
	 * @param defName
	 * 
	 * @return a definition to be used
	 */
	protected GARObject handleNoDefinition(ISEAObject obj, String defName)
	{
		GARObject chosenDef = getSeaOptionsHolder().getErrorHandler().noDefinitionFound(obj, defName);
		return chosenDef;
	}

	/**
	 * Calls the error handler
	 * 
	 * @param obj
	 * @param defName
	 * @param definitions
	 * @return the chosen one
	 */
	protected GARObject handleMultipleDefinitions(ISEAObject obj, String defName, List<? extends GARObject> definitions)
	{
		GARObject chosenDef = getSeaOptionsHolder().getErrorHandler().multipleDefinitionsFound(obj, defName,
			definitions);

		return chosenDef;
	}

	/**
	 * Calls the error handler
	 * 
	 * @param parent
	 * @param defName
	 * @param list
	 * @return value to be used, could be <code>null</code>
	 */
	protected <T> T handleMultipleValuesFound(ISEAContainer parent, String defName, List<T> list)
	{
		return getSeaOptionsHolder().getErrorHandler().multipleValuesFound(parent, defName,
			Collections.unmodifiableList(list));
	}

	/**
	 * If autoSave flag is enabled, performs saving of the dirty resources
	 */
	protected void checkAutoSave()
	{
		if (getSeaOptionsHolder().isAutoSave())
		{
			performSave();
		}
	}

	/**
	 * 
	 */
	protected void performSave()
	{
		Collection<Resource> dirtyResources = ModelUtils.getDirtyResources(getSeaModel().getProject());

		ModelUtils.saveResources(dirtyResources, new NullProgressMonitor());
	}

	/**
	 * Checks that <code>activeContainer</code> is among the fragments of <code>container</code>. If not, calls the
	 * error handler
	 * 
	 * @param activeContainer
	 *        the active container to be checked, could be <code>null</code>
	 * @param container
	 */
	protected static void checkActiveContainerValidity(GContainer activeContainer, ISEAContainer container)
	{
		if (activeContainer != null && !container.arGetContainers().contains(activeContainer))
		{
			container.getSEAModel().getOptions().getErrorHandler().wrongActiveContainer(container, activeContainer);
		}
	}

}
