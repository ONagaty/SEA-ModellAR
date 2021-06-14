package eu.cessar.ct.core.mms.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * Filters the child descriptors and children of an object.
 * 
 */
public class FilteredItemProvider extends AdapterImpl implements ITreeItemContentProvider,
	IEditingDomainItemProvider
{

	private AdapterImpl parent;
	private IFilter filter;

	/**
	 * 
	 * Filter for pop-up and tree elements.
	 * 
	 */
	public static interface IFilter
	{
		boolean acceptChildDescriptor(Object childDescriptor);

		boolean acceptChild(Object child);
	}

	/**
	 * 
	 * Filter implementation that uses a list to filter elements.
	 * 
	 */
	public static class AcceptFilter implements IFilter
	{

		private List<EStructuralFeature> acceptedFeatures;

		public AcceptFilter(List<EStructuralFeature> acceptedFeatures)
		{
			this.acceptedFeatures = acceptedFeatures;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.core.mms.providers.FilteredItemProvider.Filter#acceptChild(java.lang.Object)
		 */
		public boolean acceptChild(Object child)
		{
			EObject childEObject = (EObject) child;
			EStructuralFeature eContainingFeature = childEObject.eContainingFeature();
			return acceptedFeatures.contains(eContainingFeature);
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.core.mms.providers.FilteredItemProvider.Filter#acceptChildDescriptor(java.lang.Object)
		 */
		public boolean acceptChildDescriptor(Object childDescriptor)
		{
			if (childDescriptor instanceof CommandParameter)
			{
				CommandParameter commandParameter = (CommandParameter) childDescriptor;
				EStructuralFeature eStructuralFeature = commandParameter.getEStructuralFeature();
				return acceptedFeatures.contains(eStructuralFeature);
			}
			return false;
		}

	}

	/**
	 * 
	 * Filter implementation that uses a list to filter elements.
	 * 
	 */
	public static class DenyFilter implements IFilter
	{

		private List<EStructuralFeature> deniableFeatures;

		public DenyFilter(List<EStructuralFeature> deniableFeatures)
		{
			this.deniableFeatures = deniableFeatures;
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.core.mms.providers.FilteredItemProvider.Filter#acceptChild(java.lang.Object)
		 */
		public boolean acceptChild(Object child)
		{
			EObject childEObject = (EObject) child;
			EStructuralFeature eContainingFeature = childEObject.eContainingFeature();
			return !deniableFeatures.contains(eContainingFeature);
		}

		/* (non-Javadoc)
		 * @see eu.cessar.ct.core.mms.providers.FilteredItemProvider.Filter#acceptChildDescriptor(java.lang.Object)
		 */
		public boolean acceptChildDescriptor(Object childDescriptor)
		{
			if (childDescriptor instanceof CommandParameter)
			{
				CommandParameter commandParameter = (CommandParameter) childDescriptor;
				EStructuralFeature eStructuralFeature = commandParameter.getEStructuralFeature();
				return !deniableFeatures.contains(eStructuralFeature);
			}
			return true;
		}

	}

	/**
	 * A filter that accept all
	 */
	public static final IFilter FILTER_ACCEPT_ALL = new IFilter()
	{

		public boolean acceptChildDescriptor(Object childDescriptor)
		{
			return true;
		}

		public boolean acceptChild(Object child)
		{
			return true;
		}

	};

	/**
	 * A filter that accept none
	 */
	public static final IFilter FILTER_ACCEPT_NONE = new IFilter()
	{

		public boolean acceptChildDescriptor(Object childDescriptor)
		{
			return false;
		}

		public boolean acceptChild(Object child)
		{
			return false;
		}

	};

	/**
	 * @param parent
	 */
	public FilteredItemProvider(AdapterImpl parent, IFilter filter)
	{
		this.parent = parent;
		this.filter = filter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getNewChildDescriptors(java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain, java.lang.Object)
	 */
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain,
		Object sibling)
	{
		Collection<Object> newChildDescriptors = new ArrayList<Object>();

		Collection<?> existingChildDescriptors = ((IEditingDomainItemProvider) parent).getNewChildDescriptors(
			object, editingDomain, sibling);
		for (Object childDescriptor: existingChildDescriptors)
		{
			if (filter.acceptChildDescriptor(childDescriptor))
			{
				newChildDescriptors.add(childDescriptor);
			}
		}

		return newChildDescriptors;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.ITreeItemContentProvider#getChildren(java.lang.Object)
	 */
	public Collection<?> getChildren(Object object)
	{
		Collection<Object> children = new ArrayList<Object>();
		Collection<?> existingChildren = ((ITreeItemContentProvider) parent).getChildren(object);
		for (Object child: existingChildren)
		{
			if (filter.acceptChild(child))
			{
				children.add(child);
			}
		}
		return children;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.ITreeItemContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object)
	{
		return !getChildren(object).isEmpty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IStructuredItemContentProvider#getElements(java.lang.Object)
	 */
	public Collection<?> getElements(Object object)
	{
		return getChildren(object);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.ITreeItemContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object object)
	{
		return ((ITreeItemContentProvider) parent).getParent(object);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#createCommand(java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain, java.lang.Class, org.eclipse.emf.edit.command.CommandParameter)
	 */
	public Command createCommand(Object object, EditingDomain editingDomain,
		Class<? extends Command> commandClass, CommandParameter commandParameter)
	{
		return ((IEditingDomainItemProvider) parent).createCommand(object, editingDomain,
			commandClass, commandParameter);
	}
}
