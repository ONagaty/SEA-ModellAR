/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * 04.06.2013 16:02:04
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.sea.IGIdentifiableFilter;
import eu.cessar.ct.runtime.ecuc.internal.sea.SeaFiltersFactory;
import eu.cessar.ct.sdk.sea.ISEAModel;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sphinx.emf.model.IModelDescriptor;
import org.eclipse.sphinx.emf.util.EObjectUtil;

/**
 *
 * @author uidl6870
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Jul  9 14:16:13 2014 %
 *
 *         %version: 5 %
 */
public final class MatchingDefinitionHelper
{
	static
	{
		// to reach 100% code coverage
		new MatchingDefinitionHelper();
	}

	private MatchingDefinitionHelper()
	{
		// hide
	}

	/**
	 * @param parent
	 *        owner of the matching attributes
	 * @param defName
	 *        attribute short name in case insensitive
	 * @return a list with {@link GCommonConfigurationAttributes} owned by <code>parent</code> that match the given
	 *         short name
	 */
	public static List<? extends GCommonConfigurationAttributes> getMatchingAttributeDefs(
		GParamConfContainerDef parent, String defName)
	{
		EList<GConfigParameter> parameters = parent.gGetParameters();
		EList<GConfigReference> references = parent.gGetReferences();
		List<GCommonConfigurationAttributes> attrs = new ArrayList<GCommonConfigurationAttributes>();
		attrs.addAll(parameters);
		attrs.addAll(references);

		return filter(attrs, SeaFiltersFactory.getFilterDefinitionByName(defName, true));
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GConfigParameter}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GConfigParameter> getMatchingParameterDefs(GParamConfContainerDef parent, String defName)
	{
		return filter(parent.gGetParameters(), SeaFiltersFactory.getFilterDefinitionByName(defName, true));
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GConfigReference}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GConfigReference> getMatchingReferenceDefs(GParamConfContainerDef parent, String defName)
	{
		return filter(parent.gGetReferences(), SeaFiltersFactory.getFilterDefinitionByName(defName, true));
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GContainerDef}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GContainerDef> getMatchingContainerDefs(GModuleDef parent, String defName)
	{
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(parent);
		List<GContainerDef> containerDefs = ecucModel.collectContainerDefs(parent);
		return filter(containerDefs, SeaFiltersFactory.getFilterDefinitionByName(defName, true));
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GChoiceContainerDef}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GChoiceContainerDef> getMatchingChoiceContainerDefs(GModuleDef parent, String defName)
	{
		List<GChoiceContainerDef> result = new ArrayList<GChoiceContainerDef>();

		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		for (GContainerDef containerDef: matchingContainerDefs)
		{
			if (containerDef instanceof GChoiceContainerDef)
			{
				result.add((GChoiceContainerDef) containerDef);
			}
		}

		return result;
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GContainerDef}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GContainerDef> getMatchingContainerDefs(GContainerDef parent, String defName)
	{
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(parent);
		List<GContainerDef> containerDefs = ecucModel.collectContainerDefs(parent);
		return filter(containerDefs, SeaFiltersFactory.getFilterDefinitionByName(defName, true));
	}

	/**
	 * @param parent
	 * @param defName
	 * @return a list with {@link GChoiceContainerDef}(s) owned by <code>parent</code> that match the given short name
	 */
	public static List<GChoiceContainerDef> getMatchingChoiceContainerDefs(GContainerDef parent, String defName)
	{
		List<GChoiceContainerDef> result = new ArrayList<GChoiceContainerDef>();

		List<GContainerDef> matchingContainerDefs = getMatchingContainerDefs(parent, defName);
		for (GContainerDef containerDef: matchingContainerDefs)
		{
			if (containerDef instanceof GChoiceContainerDef)
			{
				result.add((GChoiceContainerDef) containerDef);
			}
		}

		return result;
	}

