/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Aug 18, 2014 2:06:00 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.platform.util.Pair;
import eu.cessar.ct.sdk.pm.PMInvalidSplitException;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Various utilities for writing in a split config via PM.
 *
 * @author uidu2337
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Thu Mar 26 09:17:50 2015 %
 *
 *         %version: 18 %
 */
public final class SplitPMUtils
{

	/**
	 * Hidden ctor.
	 */
	private SplitPMUtils()
	{

	}

	/**
	 * Clear all parameter values with the given definition from the given split lists.
	 *
	 * @param values
	 *        the list of values
	 * @param definition
	 *        the parameter definition
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void clearValues(final Collection values, final Object definition)
	{
		if (!values.isEmpty())
		{
			if (values instanceof DelegatingWithSourceMultiEList)
			{
				DelegatingWithSourceMultiEList splitValues = (DelegatingWithSourceMultiEList) values;
				// transaction?
				boolean wasReadOnly = splitValues.isReadOnly();
				splitValues.setReadOnly(false);
				List<EList> parentELists = splitValues.getParentELists();
				for (EList parentEList: parentELists)
				{
					List<Object> toDelete = new ArrayList<Object>();
					for (Object val: parentEList)
					{
						Object valDef = getDefinition(val);
						if (valDef.equals(definition))
						{
							toDelete.add(val);
						}
					}
					parentEList.removeAll(toDelete);
				}
				splitValues.setReadOnly(wasReadOnly);
			}
			else
			{

				values.clear();

			}
		}
	}

	/**
	 *
	 * @param definition1
	 * @param definition2
	 * @return if the split objects of two definitions are equal
	 */
	public static boolean isEqualSplitDefinition(GParamConfMultiplicity definition1, GParamConfMultiplicity definition2)
	{

		if (definition1 == null || definition2 == null || definition1.eIsProxy() || definition2.eIsProxy())
		{
			return false;
		}
		return SplitUtils.getMergedInstance(definition1) == SplitUtils.getMergedInstance(definition2);

	}

