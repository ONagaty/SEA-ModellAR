package eu.cessar.ct.edit.ui.facility.preferences.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences;
import eu.cessar.ct.edit.ui.facility.preferences.IPreferenceListeners;

/**
 * 
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4449 %
 * 
 *         %date_created: Fri Aug 29 11:34:30 2014 %
 * 
 *         %version: 5 %
 */
public abstract class AbstractEditingPreferences implements IEditingPreferences
{
	private List<IPreferenceListeners> listeners;
	private IEditingPreferences parent;

	/**
	 * @param parent
	 */
	protected AbstractEditingPreferences(IEditingPreferences parent)
	{
		this.parent = parent;
		listeners = new ArrayList<IPreferenceListeners>();
	}

	public IEditingPreferences getParentPreferences()
	{
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#addListeners(eu.cessar.ct.edit.ui.facility.preferences
	 * .IPreferenceListeners)
	 */
	public void addListener(IPreferenceListeners listener)
	{
		if (listener != null)
		{
			listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#removeListener(eu.cessar.ct.edit.ui.facility.
	 * preferences.IPreferenceListeners)
	 */
	public void removeListener(IPreferenceListeners listener)
	{
		if (listener != null)
		{
			listeners.remove(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#canChangePreferences(eu.cessar.ct.edit.ui.facility
	 * .IModelFragmentEditorProvider)
	 */
	public boolean canChangePreferences(IModelFragmentEditorProvider editor)
	{
		if (this.canChangePreferences())
		{
			String preference = doGetPreference(editor, CessarEditingPreferencesConstants.PREF_EDITOR_CHANGEABLE);
			if (preference != null)
			{
				return Boolean.valueOf(preference);
			}
			else
			{
				return getDefaultCanChangePreferences(editor);
			}
		}
		return false;
	}

	/**
	 * @param editor
	 * @return boolean
	 */
	protected abstract boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#canChangePreferences(eu.cessar.ct.edit.ui.facility
	 * .IModelFragmentEditorProvider, java.lang.String)
	 */
	public boolean canChangePreferences(IModelFragmentEditorProvider editor, String key)
	{
		if (this.canChangePreferences() && this.canChangePreferences(editor))
		{
			String preference = doGetPreference(editor, key + "." //$NON-NLS-1$
				+ CessarEditingPreferencesConstants.PREF_EDITOR_CHANGEABLE);
			if (preference != null)
			{
				return Boolean.valueOf(preference);
			}
			else
			{
				return getDefaultCanChangePreferences(editor, key);
			}
		}
		return false;
	}

	public void setCanChangePreferences(IModelFragmentEditorProvider editor, String key, boolean value)
		throws Exception
	{
		if (this.canChangePreferences() && this.canChangePreferences(editor))
		{
			this.setPreference(editor, key + "." //$NON-NLS-1$
				+ CessarEditingPreferencesConstants.PREF_EDITOR_CHANGEABLE, value);
		}
		else
		{
			throw new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_WRITING_PRIVILEGES);
		}
	}

	public void setCanChangePreferences(IModelFragmentEditorProvider editor, boolean value) throws Exception
	{
		if (this.canChangePreferences())
		{
			doSetPreference(editor, CessarEditingPreferencesConstants.PREF_EDITOR_CHANGEABLE, String.valueOf(value));
		}
		else
		{
			throw new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_WRITING_PRIVILEGES);
		}
	}

	/**
	 * @param editor
	 * @param key
	 * @return boolean
	 */
	protected abstract boolean getDefaultCanChangePreferences(IModelFragmentEditorProvider editor, String key);

	/**
	 * @param editor
	 * @param key
	 * @return String
	 */
	protected abstract String doGetPreference(IModelFragmentEditorProvider editor, String key);

