package eu.cessar.ct.edit.ui.internal.facility.selector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;

public abstract class CollectionBasedSelector extends AbstractSelectorBuilder
{

	private List<ISelector> selectors = new ArrayList<ISelector>();

	/**
	 * Getter of the property <tt>selectors</tt>
	 * 
	 * @return Returns the selectors.
	 */
	public List<ISelector> getSelectors()
	{
		return selectors;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ui.internal.properties.selector.AbstractSelector#init(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void init(IConfigurationElement element)
	{
		// get the children of the elements
		IConfigurationElement[] children = element.getChildren();
		if (children.length == 0)
		{
			CessarPluginActivator.getDefault().logError(Messages.Children_element_required,
				element.toString());
		}
		for (IConfigurationElement child: children)
		{
			selectors.add(createSelector(child));
		}
	}
}
