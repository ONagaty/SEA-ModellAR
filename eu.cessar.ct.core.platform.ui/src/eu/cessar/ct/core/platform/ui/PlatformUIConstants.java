/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui;

import org.eclipse.swt.graphics.Color;

/**
 * This interface should contains CESSAR-CT common platform UI constants.
 *
 * @Review uidl6458 - 19.04.2012
 *
 */
public interface PlatformUIConstants
{
	/**
	 *
	 */
	public static final String IMAGE_COLLAPSE_ALL = "collapse_all_mini.gif"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String IMAGE_EXPAND_ALL = "expandall.gif"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_LINK_WITH_EDITOR = "synced.gif"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_MULTI_VALUE = "multi_value.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_BOOLEAN = "boolean.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_INTEGER = "integer.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_LONG = "long.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_FLOAT = "float.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_ENUMERATION = "enum.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_STRING = "string.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_DATE_TIME = "date_time.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_PRIMITIVE = "primitive.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_MULTIPLICITY = "multiplicity.icon"; //$NON-NLS-1$

	/**  */
	public static final String IMAGE_ID_INTEGER_RADIX = "integer_radix.icon"; //$NON-NLS-1$

	/** */
	public static final String MODEL_VIEW_ID = "eu.cessar.ct.explorer.ui.CessarModelView"; //$NON-NLS-1$

	/** */
	public static final Color EDITOR_ERROR_COLOR = new Color(null, 255, 0, 0);

	/** */
	public static final Color CELL_EDITOR_COLOR = new Color(null, 250, 160, 28);

	/** */
	public static final String EDITOR_ERROR_MESSAGE = "No editor found! Using SingleGenericEditor."; //$NON-NLS-1$

	/** breadcrumb */

	public final static String KEY_ARROW_RIGHT = "arrow_right.png"; //$NON-NLS-1$

	public final static String KEY_ARROW_LEFT = "arrow_left.png"; //$NON-NLS-1$

	public final static String KEY_ARROW_DOWN = "arrow_down.png"; //$NON-NLS-1$

	// file system - temporary

	/**
	 * The key name for a computer.
	 */
	public final static String KEY_COMPUTER = "computer.png"; //$NON-NLS-1$

	/**
	 * The key name for a normal drive.
	 */
	public final static String KEY_DRIVE_DEFAULT = "drive_default.png"; //$NON-NLS-1$

	/**
	 * The key name for a hidden drive.
	 */
	public final static String KEY_DRIVE_HIDDEN = "drive_hidden.png"; //$NON-NLS-1$

	/**
	 * The key name for a normal folder.
	 */
	public final static String KEY_FOLDER_DEFAULT = "folder_default.png"; //$NON-NLS-1$

	/**
	 * The key name for a hidden folder.
	 */
	public final static String KEY_FOLDER_HIDDEN = "folder_hidden.png"; //$NON-NLS-1$

	/**
	 * The key name for a normal file.
	 */
	public final static String KEY_FILE_DEFAULT = "file_default.png"; //$NON-NLS-1$

	/**
	 * The key name for a hidden file.
	 */
	public final static String KEY_FILE_HIDDEN = "file_hidden.png"; //$NON-NLS-1$

	/**
	 * The key name for any file extension filter
	 */
	public final static String ANY_FILE_EXTENSION_KEY = "any"; //$NON-NLS-1$

	/**
	 * The key name for folder filter
	 */
	public final static String FOLDER_FILTER_KEY = "folder"; //$NON-NLS-1$

	/**
	 * Empty string
	 */
	public final static String EMPTY_STRING = ""; //$NON-NLS-1$

}
