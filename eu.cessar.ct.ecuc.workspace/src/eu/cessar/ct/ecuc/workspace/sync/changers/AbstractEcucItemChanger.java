package eu.cessar.ct.ecuc.workspace.sync.changers;

import org.eclipse.emf.ecore.EObject;

public abstract class AbstractEcucItemChanger implements Runnable
{

	public abstract boolean canExecute();

	public abstract String getActionDescription();

	public abstract EObject getEcucItem();

	/**
	 * Creates a copy of this changer
	 * 
	 * @param name
	 * @return
	 */
	public abstract AbstractEcucItemChanger copy();

}
