/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * Feb 7, 2014 2:55:15 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.autofill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.sdk.autofill.EAutomaticValuesFillType;
import eu.cessar.ct.sdk.autofill.FragmentFilterFactory;
import eu.cessar.ct.sdk.autofill.IContainerFragmentFilter;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.SEAModelUtil;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Implementation for {@link IAutofillCandidatesProvider} that uses the Simple Ecuc API
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Thu Aug 21 14:53:52 2014 %
 *
 *         %version: 5 %
 */
public abstract class AutofillCandidatesProvider implements IAutofillCandidatesProvider
{
	private static final Map<String, Object> SEA_MAP_OPTIONS;

	static
	{
		SEA_MAP_OPTIONS = new HashMap<String, Object>();
		SEA_MAP_OPTIONS.put(SEAModelUtil.READ_ONLY, true);
		SEA_MAP_OPTIONS.put(SEAModelUtil.AUTO_SAVE, false);
	}

	private Map<ISEAContainer, GContainer> result = new LinkedHashMap<ISEAContainer, GContainer>();
	/**
	 * Initialized specific to Sub classes
	 */
	protected GReferrable parameterDefinition;
	/**
	 * Required in sub method
	 */
	protected GParamConfContainerDef containerdefinition;

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.autofill.IAutofillCandidatesProvider#getCandidates()
	 */
	@Override
	public Map<ISEAContainer, GContainer> getCandidates()
	{
		return result;
	}

