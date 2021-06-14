/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 14, 2009 4:29:18 PM </copyright>
 */
package eu.cessar.ct.runtime.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6458
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{

	// CHECKSTYLE:OFF

	private static final String BUNDLE_NAME = Messages.class.getCanonicalName();

	public static String modelClassPathContainerDescription;

	public static String projectClassPathContainerDescription;

	public static String modelClassPathContainerName;

	public static String projectClassPathContainerName;

	public static String executingPluget;

	public static String runPluget;

	public static String dynamicWizardPageDescription;

	public static String editorNotImplemented;

	/**
	 * Represents the text that is written in the wizard page between the wizard page name,and the "Search" label from
	 * this wizard's page
	 */
	public static String descText;

	/**
	 * Represents the Generic text that is written in the wizard page name
	 */
	public static String genericText;

	// CHECKSTYLE:ON
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
