/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 5:51:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.runtime.ecuc.util.ChoiceContainerMultiEList;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.ParamConfContainerMultiEList;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.req.Requirement;
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
public class SingleGContainerReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private final ESplitableList<GContainer> containers;
	private final GContainerDef definition;
	private final EList<GContainer> singularParentList;
	private IModelChangeStampProvider changeProvider;
	private List<EList<? super GContainer>> multiParentList;
	private List<GIdentifiable> parentNoChildrenList;

	/**
	 * @param engine
	 * @param modules
	 * @param definition
	 * @param singularParentList
	 * @param multiParentList
	 * @param list
	 */
	public SingleGContainerReferenceWrapper(IEMFProxyEngine engine, ESplitableList<GContainer> modules,
		GContainerDef definition, EList<GContainer> singularParentList, List<EList<? super GContainer>> multiParentList)
	{
		super(engine);
		containers = modules;
		this.definition = definition;
		this.singularParentList = singularParentList;
		this.multiParentList = multiParentList;
		changeProvider = IModelChangeMonitor.INSTANCE.getChangeStampProvider(definition);

	}

	/**
	 * @param engine
	 * @param modules
	 * @param definition
	 * @param singularParentList
	 * @param multiParentList
	 * @param list
	 */
	public SingleGContainerReferenceWrapper(IEMFProxyEngine engine, ESplitableList<GContainer> modules,
		GContainerDef definition, EList<GContainer> singularParentList,
		List<EList<? super GContainer>> multiParentList, List<GIdentifiable> parentNoChildrenList)
	{
		this(engine, modules, definition, singularParentList, multiParentList);
		this.parentNoChildrenList = parentNoChildrenList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (!isSetValue())
		{
			return null;
		}
		GContainer cont = containers.get(0);
		return getEngine().getSlaveObject(getContext(), cont);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return containers != null && containers.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@Requirement(
		reqID = "26951")
	// Also covers this requirements: @Requirement(
	// reqID = "74684")
	// @Requirement(
	// reqID = "74691")
	public void setValue(Object newValue)
	{
		if (newValue == null || newValue == EStructuralFeature.Internal.DynamicValueHolder.NIL)
		{
			unsetValue();
		}

		else
		{

			if (!(newValue instanceof EMFProxyObjectImpl))
			{
				throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
			}
			EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) newValue;
			IMasterObjectWrapper<?> wrapper = childrenProxy.eGetMasterWrapper();
			List<?> childrenMasters = wrapper.getAllMasterObjects();
			if (childrenMasters.size() > 1)
			{
				throw new PMRuntimeException(Messages.cannotChangeSplitFeatureException);
			}
			final GContainer container = (GContainer) childrenMasters.get(0);

			final GContainerDef[] newTargetDef = new GContainerDef[1];

			final IEcucModel ecucModel[] = new IEcucModel[1];
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

			if (containers.size() > 1 || containers.isSplited())
			{
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

						DelegatingWithSourceMultiEList<GContainer> multiEList;
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

						if (containers.size() > 1)
						{
							// delete choice parent, if exists
							List<GContainer> parents = new ArrayList<GContainer>();
							for (int i = 1; i < multiEList.size(); i++)
							{
								GContainer gContainer = multiEList.get(i);
								EObject eParent = gContainer.eContainer();
								if (eParent instanceof GContainer)
								{
									parents.add((GContainer) eParent);
								}
							}

							deleteGChoiceContainerParents(parents);
							multiEList.set(0, container);

							for (int i = 1; i < multiEList.size(); i++)
							{
								multiEList.remove(i);
							}
						}

						else if (containers.isEmpty())
						{
						
							setNoValue(container, parentNoChildrenList);

						}

					}
				});
				getEngine().updateSlaveFeature(this);
				getEngine().updateSlave(getContext(), childrenProxy, container);

			}
			else
			{

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

						// containers.add(container);

						if (isSetValue())
						{
							containers.clear();
							containers.add(container);
						}
						else
						{
							containers.clear();
							containers.add(createContainer(getEngine().getProject(), container));
						}

					}
				});
				getEngine().updateSlaveFeature(this);
				getEngine().updateSlave(getContext(), childrenProxy, container);
			}
		}
	}

	private void setNoValue(GContainer container, List<GIdentifiable> parents)
	{
		// put in first parent. This list will always have at least one element
		GIdentifiable parent = parents.get(0);
		EList<GContainer> subContainers;
		if (parent instanceof GContainer)
		{
			GContainer parentContainer = (GContainer) parent;
			subContainers = parentContainer.gGetSubContainers();
		}
		else
		{
			// parent is a module configuration
			GModuleConfiguration parentConfiguration = (GModuleConfiguration) parent;
			subContainers = parentConfiguration.gGetContainers();
		}

		subContainers.add(createContainer(getEngine().getProject(), container));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	@Requirement(
		reqID = "74697")
	public void unsetValue()
	{
		// store current master object if a value exists
		GContainer container = null;
		EMFProxyObjectImpl value = null;
		if (!containers.isEmpty())
		{
			container = containers.get(0);
			value = (EMFProxyObjectImpl) getValue();
		}

		if (containers.size() > 1 || containers.isSplited() || singularParentList == null)
		{

			updateModel(new Runnable()
			{
				public void run()
				{

					DelegatingWithSourceMultiEList<GContainer> multiEList;
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

					// if a container has a GChoiceContainer parent, delete it
					// collect choice parents
					List<GContainer> parents = new ArrayList<GContainer>();

					for (GContainer gContainer: multiEList)
					{
						EObject eParent = gContainer.eContainer();
						if (eParent instanceof GContainer)
						{
							parents.add((GContainer) eParent);
						}
					}

					deleteGChoiceContainerParents(parents);

					multiEList.clear();
					// for (int i = 0; i < multiEList.size(); i++)
					// {
					// multiEList.remove(i);
					// }

				}
			});
			getEngine().updateSlaveFeature(this);
			// restore the master object if needed
			if (container != null)
			{
				getEngine().updateSlave(getContext(), value, container);
			}
		}

		else
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					// if a container has a GChoiceContainer parent, delete it
					deleteGChoiceContainerParents(singularParentList);

					containers.clear();

				}
			});
			getEngine().updateSlaveFeature(this);
			// restore the master object if needed
			if (container != null)
			{
				getEngine().updateSlave(getContext(), value, container);
			}
		}

	}

	/**
	 * Deletes parents which are GChoiceContainers
	 */
	private void deleteGChoiceContainerParents(List<GContainer> list)
	{

		// collect GChoiceContainer parents and their corresponding parent
		Map<EObject, Set<GContainer>> grandpaAndParents = new HashMap<EObject, Set<GContainer>>();

		for (GContainer parent: list)
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
					GModuleConfiguration grandpaConfig = (GModuleConfiguration) grandpa;
					Set<GContainer> set = grandpaAndParents.get(grandpaConfig);
					if (set == null)
					{
						set = new HashSet<GContainer>();

						grandpaAndParents.put(grandpaConfig, set);
					}
					set.add(containerParent);
				}

			}

		}
		// delete GChoiceContainer parents
		Set<EObject> grandpaSet = grandpaAndParents.keySet();
		for (EObject grandpa: grandpaSet)
		{
			Set<GContainer> set = grandpaAndParents.get(grandpa);
			if (grandpa instanceof GContainer)
			{
				((GContainer) grandpa).gGetSubContainers().removeAll(set);
			}
			else
			{
				((GModuleConfiguration) grandpa).gGetContainers().removeAll(set);
			}
		}
	}
}
