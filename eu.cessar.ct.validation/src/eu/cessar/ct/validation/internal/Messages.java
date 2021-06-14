/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jul 9, 2012 11:23:23 AM </copyright>
 */
package eu.cessar.ct.validation.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidt2045
 *
 */
public final class Messages extends NLS
{
	// CHECKSTYLE:OFF
	/**
	 *
	 */
	public static String CessarValidationFlag_ON_DEMAND;
	/**
	 *
	 */
	public static String CessarValidationFlag_NONE;

	/**
	 *
	 */
	public static String splitted_sameResource;
	/**
	 *
	 */
	public static String splitted_notAllowed;
	/**
	 *
	 */
	public static String splitted_differentEClass;
	/**
	 *
	 */
	public static String splitted_differentDefinition;
	/**
	 *
	 */
	public static String splitted_inconsistentValues_System;
	/**
	 *
	 */
	public static String splitted_inconsistentValues_Ecuc_param;
	/**
	 *
	 */
	public static String splitted_inconsistentValues_Ecuc_ref;

	/**
	 *
	 */
	public static String API_validation_results;
	/**
	 *
	 */
	public static String API_updating_markers;
	/**
	 *
	 */
	public static String API_update_markers_event;
	/**
	 *
	 */
	public static String API_validation_null_arguments;
	/**
	 *
	 */
	public static String API_validation_source;

	/**
	 * Argument name
	 */
	public static String arg_Fileinclude_Name;
	/**
	 * File include argument description
	 */
	public static String arg_Fileinclude_Desc;

	/**
	 * Argument name for URI
	 */
	public static String arg_UriInclude_Name;
	/**
	 * Uri include argument description
	 */
	public static String arg_UriInclude_Desc;

	/**
	 * Argument name output file
	 */
	public static String arg_OutFile_Name;
	/**
	 * File output arguments description
	 */
	public static String arg_OutFile_Desc;

	/**
	 * Argument name stylesheet file
	 */
	public static String arg_StyleSheet_Name;
	/**
	 * File stylesheet arguments description
	 */
	public static String arg_StyleSheet_Desc;

	/**
	 * Argument name merged
	 */
	public static String arg_Merged_Name;
	/**
	 * Merged argument description
	 */
	public static String arg_Merged_Desc;

	/**
	 * Wrong arguments error message;
	 */
	public static String error_Wrong_Arg;

	/**
	 * No match found error message
	 */
	public static String error_No_Match_File;

	/**
	 * No match eObject error message
	 */
	public static String error_No_Match_eObject;
	/**
	 * No Style Sheet found error Message
	 */
	public static String error_No_StyleSheet;
	/**
	 * Wrong file extension
	 */
	public static String error_Wrong_File_Extension;
	/**
	 * Style sheet with wrong file extension
	 */
	public static String error_Wrong_Style_NoXLS;

	/**
	 * Default style sheet
	 */
	public static String default_Stylesheet;

	/**
	 *
	 */
	public static String msgTxt_Separator;
	/**
	 *
	 */
	public static String msgTxt_error;
	/**
	 *
	 */
	public static String msgTxt_errorsNumber;
	/**
	 *
	 */
	public static String msgTxt_highestSeverity;
	/**
	 *
	 */
	public static String msgTxt_lineSeparator;
	/**
	 *
	 */
	public static String msgTxt_message;
	/**
	 *
	 */
	public static String msgTxt_objectType;
	/**
	 *
	 */
	public static String msgTxt_objectUri;
	/**
	 *
	 */
	public static String msgTxt_resourcePath;
	/**
	 *
	 */
	public static String msgTxt_severity;
	/**
	 *
	 */
	public static String msgCsv_Separator;

	/**
	 * Error message to be displayed if the postBuild constraints are not accomplished
	 */
	public static String postBuildChangeableCorrectnessContraint;
	/**
	 * Error message to be displayed if the non-postBuild constraints are not accomplished for parameters
	 */
	public static String parameterErrorMessage;
	/**
	 * Error message to be displayed if the non-postBuild constraints are not accomplished for references
	 */
	public static String referenceErrorMessage;

	// CHECKSTYLE:ON
	private static final String BUNDLE_NAME = "eu.cessar.ct.validation.internal.Messages"; //$NON-NLS-1$

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
