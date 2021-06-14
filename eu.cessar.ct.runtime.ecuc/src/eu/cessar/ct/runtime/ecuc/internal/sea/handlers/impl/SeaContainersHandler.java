/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 28.05.2013 16:52:59
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAContainerImpl;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.MatchingDefinitionHelper;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.SEAChoiceContainersEList;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.SEAContainersEList;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.SEAContainersMultiEList;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:48 2015 %
 * 
 *         %version: 11 %
 */
public class SeaContainersHandler extends AbstractSeaHandler implements ISeaContainersHandler
{
	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaContainersHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#getContainer(eu.cessar.ct.sdk.sea.
	 * ISEAContainerParent, java.lang.String)
	 */
	@Override
	public ISEAContainer getContainer(ISEAContainerParent parent, String defName)
	{
		checkArgument(defName);

		ISEAContainer seaContainer = null;
		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		GContainerDef definition = selectDefinition(parent, matchingContainerDefs, defName);

		if (definition != null)
		{
			GContainer toWrap = selectChildContainerForDefinition(parent, definition, defName);

			if (toWrap != null)
			{
				seaContainer = createSeaContainerWrapper(toWrap);
			}
		}
		return seaContainer;
	}

	/**
	 * @param parent
	 * @param defName
	 * @return
	 */
	private static List<GContainerDef> getMatchingContainerDefs(ISEAContainerParent parent, String defName)
	{
		if (parent instanceof ISEAConfig)
		{
			GModuleDef definition = ((ISEAConfig) parent).arGetDefinition();
			return MatchingDefinitionHelper.getMatchingContainerDefs(definition, defName);
		}
		else
		{
			GParamConfContainerDef definition = ((ISEAContainer) parent).arGetDefinition();
			return MatchingDefinitionHelper.getMatchingContainerDefs(definition, defName);
		}
	}

	/**
	 * @param parent
	 * @param defName
	 * @return
	 */
	private static List<GParamConfContainerDef> getMatchingContainerDefs(ISEAContainerParent parent,
		String choiceContainerDefName, String chosenContainerDefName)
	{
		if (parent instanceof ISEAConfig)
		{
			GModuleDef definition = ((ISEAConfig) parent).arGetDefinition();
			return MatchingDefinitionHelper.getMatchingContainerDefs(definition, choiceContainerDefName,
				chosenContainerDefName);
		}
		else
		{
			GParamConfContainerDef definition = ((ISEAContainer) parent).arGetDefinition();
			return MatchingDefinitionHelper.getMatchingContainerDefs(definition, choiceContainerDefName,
				chosenContainerDefName);
		}
	}

	/**
	 * Creates a wrapper around the given container
	 * 
	 * @param toWrap
	 *        wrapped container
	 * @return container wrapper
	 */
	public ISEAContainer createSeaContainerWrapper(GContainer toWrap)
	{
		return new SEAContainerImpl(getSeaModel(), toWrap, getSeaOptionsHolder());
	}

	/**
	 * Searches for child containers of the specified definition in the given <code>parent</code>. If exactly one, the
	 * container will be simply returned. If several, the error handler is called and asked for a default.
	 * 
	 * @param containerDef
	 *        the definition of the searched child container
	 * @param defName
	 *        child container definition's shortName to be passed to the error handler if the case
	 * @return the child container, could be <code>null</code>
	 */
	private GContainer selectChildContainerForDefinition(ISEAContainerParent parent, GContainerDef containerDef,
		String defName)
	{
		GContainer toWrap = null;

		List<GContainer> containers = new ArrayList<GContainer>();
		List<SplitedEntry<GContainer>> list = getOwnedSplitedContainersForDefinition(parent, containerDef);

		for (SplitedEntry<GContainer> splitedEntry: list)
		{
			ESplitableList<GContainer> splitableList = splitedEntry.getSplitableList();
			if (splitableList.size() > 0)
			{
				GContainer container = splitableList.get(0);
				containers.add(container);
			}
		}

		int count = containers.size();
		if (count == 1)
		{
			toWrap = containers.get(0);
		}
		else if (count > 1)
		{
			toWrap = handleMultipleValues(parent, defName, containers);
		}

		return toWrap;
	}

	/**
	 * Calls the error handler asking for a value to be used.
	 * 
	 * @param parent
	 * @param defName
	 * @param containers
	 * @return value to be used, could be <code>null</code>
	 */
	private GContainer handleMultipleValues(ISEAContainerParent parent, String defName, List<GContainer> containers)
	{
		List<GContainer> mergedContainers = new ArrayList<GContainer>();
		for (GContainer container: containers)
		{
			GContainer mergedInstance = SplitUtils.getMergedInstance(container);
			mergedContainers.add(mergedInstance);
		}

		Object selected = getSeaOptionsHolder().getErrorHandler().multipleValuesFound(parent, defName, mergedContainers);
		if (selected != null)
		{
			Assert.isTrue(selected instanceof GContainer);
		}
		return (GContainer) selected;
	}

	/**
	 * @param parent
	 * @param definition
	 * @return
	 */
	private List<SplitedEntry<GContainer>> getOwnedSplitedContainersForDefinition(ISEAContainerParent parent,
		GContainerDef definition)
	{
		if (parent instanceof ISEAContainer)
		{
			ISEAContainer c = (ISEAContainer) parent;
			return getEcucModel().getSplitedContainersFromContainer(c.arGetContainers(), definition);
		}
		else
		{
			ISEAConfig c = (ISEAConfig) parent;
			return getEcucModel().getSplitedContainersFromModule(c.arGetConfigurations(), definition);
		}
	}

	/**
	 * @param parent
	 * @return
	 */
	private List<GContainerDef> getChildContainerDefs(ISEAContainerParent parent)
	{
		if (parent instanceof ISEAConfig)
		{
			ISEAConfig c = (ISEAConfig) parent;
			return getChildContainerDefs(c.arGetDefinition());
		}
		else
		{
			ISEAContainer c = (ISEAContainer) parent;
			return getChildContainerDefs(c.arGetDefinition());
		}
	}

	private List<GContainerDef> getChildContainerDefs(GModuleDef def)
	{
		return getEcucModel().collectContainerDefs(def);
	}

	private List<GContainerDef> getChildContainerDefs(GContainerDef def)
	{
		return getEcucModel().collectContainerDefs(def);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#getContainers(eu.cessar.ct.sdk.sea.
	 * ISEAContainerParent, java.lang.String)
	 */
	@Override
	public ISEAList<ISEAContainer> getContainers(ISEAContainerParent parent, String defName)
	{
		checkArgument(defName);

		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		GContainerDef definition = selectDefinition(parent, matchingContainerDefs, defName);
		if (definition != null)
		{
			return doGetContainers(parent, definition);
		}
		else
		{
			return SeaUtils.emptyList();
		}
	}

	private ISEAList<ISEAContainer> doGetContainers(ISEAContainerParent parent, GContainerDef childContainerDef)
	{
		List<EList<? super GContainer>> parentLists = getParentLists(parent);

		if (childContainerDef instanceof GParamConfContainerDef)
		{
			GParamConfContainerDef definition = (GParamConfContainerDef) childContainerDef;

			if (parentLists.size() > 1)
			{
				SEAContainersMultiEList seaContainersMultiEList = new SEAContainersMultiEList(definition, parent,
					parentLists, getSeaOptionsHolder());

				return seaContainersMultiEList;
			}
			else
			{
				IModelChangeStampProvider changeStampProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(parent.getSEAModel().getProject());

				ParamConfContainerSubEList list = new ParamConfContainerSubEList(definition, parentLists.get(0), null,
					changeStampProvider);
				return new SEAContainersEList(parent, getSeaOptionsHolder(), list, definition);
			}
		}
		else
		{
			return new SEAChoiceContainersEList(parent, getSeaOptionsHolder(), (GChoiceContainerDef) childContainerDef);
		}
	}

	/**
	 * @param parent
	 * @return
	 */
	private static List<EList<? super GContainer>> getParentLists(ISEAContainerParent parent)
	{
		List<EList<? super GContainer>> parentLists = new ArrayList<EList<? super GContainer>>();

		if (parent instanceof ISEAContainer)
		{
			ISEAContainer c = (ISEAContainer) parent;
			List<GContainer> containers = c.arGetContainers();

			for (GContainer container: containers)
			{
				parentLists.add(container.gGetSubContainers());
			}
		}
		else
		{
			ISEAConfig c = (ISEAConfig) parent;

			List<GModuleConfiguration> configurations = c.arGetConfigurations();
			for (GModuleConfiguration configuration: configurations)
			{
				parentLists.add(configuration.gGetContainers());
			}
		}
		return parentLists;
	}

	/**
	 * Creates wrappers around containers that reside in this parent container and whose definition is among the ones
	 * from the provided list: <code>definitions</code>. The wrappers are collected in the <code>collected</code> list.
	 * 
	 * @param definitions
	 *        container definitions
	 */
	private void collectSeaContainerWrappers(ISEAContainerParent parent, List<ISEAContainer> collected,
		List<? extends GContainerDef> definitions)
	{
		for (GContainerDef def: definitions)
		{
			if (def instanceof GChoiceContainerDef)
			{
				collectSeaContainerWrappers(parent, collected, ((GChoiceContainerDef) def).gGetChoices());
			}
			else
			{
				collected.addAll(getSeaContainerWrappers(parent, (GParamConfContainerDef) def));
			}
		}
	}

	/**
	 * Creates and returns wrappers around containers of the given <code>definition</code> residing in <code>
	 * parent</code> or in any of its (direct or indirect) sub-containers.
	 * 
	 * 
	 * @param definition
	 *        definition of the containers the wrappers are asked for
	 * 
	 * @return a list of container wrappers or an empty list if no container with the given <code>definition</code> has
	 *         been found inside the sub-tree of <code>parent</code> (including the parent), never <code>null</code>
	 */
	private List<ISEAContainer> getSeaContainerWrappers(ISEAContainerParent parent, GParamConfContainerDef definition)
	{
		List<ISEAContainer> seaContainers = new ArrayList<ISEAContainer>();

		List<SplitedEntry<GContainer>> splitContainers = getOwnedSplitedContainersForDefinition(parent, definition);

		for (SplitedEntry<GContainer> entry: splitContainers)
		{
			String containerQName = entry.getQualifiedName();

			if (containerQName.startsWith(SeaUtils.getQualifiedName(parent)))
			{
				ESplitableList<GContainer> splitableList = entry.getSplitableList();
				if (splitableList.size() > 0)
				{
					GContainer container = splitableList.get(0);
					seaContainers.add(createSeaContainerWrapper(container));
				}
			}
		}

		return seaContainers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#searchForContainers(eu.cessar.ct.sdk.sea
	 * .ISEAContainerParent, java.lang.String)
	 */
	@Override
	public ISEAList<ISEAContainer> searchForContainers(ISEAContainerParent parent, String pathFragment)
	{
		checkArgument(pathFragment);

		List<GContainerDef> matchingDefs = new ArrayList<GContainerDef>();

		for (GContainerDef context: getChildContainerDefs(parent))
		{
			List<GContainerDef> definitions = getEcucModel().collectMatchingContainerDefs(context, pathFragment);
			matchingDefs.addAll(definitions);
		}

		List<ISEAContainer> seaContainers = new ArrayList<ISEAContainer>();
		if (matchingDefs.size() == 0)
		{
			GContainerDef definition = handleNoDefinition(parent, pathFragment);
			if (definition != null)
			{
				collectSeaContainerWrappers(parent, seaContainers, Collections.singletonList(definition));
			}
		}
		else
		{
			collectSeaContainerWrappers(parent, seaContainers, matchingDefs);
		}

		int size = seaContainers.size();
		return SeaUtils.unmodifiableSEAEList(size, seaContainers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.AbstractSeaHandler#handleNoDefinition(eu.cessar.ct.sdk.sea
	 * .ISEAObject, java.lang.String)
	 */
	@Override
	protected GContainerDef handleNoDefinition(ISEAObject obj, String defName)
	{
		Object definition = super.handleNoDefinition(obj, defName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GContainerDef);
		}
		return (GContainerDef) definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl.AbstractSeaHandler#selectDefinition(eu.cessar.ct.sdk.sea
	 * .ISEAObject, java.util.List, java.lang.String)
	 */
	@Override
	protected GContainerDef selectDefinition(ISEAObject parent, List<? extends GARObject> definitions, String defName)
	{
		Object definition = super.selectDefinition(parent, definitions, defName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GContainerDef);
		}

		return (GContainerDef) definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#isSetContainer(eu.cessar.ct.sdk.sea.
	 * ISEAContainerParent, java.lang.String)
	 */
	@Override
	public boolean isSetContainer(ISEAContainerParent parent, String defName)
	{
		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		GContainerDef definition = selectDefinition(parent, matchingContainerDefs, defName);

		if (definition == null)
		{
			return false;
		}

		return isSetContainer(parent, definition);
	}

	private boolean isSetContainer(ISEAContainerParent parent, GContainerDef definition)
	{
		List<SplitedEntry<GContainer>> childContainers = getChildContainersForDefinition(parent, definition);
		return childContainers.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#unSetContainer(eu.cessar.ct.sdk.sea.
	 * ISEAContainerParent, java.lang.String)
	 */
	@Override
	public void unSetContainer(ISEAContainerParent parent, String defName)
	{
		SeaUtils.checkSplitStatus(parent);

		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		GContainerDef definition = selectDefinition(parent, matchingContainerDefs, defName);

		if (definition != null)
		{
			unSetContainer(parent, definition);
		}
	}

	private void unSetContainer(ISEAContainerParent parent, GContainerDef definition)
	{
		if (definition == null)
		{
			return;
		}
		doUnsetContainer(parent, definition);
	}

	private void doUnsetContainer(ISEAContainerParent parent, GContainerDef containerDef)
	{
		List<SplitedEntry<GContainer>> childContainers = getChildContainersForDefinition(parent, containerDef);

		try
		{
			delete(getAllFragments(childContainers), !getSeaOptionsHolder().isReadOnly());
		}
		catch (ExecutionException e)
		{
			handleExecutionException(e);
		}
		finally
		{
			checkAutoSave();
		}
	}

	/**
	 * @param toDelete
	 *        containers to be deleted
	 * @param readWrite
	 *        whether to perform deletion within a write transaction
	 * @throws ExecutionException
	 */
	private static void delete(final List<GContainer> toDelete, boolean readWrite) throws ExecutionException
	{
		if (toDelete.size() == 0)
		{
			return;
		}

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(toDelete.get(0));
		if (readWrite)
		{
			// update model in a write transaction
			updateModel(domain, new Runnable()
			{
				@Override
				public void run()
				{
					doDelete(toDelete);
				}
			});
		}
		else
		{
			doDelete(toDelete);
		}
	}

	private static void doDelete(List<GContainer> toRemove)
	{
		for (GContainer c: toRemove)
		{
			EcoreUtil.delete(c, true);
		}
	}

	/**
	 * @param childContainers
	 * @return list with all container fragments from the specified list
	 */
	private static List<GContainer> getAllFragments(List<SplitedEntry<GContainer>> childContainers)
	{
		List<GContainer> allFragments = new ArrayList<GContainer>();
		for (SplitedEntry<GContainer> entry: childContainers)
		{
			allFragments.addAll(entry.getSplitableList());
		}

		return allFragments;
	}

	private List<SplitedEntry<GContainer>> getChildContainersForDefinition(ISEAContainerParent parent,
		GContainerDef childContainerDef)
	{
		if (parent instanceof ISEAConfig)
		{
			return getEcucModel().getSplitedContainersForModule(((ISEAConfig) parent).arGetConfigurations(),
				childContainerDef);
		}
		else
		{
			ISEAContainer c = (ISEAContainer) parent;

			return getEcucModel().getSplitedContainersForContainer(c.arGetContainers(), childContainerDef);
		}
	}

	private GContainerDef getChildContainerDef(ISEAContainerParent parent, String defName)
	{
		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		return selectDefinition(parent, matchingContainerDefs, defName);
	}

	private GParamConfContainerDef getChildContainerDef(ISEAContainerParent parent, String choiceContainerDefName,
		String chosenContainerDefName)
	{
		List<GParamConfContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, choiceContainerDefName,
			chosenContainerDefName);

		GContainerDef definition = selectDefinition(parent, matchingContainerDefs, chosenContainerDefName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GParamConfContainerDef);
		}
		return (GParamConfContainerDef) definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#createSEAChoiceContainer(eu.cessar.ct.sdk
	 * .sea.ISEAContainerParent, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createSEAChoiceContainer(ISEAContainerParent parent, String defName, String chosenDefName,
		String shortName, boolean deriveNameFromSuggestion)
	{
		SeaUtils.checkSplitStatus(parent);

		ISEAContainer container = null;
		try
		{
			container = createSEAChoiceContainer(parent, defName, chosenDefName, shortName, deriveNameFromSuggestion,
				!getSeaOptionsHolder().isReadOnly());
		}
		finally
		{
			checkAutoSave();
		}

		return container;
	}

	/**
	 * @param parent
	 * @param choiceContainerDefName
	 * @param chosenDefName
	 * @param shortName
	 * @param deriveNameFromSuggestion
	 * @param readWrite
	 * @return
	 */
	private ISEAContainer createSEAChoiceContainer(final ISEAContainerParent parent,
		final String choiceContainerDefName, final String chosenDefName, final String shortName,
		final boolean deriveNameFromSuggestion, final boolean readWrite)
	{
		final ISEAContainer[] containers = new ISEAContainer[1];
		if (readWrite)
		{
			try
			{
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getWrappedElement(parent));
				updateModel(domain, new Runnable()
				{
					@Override
					public void run()
					{
						containers[0] = doCreateChoiceContainer(parent, choiceContainerDefName, chosenDefName,
							shortName, deriveNameFromSuggestion);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleExecutionException(e1);
			}
		}
		else
		{
			containers[0] = doCreateChoiceContainer(parent, choiceContainerDefName, chosenDefName, shortName,
				deriveNameFromSuggestion);
		}

		return containers[0];
	}

	/**
	 * @param parent
	 * @param choiceContainerDefName
	 * @param chosenDefName
	 * @param shortName
	 * @param deriveNameFromSuggestion
	 * @return wrapper around the newly created container
	 */
	private ISEAContainer doCreateChoiceContainer(ISEAContainerParent parent, String choiceContainerDefName,
		String chosenDefName, String shortName, boolean deriveNameFromSuggestion)
	{
		GContainerDef chosenContainerDef = getChildContainerDef(parent, choiceContainerDefName, chosenDefName);
		if (chosenContainerDef == null)
		{
			return null;
		}

		SeaUtils.checkSplitStatus(parent);

		List<GContainer> childrenList = getChildrenList(parent);

		EObject containerDefParent = chosenContainerDef.eContainer();
		Assert.isTrue(containerDefParent instanceof GChoiceContainerDef);
		GChoiceContainerDef choiceDef = (GChoiceContainerDef) containerDefParent;

		EObject realParent = getWrappedElement(parent);

		if (getMMService().getEcucMMService().needChoiceIntermediateContainer())
		{
			GContainer choiceInstance = createChoiceIntermediateContainerIfMissing(realParent, childrenList, choiceDef);

			childrenList = choiceInstance.gGetSubContainers();
			realParent = choiceInstance;
		}

		GContainer newContainer = arCreateContainer(getGenericFactory(), realParent, chosenContainerDef, shortName,
			deriveNameFromSuggestion);

		if (!isMany(choiceDef))
		{
			parent.unSetContainer(choiceContainerDefName);
		}

		childrenList.add(newContainer);

		return createSeaContainerWrapper(newContainer);
	}

	/**
	 * If not already present, creates a choice container of the <code>choiceContainerDef</code> definition into the
	 * <code>realParent</code>.
	 * 
	 * @param realParent
	 *        either a {@link GModuleConfiguration} or a {@link GContainer }
	 * @param childrenList
	 *        list with child containers
	 * @param choiceContainerDef
	 * @return an already existing choice container or a newly created one
	 */
	private GContainer createChoiceIntermediateContainerIfMissing(EObject realParent, List<GContainer> childrenList,
		GChoiceContainerDef choiceContainerDef)
	{
		GContainer choiceInstance = null;
		if (getMMService().getAutosarReleaseOrdinal() != 4)
		{
			choiceInstance = findChoiceIntermediate(childrenList, choiceContainerDef);
		}

		if (choiceInstance == null)
		{
			choiceInstance = arCreateContainer(getGenericFactory(), realParent, choiceContainerDef, null, false);
			childrenList.add(choiceInstance);
		}

		return choiceInstance;
	}

	/**
	 * @param parent
	 * @return
	 */
	private static List<GContainer> getChildrenList(ISEAContainerParent parent)
	{
		List<GContainer> childrenList = new ArrayList<GContainer>();
		if (parent instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configurations = ((ISEAConfig) parent).arGetConfigurations();
			childrenList = configurations.get(0).gGetContainers();
		}
		else
		{
			List<GContainer> containers = ((ISEAContainer) parent).arGetContainers();
			childrenList = containers.get(0).gGetSubContainers();
		}

		return childrenList;
	}

	private static GContainer arCreateContainer(IGenericFactory genericfactory, EObject parent,
		GContainerDef definition, String shortName, boolean deriveNameFromSuggestion)
	{
		GContainer container = genericfactory.createContainer();
		container.gSetDefinition(definition);
		container.gSetShortName(getShortName(parent, definition.gGetShortName(), shortName, deriveNameFromSuggestion));

		return container;
	}

	private static String getShortName(EObject owner, String definitionShortName, String shortName,
		boolean deriveNameFromSuggestion)
	{
		if (shortName == null)
		{
			return MetaModelUtils.computeUniqueChildShortName(owner, definitionShortName);
		}
		else if (deriveNameFromSuggestion)
		{
			return MetaModelUtils.computeUniqueChildShortName(owner, shortName);
		}
		else
		{
			return shortName;
		}
	}

	private static GIdentifiable getWrappedElement(ISEAContainerParent containerParent)
	{
		GIdentifiable element = null;
		if (containerParent instanceof ISEAConfig)
		{
			element = ((ISEAConfig) containerParent).arGetConfigurations().get(0);
		}
		else
		{
			element = ((ISEAContainer) containerParent).arGetContainers().get(0);
		}

		return element;
	}

	/**
	 * Returns the first found container with the specified <code>containerDef</code>> definition from the given list.
	 * 
	 * @param childrenList
	 *        the children list where the search is performed
	 * @param containerDef
	 *        the choice container definition whose instance is searched for
	 * @return The Choice container instance if found, or <code>null</code> otherwise.
	 */
	private static GContainer findChoiceIntermediate(List<? extends GContainer> childrenList,
		GChoiceContainerDef containerDef)
	{
		for (GContainer gContainer: childrenList)
		{
			if (gContainer.gGetDefinition().equals(containerDef))
			{
				return gContainer;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#createSEAContainer(eu.cessar.ct.sdk.sea
	 * .ISEAContainerParent, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createSEAContainer(ISEAContainerParent parent, String defName, String shortName,
		boolean deriveNameFromSuggestion)
	{
		// SeaUtils.checkSplitStatus(parent);

		// if (SeaUtils.isSplit(parent))
		// {
		// ISEAConfig configuration = SeaUtils.getConfiguration(parent);
		// GModuleConfiguration activeConfiguration =
		// SeaActiveConfigurationManager.INSTANCE.getGlobalActiveConfiguration(configuration);
		// if (activeConfiguration == null)
		// {
		// String qualifiedName = SeaUtils.getQualifiedName(parent);
		// throw new SEAWriteOperationException(NLS.bind(Messages.NoActiveConfiguration, qualifiedName));
		// }
		// }

		GContainerDef definition = getChildContainerDef(parent, defName);

		if (definition != null && definition instanceof GParamConfContainerDef)
		{
			IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(parent.getSEAModel().getProject());
			GContainer container = arCreateContainer(genericFactory, getWrappedElement(parent), definition, shortName,
				deriveNameFromSuggestion);

			ISEAContainer seaContainer = createSeaContainerWrapper(container);

			ISEAList<ISEAContainer> containers = doGetContainers(parent, definition);
			// if (!EcucMetaModelUtils.isMulti(seaContainer.arGetDefinition()))
			// {
			// containers.clear();
			// }
			containers.add(seaContainer);

			return seaContainer;
		}

		return null;
	}

	private IGenericFactory getGenericFactory()
	{
		return MMSRegistry.INSTANCE.getGenericFactory(getSeaModel().getProject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler#setShortName(eu.cessar.ct.sdk.sea.
	 * ISEAContainerParent, java.lang.String)
	 */
	@Override
	public void setShortName(ISEAContainerParent parent, String shortName)
	{
		try
		{
			setShortName(parent, shortName, !getSeaOptionsHolder().isReadOnly());
		}
		catch (ExecutionException e)
		{
			handleExecutionException(e);
		}
		finally
		{
			checkAutoSave();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getContainer(eu.cessar.ct.sdk.sea.ISEAModel
	 * , gautosar.gecucdescription.GContainer)
	 */
	@Override
	public ISEAContainer getContainer(ISEAModel seaModel, GContainer container)
	{
		assertNotNull(container);

		return new SEAContainerImpl(seaModel, container, getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getContainer(eu.cessar.ct.sdk.sea.ISEAModel
	 * , eu.cessar.ct.sdk.pm.IPMContainer)
	 */
	@Override
	public ISEAContainer getContainer(ISEAModel seaModel, IPMContainer container)
	{
		assertNotNull(container);

		List<GContainer> containers = PMUtils.getContainers(container);
		Assert.isTrue(containers.size() > 0);

		ISEAContainer seaContainer = getContainer(seaModel, containers.get(0));
		return seaContainer;
	}

	/**
	 * Sets the short name of the wrapped element to the given value. The <code>readWrite</code> flag indicates if the
	 * operation will be executed inside a write transaction or not.
	 * 
	 * @param parent
	 * 
	 * @param shortName
	 *        the new shortName, can be <code>null</code>
	 * @param readWrite
	 *        whether to execute the operation within a write transaction
	 * @throws ExecutionException
	 */
	protected static void setShortName(final ISEAContainerParent parent, final String shortName, boolean readWrite)
		throws ExecutionException
	{
		if (readWrite)
		{
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getWrappedElement(parent));
			updateModel(domain, new Runnable()
			{
				@Override
				public void run()
				{
					doSetShortName(parent, shortName);
				}
			});
		}
		else
		{
			doSetShortName(parent, shortName);
		}
	}

	/**
	 * Sets the wrapped element's short name to the given value. If <code>null</code> is passed, the short name feature
	 * will be unset.
	 * 
	 * @param parent
	 * 
	 * @param shortName
	 *        the new shortName, can be <code>null</code>
	 */
	protected static void doSetShortName(ISEAContainerParent parent, String shortName)
	{
		GIdentifiable wrapped = getWrappedElement(parent);
		if (shortName != null)
		{
			wrapped.gSetShortName(shortName);
		}
		else
		{
			wrapped.gUnsetShortName();
		}
	}

}
