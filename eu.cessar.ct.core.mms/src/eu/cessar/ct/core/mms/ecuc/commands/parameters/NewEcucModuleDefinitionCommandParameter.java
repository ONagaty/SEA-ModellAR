package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;

import eu.cessar.ct.core.mms.commands.ICessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucModuleDefinitionCommand;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A generic command parameter implementation that must be passed to the command
 * that create a new ECUC container.
 */
public class NewEcucModuleDefinitionCommandParameter extends CommandParameter implements
	ICessarCommandParameter
{
	private GModuleDef moduleDefinition;

	private String shortName;

	/**
	 * Creates an instance of {@link NewEcucModuleConfigurationCommandParameter}
	 * using the provided parameters.
	 * 
	 * @param owner
	 *        the owner object for the Module Definition to be created
	 * @param definition
	 *        the definition of the Module Definition about to be created
	 * @param newShortName
	 *        the short name of Module Definition to be created by this command
	 */
	public NewEcucModuleDefinitionCommandParameter(GIdentifiable owner, GModuleDef definition,
		String newShortName)
	{
		super(owner);
		Assert.isNotNull(owner);

		moduleDefinition = definition;
		shortName = newShortName;
	}

	/**
	 * Returns the definition of the involved element.
	 * 
	 * @return the module definition
	 */
	public GModuleDef getDefinition()
	{
		return moduleDefinition;
	}

	/**
	 * Return the shortName stored inside the command parameters
	 * 
	 * @return
	 */
	public String getShortName()
	{
		return shortName;
	}

	public Command createCommand(AdapterFactory adapterFactory)
	{
		return new NewEcucModuleDefinitionCommand(this, null);
	}
}
