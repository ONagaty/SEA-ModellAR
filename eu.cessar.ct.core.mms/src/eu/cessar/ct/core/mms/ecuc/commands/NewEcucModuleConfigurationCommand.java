package eu.cessar.ct.core.mms.ecuc.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleConfigurationCommandParameter;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * Generic implementation of the command that create an ECUC Container, based on
 * the data passed through the parameter.
 */
public class NewEcucModuleConfigurationCommand extends AbstractCessarCommand
{
	private NewEcucModuleConfigurationCommandParameter cmdParameter;

	/**
	 * Creates an instance of {@link NewEcucModuleConfigurationCommand}.
	 * 
	 * @param parameter
	 *        an instance of Ecuc Module Configuration command parameter
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public NewEcucModuleConfigurationCommand(NewEcucModuleConfigurationCommandParameter parameter,
		Object image)
	{
		super(image);
		Assert.isNotNull(parameter);
		this.cmdParameter = parameter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		return (cmdParameter.getOwner() instanceof GARPackage) && canCreateNewInstances();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	@Override
	public String getText()
	{
		return GModuleConfiguration.class.getInterfaces()[0].getSimpleName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void execute()
	{
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		EList<? extends GPackageableElement> childrenList = null;
		childrenList = ((GARPackage) owner).gGetElements();

		if (childrenList != null)
		{
			EList tmpList = childrenList;
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(owner);
			IGenericFactory genericFactory = mmService.getGenericFactory();
			if (genericFactory != null)
			{
				GModuleConfiguration newModuleConfiguration = genericFactory.createModuleConfiguration();
				newModuleConfiguration.gSetShortName(cmdParameter.getShortName());
				newModuleConfiguration.gSetDefinition(cmdParameter.getDefinition());
				tmpList.add(newModuleConfiguration);

				result.add(newModuleConfiguration);
			}
		}
	}

	private boolean canCreateNewInstances()
	{
		GIdentifiable owner = (GIdentifiable) cmdParameter.getOwner();
		GModuleDef gModuleDef = cmdParameter.getDefinition();

		List<GModuleConfiguration> instances = getModuleConfigurationInstances(owner, gModuleDef);

		if (instances == null)
		{
			return false;
		}

		int noInstances = instances.size();

		BigInteger upper = MMSRegistry.INSTANCE.getMMService(gModuleDef).getEcucMMService().getUpperMultiplicity(
			gModuleDef, BigInteger.ONE, true);

		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			long longUpper = upper.longValue();
			return longUpper > noInstances;
		}
	}

	/**
	 * Return a list with all direct container instances under specified parent,
	 * that have the given container definition. The parent must be a Module
	 * configuration or a parent container.
	 * 
	 * @param parent
	 *        a module configuration or another container instance
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent
	 *         is not a module configuration or another container instance.
	 */
	private static List<GModuleConfiguration> getModuleConfigurationInstances(Object parent,
		GModuleDef moduleDef)
	{
		EList<? extends GPackageableElement> elements = null;
		if (parent instanceof GARPackage)
		{
			elements = ((GARPackage) parent).gGetElements();
		}
		if ((elements == null) || (moduleDef == null))
		{
			return Collections.emptyList();
		}

		// retrieve only module configuration instances having specified
		// definition

		// CHECKSTYLE:OFF
		// disabled checkstyles in order to be able to have an ArrayList
		// instance variable.
		ArrayList<GModuleConfiguration> result = new ArrayList<GModuleConfiguration>();
		// CHECKSTYLE:ON
		for (GPackageableElement element: elements)
		{
			if (GModuleConfiguration.class.isAssignableFrom(element.getClass()))
			{
				GModuleConfiguration moduleConfiguration = (GModuleConfiguration) element;
				if (moduleDef.equals(moduleConfiguration.gGetDefinition()))
				{
					result.add(moduleConfiguration);
				}
			}
		}

		result.trimToSize();
		return result;
	}

}