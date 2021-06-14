/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidv3687<br/>
 * May 16, 2013 12:43:00 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.security.descriptor;

/**
 * Utility class for handling the possible AUTOSAR MetaModel descriptors
 *
 * @author uidv3687
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Fri Oct 3 09:15:48 2014 %
 *
 *         %version: 5 %
 */
public final class MMDescriptorUtils
{
	/**
	 * Array of <code>MMDescriptor</code > storing all possible AUTOSAR 4X revisions
	 */
	public final static MMDescriptor[] MM_DESCRIPTORS_4 = new MMDescriptor[] {new MMDescriptor(402),
		new MMDescriptor(403), new MMDescriptor(411), new MMDescriptor(412), new MMDescriptor(421),
		new MMDescriptor(422), new MMDescriptor(430), new MMDescriptor(444)};
	// new MMDescriptor(442), new MMDescriptor(443), new MMDescriptor(445)
	/**
	 * Array of <code>MMDescriptor</code > storing all possible AUTOSAR 3X revisions
	 */
	public final static MMDescriptor[] MM_DESCRIPTORS_3 = new MMDescriptor[] {new MMDescriptor(315),
		new MMDescriptor(321), new MMDescriptor(322)};

	/**
	 * System property storing AUTOSAR 40 active revision
	 */
	public final static String SYS_PROP_MM40 = "cessar.ActiveAutosarMetamodel.40"; //$NON-NLS-1$

	/**
	 * System property storing AUTOSAR 4x active revision
	 */
	public final static String SYS_PROP_MM4X = "cessar.ActiveAutosarMetamodel.4x"; //$NON-NLS-1$

	/**
	 * System property storing AUTOSAR 3x active revision
	 */
	public final static String SYS_PROP_MM3X = "cessar.ActiveAutosarMetamodel.3x"; //$NON-NLS-1$

	/**
	 *
	 */
	private MMDescriptorUtils()
	{
		// nothing
	}

	/**
	 * @return the default descriptor for meta-model 3
	 */
	public static MMDescriptor getDefaultMMDescriptor3X()
	{
		return MM_DESCRIPTORS_3[1];
	}

	/**
	 * @return the default descriptor for meta-model 4
	 */
	public static MMDescriptor getDefaultMMDescriptor4X()
	{
		return MM_DESCRIPTORS_4[1];
	}
}
