package eu.cessar.ct.core.mms.commands;

import org.eclipse.emf.ecore.EObject;


/**
 * Interface for command parameters related to the ecuc side.
 * 
 */
public interface IEcucCessarCommandParameter extends ICessarCommandParameter
{
	/**
	 * Returns the definition of the involved element.
	 * 
	 * @return the definition (container def, parameter def, etc.)
	 */
	EObject getDefinition();
}