	/**
	 * @param parent
	 * @param choiceContainerDefName
	 * @param chosenContainerDefName
	 * @return a list with {@link GParamConfContainerDef}(s) that match the given <code>chosenContainerDefName</code>
	 *         short name, owned by a choice container definition named <code> choiceContainerDefName</code> defined in
	 *         <code>parent</code> module definition
	 */
	public static List<GParamConfContainerDef> getMatchingContainerDefs(GModuleDef parent,
		String choiceContainerDefName, String chosenContainerDefName)
	{
		List<GChoiceContainerDef> matchingChoiceContainerDefs = getMatchingChoiceContainerDefs(parent,
			choiceContainerDefName);

		return doGetMatchingContainerDefs(matchingChoiceContainerDefs, chosenContainerDefName);
	}

	/**
	 * @param parent
	 * @param choiceContainerDefName
	 * @param chosenContainerDefName
	 * @return a list with {@link GParamConfContainerDef}(s) that match the given <code>chosenContainerDefName</code>
	 *         short name, owned by a choice container definition named <code> choiceContainerDefName</code> defined in
	 *         <code>parent</code> container definition
	 */
	public static List<GParamConfContainerDef> getMatchingContainerDefs(GContainerDef parent,
		String choiceContainerDefName, String chosenContainerDefName)
	{
		List<GChoiceContainerDef> choiceContainerDefs = getMatchingChoiceContainerDefs(parent, choiceContainerDefName);
		return doGetMatchingContainerDefs(choiceContainerDefs, chosenContainerDefName);
	}

	private static List<GParamConfContainerDef> doGetMatchingContainerDefs(
		List<GChoiceContainerDef> choiceContainerDefs, String chosenContainerDefName)
	{
		List<GParamConfContainerDef> result = new ArrayList<GParamConfContainerDef>();

		for (GChoiceContainerDef choiceContDef: choiceContainerDefs)
		{
			List<GContainerDef> containerDefs = getMatchingContainerDefs(choiceContDef, chosenContainerDefName);
			for (GContainerDef containerDef: containerDefs)
			{
				result.add((GParamConfContainerDef) containerDef);
			}
		}

		return result;
	}

	/**
	 * @param project
	 * @param defName
	 * @return a list with module definitions from the given <code>project</code> that match the given short name
	 */
	public static List<GModuleDef> getMatchingModuleDefs(IProject project, String defName)
	{
		List<GModuleDef> moduleDefs = getAllModuleDefs(project);

		IGIdentifiableFilter filter = SeaFiltersFactory.getFilterDefinitionByName(defName, true);
		return filter(moduleDefs, filter);
	}

	/**
	 * @param project
	 * @param pathFragment
	 * @return a list with module definitions from the given <code>project</code>, whose qualified name matches the
	 *         given path fragment
	 * @see ISEAModel#pathFragment
	 */

	public static List<GModuleDef> searchForMatchingModuleDefs(IProject project, String pathFragment)
	{
		List<GModuleDef> moduleDefs = getAllModuleDefs(project);

		IGIdentifiableFilter filter = SeaFiltersFactory.getFilterDefinitionByPathFragment(pathFragment, true);
		return filter(moduleDefs, filter);
	}

	private static List<GModuleDef> getAllModuleDefs(IProject project)
	{
		IModelDescriptor modelDescriptor = MetaModelUtils.getAutosarModelDescriptor(project);
		if (modelDescriptor != null)
		{
			return EObjectUtil.getAllInstancesOf(modelDescriptor, GModuleDef.class, false);
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @param defs
	 *        list to be filtered
	 * @param filter
	 *        the filter to use
	 * @return list with elements that made it through the filter
	 */
	private static <T extends GIdentifiable> List<T> filter(List<T> defs, IGIdentifiableFilter filter)
	{
		List<T> matchingContainerDefs = new ArrayList<T>();

		for (T def: defs)
		{
			if (filter.isPassingFilter(def))
			{
				matchingContainerDefs.add(def);
			}
		}

		return matchingContainerDefs;
	}

}
