/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Apr 15, 2011 9:48:08 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;
import eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansionProvider;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidl6870
 * 
 */
public class RelationDefinitionElement implements IDefinitionElement
{
	private ExtensionClassWrapper<IModelFragmentEditorExpansionProvider> expansionProviderWrapper;
	private List<String> masterEditorIDList = new ArrayList<String>();

	private Map<String, String> propertiesMap = new HashMap<String, String>();

	/**
	 * 
	 * @param extension
	 */
	public RelationDefinitionElement(IConfigurationElement extension)
	{
		IConfigurationElement[] children = extension.getChildren(FacilityConstants.ATT_MASTER_EDITOR);
		for (IConfigurationElement el: children)
		{
			String id = el.getAttribute("masterEditorID"); //$NON-NLS-1$
			if (id != null)
			{
				masterEditorIDList.add(id);
			}
		}

		children = extension.getChildren(FacilityConstants.ATT_PROPERTIES);

		// extension.
		for (IConfigurationElement el: children)
		{
			String key = el.getAttribute("key"); //$NON-NLS-1$
			String value = el.getAttribute("value"); //$NON-NLS-1$
			propertiesMap.put(key, value);

		}

		expansionProviderWrapper = new ExtensionClassWrapper<IModelFragmentEditorExpansionProvider>(
			extension, FacilityConstants.ATT_PROVIDER);
	}

	public IModelFragmentEditorExpansionProvider getExpansionProvider()
	{

		try
		{
			return expansionProviderWrapper.getInstance();
		}
		catch (Throwable e) // SUPPRESS CHECKSTYLE change in future
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return null;
	}

	public List<String> getMasterEditorID()
	{
		return masterEditorIDList;
	}

	public Map<String, String> getProperties()
	{
		return propertiesMap;
	}
}
