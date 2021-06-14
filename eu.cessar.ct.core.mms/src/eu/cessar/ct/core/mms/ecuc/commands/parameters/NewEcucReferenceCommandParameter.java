package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.commands.IEcucCessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucReferenceCommand;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A generic command parameter implementation that must be passed to the command
 * that create a new ECUC reference.<br/>
 * When properties sections are properly implemented, this class can be remove,
 * since there is no need to create a reference in this way.
 */
public class NewEcucReferenceCommandParameter extends CommandParameter implements
	IEcucCessarCommandParameter
{
	/** the definition of the reference about to be created */
	private GConfigReference referenceDefinition;

	/**
	 * Default constructor for the command parameter.
	 * 
	 * @param owner
	 *        the owner object that will have the reference about to be created
	 * @param definition
	 *        the definition of the reference about to be created
	 */
	public NewEcucReferenceCommandParameter(GIdentifiable owner, GConfigReference definition)
	{
		super(owner);
		Assert.isNotNull(owner);
		Assert.isNotNull(definition);
		referenceDefinition = definition;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.IEcucCessarCommandParameter#getDefinition()
	 */
	public GConfigReference getDefinition()
	{
		return referenceDefinition;
	}

	/**
	 * The method that gets an ECU reference creation command, based on this
	 * command parameter.
	 * 
	 * @return An EMF {@link Command} instance that, when executed, create a new
	 *         ECUC reference instance.
	 */
	public Command createCommand(AdapterFactory adapterFactory)
	{
		Object image = null;
		if (adapterFactory != null)
		{
			Adapter adapter = adapterFactory.adapt(referenceDefinition, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider)
			{
				image = ((IItemLabelProvider) adapter).getImage(referenceDefinition);
			}
		}
		return new NewEcucReferenceCommand(this, image);
	}
}
