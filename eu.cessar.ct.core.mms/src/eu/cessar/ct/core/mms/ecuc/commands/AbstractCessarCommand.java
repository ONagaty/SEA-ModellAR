package eu.cessar.ct.core.mms.ecuc.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandActionDelegate;

public abstract class AbstractCessarCommand extends AbstractCommand implements
	CommandActionDelegate
{
	private Object commandImage;

	protected List<EObject> result = new ArrayList<EObject>();

	public AbstractCessarCommand(Object image)
	{
		commandImage = image;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getImage()
	 */
	public Object getImage()
	{
		return commandImage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getText()
	 */
	public abstract String getText();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.CommandActionDelegate#getToolTipText()
	 */
	public String getToolTipText()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	public abstract void execute();

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo()
	{
		// redo not supported, yet
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.AbstractCommand#getResult()
	 */
	@Override
	public Collection<?> getResult()
	{
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.command.AbstractCommand#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects()
	{
		return getResult();
	}
}