	public <T> IStatus computeCandidates(IProject project, String moduleConfigQualifiedName,
		String containerDefinitionQualifiedName, String parameterName, EAutomaticValuesFillType automaticFillType,
		IContainerFragmentFilter filter)
	{

		EObject eObject = ModelUtils.getEObjectWithQualifiedName(project, containerDefinitionQualifiedName, true);

		// check passed container definition qualified name
		if (!(eObject instanceof GParamConfContainerDef))
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, NLS.bind(
				"No container definition found for {0} within project {1}", new Object[] { //$NON-NLS-1$
					containerDefinitionQualifiedName, project.getName()}), new RuntimeException());
		}

		containerdefinition = (GParamConfContainerDef) eObject;

		GModuleConfiguration usedConfiguration = null;

		// check passed module configuration qualified name
		if (moduleConfigQualifiedName != null)
		{
			EObject eObject1 = ModelUtils.getEObjectWithQualifiedName(project, moduleConfigQualifiedName, true);
			if (eObject1 == null || !(eObject1 instanceof GModuleConfiguration))
			{
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, NLS.bind(
					"No module configuration found for {0} inside project {1}", new Object[] { //$NON-NLS-1$
						moduleConfigQualifiedName, project.getName()}), new RuntimeException());
			}
			usedConfiguration = (GModuleConfiguration) eObject1;
		}

		GModuleDef moduleDefinition = getModuleDefinition(containerdefinition);

		// check module def
		if (moduleDefinition == null)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, NLS.bind(
				"No module definition exists for {0} inside project {1}", new Object[] { //$NON-NLS-1$
					containerDefinitionQualifiedName, project.getName()}), new RuntimeException());
		}

		// check usedConfiguration against moduleDefinition
		if (usedConfiguration != null)
		{
			if (usedConfiguration.gGetDefinition() != moduleDefinition)
			{
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, NLS.bind(
					"The passed module configuration {0} is not an instance of {1} module definition!", new Object[] { //$NON-NLS-1$
						moduleConfigQualifiedName, moduleDefinition.gGetShortName()}), new RuntimeException());
			}
		}

		List<ISEAContainer> allContainers = new ArrayList<ISEAContainer>();

		ISEAModel seaModel = getSEAModel(project);
		ISEAList<ISEAConfig> configurations = seaModel.getConfigurations(moduleDefinition.gGetShortName());

		if (usedConfiguration != null)
		{
			ISEAConfig seaConfiguration = getSEAConfiguration(configurations, usedConfiguration);
			if (seaConfiguration != null)
			{
				allContainers.addAll(seaConfiguration.searchForContainers(containerDefinitionQualifiedName));
			}
			else
			{
				// should not get here
			}
		}
		else
		{ // use the first configuration considering the qualified name

			List<ISEAConfig> copyList = new ArrayList<ISEAConfig>(configurations);
			sortConfigs(copyList);
			if (!copyList.isEmpty())
			{
				ISEAList<ISEAContainer> containers = copyList.get(0).searchForContainers(
					containerDefinitionQualifiedName);
				allContainers.addAll(containers);
			}
		}

		// filter based on auto-fill type
		List<ISEAContainer> filteredBasedOnFillType = filterBasedOnFillType(allContainers, parameterName,
			automaticFillType);

		// parameterDefinition = getReferenceDefByName(containerdefinition, parameterName);
		result = getSEAContainerToFragmentMapping(filteredBasedOnFillType, filter);

		return Status.OK_STATUS;
	}

	/**
	 * @param seaConfigs
	 * @param moduleConfiguration
	 * @return the {@link ISEAConfig} from the <code>configurations</code> list that wraps the specified module
	 *         configuration or <code>null</code>
	 */
	private static ISEAConfig getSEAConfiguration(List<ISEAConfig> seaConfigs, GModuleConfiguration moduleConfiguration)
	{
		ISEAConfig seaConfiguration = null;
		for (ISEAConfig seaConfig: seaConfigs)
		{
			String qualifiedName = SeaUtils.getQualifiedName(seaConfig);
			String qName = ModelUtils.getAbsoluteQualifiedName(moduleConfiguration);
			if (qName.equals(qualifiedName))
			{
				seaConfiguration = seaConfig;
				break;
			}
		}
		return seaConfiguration;
	}

	/**
	 * @return
	 */
	private static ISEAModel getSEAModel(IProject project)
	{
		return SEAModelUtil.getSEAModel(project, SEA_MAP_OPTIONS);
	}

	/**
	 * @param definition
	 * @return the containing module definition, could be <code>null</code>
	 */
	private static GModuleDef getModuleDefinition(GParamConfContainerDef definition)
	{
		GModuleDef moduleDefinition = null;

		EObject eContainer = definition.eContainer();
		while (eContainer != null)
		{
			if (eContainer instanceof GModuleDef)
			{
				moduleDefinition = (GModuleDef) eContainer;
				break;
			}
			eContainer = eContainer.eContainer();
		}

		return moduleDefinition;
	}

	/**
	 * @param configurations
	 */
	private static void sortConfigs(List<ISEAConfig> configurations)
	{
		Comparator<ISEAConfig> comparator = new Comparator<ISEAConfig>()
			{
			/*
			 * (non-Javadoc)
			 *
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(ISEAConfig o1, ISEAConfig o2)
			{
				int comparisonResult = o1.getShortName().compareTo(o2.getShortName());
				if (comparisonResult != 0)
				{
					return comparisonResult;
				}

				String qualifiedName1 = SeaUtils.getQualifiedName(o1);
				String qualifiedName2 = SeaUtils.getQualifiedName(o2);

				return qualifiedName1.compareTo(qualifiedName2);
			}
			};

			Collections.sort(configurations, comparator);
	}

	private static List<ISEAContainer> filterBasedOnFillType(List<ISEAContainer> containers, String paramName,
		EAutomaticValuesFillType fillType)
		{
		List<ISEAContainer> filtered = new ArrayList<ISEAContainer>();
		for (ISEAContainer container: containers)
		{
			boolean shouldFill = false;
			boolean isSet = container.isSet(paramName);
			switch (fillType)
			{
				case FILL_ALL:
					shouldFill = true;
					break;

				case FILL_NOT_SET:

					shouldFill = !isSet;
					break;
				case FILL_SET:

					shouldFill = isSet;
					break;

				default:
					// do nothing
					shouldFill = false;
			}

			if (shouldFill)
			{
				filtered.add(container);
			}
		}
		return filtered;
		}

	/**
	 * @param filteredBasedOnFillType
	 * @param filter
	 * @return
	 */
	private static Map<ISEAContainer, GContainer> getSEAContainerToFragmentMapping(
		List<ISEAContainer> filteredBasedOnFillType, IContainerFragmentFilter filter)
		{
		final Map<ISEAContainer, GContainer> result = new LinkedHashMap<ISEAContainer, GContainer>();

		IContainerFragmentFilter usedFilter = filter;
		if (usedFilter == null)
		{
			usedFilter = FragmentFilterFactory.newFirstAlphaFragmentFilter();
		}

		for (ISEAContainer seaContainer: filteredBasedOnFillType)
		{
			GContainer acceptedFragment = usedFilter.getAcceptedFragment(seaContainer.arGetContainers());
			if (acceptedFragment != null)
			{
				if (seaContainer.arGetContainers().contains(acceptedFragment))
				{
					result.put(seaContainer, acceptedFragment);
				}
			}
		}

		return result;
		}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.internal.autofill.IAutofillCandidatesProvider#getParameterDefinition()
	 */
	@Override
	public GReferrable getParameterDefinition()
	{
		return parameterDefinition;
	}

}
