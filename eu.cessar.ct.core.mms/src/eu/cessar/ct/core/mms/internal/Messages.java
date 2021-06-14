/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 20, 2009 6:18:45 PM </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6458
 *
 *         CHECKSTYLE:OFF
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	public static String VALIDATION_ECUC_DEF_OK;
	public static String VALIDATION_ECUC_ELEM_IS_PROXY;
	public static String VALIDATION_ECUC_DEF_IS_PROXY;
	public static String VALIDATION_ECUC_ELEM_IS_NULL;
	public static String VALIDATION_ECUC_DEF_IS_NULL;
	public static String VALIDATION_ECUC_PARENTS_ARE_DIFF;
	public static String VALIDATION_ECUC_ELEMENTS_ARE_DIFF;
	public static String VALIDATION_ECUC_PARENT_OF_ELEM_NOT_VALID;
	public static String VALIDATION_ECUC_PARENT_OF_ELEM_NULL;
	public static String VALIDATION_ECUC_PARENT_OF_DEF_NULL;
	public static String VALIDATION_ECUC_PARENT_OF_DEF_NOT_VALID;
	public static String VALIDATION_ECUC_DEF_NOT_VALID;

	public static String MMSRegistry_Invalid_Extension_Class;
	public static String MMSRegistry_MM_Already_Set;
	public static String MMSRegistry_Unknown_MM;

	public static String subtask_savingResource;

	public static String SplitAPI_argumentNotMergedObject;
	public static String SplitAPI_argumentNotAmongConcreteInstances;
	public static String SplitAPI_argumentNotAmongInstanceResources;

	public static String DEST_URI_POLICY;

	/**
	 * null childObject was supplied to getParent.
	 */
	public static String MDL_NULL_CHILD_OBJECT;

	/**
	 * null childClass was supplied to getChildren.
	 */
	public static String MDL_NULL_CHILD_CLASS;

	/**
	 * null parentObject was supplied to getChildren.
	 */
	public static String MDL_NULL_PARENT_OBJECT;

	/**
	 * null parentClass was supplied to getParent.
	 */
	public static String MDL_NULL_PARENT_CLASS;

	/**
	 * null fromObject was supplied to getForwardReferences.
	 */
	public static String MDL_NULL_FROM_OBJECT;

	/**
	 * null toClass was supplied to getForwardReferences.
	 */
	public static String MDL_NULL_TO_CLASS;

	/**
	 * null toClass was supplied to getForwardReferences.
	 */
	public static String MDL_NULL_TO_OBJECT;

	/**
	 * null {@code IMDLFilter} supplied to a {@code ModelDepedencyLookup} method.
	 */
	public static String MDL_NULL_FILTER;

	/**
	 * No editing domain for a project
	 */
	public static String MONITOR_NO_EDITING_DOMAIN;

	public static String RESOURCESET_NOT_IModelChangeStampProvider;

	private final static String BUNDLE_NAME = Messages.class.getCanonicalName();

	public static String AR;
	public static String AR_REGULAR_EXPRESSION;
	public static String AUTOSAR;
	public static String AUTOSAR_SPACE;
	public static String AUTOSAR_XML_FILE;
	public static String BLANK;
	public static String DOT;
	public static String XML_FILE;

	// CHECKSTYLE:ON

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
