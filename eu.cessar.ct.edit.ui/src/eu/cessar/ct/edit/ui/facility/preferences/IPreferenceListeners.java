/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Feb 10, 2012 11:56:16 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.preferences;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;

/**
 * @author uidt2045
 * 
 */
public interface IPreferenceListeners
{
	/**
	 * This event is thrown before the modification will occur. If newValue is
	 * <b>null</b> then the value has been unset
	 * 
	 * @param editor
	 * @param preference
	 * @param oldValue
	 * @param newValue
	 */
	public void preferenceChanged(IModelFragmentEditorProvider editor, String preference,
		String oldValue, String newValue);

}
