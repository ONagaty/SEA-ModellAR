package eu.cessar.ct.core.mms.providers;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;

/**
 * A generic implementation of {@link IEditingDomainItemProvider}. It delegates
 * all the methods to the parent specified at creation time. Customizations for
 * {@link IEditingDomainItemProvider} can inherit from this.
 */
public class DelegateEditingDomainItemProvider extends AdapterImpl implements
	IEditingDomainItemProvider
{
	private IEditingDomainItemProvider parentProvider;

	/**
	 * Default constructor
	 * 
	 * @param parent
	 *        an {@link IEditingDomainItemProvider} instance to which are
	 *        delegated all the methods
	 */
	public DelegateEditingDomainItemProvider(IEditingDomainItemProvider parent)
	{
		parentProvider = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#createCommand(java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain, java.lang.Class, org.eclipse.emf.edit.command.CommandParameter)
	 */
	public Command createCommand(Object object, EditingDomain editingDomain,
		Class<? extends Command> commandClass, CommandParameter commandParameter)
	{
		return parentProvider.createCommand(object, editingDomain, commandClass, commandParameter);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getChildren(java.lang.Object)
	 */
	public Collection<?> getChildren(Object object)
	{
		return parentProvider.getChildren(object);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getNewChildDescriptors(java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain, java.lang.Object)
	 */
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain,
		Object sibling)
	{
		return parentProvider.getNewChildDescriptors(object, editingDomain, sibling);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object object)
	{
		return parentProvider.getParent(object);
	}
}
