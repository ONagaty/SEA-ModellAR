package eu.cessar.ct.edit.ui.internal.facility.selector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;

public class NotSelector extends AbstractSelectorBuilder
{

	/**
	 * @uml.property name="selector"
	 * @uml.associationEnd inverse=
	 *                     "notSelector:eu.cessar.ct.ui.internal.properties.selector.ISelector"
	 */
	private ISelector selector;

	/**
	 * Return the isSelected() method result of custom Selection
	 * 
	 * @param EClass
	 *        {@link EClass}
	 * @param EStructuralFeature
	 *        feature {@link EStructuralFeature}
	 */
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature)
	{
		if (selector == null)
		{
			return false;
		}
		return !selector.isSelected(mmService, object, clz, feature);
	}

	/**
	 * Getter of the property <tt>selector</tt>
	 * 
	 * @return Returns the selector.
	 * @uml.property name="selector"
	 */
	public ISelector getSelector()
	{
		return selector;
	}

	/**
	 * Setter of the property <tt>selector</tt>
	 * 
	 * @param selector
	 *        The selector to set.
	 * @uml.property name="selector"
	 */
	public void setSelector(ISelector selector)
	{
		this.selector = selector;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ui.internal.properties.selector.AbstractSelector#init(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void init(IConfigurationElement element)
	{
		IConfigurationElement[] children = element.getChildren();
		// should have exactly one child
		if (children.length == 1)
		{
			setSelector(createSelector(children[0]));
		}
		else
		{
			CessarPluginActivator.getDefault().logError(Messages.Children_element_required, element);
		}
	}

}
