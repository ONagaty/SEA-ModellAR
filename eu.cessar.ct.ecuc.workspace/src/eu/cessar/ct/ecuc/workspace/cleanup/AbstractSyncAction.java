/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 5:20:36 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucContainerCommand;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucReferenceCommand;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucContainerCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucReferenceCommandParameter;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import eu.cessar.ct.ecuc.workspace.jobs.MultiplicityUtils;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

/**
 * @author aurel_avramescu
 * 
 *         AbstractSyncAction class contains all the required methods for creation of mandatory elements
 */
public abstract class AbstractSyncAction implements ISyncAction
{
	//private static final String DEFAULT_VALUE_FEATURE_NAME = "defaultValue"; //$NON-NLS-1$
	//private static final String VALUE_FEATURE_NAME = "value"; //$NON-NLS-1$
	private boolean createMandatoryElements = true;
	private boolean createOptionalElements = false;
	private boolean createElementsBasedOnUpperMultiplicity = false;
	private int maximumElementsCount = 0;

	/**
	 * Create a container as child of the given parent (either {@link GModuleConfiguration} or {@link GContainer}) based
	 * on the given {@link GContainerDef}.
	 * 
	 * The new container can either by ParamConf or Choice, depending on the type of containerDef.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @param index
	 *        index of this child within parent's children of its kind
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createContainer(GIdentifiable parent, final GContainerDef containerDef) throws CoreException
	{
		try
		{
			if (containerDef instanceof GChoiceContainerDef)
			{
				return createChoiceContainer(parent, (GChoiceContainerDef) containerDef);
			}
			else if (containerDef instanceof GParamConfContainerDef)
			{
				return createParamConfContainer(parent, (GParamConfContainerDef) containerDef);
			}
			Assert.isTrue(false,
				NLS.bind(Messages.CessarModuleConfigurationJob_InvalidContainerType, containerDef.gGetShortName()));
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
		return null;
	}

	/**
	 * Create a Choice container as child of the given parent (either {@link GModuleConfiguration} or {@link GContainer}
	 * ) based on the given {@link GChoiceContainerDef}.
	 * 
	 * Depending on parent's multiplicity, a number of instances are created of the first available choice. If needed
	 * (versions 3.x and 4.0) and intermediate container is created for choices, based on the given
	 * {@link GChoiceContainerDef}.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createChoiceContainer(GIdentifiable parent, final GChoiceContainerDef containerDef)
		throws CoreException
	{
		try
		{
			// Get the choices list
			List<? extends GParamConfContainerDef> choices = (containerDef).gGetChoices();
			if (null == choices || choices.isEmpty())
			{
				// There are no choices available - cannot create anything
				return null;
			}

			// Select the first choice as default, create this choice instead of
			// parent container
			GContainerDef choice = choices.get(0);

			// Execute command to create new Container and install it
			// into parent's containers list.
			final NewEcucContainerCommandParameter cmdParameter = new NewEcucContainerCommandParameter(parent, choice);
			cmdParameter.setChoiceContainerDefinition(containerDef);
			final NewEcucContainerCommand command = new NewEcucContainerCommand(cmdParameter, null);
			Assert.isTrue(command.canExecute());
			command.execute();

			GIdentifiable commandResult = (GIdentifiable) command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			GContainer container = (GContainer) commandResult;

			return container;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * Create a ParamConf container as child of the given parent (either {@link GModuleConfiguration} or
	 * {@link GContainer}) based on the given {@link GParamConfContainerDef}.
	 * 
	 * Sub-containers, parameters and references with lower multiplicity > 0 are automatically added.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param containerDef
	 *        definition of the new container
	 * @return newly created container
	 * @throws CoreException
	 */
	protected GContainer createParamConfContainer(GIdentifiable parent, final GParamConfContainerDef containerDef)
		throws CoreException
	{
		try
		{
			// Execute command to create new Container and install it
			// into parent's containers list.
			final NewEcucContainerCommand command = new NewEcucContainerCommand(new NewEcucContainerCommandParameter(
				parent, containerDef), null);
			Assert.isTrue(command.canExecute());
			command.execute();

			GIdentifiable commandResult = (GIdentifiable) command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			GContainer container = (GContainer) commandResult;

			// Recurse sub-containers
			EList<? extends GContainerDef> subContainers = containerDef.gGetSubContainers();
			for (final GContainerDef subContainerDef: subContainers)
			{
				long containersCount = getNumberOfInstancesToCreate(subContainerDef);
				for (long index2 = 0; index2 < containersCount; ++index2)
				{
					createContainer(container, subContainerDef);
				}
			}

			// Create parameters
			EList<? extends GConfigParameter> parameters = containerDef.gGetParameters();
			for (GConfigParameter parameter: parameters)
			{
				// Create parameter will actually only create parameters that
				// have default values.
				long parametersCount = getNumberOfInstancesToCreate(parameter);
				for (long index3 = 0; index3 < parametersCount; ++index3)
				{
					createParameter(container, parameter);
				}
			}

			// TT: Removed reference creation: there's no default value for them
			// Create references
			// EList<? extends GConfigReference> references =
			// containerDef.gGetReferences();
			// for (GConfigReference reference: references)
			// {
			// long referencesCount = getNumberOfInstancesToCreate(reference);
			// for (long index4 = 0; index4 < referencesCount; ++index4)
			// {
			// createReference(container, reference);
			// }
			// }
			return container;
		}
		catch (Exception e)
		{
			throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
		}
	}

