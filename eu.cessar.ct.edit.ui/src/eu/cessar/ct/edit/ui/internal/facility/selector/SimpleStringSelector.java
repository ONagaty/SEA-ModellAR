/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 16.06.2012 12:03:10 </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.selector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

/**
 * @author uidl6458
 * 
 */
public abstract class SimpleStringSelector extends AbstractSelectorBuilder
{

	protected String attribute;

	public static class EClassSelector extends SimpleStringSelector implements ISelector
	{
		/**
		 * Return true if EClass.getName() method result is regular expression
		 * 
		 * @param EClass
		 *        {@link EClass}
		 * @param EStructuralFeature
		 *        feature {@link EStructuralFeature}
		 */
		public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
			EStructuralFeature feature)
		{
			return attribute.equals(clz.getName());
		}
	}

	public static class EFeatureSelector extends SimpleStringSelector implements ISelector
	{
		/**
		 * Return true if EStructuralFeature.getName() method result is regular
		 * expression false otherwise
		 * 
		 * @param EClass
		 *        {@link EClass}
		 * @param EStructuralFeature
		 *        feature {@link EStructuralFeature}
		 */
		public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
			EStructuralFeature feature)
		{
			if (feature != null)
			{
				return attribute.equals(feature.getName());
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.internal.facility.selector.AbstractSelectorBuilder#init(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void init(IConfigurationElement element)
	{
		String attribute = element.getAttribute(FacilityConstants.ATT_REGEXP);
		if (attribute == null)
		{
			CessarPluginActivator.getDefault().logWarning(Messages.No_Regexp_specified, element);
			attribute = ""; //$NON-NLS-1$
		}
		setStringValue(attribute);
	}

	/**
	 * @param attribute
	 */
	private void setStringValue(String attribute)
	{
		this.attribute = attribute;
	}

}
