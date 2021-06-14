/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 5:51:55 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.*;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EObjectUtil;

import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.IMasterReferenceWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.runtime.ecuc.util.ChoiceContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ChoiceContainerSubEList;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.Messages;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerSubEList;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Reference to a list of containers
 */
public class MultiGContainerReferenceWrapper extends AbstractMultiMasterFeatureWrapper<EObject> implements
IMasterReferenceWrapper
{
	/**
	 * List of all containers with this definition, every container with a different qualified name has an
	 * {@link ESplitableList} containing its fragments.
	 */
	private List<ESplitableList<GContainer>> splitedContainers = null;
	/**
	 * All brother containers. For non-split containers
	 */
	private final EList<GContainer> singularParentList;
	private final GContainerDef definition;

	private GContainer lastAddedOrSet = null;
	private IModelChangeStampProvider changeProvider;
	/**
	 * All brother containers having this definition. Only for splitable containers
	 */
	private List<EList<? super GContainer>> multiParentList;
	/**
	 * All parents list
	 */
	private List<EList<? super GContainer>> parentsList;
	private List<GIdentifiable> parentNoChildrenList;
	@SuppressWarnings("rawtypes")
	private DelegatingWithSourceMultiEList multiEListAux;

	/**
	 * @param engine
	 * @param definition
	 * @param multiModules
	 * @param singularParentList
	 * @param multiParentList
	 * @param parentNoChildrenList
	 */
	public MultiGContainerReferenceWrapper(IEMFProxyEngine engine, GContainerDef definition,
		List<ESplitableList<GContainer>> multiModules, EList<GContainer> singularParentList,
		List<EList<? super GContainer>> multiParentList, List<GIdentifiable> parentNoChildrenList)
	{
		this(engine, definition, multiModules, singularParentList, multiParentList);
		this.parentNoChildrenList = parentNoChildrenList;

	}

	/**
	 * @param engine
	 * @param definition
	 * @param multiModules
	 * @param singularParentList
	 * @param multiParentList
	 */
	public MultiGContainerReferenceWrapper(IEMFProxyEngine engine, GContainerDef definition,
		List<ESplitableList<GContainer>> multiModules, EList<GContainer> singularParentList,
		List<EList<? super GContainer>> multiParentList)
	{
		super(engine);
		splitedContainers = multiModules;
		this.singularParentList = singularParentList;
		this.definition = definition;
		changeProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(definition);
		this.multiParentList = multiParentList;
		parentsList = collectParentContainers();
	}

	private List<EList<? super GContainer>> collectParentContainers()
	{
		List<EList<? super GContainer>> parentsContainersList = new ArrayList<EList<? super GContainer>>();

		for (ESplitableList<GContainer> list: splitedContainers)
		{
			EList<GContainer> parents = new BasicEList<GContainer>();
			for (GContainer gContainer: list)
			{
				EObject parent = gContainer.eContainer();
				if (parent instanceof GContainer)
				{
					parents.add((GContainer) parent);
				}
			}
			parentsContainersList.add(parents);
		}
		return parentsContainersList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<EObject> getFeatureClass()
	{
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMultiMasterFeatureWrapper#getWrappedList()
	 */
	@Override
	protected List<?> getWrappedList()
	{
		updateSplitedContainers();
		return splitedContainers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#get(int)
	 */
	public EObject get(int index)
	{
		updateSplitedContainers();
		if (index < 0 || index >= splitedContainers.size())
		{
			throw new IndexOutOfBoundsException(NLS.bind(Messages.splitPMUtils_SizeIndex, splitedContainers.size(),
				index));
		}

		List<GContainer> list = splitedContainers.get(index);

		GContainer pack = list.get(0);
		return getEngine().getSlaveObject(getContext(), pack);
	}

	/**
	 *
	 */
	@Requirement(
		reqID = "190025")
	private void updateSplitedContainers()
	{
		int i = 0;
		int size = splitedContainers.size();
		while (i < size)
		{
			if (splitedContainers.get(i).isEmpty())
			{
				splitedContainers.remove(i);
				size = splitedContainers.size();

			}
			else
			{
				i++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#clear()
	 */
	@Requirement(
		reqID = "43378")
	@Override
	public void clear()
	{
		List<Object> sources = null;
		for (ESplitableList<GContainer> containers: splitedContainers)
		{
			if (sources == null)
			{
				sources = containers.getSources();
			}
		}

		// CHECKSTYLE:OFF not needed
		updateModel(new Runnable()
		{
			public void run()
			{
				if (!isSplitContainer())
				{
					for (ESplitableList<GContainer> containers: splitedContainers)
					{
						GContainer slaveContainer = containers.get(0);
						// getCachedSlaveObject(getContext(), slaveContainer);

						IEMFProxyObject slaveObject = getEngine().getSlaveObject(getContext(), slaveContainer);

						containers.clear();
						//
						if (slaveObject != null && slaveObject instanceof EMFProxyObjectImpl)
						{
							getEngine().updateSlave(getContext(), (EMFProxyObjectImpl) slaveObject, slaveContainer);
						}
					}
					// if a container has a GChoiceContainer parent, delete it
					deleteGChoiceContainerParents();

					splitedContainers.clear();
				}
				else
				{
					for (ESplitableList<GContainer> containers: splitedContainers)
					{
						GContainer slaveContainer = containers.get(0);
						// getCachedSlaveObject(getContext(), slaveContainer);

						IEMFProxyObject slaveObject = getEngine().getSlaveObject(getContext(), slaveContainer);

						containers.clear();
						// to do update slave for each container from containers
						if (slaveObject != null && slaveObject instanceof EMFProxyObjectImpl)
						{
							getEngine().updateSlave(getContext(), (EMFProxyObjectImpl) slaveObject, slaveContainer);
						}
					}
					// if a container has a GChoiceContainer parent, delete it
					deleteGChoiceContainerParents();

					splitedContainers.clear();
				}
			}

		});
		super.clear();

	}

	/**
	 * Deletes parents which are GChoiceContainers
	 */
	private void deleteGChoiceContainerParents()
	{ // collect GChoiceContainer parents and their corresponding grandparent
		Map<GContainer, Set<GContainer>> grandpaAndParents = new HashMap<GContainer, Set<GContainer>>();
		Map<GModuleConfiguration, Set<GContainer>> grandpaModuleConfigAndParents = new HashMap<GModuleConfiguration, Set<GContainer>>();
		List<GContainer> parents = new ArrayList<GContainer>();
		if (!isSplitContainer())
		{
			parents.addAll(singularParentList);
		}
		else
		{
			// collect all parents
			for (EList<? super GContainer> parentList: parentsList)
			{
				for (Object object: parentList)
				{
					parents.add((GContainer) object);
				}
			}

		}

		for (GContainer parent: parents)
		{
			GContainer containerParent = parent;
			GContainerDef parentDefinition = containerParent.gGetDefinition();
			if (parentDefinition != definition)
			{
				continue;
			}

			if (parentDefinition instanceof GChoiceContainerDef)
			{
				EObject grandpa = containerParent.eContainer();
				if (grandpa instanceof GContainer)
				{
					GContainer grandpaContainer = (GContainer) grandpa;
					Set<GContainer> set = grandpaAndParents.get(grandpaContainer);
					if (set == null)
					{
						set = new HashSet<GContainer>();

						grandpaAndParents.put(grandpaContainer, set);
					}
					set.add(containerParent);

				}
				else
				{
					if (grandpa instanceof GModuleConfiguration)
					{
						GModuleConfiguration grandpaConfig = (GModuleConfiguration) grandpa;
						Set<GContainer> set = grandpaModuleConfigAndParents.get(grandpaConfig);
						if (set == null)
						{
							set = new HashSet<GContainer>();

							grandpaModuleConfigAndParents.put(grandpaConfig, set);
						}
						set.add(containerParent);

					}
				}

			}

		}
		// delete GChoiceContainer parents
		Set<GContainer> grandpaSet = grandpaAndParents.keySet();
		for (GContainer grandpa: grandpaSet)
		{
			Set<GContainer> set = grandpaAndParents.get(grandpa);
			EList<GContainer> containers = grandpa.gGetSubContainers();

			containers.removeAll(set);

		}
		Set<GModuleConfiguration> configSet = grandpaModuleConfigAndParents.keySet();
		for (GModuleConfiguration grandpaConfig: configSet)
		{
			Set<GContainer> set = grandpaModuleConfigAndParents.get(grandpaConfig);
			EList<GContainer> containers = grandpaConfig.gGetContainers();

			containers.removeAll(set);

		}
	}

	/**
	 * Returns the element at given index from the flat list made of all subcontainers of the given list (@param
	 * listOfParents)
	 *
	 * @param sub
	 * @param index
	 * @param listOfParents
	 * @return
	 */
	private GContainer getContainerAtIndex(int index, List<GContainer> listOfParents)
	{
		int i = 0;
		for (GContainer cnt: listOfParents)
		{
			if (cnt.gGetSubContainers().size() > (index - i))
			{
				return cnt.gGetSubContainers().get(index - i);
			}
			else
			{
				i += cnt.gGetSubContainers().size();
			}
		}
		return null;
	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#remove(int)
 */
	@Requirement(
		reqID = "43382")
	// Also covers the
	// @Requirement( reqID = "43384") and @Requirement( reqID = "43587")
	public void remove(final int index)
	{
		// checkIfSplitContainer();
		if (!isSplitContainer())
		{
			ESplitableList<GContainer> subCnts;
			if (definition instanceof GChoiceContainerDef)
			{
				subCnts = new ChoiceContainerSubEList((GChoiceContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			else
			{
				subCnts = new ParamConfContainerSubEList((GParamConfContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			final ESplitableList<GContainer> sub = subCnts;

			List<GContainer> intermediateIntermediateList = new ArrayList<GContainer>();
			for (GContainer aux: singularParentList)
			{
				if (aux.gGetDefinition().equals(definition))
				{
					intermediateIntermediateList.add(aux);
				}
			}
			final List<GContainer> intermediateList = intermediateIntermediateList;

			if (lastAddedOrSet == null || sub.get(index) != lastAddedOrSet)
			{
				GContainer oldContainer;
				if (index > (sub.size() - 1))
				{
					oldContainer = getContainerAtIndex(index, intermediateList);
				}
				else
				{
					oldContainer = sub.get(index);
				}
				// GContainer oldContainer = sub.get(index);
				boolean oldIsProxy = oldContainer.eIsProxy();

				updateModel(new Runnable()
				{
					public void run()
					{
						if (index > (sub.size() - 1))
						{
							removeContainer(index, intermediateList);
						}
						else
						{
							sub.remove(index);
						}
					}

					private void removeContainer(int indexContainer, List<GContainer> listOfParents)
					{
						int i = 0;
						for (GContainer cnt: listOfParents)
						{
							if (cnt.gGetSubContainers().size() > (indexContainer - i))
							{
								cnt.gGetSubContainers().remove(indexContainer - i);
								// if after removal the intermediate level is empty
								// then remove it
								if (cnt.gGetSubContainers().isEmpty())
								{
									singularParentList.remove(cnt);
								}
								break;
							}
							else
							{
								i += cnt.gGetSubContainers().size();
							}
						}
					}
				});
				// Removing an object will proxify it, we don't want that
				if (!oldIsProxy && oldContainer.eIsProxy())
				{
					EObjectUtil.deproxify(oldContainer);
				}
				// update both objects
				IEMFProxyObject slaveObject = getEngine().getCachedSlaveObject(getContext(), oldContainer);
				getEngine().updateSlaveFeature(this);
				if (slaveObject != null && slaveObject instanceof EMFProxyObjectImpl)
				{
					getEngine().updateSlave(getContext(), (EMFProxyObjectImpl) slaveObject, oldContainer);
				}
				lastAddedOrSet = null;
			}
		}
		else
		{

			// List<EList<? super GContainer>> elistContainers = getListOfEListGContainers(splitedContainers);
			// to do: remove this when all operations are implemented and set at higher level
			// setReadOnlyForList(false, elistContainers);

			removeFromIndex(splitedContainers, index);

			// setReadOnlyForList(false, elistContainers);
		}
	}

	private void removeFromIndex(List<ESplitableList<GContainer>> splitableContainers, int index)
	{
		ESplitableList<GContainer> targetList = splitableContainers.get(index);
		// remove choice container parents only if the container to be deleted is their only child.
		// is this ok? normally a choice container should have only one child
		for (GContainer gContainer: targetList)
		{
			EObject parent = gContainer.eContainer();
			if (parent instanceof GContainer)
			{
				GContainer parentContainer = (GContainer) parent;
				GContainerDef parentDefinition = parentContainer.gGetDefinition();
				if (parentDefinition instanceof GChoiceContainerDef)
				{// only if the container to be deleted is their only child.
					EList<EObject> eContents = parentContainer.eContents();
					if (eContents.size() != 1)
					{
						continue;
					}
					// remove choice parent

					EObject grandpa = parentContainer.eContainer();
					if (grandpa instanceof GContainer)
					{
						GContainer grandpaContainer = (GContainer) grandpa;
						EList<GContainer> subContainers = grandpaContainer.gGetSubContainers();

						subContainers.remove(parentContainer);
					}
					else
					{
						// should be a module configuration grandpa
						if (grandpa instanceof GModuleConfiguration)
						{
							GModuleConfiguration grandpaConfiguration = (GModuleConfiguration) grandpa;
							EList<GContainer> containers = grandpaConfiguration.gGetContainers();
							containers.remove(parentContainer);
						}
					}
				}
			}
		}

		targetList.clear();
		splitableContainers.remove(index);

	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#set(int, java.lang.Object)
 */
	@Requirement(
		reqID = "26951")
	// Also for splitable covers the @Requirement( reqID = "43368")
	public void set(final int index, Object value)
	{
		if (!(value instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + value); //$NON-NLS-1$
		}
		boolean splitContainer = isSplitContainer();
		EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) value;
		IMasterObjectWrapper<?> wrapper = ((EMFProxyObjectImpl) value).eGetMasterWrapper();
		List<?> childrenMasters = wrapper.getAllMasterObjects();
		if (childrenMasters.size() > 1)
		{
			throw new PMRuntimeException(
				eu.cessar.ct.runtime.ecuc.pmproxy.wrap.Messages.cannotChangeSplitFeatureException);
		}
		final GContainer container = (GContainer) childrenMasters.get(0);

		final GContainerDef[] newTargetDef = new GContainerDef[1];
		final IEcucModel ecucModel[] = new IEcucModel[1];
		final GModuleDef targetModuleDef[] = new GModuleDef[1];

		if (PMProxyUtils.haveRefinementSupport(engine))
		{
			ecucModel[0] = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
			targetModuleDef[0] = ecucModel[0].getModuleDef(definition);
			if (targetModuleDef[0] != ecucModel[0].getModuleDef(container.gGetDefinition()))
			{
				// the definition need to be changed
				newTargetDef[0] = ecucModel[0].getRefinedContainerDefFamily(targetModuleDef[0],
					container.gGetDefinition());
			}
		}
		if (!splitContainer)
		{

			ESplitableList<GContainer> subCnts;
			if (definition instanceof GChoiceContainerDef)
			{
				subCnts = new ChoiceContainerSubEList((GChoiceContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			else
			{
				subCnts = new ParamConfContainerSubEList((GParamConfContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			final ESplitableList<GContainer> sub = subCnts;
			// the sub size and the splitedContainers size should match
			// if (sub.size() != splitedContainers.size())
			// {
			//			throw new PMRuntimeException("Internal error"); //$NON-NLS-1$
			// }
			updateModel(new Runnable()
			{
				public void run()
				{
					if (newTargetDef[0] != null)
					{
						// change the definition
						// container.gSetDefinition(newTargetDef[0]);
						if (ecucModel[0] != null)
						{
							ecucModel[0].recursiveDefUpdate(container, targetModuleDef[0]);
						}
					}
					// sub.set(index, container);
					setMultiContainer(sub, singularParentList, container, index);
				}
			});
			lastAddedOrSet = container;
			// update both objects
			getEngine().updateSlaveFeature(this);
			getEngine().updateSlave(getContext(), childrenProxy, container);
		}
		else
		{ // split container

			updateModel(new Runnable()
			{
				@SuppressWarnings("unchecked")
				public void run()
				{
					if (newTargetDef[0] != null)
					{
						// change the definition
						// container.gSetDefinition(newTargetDef[0]);
						if (ecucModel[0] != null)
						{
							ecucModel[0].recursiveDefUpdate(container, targetModuleDef[0]);
						}
					}
					@SuppressWarnings("rawtypes")
					DelegatingWithSourceMultiEList multiEList;
					if (definition instanceof GChoiceContainerDef)
					{
						multiEList = new ChoiceContainerMultiEList((GChoiceContainerDef) definition, multiParentList,
							changeProvider);
					}
					else
					{
						multiEList = new ParamConfContainerMultiEList((GParamConfContainerDef) definition,
							multiParentList, changeProvider);
					}

					setMultiContainerForSplitable(multiEList, container, index);

				}
			});
			lastAddedOrSet = container;
			// update both objects
			getEngine().updateSlaveFeature(this);
			getEngine().updateSlave(getContext(), childrenProxy, container);
		}
	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#move(int, int)
 */
	public Object move(final int targetIndex, final int sourceIndex)
	{
		Object result = get(sourceIndex);
		boolean splitContainer = isSplitContainer();
		if (!splitContainer)
		{
			doMoveNonSplitContainer(targetIndex, sourceIndex);
		}
		else
		{
			doMoveSplitContainer(targetIndex, sourceIndex);
		}
		getEngine().updateSlaveFeature(this);

		return result;
	}

	private void doMoveNonSplitContainer(final int targetIndex, final int sourceIndex)
	{
		ESplitableList<GContainer> subCnts;
		if (definition instanceof GChoiceContainerDef)
		{
			subCnts = new ChoiceContainerSubEList((GChoiceContainerDef) definition, singularParentList, null,
				changeProvider);
		}
		else
		{
			subCnts = new ParamConfContainerSubEList((GParamConfContainerDef) definition, singularParentList, null,
				changeProvider);
		}
		final ESplitableList<GContainer> sub = subCnts;

		List<GContainer> intermediateListList = new ArrayList<GContainer>();
		for (GContainer aux: singularParentList)
		{
			if (aux.gGetDefinition().equals(definition))
			{
				intermediateListList.add(aux);
				// break;
			}
		}
		final List<GContainer> intermediateList = intermediateListList;

		updateModel(new Runnable()
		{

			public void run()
			{
				EList<GContainer> sourceList = getParentContainer(sourceIndex, intermediateList);
				EList<GContainer> targetList = getParentContainer(targetIndex, intermediateList);
				// the same list
				if (sourceList.containsAll(targetList))
				{
					sourceList.move(targetIndex, sourceIndex);
				}
				else
				{
					moveFromBothLists(sourceList, targetList, targetIndex, sourceIndex);
				}
			}

			private EList<GContainer> getParentContainer(int index, List<GContainer> listOfParents)
			{
				if (sub.size() > index)
				{
					return sub;
				}
				else
				{
					int i = 0;
					for (GContainer cnt: listOfParents)
					{
						if (cnt.gGetSubContainers().size() > (index - i))
						{
							return cnt.gGetSubContainers();
						}
						else
						{
							i += cnt.gGetSubContainers().size();
						}

					}
				}
				return null;
			}

			private void moveFromBothLists(EList<GContainer> interList, EList<GContainer> subList, int targIndex,
				int srcIndex)
			{
				// a flag that says if the origin is from sub (if flag false),
				// or from intermediate (if flag true)
				boolean sourceOriginFlag = true;
				boolean targetOriginFlag = true;

				GContainer target;
				GContainer source;
				if (srcIndex > (subList.size() - 1))
				{
					srcIndex -= subList.size();
					source = interList.get(srcIndex);
					interList.remove(srcIndex);
					sourceOriginFlag = true;
				}
				else
				{
					source = subList.get(srcIndex);
					subList.remove(srcIndex);
					sourceOriginFlag = false;
				}

				if (targIndex > (subList.size() - 1))
				{
					targIndex -= subList.size();
					target = interList.get(targIndex);
					interList.remove(targIndex);
					targetOriginFlag = true;
				}
				else
				{
					target = subList.get(targIndex);
					subList.remove(targIndex);
					targetOriginFlag = false;
				}

				if (sourceOriginFlag)
				{
					interList.add(srcIndex, target);
				}
				else
				{
					subList.add(srcIndex, target);
				}

				if (targetOriginFlag)
				{
					interList.add(targIndex, source);
				}
				else
				{
					subList.add(targIndex, source);
				}

			}
		});
	}

	private void doMoveSplitContainer(final int targetIndex, final int sourceIndex)
	{

		if (definition instanceof GChoiceContainerDef)
		{
			multiEListAux = new ChoiceContainerMultiEList((GChoiceContainerDef) definition, multiParentList,
				changeProvider);
		}
		else
		{
			multiEListAux = new ParamConfContainerMultiEList((GParamConfContainerDef) definition, multiParentList,
				changeProvider);
		}

		updateModel(new Runnable()
		{

			public void run()
			{
				EList<? super GContainer> sourceList = multiParentList.get(sourceIndex);
				GContainer firstSourceFragment = (GContainer) sourceList.get(0);
				int indexSourceInSplitList = findIndexInSplitListForChoiceContainerParent(multiEListAux, sourceIndex);

				Object object = multiEListAux.get(indexSourceInSplitList);
				if (!(object instanceof GContainer))
				{
					return;
				}
				GContainer container = (GContainer) object;

				if (hasChoiceContainerParent(container))
				{

					handleMoveContainerWithChoiceParent(targetIndex, sourceIndex, firstSourceFragment);

				}
				else
				{

					handleMoveContainerWithoutChoiceParent(targetIndex, sourceIndex, firstSourceFragment);

				}

			}

			/**
			 * @param targetIndex
			 * @param sourceIndex
			 * @param firstSourceFragment
			 */
			@Requirement(
				reqID = "74674")
			private void handleMoveContainerWithoutChoiceParent(final int targetIndex, final int sourceIndex,
				GContainer firstSourceFragment)
			{
				EList<? super GContainer> targetList = multiParentList.get(targetIndex);
				// move the first fragment of the source at the position of the first fragment of the target
				GContainer targetFragment = (GContainer) targetList.get(0);
				// in case the target container is a choice container and has a choice parent, we go a level up
				boolean hasChoiceContainerParent = hasChoiceContainerParent(targetFragment);
				EList<GContainer> targetBrotherContainers;
				if (hasChoiceContainerParent)
				{
					targetBrotherContainers = getBrotherContainers((GContainer) targetFragment.eContainer());
				}
				else
				{
					targetBrotherContainers = getBrotherContainers(targetFragment);
				}
				// check if in the target list there is already a fragment of the source container

				// to do: is not needed when non split container
				GContainer mergedSource = SplitUtils.getMergedInstance(firstSourceFragment);
				GContainerDef sourceDefinition = firstSourceFragment.gGetDefinition();
				Collection<GContainer> concreteSourceInstances = SplitUtils.getConcreteInstances(mergedSource);

				boolean contains = false;
				// do not check the first source fragment
				concreteSourceInstances.remove(firstSourceFragment);

				for (GContainer cont: concreteSourceInstances)
				{
					if (targetBrotherContainers.contains(cont))
					{
						contains = true;
						break;
					}
				}
				if (contains)
				{ // if in the target list there is already a fragment of the source container, merge the container
					// and add it to the target index

					concreteSourceInstances.add(firstSourceFragment);
					for (GContainer cont: concreteSourceInstances)
					{
						EList<GContainer> brotherContainers = getBrotherContainers(cont);
						brotherContainers.remove(cont);
					}

					// find index
					int index;
					if (hasChoiceContainerParent)
					{
						index = targetBrotherContainers.indexOf(targetFragment.eContainer());
					}
					else
					{
						index = targetBrotherContainers.indexOf(targetFragment);

					}
					if (sourceIndex < targetIndex)
					{ // one position of the removed source
						index++;
					}
					GContainer copy = EcoreUtil.copy(mergedSource);
					copy.gSetDefinition(sourceDefinition);
					targetBrotherContainers.add(index, copy);

				}

				else
				{
					// remove first fragment from initial source
					EList<GContainer> sourceBrotherContainers = getBrotherContainers(firstSourceFragment);
					int indexOf = sourceBrotherContainers.indexOf(firstSourceFragment);
					sourceBrotherContainers.remove(indexOf);

					// add it to the target index
					int index;
					if (hasChoiceContainerParent)
					{
						index = targetBrotherContainers.indexOf(targetFragment.eContainer());
					}
					else
					{
						index = targetBrotherContainers.indexOf(targetFragment);

					}
					if (sourceIndex < targetIndex)
					{ // one position of the removed source
						index++;
					}

					targetBrotherContainers.add(index, firstSourceFragment);
				}
			}

			/**
			 * @param targetIndex
			 * @param sourceIndex
			 * @param firstSourceFragment
			 */
			@Requirement(
				reqID = "74680")
			private void handleMoveContainerWithChoiceParent(@SuppressWarnings("hiding") final int targetIndex,
				@SuppressWarnings("hiding") final int sourceIndex, GContainer firstSourceFragment)
			{
				EList<? super GContainer> targetList = multiParentList.get(targetIndex);

				// move the first fragment of the source at the position of the first fragment of the target
				GContainer targetFragment = (GContainer) targetList.get(0);
				EObject parentTargetObject = targetFragment.eContainer();
				GContainer parentChoiceContainerSource = (GContainer) firstSourceFragment.eContainer();
				GContainerDef parentDefinition = parentChoiceContainerSource.gGetDefinition();
				GContainerDef sourceDefinition = firstSourceFragment.gGetDefinition();
				// delete first fragment from initial parent
				EList<GContainer> sourceBrotherContainers = getBrotherContainers(firstSourceFragment);
				// also delete choice container parent
				GContainer mergedParentChoiceContainerSource = SplitUtils.getMergedInstance(parentChoiceContainerSource);
				boolean hasOtherChildren = hasOtherChildren(parentChoiceContainerSource, firstSourceFragment);

				// if the choice parent has other children create a copy for him and move the copy
				GContainer parentChoiceContainerToMove = parentChoiceContainerSource;
				GContainer mergedParentChoiceContainerSourceToMove = mergedParentChoiceContainerSource;

				if (hasOtherChildren)
				{
					parentChoiceContainerToMove = EcoreUtil.copy(parentChoiceContainerSource);
					mergedParentChoiceContainerSourceToMove = EcoreUtil.copy(mergedParentChoiceContainerSource);
				}

				// check if in the target list there is already a fragment of the source container
				GContainer mergedSource = SplitUtils.getMergedInstance(firstSourceFragment);
				Collection<GContainer> concreteSourceInstances = SplitUtils.getConcreteInstances(mergedSource);
				concreteSourceInstances.remove(firstSourceFragment);
				Collection<GContainer> concreteParentSourceInstances = SplitUtils.getConcreteInstances(mergedParentChoiceContainerSource);
				boolean contains = false;
				boolean hasChoiceContainerParent = hasChoiceContainerParent(targetFragment);
				EList<GContainer> targetBrotherContainers;
				if (hasChoiceContainerParent)
				{
					targetFragment = (GContainer) targetFragment.eContainer();

				}
				targetBrotherContainers = getBrotherContainers(targetFragment);

				for (GContainer contParent: concreteParentSourceInstances)
				{ // test if target contains parent choice
					if (targetBrotherContainers.contains(contParent))
					{
						// test if parent choice contains a source fragment
						EList<GContainer> subContainers = contParent.gGetSubContainers();
						List<GContainer> aux = new ArrayList<GContainer>();
						aux.addAll(subContainers);
						aux.retainAll(concreteSourceInstances);

						if (aux.size() > 0)
						{
							contains = true;
							break;
						}
					}

				}
				if (contains)
				{

					// if in the target list there is already a fragment of the source container, merge the
					// container and add it to the target index

					// remove instances
					concreteSourceInstances.add(firstSourceFragment);
					ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
					for (GContainer cont: concreteSourceInstances)
					{
						EObject parentContainer = cont.eContainer();
						if (parentContainer != null)
						{
							EList<GContainer> brotherContainers = getBrotherContainers(cont);
							brotherContainers.remove(cont);
							// also remove choice container parent
							removeContainer((GContainer) parentContainer, false);
						}
					}

					if (!compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
					{
						// if it is a 3x project in compatibility check if the target parent is the same as the
						// source
						if (hasChoiceContainerParent)
						{
							GContainer parentTargetContainer = (GContainer) parentTargetObject;
							GContainerDef parentChoiceDefinition = parentTargetContainer.gGetDefinition();
							boolean equalDefinition = (parentTargetContainer.gGetDefinition() == parentChoiceDefinition);
							boolean equalShortName = parentTargetContainer.gGetShortName().equals(
								parentChoiceContainerSource.gGetShortName());
							if (equalShortName && equalDefinition)
							{
								parentTargetContainer.gGetSubContainers().add(0, mergedSource);
								// targetBrotherContainers.add(index, mergedSource);
								return;
							}

						}
					}

					GContainer mergedChoice = EcoreUtil.copy(mergedParentChoiceContainerSourceToMove);
					mergedChoice.gSetDefinition(parentDefinition);
					EList<GContainer> subContainers = mergedChoice.gGetSubContainers();

					int size = subContainers.size();
					for (int i = 0; i < size; i++)
					{
						subContainers.remove(0);
					}
					GContainer merge = EcoreUtil.copy(mergedSource);
					merge.gSetDefinition(sourceDefinition);
					subContainers.add(merge);
					targetBrotherContainers = getBrotherContainers(targetFragment);
					int index = targetBrotherContainers.indexOf(targetFragment);
					if ((sourceIndex < targetIndex))
					{ // one position of the removed source
						index++;
					}
					targetBrotherContainers.add(index, mergedChoice);

				}

				else
				{ // if in the target list there is no fragment of the source container

					int index = targetBrotherContainers.indexOf(targetFragment);
					boolean notLast = (index < targetBrotherContainers.size() - 1);
					if (sourceIndex < targetIndex)
					{ // one position of the removed source
						index++;
					}

					if (hasOtherChildren)
					{
						// remove first fragment from initial source
						int indexOf = sourceBrotherContainers.indexOf(firstSourceFragment);
						sourceBrotherContainers.remove(indexOf);
						// in the copy parent add the source fragment
						parentChoiceContainerToMove.gSetDefinition(parentDefinition);
						GContainer gContainer = parentChoiceContainerToMove.gGetSubContainers().get(indexOf);

						parentChoiceContainerToMove.gGetSubContainers().retainAll(Arrays.asList(gContainer));
					}
					if (targetBrotherContainers.contains(parentChoiceContainerToMove))
					{
						targetBrotherContainers.remove(parentChoiceContainerToMove);
					}
					if (notLast)
					{
						targetBrotherContainers.add(index, parentChoiceContainerToMove);
					}
					else
					{
						targetBrotherContainers.add(parentChoiceContainerToMove);
					}
				}
			}

			/**
			 * Get all brother containers, including himself
			 *
			 * @param container
			 * @return
			 */
			private EList<GContainer> getBrotherContainers(GContainer container)
			{
				EList<GContainer> containers;
				EObject eParent = container.eContainer();

				if (eParent instanceof GModuleConfiguration)
				{
					GModuleConfiguration configurationParent = (GModuleConfiguration) eParent;
					containers = configurationParent.gGetContainers();

				}
				else
				{

					GContainer parentContainer = (GContainer) eParent;
					containers = parentContainer.gGetSubContainers();

				}
				return containers;
			}
		});
	}

	/**
	 * Get the real index considering the split containers from the <@code multiParentList>. Can be used for containers
	 * with {@link GChoiceContainerDef} parent definitions. The containers from the given list are split only if they
	 * also have the same parent (it could be different because of the choice container parent)
	 *
	 * @param multiParentsList
	 * @param index
	 * @return
	 */
	private int findIndexInSplitListForChoiceContainerParent(
		DelegatingWithSourceMultiEList<GContainer> multiParentsList, int index)
	{
		Map<String, Integer> splitListDimensions = new LinkedHashMap<String, Integer>();

		// create map based on qualified name. Computing the shortName is enough as the parent
		// is the same (no choice
		// container parent) and definition is the same
		for (GContainer gContainer: multiParentsList)
		{
			String qualifiedName = MetaModelUtils.getAbsoluteQualifiedName(gContainer);
			boolean containsKey = splitListDimensions.containsKey(qualifiedName);
			if (containsKey)
			{
				Integer splitObjects = splitListDimensions.get(qualifiedName);
				splitListDimensions.put(qualifiedName, splitObjects + 1);
			}
			else
			{
				splitListDimensions.put(qualifiedName, 1);
			}
		}

		// find real index
		Collection<Integer> values = splitListDimensions.values();
		int realIndex = 0;
		Iterator<Integer> iterator = values.iterator();
		for (int i = 0; i < index; i++)
		{
			Integer next = iterator.next();
			realIndex = realIndex + next;
		}
		return realIndex;
	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper#add(int, java.lang.Object)
 */
	@Requirement(
		reqID = "26951")
	public void add(final int index, Object value)
	{
		if (!(value instanceof EMFProxyObjectImpl))
		{
			throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + value); //$NON-NLS-1$
		}
		boolean splitContainer = isSplitContainer();
		EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) value;
		IMasterObjectWrapper<?> wrapper = childrenProxy.eGetMasterWrapper();
		List<?> childrenMasters = wrapper.getAllMasterObjects();
		if (childrenMasters.size() > 1)
		{
			throw new PMRuntimeException(
				eu.cessar.ct.runtime.ecuc.pmproxy.wrap.Messages.cannotChangeSplitFeatureException);
		}
		final GContainer container = (GContainer) childrenMasters.get(0);

		final GContainerDef[] newTargetDef = new GContainerDef[1];
		final IEcucModel[] ecucModel = new IEcucModel[1];
		final GModuleDef[] targetModuleDef = new GModuleDef[1];

		if (PMProxyUtils.haveRefinementSupport(engine))
		{
			ecucModel[0] = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
			targetModuleDef[0] = ecucModel[0].getModuleDef(definition);
			if (targetModuleDef[0] != ecucModel[0].getModuleDef(container.gGetDefinition()))
			{
				// the definition need to be changed
				newTargetDef[0] = ecucModel[0].getRefinedContainerDefFamily(targetModuleDef[0],
					container.gGetDefinition());
			}
		}
		if (!splitContainer)
		{ // behavior non split containers

			ESplitableList<GContainer> subCnts;
			if (definition instanceof GChoiceContainerDef)
			{
				subCnts = new ChoiceContainerSubEList((GChoiceContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			else
			{
				subCnts = new ParamConfContainerSubEList((GParamConfContainerDef) definition, singularParentList, null,
					changeProvider);
			}
			final ESplitableList<GContainer> sub = subCnts;
			// the sub size and the splitedContainers size should match
			// if (sub.size() != splitedContainers.size())
			// {
			//			throw new PMRuntimeException("Internal error"); //$NON-NLS-1$
			// }
			updateModel(new Runnable()
			{
				public void run()
				{
					if (newTargetDef[0] != null)
					{
						// change the definition
						// container.gSetDefinition(newTargetDef[0]);
						if (ecucModel[0] != null)
						{
							ecucModel[0].recursiveDefUpdate(container, targetModuleDef[0]);
						}
					}
					addMultiContainer(sub, singularParentList, container, index);
				}
			});
			lastAddedOrSet = container;
			// update both objects
			getEngine().updateSlaveFeature(this);
			getEngine().updateSlave(getContext(), childrenProxy, container);
		}
		else
		{ // behavior split containers

			updateModel(new Runnable()
			{
				@SuppressWarnings("unchecked")
				public void run()
				{
					if (newTargetDef[0] != null)
					{
						if (ecucModel[0] != null)
						{
							ecucModel[0].recursiveDefUpdate(container, targetModuleDef[0]);
						}
					}

					if (multiParentList.size() == 0)
					{ // this is the case when not brother containers exist
						addMultiContainerForSplitableNoChildren(container, parentNoChildrenList);
					}
					else
					{// create multiParentList
						@SuppressWarnings("rawtypes")
						DelegatingWithSourceMultiEList multiEList;
						if (definition instanceof GChoiceContainerDef)
						{
							multiEList = new ChoiceContainerMultiEList((GChoiceContainerDef) definition,
								multiParentList, changeProvider);
						}
						else
						{
							multiEList = new ParamConfContainerMultiEList((GParamConfContainerDef) definition,
								multiParentList, changeProvider);
						}
						// multiEList.setReadOnly(false);
						addMultiContainerForSplitable(multiEList, container, index);
						// multiEList.setReadOnly(true);
					}
				}
			});
			lastAddedOrSet = container;
			// update both objects
			getEngine().updateSlaveFeature(this);
			getEngine().updateSlave(getContext(), childrenProxy, container);
		}
	}

	/**
	 * Returns true if given parent container has other children except the given @param child, false otherwise
	 *
	 * @param container
	 */
	private boolean hasOtherChildren(GContainer parent, GContainer child)
	{
		EList<GContainer> subContainers = parent.gGetSubContainers();
		int size = subContainers.size();
		if (size != 0)
		{ // remove given container only if it has no children
			if (size == 1)
			{
				GContainer cont = subContainers.get(0);
				if (cont == child)
				{
					return false;
				}

			}
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Remove given container if it has no children, or if it has children remove it only if evenIfHasChildren is true
	 *
	 * @param container
	 */
	private void removeContainer(GContainer container, boolean evenIfHasChildren)
	{
		EList<GContainer> subContainers = container.gGetSubContainers();

		if (subContainers.size() != 0)
		{ // remove given container only if it has no children
			if (!evenIfHasChildren)
			{
				return;
			}
		}
		EObject parentObject = container.eContainer();
		if (parentObject instanceof GContainer)
		{
			GContainer parentContainer = (GContainer) parentObject;
			EList<GContainer> parentSubContainers = parentContainer.gGetSubContainers();
			parentSubContainers.remove(container);
			return;
		}
		if (parentObject instanceof GModuleConfiguration)
		{
			GModuleConfiguration parentConfiguration = (GModuleConfiguration) parentObject;
			EList<GContainer> containers = parentConfiguration.gGetContainers();
			containers.remove(container);
		}
		return;
	}

	// /**
	// * @param sources
	// */
	// @SuppressWarnings("rawtypes")
	// private void setReadOnlyForContainers(boolean readOnly)
	// {
	// for (ESplitableList<GContainer> containers: splitedContainers)
	// {
	// // for split containers
	// if (containers instanceof MultiESplitableList)
	// {
	// ((MultiESplitableList) containers).setReadOnly(readOnly);
	// }
	// }
	// }

	private boolean isSplitContainer()
	{
		if (singularParentList == null)
		{
			return true;
		}
		return false;
	}

}
