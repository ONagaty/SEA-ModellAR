/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 4, 2010 11:36:24 AM </copyright>
 */
package eu.cessar.ct.edit.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6458
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	// CHECKSTYLE:OFF

	// multi value dialog message
	public static String MultiValueDialog_available_values;
	public static String MultiValueDialog_button_add;
	public static String MultiValueDialog_button_down;
	public static String MultiValueDialog_button_remove;
	public static String MultiValueDialog_button_up;
	public static String MultiValueDialog_edit_value;
	public static String MultiValueDialog_options;
	public static String MultiValueDialog_title;

	// reference selection dialog messages

	public static String ReferenceSelectionDialog_case_sensitive;
	public static String ReferenceSelectionDialog_auto_filter;
	public static String ReferenceSelectionDialog_choose_object;
	public static String ReferenceSelectionDialog_filter;
	public static String ReferenceSelectionDialog_selected_not_Valid;
	public static String ReferenceSelectionDialog_title;

	// system instance reference

	public static String Dialog_show_candidates_for_incomplete_configs_label;
	public static String NoCandidatesForCompleteConfig;
	public static String CandidatesForIncompleteConfig;
	public static String ApplyFilterOnKeyPressed;
	public static String MessageDialog_no_targets;
	public static String MessageDialog_error_title;
	public static String Dialog_tree_column_prototype;
	public static String Dialog_tree_column_type;

	// commands operations labels

	public static String Command_removeValue;

	// Registry related messages

	public static String Unknowed_SelectorType;
	public static String Unknown_configuration_element;
	public static String Children_element_required;
	public static String Selector_not_of_the_right_type;
	public static String Error_in_selector_initialisation;
	public static String No_Regexp_specified;

	// Tree Editor

	public static String TreeEditor_showFeatureType;
	public static String TreeEditor_showFeatureName;

	/**
	 * Error message in the case that an editor input cannot be created for a target object.
	 */
	public static String TreeEditor_noEditorInput;

	// reference by editor part
	public static String ReferenceLoading_message;
	public static String ReferenceLabel;
	public static String MenuGoto;

	// Validation editor

	public static String ValidationButton_text;
	public static String ValidationNote_text;
	public static String ValidationCheckBox_text;

	public static String Validation_Problem_Type;
	public static String Validation_Description;
	public static String Validation_Object;
	public static String Validation_Uri;
	public static String Validation_Resource;
	public static String Validation_EObjectType;

	// CHECKSTYLE:ON

	static
	{
		NLS.initializeMessages(Messages.class.getCanonicalName(), Messages.class);
	}

}
