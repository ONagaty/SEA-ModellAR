/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * May 7, 2014 1:02:17 PM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Messages class for the workspace wizards
 */
public final class Messages extends NLS
{
	/**
	 * Informative message describing the project phases.
	 */
	public static String projectVariantInfo;
	/**
	 * Message regarding model API
	 */
	public static String autosarReleaseMessage;
	/**
	 * Information message for full compatibility mode
	 */
	public static String fullCompatibilityModeInfo;
	/**
	 * Information message for no compatibility mode
	 */
	public static String noCompatibilityModeInfo;

	/**
	 * Release label for the wizard group
	 */
	public static String label_metaModelReleaseCT;

	private static final String BUNDLE_NAME = "eu.cessar.ct.workspace.ui.internal.wizards.messages"; //$NON-NLS-1$
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
