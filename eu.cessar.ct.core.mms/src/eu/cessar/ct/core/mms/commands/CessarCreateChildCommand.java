package eu.cessar.ct.core.mms.commands;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.core.mms.adapter.CessarAdapterFactoryFactory;

/**
 * Child object creation implementation
 * 
 */
public class CessarCreateChildCommand extends CreateChildCommand
{
	private Command wrappedCommand;
	private IItemLabelProvider adapter;

	/**
	 * Creates a {@link CessarCreateChildCommand} using the given parameters.
	 * 
	 * @param domain
	 *        the {@link EditingDomain} on which the command will operate
	 * @param owner
	 *        the owner {@link EObject} that triggered the command
	 * @param descriptor
	 *        needed {@link CommandParameter}s
	 * @param selection
	 *        the collection of values involved in the command
	 */
	public CessarCreateChildCommand(EditingDomain domain, EObject owner,
		CommandParameter descriptor, Collection<?> selection)
	{
		super(domain, owner, descriptor.getEStructuralFeature(), descriptor.getValue(), selection);
		wrappedCommand = CreateChildCommand.create(domain, owner, descriptor, selection);
		AdapterFactory adapterFactory = CessarAdapterFactoryFactory.eINSTANCE.getAdapterFactory((TransactionalEditingDomain) domain);
		if (adapterFactory != null)
		{
			Object obj = adapterFactory.adapt(child, IItemLabelProvider.class);
			if (obj instanceof IItemLabelProvider)
			{
				adapter = (IItemLabelProvider) obj;
			}
		}
	}

	/**
	 * @return
	 * @see org.eclipse.emf.edit.command.CreateChildCommand#getImage()
	 */
	@Override
	public Object getImage()
	{
		if (adapter != null)
		{
			Object image = adapter.getImage(child);
			if (image != null)
			{
				return image;
			}
		}

		if (wrappedCommand instanceof CommandActionDelegate)
		{
			return ((CommandActionDelegate) wrappedCommand).getImage();
		}
		return null;
	}

	/**
	 * @return
	 * @see org.eclipse.emf.edit.command.CreateChildCommand#getText()
	 */
	@Override
	public String getText()
	{
		if (wrappedCommand instanceof CommandActionDelegate)
		{
			return ((CommandActionDelegate) wrappedCommand).getText();
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * @return
	 * @see org.eclipse.emf.edit.command.CreateChildCommand#getToolTipText()
	 */
	@Override
	public String getToolTipText()
	{
		if (wrappedCommand instanceof CommandActionDelegate)
		{
			return ((CommandActionDelegate) wrappedCommand).getToolTipText();
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#canExecute()
	 */
	@Override
	public boolean canExecute()
	{
		return wrappedCommand.canExecute();
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#canUndo()
	 */
	@Override
	public boolean canUndo()
	{
		return wrappedCommand.canUndo();
	}

	/**
	 * @param command
	 * @return
	 * @see org.eclipse.emf.common.command.Command#chain(org.eclipse.emf.common.command.Command)
	 */
	@Override
	public Command chain(Command command)
	{
		return wrappedCommand.chain(command);
	}

	/**
	 * 
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	@Override
	public void dispose()
	{
		wrappedCommand.dispose();
	}

	/**
	 * 
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	@Override
	public void execute()
	{
		wrappedCommand.execute();
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects()
	{
		return wrappedCommand.getAffectedObjects();
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return wrappedCommand.getDescription();
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return wrappedCommand.getLabel();
	}

	/**
	 * @return
	 * @see org.eclipse.emf.common.command.Command#getResult()
	 */
	@Override
	public Collection<?> getResult()
	{
		return wrappedCommand.getResult();
	}

	/**
	 * 
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	@Override
	public void redo()
	{
		wrappedCommand.redo();
	}

	/**
	 * 
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	@Override
	public void undo()
	{
		wrappedCommand.undo();
	}

}