	/**
	 * If the given {@link GConfigParameter} definition has a default value, this method creates a new parameter based
	 * on that definition, and sets its value to definition's defaultValue. Otherwise, null is returned.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param parameter
	 *        definition of the new parameter
	 * @return newly created parameter with value set on definition's defaultValue, or null if no default value is set.
	 */
	@SuppressWarnings("unchecked")
	protected GParameterValue createParameter(GContainer container, GConfigParameter parameter)
	{
		try
		{
			GParameterValue parameterValue = null;

			// Create directly in this case: the command creates a new parameter
			// regardless of whether the parameter has a default value set.
			IGenericFactory genericFactory = MMSRegistry.INSTANCE.getGenericFactory(container);
			if (genericFactory != null)
			{
				parameterValue = genericFactory.createParameterValueWithDefault(parameter);
				if (parameterValue != null)
				{
					parameterValue.gSetDefinition(parameter);
					EList<? extends GParameterValue> parameterValues = container.gGetParameterValues();
					EList tmpList = parameterValues;
					tmpList.add(parameterValue);
				}
			}
			return parameterValue;
		}
		catch (Exception e)
		{
			// Changed to log the error, but not throw - execution should
			// continue with the next mandatory element
			// throw new CoreException(Activator.getDefault().createStatus(e));
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/**
	 * Create a new Reference value child of container, based on the given {@link GConfigReference} definition.
	 * 
	 * @param parent
	 *        parent for the element to be created
	 * @param reference
	 *        definition of the new reference
	 * @return newly created reference
	 */
	protected GConfigReferenceValue createReference(GContainer container, GConfigReference reference)
	{
		try
		{
			final NewEcucReferenceCommand command = new NewEcucReferenceCommand(new NewEcucReferenceCommandParameter(
				container, reference), null);
			Assert.isTrue(command.canExecute());
			command.execute();

			Object commandResult = command.getResult().iterator().next();
			Assert.isNotNull(commandResult);
			return (GConfigReferenceValue) commandResult;
		}
		catch (Exception e)
		{
			// Changed to log the error, but not throw - execution should
			// continue with the next mandatory element
			// throw new CoreException(Activator.getDefault().createStatus(e));
			CessarPluginActivator.getDefault().logError(e);
			return null;
		}
	}

	/**
	 * Given an object with multiplicity, this method calculates and returns the number of instances of this object that
	 * have to be created, considering the creation flags set in the job:<br>
	 * 
	 * <ulist><li>if createElementsBasedOnUpperMultiplicity is set, upper multiplicity is returned, but not more than
	 * maximumElementsCount;</li><br>
	 * 
	 * <li>if createElementsBasedOnUpperMultiplicity is not set, but createOptionalElements is set, lower multiplicity
	 * is returned, but not less than 1;</li><br>
	 * 
	 * <li>if createElementsBasedOnUpperMultiplicity is not set, createOptionalElements is not set, but
	 * createMandatoryElements is set, lower multiplicity is returned;</li><br>
	 * 
	 * <li>if none of the above applies, the method returns 0.</li></ulist><br>
	 * <br>
	 * 
	 * @param object
	 *        Object for which the number of instances to be created is queried.
	 * @return Number of instances of this object to create
	 */
	protected long getNumberOfInstancesToCreate(GParamConfMultiplicity object)
	{
		if (createElementsBasedOnUpperMultiplicity)
		{
			BigInteger upperMultiplicity = MultiplicityUtils.getUpperMultiplicity(object);
			if (MultiplicityUtils.isInfinite(upperMultiplicity))
			{
				return maximumElementsCount;
			}
			return Math.min(upperMultiplicity.longValue(), maximumElementsCount);
		}

		BigInteger lowerMultiplicity = MultiplicityUtils.getLowerMultiplicity(object);
		if (createOptionalElements && MultiplicityUtils.isZero(lowerMultiplicity))
		{
			return 1;
		}

		if (createMandatoryElements)
		{
			return lowerMultiplicity.longValue();
		}

		return 0;
	}

	public String getActionName()
	{
		return Messages.SyncAction_Create;
	}
}
