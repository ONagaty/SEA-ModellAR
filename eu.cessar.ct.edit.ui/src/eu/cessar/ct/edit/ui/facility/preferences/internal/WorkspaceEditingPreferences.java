/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Feb 10, 2012 1:38:25 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.preferences.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences;

/**
 * @author uidt2045
 *
 */
public class WorkspaceEditingPreferences extends AbstractEditingPreferences
{
	private String namespace;
	// HashMap<editorID,HASHMAP<preference,value>>
	private Map<String, Map<String, String>> cache;

	/**
	 * @param parent
	 * @param autosarRelease
	 * @param namespace
	 */
	public WorkspaceEditingPreferences(IEditingPreferences parent, AutosarReleaseDescriptor autosarRelease,
		String namespace)
	{
		super(parent);
		int ordinal = MetaModelUtils.getAutosarReleaseOrdinal(autosarRelease) / 10;

		// for compatibility reasons
		if (namespace == null)
		{
			namespace = CessarEditingPreferencesConstants.WORKSPACE_NAMESPACE;
		}

		this.namespace = namespace + ordinal;

		// first compute the namespace the load the cache
		loadMap();
	}

	/**
	 * loads all the preference information in the cache. Should be called only once (from the constructor).
	 */
	private void loadMap()
	{
		cache = new HashMap<String, Map<String, String>>();
		List<String> allKeys = AbstractPreferencesAccessor.getAllKeys(namespace);
		for (String key: allKeys)
		{
			int delimiter = key.indexOf(CessarEditingPreferencesConstants.DELIMITER);
			String editorID = key.substring(0, delimiter);
			String preference = key.substring(delimiter + 1);
			String value = EditingPreferencesAccessor.getKey(namespace, editorID, preference, null);

			addToCache(cache, editorID, preference, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#canChangePreferences()
	 */
	public boolean canChangePreferences()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.AbstractEditingPreferences#doCanChangePreferences(eu.cessar.ct.edit
	 * .ui.facility.IModelFragmentEditorProvider)
	 */
	@Override
	protected boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.AbstractEditingPreferences#doCanChangePreferences(eu.cessar.ct.edit
	 * .ui.facility.IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor, String key)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.AbstractEditingPreferences#doGetPreference(eu.cessar.ct.edit.ui.facility
	 * .IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected String doGetPreference(IModelFragmentEditorProvider editorProvider, String key)
	{
		// return EditingPreferencesAccessor.getKey(NAMESPACE, editor.getId(),
		// key, null);
		IModelFragmentEditor editor = editorProvider.createEditor();
		if (editor != null)
		{
			String editorInstanceID = editor.getInstanceId();
			int delimiter = editorInstanceID.indexOf(CessarEditingPreferencesConstants.DELIMITER);
			String edTrimmed = null;
			if (delimiter == -1)
			{
				edTrimmed = editorInstanceID;
			}
			else
			{
				edTrimmed = editorInstanceID.substring(0, delimiter);
			}
			Map<String, String> preferenceMap = cache.get(edTrimmed);
			if (preferenceMap == null)
			{
				return null;
			}
			else
			{
				String fullKey = editorInstanceID.substring(delimiter + 1, editorInstanceID.length())
					+ CessarEditingPreferencesConstants.DELIMITER + key;
				return preferenceMap.get(fullKey);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.AbstractEditingPreferences#doSetPreference(eu.cessar.ct.edit.ui.facility
	 * .IModelFragmentEditorProvider, java.lang.String, java.lang.String)
	 */
	@Override
	protected void doSetPreference(IModelFragmentEditorProvider editorProvider, String key, String value)
	{
		IModelFragmentEditor editor = editorProvider.createEditor();
		if (editor != null)
		{
			String editorInstanceID = editor.getInstanceId();
			int delimiter = editorInstanceID.indexOf(CessarEditingPreferencesConstants.DELIMITER);
			String keyAdapter = editorInstanceID.substring(delimiter + 1) + CessarEditingPreferencesConstants.DELIMITER
				+ key;
			String editorName = editor.getTypeId();
			EditingPreferencesAccessor.setKey(namespace, editorInstanceID, key, value);
			// update the MAP
			addToCache(cache, editorName, keyAdapter, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.AbstractEditingPreferences#doUnsetPreference(eu.cessar.ct.edit.ui.facility
	 * .IModelFragmentEditorProvider, java.lang.String)
	 */
	@Override
	protected void doUnsetPreference(IModelFragmentEditorProvider editorProvider, String key)
	{
		IModelFragmentEditor editor = editorProvider.createEditor();
		if (editor != null)
		{
			String editorInstanceID = editor.getInstanceId();
			int delimiter = editorInstanceID.indexOf(CessarEditingPreferencesConstants.DELIMITER);
			String keyAdapter = editorInstanceID.substring(delimiter + 1) + CessarEditingPreferencesConstants.DELIMITER
				+ key;

			String editorName = editor.getTypeId();
			EditingPreferencesAccessor.setKey(namespace, editorInstanceID, key, null);
			// remove from MAP
			Map<String, String> preferenceMap = cache.get(editorName);
			if (preferenceMap != null)
			{
				preferenceMap.remove(keyAdapter);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#resetAllPreferences()
	 */
	public void resetAllPreferences()
	{
		EditingPreferencesAccessor.removeAllPreferences(namespace);
		cache.clear();
	}

}
