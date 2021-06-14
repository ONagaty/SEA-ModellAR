/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 12, 2010 10:47:16 AM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui;

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
	public static String BreadcrumbItemDropDown_Action_ToolTip;

	public static String ContainerSelectionDialog_title;
	public static String ContainerSelectionDialog_message;

	public static String ContainerGroup_message;
	public static String ContainerGroup_selectFolder;

	// Table viewer
	public static String FirstBreadcrumbItem_label;

	public static String no_Projects_title;
	public static String no_CESSAR_Projects_in_Workspace;
	public static String no_Project_Selected_title;
	public static String select_Project;

	public static String project_Nature_title;
	public static String nature_not_CESSAR;

	public static String project_Not_Opened_title;
	public static String open_Selected_Project;

	public static String ButtonAutoFilter;
	public static String LabelFilter;
	public static String ButtonCaseSensitive;

	public static String MESSAGE_NO_WORKFLOW;
	// CHECKSTYLE:ON
	static
	{
		NLS.initializeMessages(Messages.class.getCanonicalName(), Messages.class);
	}

}