	public String getPreference(IModelFragmentEditorProvider editor, String key, String def)
	{
		String value = doGetPreference(editor, key);
		if (value == null)
		{
			if (parent != null)
			{
				value = parent.getPreference(editor, key, def);
			}
			else
			{
				value = def;
			}
		}
		return value;
	}

	public boolean getPreference(IModelFragmentEditorProvider editor, String key, boolean def)
	{
		String value = getPreference(editor, key, (String) null);
		if (value == null)
		{
			return def;
		}
		else
		{
			return Boolean.valueOf(value);
		}
	}

	public int getPreference(IModelFragmentEditorProvider editor, String key, int def)
	{
		String value = getPreference(editor, key, (String) null);
		if (value == null)
		{
			return def;
		}
		else
		{
			return Integer.valueOf(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#setPreference(eu.cessar.ct.edit.ui.facility.
	 * IModelFragmentEditorProvider, java.lang.String, java.lang.String)
	 */
	public void setPreference(IModelFragmentEditorProvider editor, String key, String value) throws Exception
	{
		if (this.canChangePreferences() && this.canChangePreferences(editor) && this.canChangePreferences(editor, key))
		{
			notifyListeners(editor, key, value);
			doSetPreference(editor, key, value);
		}
		else
		{
			// throw exception
			throw new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_WRITING_PRIVILEGES);
		}

	}

	/**
	 * Sets the preference for the given editor with the given value <br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not have writing privileges on the given editor
	 * or preference.
	 * 
	 * @param editor
	 * @param key
	 * @param value
	 */
	protected abstract void doSetPreference(IModelFragmentEditorProvider editor, String key, String value);

	public void setPreference(IModelFragmentEditorProvider editor, String key, boolean value) throws Exception
	{
		setPreference(editor, key, String.valueOf(value));
	}

	public void setPreference(IModelFragmentEditorProvider editor, String key, int value) throws Exception
	{
		setPreference(editor, key, String.valueOf(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.preferences.IEditingPreferences#unsetPreference(eu.cessar.ct.edit.ui.facility.
	 * IModelFragmentEditorProvider, java.lang.String)
	 */
	public void unsetPreference(IModelFragmentEditorProvider editor, String key) throws Exception
	{
		if (this.canChangePreferences() && this.canChangePreferences(editor) && this.canChangePreferences(editor, key))
		{
			notifyListeners(editor, key, null);
			doUnsetPreference(editor, key);
		}
		else
		{
			// throw exception
			throw new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_WRITING_PRIVILEGES);
		}
	}

	/**
	 * Removes the given preference for the given editor from this instance of IEditingPreferences, it does not look at
	 * the parents of the IEditingPreferences.<br>
	 * It is <b>not</b> recursive like the get methods. <br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not have writing privileges on the given editor
	 * or preference.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 */
	protected abstract void doUnsetPreference(IModelFragmentEditorProvider editor, String key);

	/**
	 * Takes each listener in the listeners list and calls the method preferenceChanged()
	 * 
	 * @param editor
	 * @param key
	 * @param value
	 */
	private void notifyListeners(IModelFragmentEditorProvider editor, String key, String value)
	{
		String oldValue = this.getPreference(editor, key, null);
		for (IPreferenceListeners listener: listeners)
		{
			listener.preferenceChanged(editor, key, oldValue, value);
		}
	}

	/**
	 * @param cache
	 * @param editor
	 * @param key
	 * @param value
	 */
	protected void addToCache(Map<String, Map<String, String>> cache, String editor, String key, String value)
	{
		int delimiter = editor.indexOf(CessarEditingPreferencesConstants.DELIMITER);
		String preference = editor.substring(delimiter + 1);
		Map<String, String> preferenceMap = cache.get(preference);
		if (preferenceMap == null)
		{
			preferenceMap = new HashMap<String, String>();
			preferenceMap.put(key, value);
		}
		else
		{
			preferenceMap.put(key, value);
		}
		cache.put(editor, preferenceMap);
	}
}
