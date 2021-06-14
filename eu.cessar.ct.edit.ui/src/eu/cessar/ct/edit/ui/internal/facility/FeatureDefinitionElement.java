/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 5, 2010 11:10:00 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.attribute.CacheableTypeEnum;
import eu.cessar.ct.edit.ui.internal.facility.selector.AbstractSelectorBuilder;

/**
 * @author uidl6458
 * 
 */
public class FeatureDefinitionElement extends SingularDefinitionElement
{

	private ISelector selector;
	private CacheableTypeEnum cacheableType = CacheableTypeEnum.NONE;

	/**
	 * @param extension
	 */
	public FeatureDefinitionElement(IConfigurationElement extension)
	{
		super(extension);
		IConfigurationElement[] children = extension.getChildren(FacilityConstants.ELEM_SELECTOR);
		if (children.length == 1)
		{
			IConfigurationElement[] children2 = children[0].getChildren();
			if (children2.length == 1)
			{
				selector = AbstractSelectorBuilder.createSelector(children2[0]);
			}
			else
			{
				CessarPluginActivator.getDefault().logWarning(Messages.Children_element_required,
					children[0]);
			}
		}
		else
		{
			CessarPluginActivator.getDefault().logWarning(Messages.Children_element_required,
				new Object[] {extension});
		}
		String cacheable = extension.getAttribute(FacilityConstants.ATT_CACHEABLE);
		cacheableType = CacheableTypeEnum.getEnum(cacheable);
	}

	public CacheableTypeEnum getCacheableType()
	{
		return cacheableType;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.internal.facility.SingularDefinitionElement#isSelected(eu.cessar.ct.core.mms.IMetaModelService, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature)
	{
		if (selector == null)
		{
			return false;
		}
		else
		{
			return selector.isSelected(mmService, object, clz, feature);
		}
	}

	public boolean isCacheable()
	{
		if (cacheableType.compareTo(CacheableTypeEnum.SIMPLE) == 0)
		{
			return true;
		}
		return false;
	}

}
