package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.commands.IEcucCessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucModuleConfigurationCommand;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A generic command parameter implementation that must be passed to the command
 * that create a new ECUC container.
 */
public class NewEcucModuleConfigurationCommandParameter extends CommandParameter implements
	IEcucCessarCommandParameter
{
	private GModuleDef moduleDefinition;

	private String shortName = null;

	/**
	 * Creates an instance of {@link NewEcucModuleConfigurationCommandParameter}
	 * using the provided parameters.
	 * 
	 * @param owner
	 *        the owner object for the Module Configuration to be created
	 * @param definition
	 *        the definition of the Module Configuration about to be created
	 * @param newShortName
	 *        the short name of Module Configuration to be created by this
	 *        command
	 */
	public NewEcucModuleConfigurationCommandParameter(GIdentifiable owner, GModuleDef definition,
		String newShortName)
	{
		super(owner);
		Assert.isNotNull(owner);
		Assert.isNotNull(definition);

		moduleDefinition = definition;
		shortName = newShortName;
	}

	/**
	 * Creates an instance of {@link NewEcucModuleConfigurationCommandParameter}
	 * using the provided parameters and having the shortName <code>null</code>.
	 * 
	 * @param owner
	 *        the owner object for the Module Configuration to be created
	 * @param definition
	 *        the definition of the Module Configuration about to be created
	 * 
	 */
	public NewEcucModuleConfigurationCommandParameter(GIdentifiable owner, GModuleDef definition)
	{
		this(owner, definition, null);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.IEcucCessarCommandParameter#getDefinition()
	 */
	public GModuleDef getDefinition()
	{
		return moduleDefinition;
	}

	/**
	 * Return the shortName stored inside the command parameters. If there is no
	 * such shortname stored a new one will be generated
	 * 
	 * @return
	 */
	public String getShortName()
	{
		if (shortName == null)
		{
			return MetaModelUtils.computeUniqueChildShortName((EObject) owner,
				moduleDefinition.gGetShortName());
		}
		else
		{
			return shortName;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.ICessarCommandParameter#createCommand(org.eclipse.emf.common.notify.AdapterFactory)
	 */
	public Command createCommand(AdapterFactory adapterFactory)
	{
		Object image = null;
		if (adapterFactory != null)
		{
			Adapter adapter = adapterFactory.adapt(moduleDefinition, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider)
			{
				image = ((IItemLabelProvider) adapter).getImage(moduleDefinition);
			}
		}
		return new NewEcucModuleConfigurationCommand(this, image);
	}
}
