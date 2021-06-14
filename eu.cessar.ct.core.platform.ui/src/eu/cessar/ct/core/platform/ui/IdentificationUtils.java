/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 May 19, 2010 10:49:18 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui;

/**
 * @author uidl6870
 *
 */
public class IdentificationUtils
{
	public static final String KEY_NAME = "name"; //$NON-NLS-1$

	// <control_id>=<prefix_id>#<suffix_id>

	public static final String CAPTION_PART_ID = "#caption"; //$NON-NLS-1$
	public static final String VALIDATOR_PART_ID = "#validator"; //$NON-NLS-1$
	public static final String ACTION_PART_ID = "#action"; //$NON-NLS-1$

	public static final String TEXT_ID = "text"; //$NON-NLS-1$
	public static final String LABEL_ID = "label"; //$NON-NLS-1$
	public static final String COMBO_ID = "combo"; //$NON-NLS-1$
	public static final String LIST_ID = "list"; //$NON-NLS-1$
	public static final String CHECK_BUTTON_ID = "check"; //$NON-NLS-1$
	public static final String PUSH_BUTTON_ID = "push"; //$NON-NLS-1$
	public static final String TABLE_ID = "table"; //$NON-NLS-1$
	public static final String DATE_ID = "date.time#date"; //$NON-NLS-1$
	public static final String TIME_ID = "date.time#time"; //$NON-NLS-1$

	/**
	 * prefix IDs for add and remove button that appear inside the editor for multi instance reference
	 */
	public static final String ADD_BUTTON = PUSH_BUTTON_ID + "#add"; //$NON-NLS-1$
	public static final String REMOVE_BUTTON = PUSH_BUTTON_ID + "#remove"; //$NON-NLS-1$

	public static final String MIN_TEXT = TEXT_ID + "#min"; //$NON-NLS-1$
	public static final String MAX_TEXT = TEXT_ID + "#max"; //$NON-NLS-1$

	/**
	 * prefix IDs for the 2 text fields and the 2 labels that appear when editing a single instance reference
	 */
	public static final String CONTEXT_TEXT_ID = TEXT_ID + "#context"; //$NON-NLS-1$
	public static final String TARGET_TEXT_ID = TEXT_ID + "#target"; //$NON-NLS-1$
	public static final String CONTEXT_LABEL_ID = LABEL_ID + "#context"; //$NON-NLS-1$
	public static final String TARGET_LABEL_ID = LABEL_ID + "#target"; //$NON-NLS-1$
}
