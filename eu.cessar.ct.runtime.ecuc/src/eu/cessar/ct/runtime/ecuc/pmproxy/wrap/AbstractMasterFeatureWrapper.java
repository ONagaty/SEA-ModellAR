/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 17, 2010 2:48:56 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.NotifyingList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceSubEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6458
 * @param <T>
 *
 */
public abstract class AbstractMasterFeatureWrapper<T> extends AbstractWrapper implements IMasterFeatureWrapper<T>
{

	private EStructuralFeature feature;

	/**
	 * @param engine
	 */
	public AbstractMasterFeatureWrapper(IEMFProxyEngine engine)
	{
		super(engine);
	}

	/**
	 * @param feature
	 */
	public void setWrappedFeature(EStructuralFeature wrappedFeature)
	{
		this.feature = wrappedFeature;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("javadoc")
	public EStructuralFeature getWrappedFeature()
	{
		return feature;
	}

	/**
	 * @param sub
	 * @param singularParentList
	 * @param container
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	protected void setMultiContainer(ESplitableList<GContainer> sub, EList<GContainer> singularParentList,
		GContainer container, int index)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(container).getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				setToIntermediateLevel(sub, singularParentList, container, index);
			}
			else
			{
				// if it is a 3x project in compatibility
				sub.set(index, container);
			}
		}
		else
		{
			sub.set(index, container);
		}

	}

	/**
	 * @param sub
	 * @param multiParentList
	 * @param container
	 * @param index
	 */
	protected void setMultiContainerForSplitable(DelegatingWithSourceMultiEList<GContainer> multiParentList,
		GContainer container, int index)
	{
		@SuppressWarnings("deprecation")
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(container).getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				setToIntermediateLevelForSplitable(multiParentList, container, index);

			}
			else
			{
				// if it is a 3x project in compatibility
				multiParentList.set(index, container);
			}
		}
		else
		{

			multiParentList.set(index, container);
		}

	}

	/**
	 * @param singularParentList
	 * @param sub
	 * @param container
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	private void setToIntermediateLevel(ESplitableList<GContainer> sub, EList<GContainer> singularParentList,
		GContainer container, int index)
	{
		// TODO Auto-generated method stub
		if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
		{
			GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();
			boolean aux = true;
			for (GContainer singParent: singularParentList)
			{
				if (singParent.gGetDefinition().equals(choiceDef))
				{
					singParent.gGetSubContainers().set(index, container);
					aux = false;
					break;
				}
			}

			if (aux)
			{
				IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
				GContainer intermediateContainer = ecucGenericFactory.createContainer();
				intermediateContainer.gSetShortName(choiceDef.gGetShortName());
				intermediateContainer.gSetDefinition(choiceDef);
				EList<GContainer> subContainers = intermediateContainer.gGetSubContainers();
				subContainers.add(container);

				sub.set(index, intermediateContainer);
			}
		}
		else
		{
			sub.set(index, container);
		}
	}

	/**
	 * This method sets the given container to the specified position. </p> If the specified container has a choice
	 * container parent, then: </p> 1. if the container currently at the index position (which will be replaced) has a
	 * choice container parent, then the new container will keep the same choice container parent. </p>2. Else, a new
	 * choice container shall be created with an unique name in its parent.
	 *
	 * @param multiParentList
	 * @param sub
	 * @param container
	 * @param index
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void setToIntermediateLevelForSplitable(DelegatingWithSourceMultiEList<GContainer> multiParentList,
		GContainer container, int index)
	{
		if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
		{ // has parent choice container

			// create choice container parent
			GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();
			GContainer intermediateContainer = createIntermediateContainer(container, choiceDef);

			List<EList<? super GContainer>> parentELists = multiParentList.getParentELists();

			EList<? super GContainer> eList = parentELists.get(index);
			String shortName = ((GContainer) eList.get(0)).gGetShortName();

			if (eList instanceof DelegatingWithSourceSubEList<?>)
			{ // get the actual list to set
				EList parentList = ((DelegatingWithSourceSubEList) eList).getParentList();

				// find the container to replace
				GContainer toReplace = null;
				for (Object object: parentList)
				{
					if (((GContainer) object).gGetShortName().equals(shortName))
					{
						toReplace = (GContainer) object;
						break;
					}
				}

				int indexToSet = parentList.indexOf(toReplace);

				boolean needsAux = needsAux(toReplace);
				if (needsAux)
				{ // the container needs a parent choice container
					boolean hasChoiceContainerParent = hasChoiceContainerParent(toReplace);
					EObject parent = getConcreteParent(toReplace, hasChoiceContainerParent);
					String shortNameForParent = getShortNameForParent(parent, choiceDef);
					intermediateContainer.gSetShortName(shortNameForParent);

					parentList.set(indexToSet, intermediateContainer);
				}
				else
				{ // the existing container has a parent choice container and it will be reused
					parentList.set(indexToSet, container);
				}

			}
			else if (eList instanceof DelegatingWithSourceMultiEList<?>)
			{
				List parentList = ((DelegatingWithSourceMultiEList) eList).getParentELists();

				GContainer toBeRemoved = null;
				for (int i = 0; i < parentList.size(); i++)
				{
					List list = (List) parentList.get(i);
					// find the container to replace
					for (Object object2: list)
					{
						if (((GContainer) object2).gGetShortName().equals(shortName))
						{
							toBeRemoved = (GContainer) object2;
							break;
						}
					}
					int indexOfToBeRemoved = list.indexOf(toBeRemoved);

					if (i == 0)
					{ // only keep the fragment from the first resource
						boolean needsAux = needsAux(toBeRemoved);
						if (needsAux)
						{
							boolean hasChoiceContainerParent = hasChoiceContainerParent(toBeRemoved);
							EObject parent = getConcreteParent(toBeRemoved, hasChoiceContainerParent);
							String shortNameForParent = getShortNameForParent(parent, choiceDef);
							intermediateContainer.gSetShortName(shortNameForParent);

							list.set(indexOfToBeRemoved, intermediateContainer);
						}
						else
						{
							list.set(indexOfToBeRemoved, container);
						}
					}
					else
					{ // remove fragments from other resources
						boolean hasChoiceContainerParent = hasChoiceContainerParent(toBeRemoved);
						EObject parentObject = toBeRemoved.eContainer();
						list.remove(toBeRemoved);

						// also remove their parent if it is a choice container and has only this child
						if (hasChoiceContainerParent)
						{
							removeContainer((GContainer) parentObject);
						}
					}

				}

			}

		}
		else
		{
			Map<String, List<GContainer>> splitListElements = new LinkedHashMap<String, List<GContainer>>();
			// Map<String, Integer> splitListDimensions = new LinkedHashMap<String, Integer>();
			// create map based on qualified name. Computing the shortName is enough as the parent
			// is the same (no choice
			// container parent) and definition is the same
			for (GContainer gContainer: multiParentList)
			{
				String shortName = gContainer.gGetShortName();
				boolean containsKey = splitListElements.containsKey(shortName);
				if (containsKey)
				{
					List<GContainer> splitObjects = splitListElements.get(shortName);
					splitObjects.add(gContainer);
					// splitListElements.put(shortName, splitObjects + 1);
				}
				else
				{
					ArrayList<GContainer> arrayList = new ArrayList<GContainer>();
					arrayList.add(gContainer);
					splitListElements.put(shortName, arrayList);
				}
			}

			// find real index
			Collection<List<GContainer>> values = splitListElements.values();
			int realIndex = 0;
			List<GContainer> splitObjectListFromIndex;
			Iterator<List<GContainer>> iterator = values.iterator();
			for (int i = 0; i < index; i++)
			{
				List<GContainer> nextList = iterator.next();
				realIndex = realIndex + nextList.size();
			}

			splitObjectListFromIndex = values.iterator().next();
			// if there are more split fragments, only one the first one is replaced, the rest are deleted
			if (splitObjectListFromIndex.size() > 1)
			{
				// first fragments is replaced, not deleted
				splitObjectListFromIndex.remove(0);
				for (GContainer gContainer: splitObjectListFromIndex)
				{
					multiParentList.remove(gContainer);
				}
			}

			multiParentList.set(realIndex, container);

		}
	}

	/**
	 * Get the real index considering the split containers from the <@code multiParentList>. ONly used for containers
	 * with no {@link GChoiceContainerDef} parent definitions.
	 *
	 * @param multiParentList
	 * @param index
	 * @return
	 */
	private int findIndexInSplitList(DelegatingWithSourceMultiEList<GContainer> multiParentList, int index)
	{
		Map<String, Integer> splitListDimensions = new LinkedHashMap<String, Integer>();
		// create map based on qualified name. Computing the shortName is enough as the parent
		// is the same (no choice
		// container parent) and definition is the same
		for (GContainer gContainer: multiParentList)
		{
			String shortName = gContainer.gGetShortName();
			boolean containsKey = splitListDimensions.containsKey(shortName);
			if (containsKey)
			{
				Integer splitObjects = splitListDimensions.get(shortName);
				splitListDimensions.put(shortName, splitObjects + 1);
			}
			else
			{
				splitListDimensions.put(shortName, 1);
			}
		}

		// find real index
		Collection<Integer> values = splitListDimensions.values();
		int realIndex = 0;
		Iterator<Integer> iterator = values.iterator();
		for (int i = 0; i < index; i++)
		{ // it shall always have a next
			if (iterator.hasNext())
			{
				Integer next = iterator.next();
				realIndex = realIndex + next;
			}
			else
			{
				break;
			}
		}
		return realIndex;
	}

	/**
	 * Get the real index considering the split containers from the <@code multiParentList>. Can be used for containers
	 * with {@link GChoiceContainerDef} parent definitions. The containers from the given list are split only if they
	 * also have the same parent (it could be different because of the choice container parent)
	 *
	 * @param multiParentList
	 * @param index
	 * @return
	 */
	private int findIndexInSplitListForChoiceContainerParent(
		DelegatingWithSourceMultiEList<GContainer> multiParentList, int index)
	{
		Map<String, Integer> splitListDimensions = new LinkedHashMap<String, Integer>();

		// create map based on qualified name. Computing the shortName is enough as the parent
		// is the same (no choice
		// container parent) and definition is the same
		for (GContainer gContainer: multiParentList)
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

	/**
	 * @param container
	 * @param choiceDef
	 * @return
	 */
	private GContainer createIntermediateContainer(GContainer container, GChoiceContainerDef choiceDef)
	{
		@SuppressWarnings("deprecation")
		IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
		GContainer intermediateContainer = ecucGenericFactory.createContainer();
		intermediateContainer.gSetDefinition(choiceDef);
		EList<GContainer> subContainers = intermediateContainer.gGetSubContainers();
		subContainers.add(container);
		return intermediateContainer;
	}

	/**
	 *
	 * @param container
	 * @return
	 */
	private boolean needsAux(GContainer container)
	{
		boolean needsAux = true;
		EObject parent = container.eContainer();
		if (parent instanceof GContainer)
		{
			if (((GContainer) parent).gGetDefinition() instanceof GChoiceContainerDef)
			{
				needsAux = false;
			}
		}
		return needsAux;
	}

	/**
	 *
	 * @param container
	 * @return true if the parent is a choice container, else returns false
	 */
	public boolean hasChoiceContainerParent(GContainer container)
	{
		EObject parentObject = container.eContainer();
		if (!(parentObject instanceof GContainer))
		{
			return false;
		}
		GContainer parentContainer = (GContainer) parentObject;
		GContainerDef parentDefinition = parentContainer.gGetDefinition();
		if (parentDefinition instanceof GChoiceContainerDef)
		{
			return true;
		}
		return false;

	}

	/**
	 * Remove given container only if it has no children
	 *
	 * @param container
	 */
	private void removeContainer(GContainer container)
	{
		EList<GContainer> subContainers = container.gGetSubContainers();

		if (subContainers.size() != 0)
		{ // remove given container only if it has no children
			return;
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
	}

	/**
	 * @param sub
	 * @param singularParentList
	 * @param container
	 * @param index
	 */
	@SuppressWarnings("deprecation")
	protected void addMultiContainer(ESplitableList<GContainer> sub, EList<GContainer> singularParentList,
		GContainer container, int index)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(container);
		IEcucMMService ecucMMService = mmService.getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				addToIntermediateLevel(mmService, sub, singularParentList, container, index);
			}
			else
			{
				// if it is a 3x project in compatibility
				sub.add(index, container);
			}
		}
		else
		{
			sub.add(index, container);
		}

	}

	/**
	 * @param multiParentList
	 * @param sub
	 * @param singularParentList
	 * @param container
	 * @param index
	 */
	@Requirement(
		reqID = "52872")
	protected void addMultiContainerForSplitable(DelegatingWithSourceMultiEList<GContainer> multiParentList,
		GContainer container, int index)
	{
		@SuppressWarnings("deprecation")
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(container);
		IEcucMMService ecucMMService = mmService.getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				addToIntermediateLevelForSplitable(mmService, multiParentList, container, index);
			}
			else
			{
				// if it is a 3x project in compatibility
				multiParentList.add(index, container);
			}
		}
		else
		{
			multiParentList.add(index, container);
		}

	}

	/**
	 * @param container
	 * @param parents
	 */
	protected void addMultiContainerForSplitableNoChildren(GContainer container, List<GIdentifiable> parents)
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

		@SuppressWarnings("deprecation")
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(container);
		IEcucMMService ecucMMService = mmService.getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(getEngine().getProject());
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
				{
					GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();
					GContainer intermediateContainer = createIntermediateContainer(container, choiceDef);
					intermediateContainer.gSetShortName(choiceDef.gGetShortName());
					subContainers.add(intermediateContainer);
				}
				else
				{
					// if it is a 3x project in compatibility
					subContainers.add(container);
				}

			}
			else
			{
				// if it is a 3x project in compatibility
				subContainers.add(container);
			}
		}
		else
		{
			subContainers.add(container);
		}

	}

	/**
	 * @param mmService
	 * @param sub
	 *        is a list that contains all the instances of a container definition
	 * @param singularParentList
	 *        is the list of children the parent of the container has
	 * @param container
	 * @param index
	 */
	private void addToIntermediateLevel(IMetaModelService mmService, ESplitableList<GContainer> sub,
		EList<GContainer> singularParentList, GContainer container, int index)
	{
		if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
		{
			boolean aux = true;
			NotifyingList<GContainer> singularParentL = (NotifyingList<GContainer>) singularParentList;
			GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();
			if (mmService.getAutosarReleaseOrdinal() != 4)
			{
				for (GContainer singParent: singularParentList)
				{
					if (singParent.gGetDefinition().equals(choiceDef))
					{
						singParent.gGetSubContainers().add(index, container);
						aux = false;
						break;
					}
				}
			}
			if (aux)
			{
				@SuppressWarnings("deprecation")
				IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
				GContainer intermediateContainer = ecucGenericFactory.createContainer();

				String shortName = MetaModelUtils.computeUniqueChildShortName((EObject) singularParentL.getNotifier(),
					choiceDef.gGetShortName());

				intermediateContainer.gSetShortName(shortName);
				intermediateContainer.gSetDefinition(choiceDef);
				EList<GContainer> subContainers = intermediateContainer.gGetSubContainers();
				subContainers.add(container);

				sub.add(intermediateContainer);
				singularParentList.move(index, intermediateContainer);

			}
		}
		else
		{
			sub.add(index, container);
		}

	}

	/**
	 * @param mmService
	 * @param container
	 * @param index
	 */
	private void addToIntermediateLevelForSplitable(IMetaModelService mmService,
		DelegatingWithSourceMultiEList<GContainer> multiParentList, GContainer container, int index)
	{
		boolean addAtTheEndOfTheList = false;

		if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
		{

			// different behavior in case we add at the end of the list

			int realIndex = findIndexInSplitListForChoiceContainerParent(multiParentList, index);
			if (realIndex == multiParentList.size())
			{
				// this is the case where the new element is added at the end of the list
				addAtTheEndOfTheList = true;
			}
			GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();

			GContainer intermediateContainer = createIntermediateContainer(container, choiceDef);

			EObject parent = null;

			// different behavior in case we add at the end of the list
			GContainer atIndexContainer;
			if (addAtTheEndOfTheList)
			{
				// we consider the last element from the list to obtain the necessary information
				atIndexContainer = multiParentList.get(multiParentList.size() - 1);
			}
			else
			{
				atIndexContainer = multiParentList.get(realIndex);
			}

			// compute the short name
			boolean hasChoiceContainerParent = hasChoiceContainerParent(atIndexContainer);
			EObject directParent = atIndexContainer.eContainer();
			parent = directParent;
			// the choice container parent is not considered
			if (hasChoiceContainerParent)
			{
				parent = parent.eContainer();

			}
			EObject mergedParent = getMergedParentInstance(parent);

			String shortName = MetaModelUtils.computeUniqueChildShortName(mergedParent, choiceDef.gGetShortName());

			intermediateContainer.gSetShortName(shortName);

			// get the resource specific list to add the new container
			EList<GContainer> subContainers = new BasicEList<GContainer>();
			if (parent instanceof GContainer)
			{
				GContainer parentContainer = (GContainer) parent;
				subContainers = parentContainer.gGetSubContainers();

			}
			else
			{
				// can this check be deleted?
				if (parent instanceof GModuleConfiguration)
				{
					GModuleConfiguration parentConfiguration = (GModuleConfiguration) parent;
					subContainers = parentConfiguration.gGetContainers();
				}
			}

			// find the new resource specific index for current container at index position in the merged list
			if (!addAtTheEndOfTheList)
			{
				int resourceIndex = -1;
				String searchedShortName = atIndexContainer.gGetShortName();
				if (hasChoiceContainerParent)
				{
					searchedShortName = ((GContainer) directParent).gGetShortName();
				}
				GContainer contForError = null;
				for (GContainer cont: subContainers)
				{
					if (cont.gGetShortName().equals(searchedShortName))
					{
						resourceIndex = subContainers.indexOf(cont);
						contForError = cont;
						break;
					}
				}
				if (resourceIndex == -1)
				{
					// throw exception.
					String containerName = (contForError != null) ? contForError.gGetShortName() : ""; //$NON-NLS-1$
					String text = NLS.bind(Messages.COULD_NOT_SET_CONTAINER, new Object[] {containerName});
					throw new PMRuntimeException(text);
				}
				subContainers.add(resourceIndex, intermediateContainer);
			}
			else
			{
				subContainers.add(intermediateContainer);
			}
		}
		else
		{
			int realIndex = findIndexInSplitList(multiParentList, index);
			if (realIndex == multiParentList.size())
			{
				// this is the case where the new element is added at the end of the list
				addAtTheEndOfTheList = true;
			}
			if (addAtTheEndOfTheList)
			{
				// this is the case where the new element is added at the end of the list
				multiParentList.add(container);
			}
			else
			{
				multiParentList.add(realIndex, container);
			}
		}

	}

	/**
	 * @param parent
	 * @return
	 */
	private EObject getMergedParentInstance(EObject parent)
	{
		EObject mergedParent;
		if (parent instanceof GContainer)
		{
			mergedParent = SplitUtils.getMergedInstance((GContainer) parent);
		}
		else
		{
			mergedParent = SplitUtils.getMergedInstance((GModuleConfiguration) parent);

		}
		return mergedParent;
	}

	/**
	 * Computes the short name for a container having the definition {@code choiceParentDef}
	 *
	 * @param container
	 * @param choiceParentDef
	 * @param hasChoiceContainerParent
	 * @return
	 */
	private String getShortNameForParent(EObject parent, GChoiceContainerDef choiceParentDef)
	{
		EObject mergedParent = getMergedParentInstance(parent);
		String shortName = MetaModelUtils.computeUniqueChildShortName(mergedParent, choiceParentDef.gGetShortName());
		return shortName;
	}

	/**
	 * Returns the parent for the container. If the direct parent is a choice container, then the parent of this choice
	 * container is returned
	 *
	 * @param container
	 * @param hasChoiceContainerParent
	 * @return
	 */
	private EObject getConcreteParent(GContainer container, boolean hasChoiceContainerParent)
	{
		EObject directParent = container.eContainer();
		EObject parent = directParent;
		// the choice container parent is not considered
		if (hasChoiceContainerParent)
		{
			parent = parent.eContainer();
		}
		return parent;

	}

	/**
	 * @param project
	 *        - The project in which the container will be created
	 * @param container
	 *        - The container to create
	 * @return - The newly created container
	 */
	@SuppressWarnings("deprecation")
	protected GContainer createContainer(IProject project, GContainer container)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(container).getEcucMMService();
		if (ecucMMService.needChoiceIntermediateContainer())
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
			if (compatibilityMode.name().equalsIgnoreCase(ECompatibilityMode.NONE.name()))
			{
				return createIntermediateLevel(container);
			}
			else
			{
				// if it is a 3x project in compatibility
				return container;
			}
		}
		else
		{
			return container;
		}
	}

	/**
	 * @param container
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private GContainer createIntermediateLevel(GContainer container)
	{
		if (container.gGetDefinition().eContainer() instanceof GChoiceContainerDef)
		{
			GChoiceContainerDef choiceDef = (GChoiceContainerDef) container.gGetDefinition().eContainer();

			IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
			GContainer intermediateContainer = ecucGenericFactory.createContainer();
			intermediateContainer.gSetShortName(choiceDef.gGetShortName());
			intermediateContainer.gSetDefinition(choiceDef);
			EList<GContainer> subContainers = intermediateContainer.gGetSubContainers();
			subContainers.add(container);
			return intermediateContainer;
		}
		return container;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#haveLiveValues()
	 */
	public boolean haveLiveValues()
	{
		return true;
	}

}
