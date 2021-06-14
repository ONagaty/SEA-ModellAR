package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.commands.IEcucCessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucParameterCommand;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A generic command parameter implementation that must be passed to the command
 * that create a new ECUC parameter.<br/>
 * When properties sections are properly implemented, this class can be remove,
 * since there is no need to create a parameter in this way.
 */
public class NewEcucParameterCommandParameter extends CommandParameter implements
	IEcucCessarCommandParameter
{
	/** the definition of the parameter about to be created */
	private GConfigParameter parameterDefinition;

	/**
	 * Default constructor for the command parameter.
	 * 
	 * @param owner
	 *        the owner object that will have the parameter about to be created
	 * @param definition
	 *        the definition of the parameter about to be created
	 */
	public NewEcucParameterCommandParameter(GIdentifiable owner, GConfigParameter definition)
	{
		super(owner);
		Assert.isNotNull(owner);
		Assert.isNotNull(definition);
		parameterDefinition = definition;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.IEcucCessarCommandParameter#getDefinition()
	 */
	public GConfigParameter getDefinition()
	{
		return parameterDefinition;
	}

	/**
	 * The method that gets an ECU parameter creation command, based on this
	 * command parameter.
	 * 
	 * @return An EMF {@link Command} instance that, when executed, create a new
	 *         ECUC parameter instance.
	 */
	public Command createCommand(AdapterFactory adapterFactory)
	{
		Object image = null;
		if (adapterFactory != null)
		{
			Adapter adapter = adapterFactory.adapt(parameterDefinition, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider)
			{
				image = ((IItemLabelProvider) adapter).getImage(parameterDefinition);
			}
		}
		return new NewEcucParameterCommand(this, image);
	}
}
