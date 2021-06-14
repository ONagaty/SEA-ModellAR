/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 8, 2009 2:29:46 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.selector;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

/**
 * The ISelectors are created here based on an IConfigurationElement.
 * 
 * @author uidl6458
 * 
 */
public abstract class AbstractSelectorBuilder implements ISelector
{
	public abstract void init(IConfigurationElement element);

	/**
	 * Create a selector based on the information from registry
	 * 
	 * @param element
	 * @return
	 */
	public static ISelector createSelector(IConfigurationElement element)
	{
		String selectorName = element.getName();
		AbstractSelectorBuilder result = null; // SUPPRESS CHECKSTYLE ok
		if (FacilityConstants.SELECTOR_AND.equals(selectorName))
		{
			result = new AndSelector();
		}
		else if (FacilityConstants.SELECTOR_OR.equals(selectorName))
		{
			result = new OrSelector();
		}
		else if (FacilityConstants.SELECTOR_NOT.equals(selectorName))
		{
			result = new NotSelector();
		}
		else if (FacilityConstants.SELECTOR_ECLASS.equals(selectorName))
		{
			if (RegExpSelector.isRegExpAttribute(element))
			{
				result = new RegExpSelector.EClassSelector();
			}
			else
			{
				result = new SimpleStringSelector.EClassSelector();
			}
		}
		else if (FacilityConstants.SELECTOR_EFEATURE.equals(selectorName))
		{
			if (RegExpSelector.isRegExpAttribute(element))
			{
				result = new RegExpSelector.EFeatureSelector();
			}
			else
			{
				result = new SimpleStringSelector.EFeatureSelector();
			}
		}
		else if (FacilityConstants.SELECTOR_SELECTORPROVIDER.equals(selectorName))
		{
			result = new CustomSelector();
		}
		else
		{
			CessarPluginActivator.getDefault().logError(Messages.Unknowed_SelectorType,
				selectorName);
			return null;
		}
		result.init(element);
		return result;
	}
}
