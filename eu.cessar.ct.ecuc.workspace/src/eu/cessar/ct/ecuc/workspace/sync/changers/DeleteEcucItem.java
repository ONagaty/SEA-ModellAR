package eu.cessar.ct.ecuc.workspace.sync.changers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;

public class DeleteEcucItem extends AbstractEcucItemChanger
{
	EObject object;

	/**
	 * 
	 */
	public DeleteEcucItem(EObject objectToBeDeleted)
	{
		this.object = objectToBeDeleted;
	}

	@Override
	public boolean canExecute()
	{
		// you can always delete an GIdentifiable from the configuration
		if (object != null)
		{
			return true;
		}
		return false;
	}

	@Override
	public String getActionDescription()
	{
		return SynchronizeConstants.deleteItemDescr;
	}

	public void run()
	{
		if (canExecute())
		{
			EcoreUtil.delete(object, true);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.synchronizer.internal.modulechanger.AbstractModuleChanger#getOwner()
	 */
	@Override
	public EObject getEcucItem()
	{
		return object;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.sync.changers.AbstractEcucItemChanger#copy(java.lang.String)
	 */
	@Override
	public AbstractEcucItemChanger copy()
	{
		return this;
	}

}
