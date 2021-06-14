/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jun 7, 2012 2:23:05 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.preferences.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.preferences.ICacheEditingPreference;
import eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidt2045
 * 
 */
public class CacheEditingPreference extends AbstractEditingPreferences implements
	ICacheEditingPreference
{

	private Map<IModelFragmentEditorProvider, Map<String, String>> cache;

	/**
	 * @param parent
	 */
	public CacheEditingPreference(IEditingPreferences parent)
	{
		super(parent);
		cache = new HashMap<IModelFragmentEditorProvider, Map<String, String>>();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#canChangePreferences()
	 */
	public boolean canChangePreferences()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#resetAllPreferences()
	 */
	public void resetAllPreferences()
	{
		cache.clear();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.ICacheEditingPreference#commit()
	 */
	public void commit()
	{
		for (IModelFragmentEditorProvider editorProvider: cache.keySet())
		{
			IEditingPreferences parentPreferences = getParentPreferences();
			Map<String, String> hashMap = cache.get(editorProvider);
			for (String key: hashMap.keySet())
			{
				try
				{
					((AbstractEditingPreferences) parentPreferences).setPreference(editorProvider,
						key, hashMap.get(key));
				}
				catch (Exception e) // SUPPRESS CHECKSTYLE change in future
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}

	}

	/* (non-Javadoc)
	* @see eu.cessar.ct.edit.ui.facility.preferences.internal.AbstractEditingPreferences#getDefaultCanChangePreferences(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider)
	*/
	@Override
	protected boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.internal.AbstractEditingPreferences#getDefaultCanChangePreferences(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor, String key)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.internal.AbstractEditingPreferences#doGetPreference(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected String doGetPreference(IModelFragmentEditorProvider editor, String key)
	{
		IModelFragmentEditor givenEditor = editor.createEditor();
		if (givenEditor != null)
		{
			String instanceID = givenEditor.getInstanceId();
			IModelFragmentEditorProvider prov = getProviderFromCache(instanceID);
			if (prov != null)
			{
				Map<String, String> map = cache.get(prov);
				return map.get(key);
			}
		}
		return null;
	}

	private IModelFragmentEditorProvider getProviderFromCache(String instanceID)
	{
		Set<IModelFragmentEditorProvider> keySet = cache.keySet();
		for (IModelFragmentEditorProvider editorProvider: keySet)
		{
			IModelFragmentEditor edt = editorProvider.createEditor();
			if (edt != null)
			{
				if (edt.getInstanceId().equals(instanceID))
				{
					return editorProvider;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	* @see eu.cessar.ct.edit.ui.facility.preferences.internal.AbstractEditingPreferences#doSetPreference(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider, java.lang.String, java.lang.String)
	*/
	@Override
	protected void doSetPreference(IModelFragmentEditorProvider editor, String key, String value)
	{
		IModelFragmentEditor givenEditor = editor.createEditor();
		if (givenEditor != null)
		{
			IModelFragmentEditorProvider providerFromCache = getProviderFromCache(givenEditor.getInstanceId());
			Map<String, String> hashMap;
			if (providerFromCache != null)
			{
				hashMap = cache.get(providerFromCache);
			}
			else
			{
				hashMap = new HashMap<String, String>();
			}
			hashMap.put(key, value);
			cache.put(editor, hashMap);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.preferences.internal.AbstractEditingPreferences#doUnsetPreference(eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected void doUnsetPreference(IModelFragmentEditorProvider editor, String key)
	{

		IModelFragmentEditor givenEditor = editor.createEditor();
		if (givenEditor != null)
		{
			IModelFragmentEditorProvider providerFromCache = getProviderFromCache(givenEditor.getInstanceId());
			if (providerFromCache != null)
			{
				Map<String, String> hashMap = cache.get(providerFromCache);
				hashMap.remove(key);
			}
		}
	}

}