	/**
	 * Get the definition of a parameter or reference.
	 *
	 * @param o
	 *        the element (parameter or reference)
	 * @return the element's definition
	 */
	private static Object getDefinition(final Object o)
	{
		if (o instanceof GParameterValue)
		{
			return ((GParameterValue) o).gGetDefinition();
		}
		else if (o instanceof GConfigReferenceValue)
		{
			return ((GConfigReferenceValue) o).gGetDefinition();
		}
		else if (o instanceof GContainer)
		{
			return ((GContainer) o).gGetDefinition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Check for multiple values for this parameter.
	 *
	 * @return true <b>iff</b> multiple values are present for this parameter.
	 */
	@SuppressWarnings("rawtypes")
	private static boolean hasMultipleValues(final Collection values)
	{
		return values != null && values.size() > 1;
	}

	/**
	 * Check if two values are the same.
	 *
	 * @param v1
	 *        first value
	 * @param v2
	 *        second value
	 * @return true <b>iff</b> they are the same (either both <code>null</code> or equal)
	 */
	private static boolean sameValue(Object v1, Object v2)
	{
		if (null == v1 && null == v2)
		{
			return true;
		}
		if (null != v1)
		{
			return v1.equals(v2);
		}
		return false;
	}

	/**
	 * Handles the cases of multiple values set for a split parameter with single multiplicity:<br>
	 * - diff values in the fragments from the same/several file(s)<br>
	 * A {@link PMInvalidSplitException} is thrown.
	 */
	@SuppressWarnings({"javadoc", "rawtypes"})
	@Requirement(
		reqID = "14664")
	public static void handleMultipleValues(final Collection values, final String message)
	{
		if (hasMultipleValues(values))
		{
			Iterator<?> iterator = values.iterator();
			Object firstValue = iterator.next();
			boolean sameValue = true;
			while (iterator.hasNext() && sameValue)
			{
				if (!sameValue(firstValue, iterator.next()))
				{
					sameValue = false;
				}
			}
			if (!sameValue)
			{
				if (PMUtils.isSplitChecking())
				{
					throw new PMInvalidSplitException(message);
				}
			}
		}

	}

	/**
	 * Handles the cases of multiple values set for a split instance reference with single multiplicity:<br>
	 * - diff values in the fragments from the same/several file(s)<br>
	 * A {@link PMInvalidSplitException} is thrown.
	 *
	 * @param targets
	 * @param contextsList
	 * @param message
	 */
	@Requirement(
		reqID = "14665")
	public static void handleMultipleInstanceReferencesValues(final List<GIdentifiable> targets,
		List<EList<GIdentifiable>> contextsList, final String message)
	{ // handle targets
		handleMultipleValues(targets, message);
		// handle contexts
		handleMultipleValues(contextsList, message);
	}

	/**
	 * Determine the first list where values are set for element with the given {@code definition}.
	 *
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the searched element's definition
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static EList firstListWhereElementIsSet(final DelegatingWithSourceMultiEList splitValues,
		final Object definition)
	{
		List<EList<?>> parentELists = splitValues.getParentELists();
		for (EList<?> parentList: parentELists)
		{
			for (Object val: parentList)
			{
				Object valDef = getDefinition(val);

				if (valDef.equals(definition))
				{
					return parentList;
				}
			}
		}
		return null;
	}

	/**
	 * Find a parent list and an index relative to it for the given global index and split values list.
	 *
	 * @param globalIndex
	 *        the global index
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the definition of the searched element
	 * @return a {@code Pair} containing the parent list as key and the relative index as value.
	 */
	@SuppressWarnings("rawtypes")
	private static Pair<EList, Integer> findParentListAndIndex(int globalIndex,
		final DelegatingWithSourceMultiEList splitValues, Object definition)
	{
		EList targetList = null;
		int targetIndex = -1;

		int overallIndex = 0;
		List<EList<?>> parentELists = splitValues.getParentELists();
		for (EList<?> parentList: parentELists)
		{
			int listIndex = 0;
			for (Object val: parentList)
			{
				Object valDef = getDefinition(val);

				if (valDef.equals(definition))
				{
					if (overallIndex == globalIndex)
					{
						targetList = parentList;
						targetIndex = listIndex;
						break;
					}
					overallIndex++;
				}
				listIndex++;
			}
			if (null != targetList)
			{
				break;
			}
		}

		if (null != targetList)
		{
			return new Pair<EList, Integer>(targetList, targetIndex);
		}
		return null;
	}

	/**
	 * Determine the last list where values are set for element with the given {@code definition}.
	 *
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the searched element's definition
	 * @return
	 */
	@SuppressWarnings({"unused", "rawtypes"})
	private static EList lastListWhereElementIsSet(final DelegatingWithSourceMultiEList splitValues,
		final Object definition)
	{
		EList lastList = null;
		List<EList<?>> parentELists = splitValues.getParentELists();
		for (EList<?> parentList: parentELists)
		{
			for (Object val: parentList)
			{
				Object valDef = getDefinition(val);

				if (valDef.equals(definition))
				{
					lastList = parentList;
					break;
				}
			}
		}
		return lastList;
	}

	/**
	 * Find the default target list for set/add if element is not set anywhere.
	 *
	 * @param splitValues
	 *        the split values list
	 * @return the default target list
	 */
	@SuppressWarnings("rawtypes")
	private static EList findDefaultTargetListIfNotSet(final DelegatingWithSourceMultiEList splitValues)
	{
		Object targetSource = null;
		List<Object> sources = splitValues.getSources();
		if (!sources.isEmpty())
		{
			targetSource = sources.get(0);
		}

		return null != targetSource ? splitValues.getParentList(targetSource) : null;
	}

	/**
	 * Find the default target list for setting an element (parameter/reference).
	 *
	 * @param splitValues
	 *        the current list of values
	 * @param definition
	 * @param refDef
	 *        the searched element's definition
	 * @return the default target list of the chosen fragment corresponding to <code>refDef</code>
	 */
	@SuppressWarnings("rawtypes")
	public static EList findDefaultTargetListForSet(final DelegatingWithSourceMultiEList splitValues,
		final Object definition)
	{
		EList firstListWhereElementIsSet = firstListWhereElementIsSet(splitValues, definition);
		if (null != firstListWhereElementIsSet)
		{
			return firstListWhereElementIsSet;
		}

		return findDefaultTargetListIfNotSet(splitValues);
	}

	/**
	 * Remove a parameter/reference at position {@code index} in the {@code splitValues} list.
	 *
	 * @param index
	 *        the position of the element to remove
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the definition of the element to be removed
	 */
	@SuppressWarnings("rawtypes")
	public static void remove(final int index, final EList splitValues, final Object definition)
	{
		EList targetList = splitValues;
		int targetIndex = index;

		if (splitValues instanceof DelegatingWithSourceMultiEList)
		{
			Pair<EList, Integer> listIndex = findParentListAndIndex(index, (DelegatingWithSourceMultiEList) splitValues,
				definition);
			if (null != listIndex)
			{
				targetList = listIndex.getKey();
				targetIndex = listIndex.getValue();
			}
			else
			{
				throw new IndexOutOfBoundsException(NLS.bind(Messages.splitPMUtils_SizeIndex,
					getMergedListSize((DelegatingWithSourceMultiEList) splitValues, definition), index));
			}
		}
		targetList.remove(targetIndex);
	}

	/**
	 * Add a parameter/reference to position {@code index} inside the {@code splitValues} list.
	 *
	 * @param index
	 *        the position in which the element is added
	 * @param toAdd
	 * @param splitVals
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the definition of the added element
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <E> void add(final int index, Object toAdd, final EList splitVals, final Object definition)
	{
		if (index < 0)
		{
			throw new IndexOutOfBoundsException(NLS.bind(Messages.splitPMUtils_NegativeIndex, index));
		}
		if (splitVals instanceof DelegatingWithSourceMultiEList)
		{
			DelegatingWithSourceMultiEList splitValues = (DelegatingWithSourceMultiEList) splitVals;
			int overallIndex = 0;
			EList targetList = null;
			// It may be tempting to refactor this using findParentListAndIndex.
			// Beware that they are not equivalent (targetList is getting assigned multiple times here).
			List<EList<? super E>> parentELists = splitValues.getParentELists();
			for (EList<? super E> parentList: parentELists)
			{
				int listIndex = 0;
				for (Object val: parentList)
				{
					Object valDef = getDefinition(val);

					if (valDef.equals(definition))
					{
						targetList = parentList;
						if (overallIndex == index)
						{
							addElement(listIndex, parentList, toAdd, definition);
							return;
						}
						overallIndex++;
					}
					listIndex++;
				}
			}
			if (null == targetList)
			{
				targetList = findDefaultTargetListIfNotSet(splitValues);
			}
			addElement(targetList, toAdd, definition);
		}
		else
		{
			splitVals.add(index, toAdd);
		}
	}

	/**
	 * Add a generic element to a parent list.
	 *
	 * @param targetList
	 *        the target list
	 * @param value
	 *        the value to add
	 */
	@SuppressWarnings("rawtypes")
	private static void addElement(EList targetList, Object value, Object definition)
	{
		addElement(targetList.size(), targetList, value, definition);
	}

	/**
	 * Add an element to a parent list on a specified index.
	 *
	 * @param targetIndex
	 *        the index in the target list
	 * @param targetList
	 *        the target list
	 * @param value
	 *        the value to add
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void addElement(int targetIndex, EList targetList, Object value, Object definition)
	{
		if (definition instanceof GInstanceReferenceDef)
		{
			addInstanceReference(targetIndex, targetList, (GInstanceReferenceValue) value,
				(GInstanceReferenceDef) definition);
		}
		else
		{
			targetList.add(targetIndex, value);
		}
	}

	/**
	 * Add an instance reference value in the target list.
	 *
	 * @param targetList
	 *        the target list
	 * @param toAdd
	 *        the instance reference value to add
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void addInstanceReference(int targetIndex, EList targetList, GInstanceReferenceValue value,
		GInstanceReferenceDef defintion)
	{
		IMetaModelService mmService = null;
		mmService = MMSRegistry.INSTANCE.getMMService(value.gGetDefinition().eClass());
		GInstanceReferenceValue gInstanceReferenceValue = (GInstanceReferenceValue) mmService.getGenericFactory().createReferenceValue(
			defintion);
		targetList.add(targetIndex, gInstanceReferenceValue);
		setInstanceReferenceValue(targetIndex, value, targetList, mmService);
	}

	/**
	 * Set parameter value inside the given list.
	 *
	 * @param index
	 *        the position inside the list
	 * @param value
	 *        the value to set
	 * @param list
	 *        the list of parameter values
	 * @param convertor
	 *        used to set the value
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void setParameterValue(final int index, final Object value, final EList<GParameterValue> list,
		IParameterValueConvertor convertor)
	{
		convertor.setValue(list.get(index), value);
	}

	/**
	 * Set a reference value inside a list.
	 *
	 * @param index
	 *        the position inside the list
	 * @param value
	 *        the reference value
	 * @param list
	 *        the list of reference values
	 */
	public static void setReferenceValue(final int index, final Object value, final EList<GReferenceValue> list)
	{
		list.get(index).gSetValue((GIdentifiable) value);
	}

	/**
	 * Set an instance reference value inside a list.
	 *
	 * @param index
	 *        the position inside the list
	 * @param value
	 *        the reference value
	 * @param list
	 *        the list of reference values
	 * @param mmService
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void setInstanceReferenceValue(final int index, final Object value, final EList list,
		IMetaModelService mmService)
	{
		if (null != mmService)
		{
			GInstanceReferenceValue gInstanceReferenceValue = (GInstanceReferenceValue) list.get(index);
			IEcucMMService ecucMMService = mmService.getEcucMMService();
			EList<GIdentifiable> instanceRefContext = ecucMMService.getInstanceRefContext(
				(GInstanceReferenceValue) value);
			GIdentifiable instanceRefTarget = ecucMMService.getInstanceRefTarget((GInstanceReferenceValue) value);
			ecucMMService.setInstanceRefContext(gInstanceReferenceValue, instanceRefContext);
			ecucMMService.setInstanceRefTarget(gInstanceReferenceValue, instanceRefTarget);
		}
		else
		{
			list.set(index, value);
		}
	}

	/**
	 * Set a parameter/reference at position {@code index} in the {@code splitValues} list.
	 *
	 * @param index
	 *        the position to set
	 * @param value
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the definition of the element to be set
	 * @param convertor
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void set(final int index, final Object value, final EList splitValues, final Object definition,
		IParameterValueConvertor convertor)
	{
		int targetIndex = index;
		EList targetList = splitValues;
		if (splitValues instanceof DelegatingWithSourceMultiEList)
		{
			Pair<EList, Integer> listIndex = findParentListAndIndex(index, (DelegatingWithSourceMultiEList) splitValues,
				definition);
			if (null != listIndex)
			{
				targetList = listIndex.getKey();
				targetIndex = listIndex.getValue();
			}
			else
			{
				throw new IndexOutOfBoundsException(NLS.bind(Messages.splitPMUtils_SizeIndex,
					getMergedListSize((DelegatingWithSourceMultiEList) splitValues, definition), index));
			}
		}
		if (definition instanceof GConfigParameter)
		{
			setParameterValue(targetIndex, value, targetList, convertor);
		}
		else if (definition instanceof GInstanceReferenceDef)
		{
			IMetaModelService mmService = null;
			if (targetList != splitValues)
			{
				mmService = MMSRegistry.INSTANCE.getMMService(((GARObject) definition).eClass());
			}
			setInstanceReferenceValue(targetIndex, value, targetList, mmService);
		}
		else
		{
			setReferenceValue(targetIndex, value, targetList);
		}

	}

	/**
	 * Get the size of the merged list containing elements from the given split values list with the given definition
	 *
	 * @param splitValues
	 *        the split values list
	 * @param definition
	 *        the definition
	 * @return the list size
	 */
	@SuppressWarnings("rawtypes")
	private static int getMergedListSize(final DelegatingWithSourceMultiEList splitValues, final Object definition)
	{
		int size = 0;
		for (Object o: splitValues)
		{
			if (definition.equals(getDefinition(o)))
			{
				size++;
			}
		}
		return size;
	}
}
