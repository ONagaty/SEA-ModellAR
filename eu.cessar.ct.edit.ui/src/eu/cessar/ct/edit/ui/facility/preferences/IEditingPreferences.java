package eu.cessar.ct.edit.ui.facility.preferences;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;

public interface IEditingPreferences
{

	/**
	 * @param listener
	 */
	public void addListener(IPreferenceListeners listener);

	/**
	 * @param listener
	 */
	public void removeListener(IPreferenceListeners listener);

	/**
	 * @return The parent of this IEditingPreference<br>
	 *         or<br>
	 *         null if it does not have one
	 */
	public IEditingPreferences getParentPreferences();

	/**
	 * @return <b>true</b> if this implementation of IEditingPreferences has
	 *         preference writing privileges<br>
	 *         <b>false</b> if it does not have writing privileges.
	 */
	public boolean canChangePreferences();

	/**
	 * @param editor
	 *        the editor on which we ask if this implementation of
	 *        IEditingPreferences has writing privileges
	 * @return <b>true</b> if this implementation of IEditingPreferences has
	 *         preference writing privileges over this specific editor<br>
	 *         <b>false</b> if it does not have writing privileges over this
	 *         specific editor.<br>
	 */
	public boolean canChangePreferences(IModelFragmentEditorProvider editor);

	/**
	 * @param editor
	 *        the editor on which we ask if this implementation of
	 *        IEditingPreferences has writing privileges
	 * @param key
	 *        the specific preference that we want to edit
	 * @return <b>true</b> if this IEditingPreferences has preference writing
	 *         privileges over this specific editor and preference<br>
	 *         <b>false</b> if this IEditingPreferences does not have preference
	 *         writing privileges over this specific editor and preference<br>
	 */
	public boolean canChangePreferences(IModelFragmentEditorProvider editor, String key);

	/**
	 * Sets the canChangePreference for the given editor with the given value.<br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges on the editor.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param value
	 *        the value of the preference
	 */
	public void setCanChangePreferences(IModelFragmentEditorProvider editor, String key,
		boolean value) throws Exception;

	/**
	 * Sets the canChangePreference for the given editor with the given value.<br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges.
	 * 
	 * @param editor
	 *        the editor
	 * @param value
	 *        the value of the preference
	 */
	public void setCanChangePreferences(IModelFragmentEditorProvider editor, boolean value)
		throws Exception;

	// get
	/**
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param def
	 *        the default value that will be return in case the given preference
	 *        is not set in any of the parent IEditingPreferences.
	 * @return the preference for the given editor. If the current
	 *         IEditingPreferences does not have it set, it searches in its
	 *         parent and so on. If it is not found the the given <b>def</b>
	 *         parameter will be returned
	 */
	public String getPreference(IModelFragmentEditorProvider editor, String key, String def);

	/**
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param def
	 *        the default value that will be return in case the given preference
	 *        is not set in any of the parent IEditingPreferences.
	 * @return the preference for the given editor. If the current
	 *         IEditingPreferences does not have it set, it searches in its
	 *         parent and so on. If it is not found the the given <b>def</b>
	 *         parameter will be returned
	 */
	public boolean getPreference(IModelFragmentEditorProvider editor, String key, boolean def);

	/**
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param def
	 *        the default value that will be return in case the given preference
	 *        is not set in any of the parent IEditingPreferences.
	 * @return the preference for the given editor. If the current
	 *         IEditingPreferences does not have it set, it searches in its
	 *         parent and so on. If it is not found the the given <b>def</b>
	 *         parameter will be returned
	 */
	public int getPreference(IModelFragmentEditorProvider editor, String key, int def);

	// set
	/**
	 * Sets the preference for the given editor with the given value.<br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges on the given editor or preference.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param value
	 *        the value of the preference
	 */
	public void setPreference(IModelFragmentEditorProvider editor, String key, String value)
		throws Exception;

	/**
	 * Sets the preference for the given editor with the given value.<br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges on the given editor or preference.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param value
	 *        the value of the preference
	 */
	public void setPreference(IModelFragmentEditorProvider editor, String key, boolean value)
		throws Exception;

	/**
	 * Sets the preference for the given editor with the given value <br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges on the given editor or preference.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 * @param value
	 *        the value of the preference
	 */
	public void setPreference(IModelFragmentEditorProvider editor, String key, int value)
		throws Exception;

	// unset
	/**
	 * Removes the given preference for the given editor from this instance of
	 * IEditingPreferences, it does not look at the parents of the
	 * IEditingPreferences.<br>
	 * It is <b>not</b> recursive like the get methods. <br>
	 * It will throw an <b>exception</b> if the IEditingPreferences does not
	 * have writing privileges on the given editor or preference.
	 * 
	 * @param editor
	 *        the editor
	 * @param key
	 *        the preference
	 */
	public void unsetPreference(IModelFragmentEditorProvider editor, String key) throws Exception;

	/**
	 * clears all the preferences saved in the current PreferenceAccessor
	 */
	public void resetAllPreferences();

	// /**
	// * Removes the given preference for the given editor from this instance of
	// * IEditingPreferences, if the current IEditingPreference does not have
	// the
	// * preference set it looks at its parent<br>
	// *
	// * It will throw an <b>exception</b> if the IEditingPreferences that has
	// the
	// * preference set does not have writing privileges on the given editor or
	// * preference.
	// **/
	// public void unsetPreferenceRecursively(IModelFragmentEditorProvider
	// editor, String key);

}
