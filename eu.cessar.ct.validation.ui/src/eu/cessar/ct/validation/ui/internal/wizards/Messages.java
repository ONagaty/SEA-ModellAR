/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Oct 20, 2014 9:50:15 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for the validation wizard
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Tue Jan 20 14:41:32 2015 %
 * 
 *         %version: 3 %
 */
public final class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.validation.ui.internal.wizards.messages"; //$NON-NLS-1$
	// CHECKSTYLE:OFF not needed
	/** FilteredAutosarValidation Wizard Title */
	public static String filteredAutosarValidationWizardTitle;
	/** FilteredAutosarValidation Page Title */
	public static String filteredAutosarValidationPageTitle;
	/** SelectAll Button Text */
	public static String selectAllButtonText;
	/** DeselectAll Button Text */
	public static String deselectAllButtonText;
	/** Hide Wizard Text */
	public static String hideWizardText;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
