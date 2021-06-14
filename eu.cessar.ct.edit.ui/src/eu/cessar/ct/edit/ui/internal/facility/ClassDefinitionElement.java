/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 5, 2010 12:36:58 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate;
import eu.cessar.ct.edit.ui.facility.ISelector;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.internal.facility.attribute.CacheableTypeEnum;
import eu.cessar.ct.edit.ui.internal.facility.selector.AbstractSelectorBuilder;

/**
 * @author uidl6458
 * 
 */
public class ClassDefinitionElement extends AbstractDefinitionElement
{

	private ExtensionClassWrapper<IModelFragmentEditorDelegate> delegate;

	private ISelector selector;
	private CacheableTypeEnum cacheableType = CacheableTypeEnum.NONE;

	public CacheableTypeEnum getCacheableType()
	{
		return cacheableType;
	}

	/**
	 * @param extension
	 */
	public ClassDefinitionElement(IConfigurationElement extension)
	{
		super(extension);
		delegate = new ExtensionClassWrapper<IModelFragmentEditorDelegate>(extension,
			FacilityConstants.ATT_CLASS);
		IConfigurationElement[] children = extension.getChildren(FacilityConstants.SELECTOR_ECLASS);
		if (children.length == 1)
		{
			selector = AbstractSelectorBuilder.createSelector(children[0]);
		}
		else
		{
			CessarPluginActivator.getDefault().logWarning(Messages.Children_element_required,
				new Object[] {extension});
		}
		String cacheable = extension.getAttribute(FacilityConstants.ATT_CACHEABLE);
		cacheableType = CacheableTypeEnum.getEnum(cacheable);
	}

	/**
	 * @return
	 */
	public IModelFragmentEditorDelegate getDelegate()
	{
		try
		{
			return delegate.getInstance();
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * @param mmService
	 * @param object
	 * @param clz
	 * @return
	 */
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz)
	{
		if (selector == null)
		{
			return false;
		}
		else
		{
			return selector.isSelected(mmService, object, clz, null);
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
