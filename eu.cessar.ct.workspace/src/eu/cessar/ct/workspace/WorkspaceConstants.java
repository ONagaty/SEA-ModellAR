/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 21, 2014 6:08:32 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace;

/**
 * Constants used in the workspace.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Tue Jun 23 10:45:43 2015 %
 * 
 *         %version: RAUTOSAR~3 %
 */
public final class WorkspaceConstants
{

	// Consistency check
	/**
	 * Name of consistency check contribution, from the plugin registry
	 */
	public static final String EXTENSION_CONSISTENCY_CHECK = "eu.cessar.ct.consistency.check"; //$NON-NLS-1$

	/**
	 * Configuration element mapping, from the plugin registry
	 */
	public static final String ELEMENT_CONSISTENCY_CHECK = "consistencyCheck"; //$NON-NLS-1$

	/**
	 * Attribute id of a consistency check, from the plugin registry
	 */
	public static final String ATT_ID = "id"; //$NON-NLS-1$

	/**
	 * Attribute name of a consistency check, from the plugin registry
	 */
	public static final String ATT_NAME = "name"; //$NON-NLS-1$

	/**
	 * Attribute description of a consistency check, from the plugin registry
	 */
	public static final String ATT_DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * Attribute description of a consistency checker class, from the plugin registry
	 */
	public static final String ATT_CHECKER_CLASS = "checkerClass"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for duplicate module def.
	 */
	public static final String CHECK_DUPLICATE_MODULE_DEF_ID = "eu.cessar.ct.consistencycheck.duplicatemd"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for wrong metamodel.
	 */
	public static final String CHECK_WRONG_MODEL_ID = "eu.cessar.ct.consistencycheck.wrongmodel"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for Jet files
	 */
	public static final String CHECK_JET_VERIFICATION_ID = "eu.cessar.ct.consistencycheck.jetVerification"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for .project
	 */
	public static final String CHECK_PROJECT_VERIFICATION_ID = "eu.cessar.ct.consistencycheck.projectVerification"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for .classpath
	 */
	public static final String CHECK_CLASSPATH_VERIFICATION_ID = "eu.cessar.ct.consistencycheck.classpathVerification"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for .settings
	 */
	public static final String CHECK_SETTINGS_VERIFICATION_ID = "eu.cessar.ct.consistencycheck.settingsVerification"; //$NON-NLS-1$

	/**
	 * Id of the consistency check for pmbin folder
	 */
	public static final String CHECK_PMBIN_VERIFICATION_ID = "eu.cessar.ct.consistencycheck.pmbinFolderVerification"; //$NON-NLS-1$

	// utility class, cannot be instantiated
	private WorkspaceConstants()
	{

	}

}
