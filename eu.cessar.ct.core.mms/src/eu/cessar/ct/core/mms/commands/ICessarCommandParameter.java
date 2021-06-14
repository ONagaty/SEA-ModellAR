package eu.cessar.ct.core.mms.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * 
 * Interface for generic Cessar command parameters.
 * 
 */
public interface ICessarCommandParameter
{
	/**
	 * Creates a {@link Command} using the given {@link AdapterFactory}.
	 * 
	 * @param adapterFactory
	 *        the AdapterFactory to use at the command creation
	 * @return the newly crate command
	 */
	public Command createCommand(AdapterFactory adapterFactory);
}
