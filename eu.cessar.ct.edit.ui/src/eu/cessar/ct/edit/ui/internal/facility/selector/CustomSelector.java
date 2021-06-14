package eu.cessar.ct.edit.ui.internal.facility.selector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

public class CustomSelector extends AbstractSelectorBuilder
{

	private ISelector selectorObj;

	/**
	 * @param selectorObj
	 *        the selectorObj to set
	 */
	public void setSelectorObj(ISelector selectorObj)
	{
		this.selectorObj = selectorObj;
	}

	/**
	 * @return the selectorObj
	 */
	public ISelector getSelectorObj()
	{
		return selectorObj;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ui.internal.properties.selector.AbstractSelector#init(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void init(IConfigurationElement element)
	{

		try
		{
			Object extension = element.createExecutableExtension(FacilityConstants.ATT_VALUE);
			if (!(extension instanceof ISelector))
			{
				CessarPluginActivator.getDefault().logError(Messages.Selector_not_of_the_right_type, extension);
			}
			else
			{
				setSelectorObj((ISelector) extension);
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e, Messages.Error_in_selector_initialisation);
		}
	}

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
		return getSelectorObj().isSelected(mmService, object, clz, feature);
	}
}
