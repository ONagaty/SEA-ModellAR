/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Oct 26, 2010 12:47:30 PM </copyright>
 */
package org.autosartools.general.core.project;

/**
 * @author uidt2045
 * @Review uidl7321 - Apr 5, 2012
 */
import java.math.BigInteger;

/**
 * Defines some common constants.
 * 
 * @Review uidl7321 - Apr 4, 2012
 */
public class CommonConstants
{
	/**
	 * Marker type for ECUC problems related to BSW project structure.
	 */
	public static final String MARKER_TYPE_ECUC = "org.autosartools.ecuconfig.genericeditor.core." //$NON-NLS-1$
		+ "internal.project.ecucmodel.ECUCContentProblem"; //$NON-NLS-1$

	/**
	 * A regular expression that matches the file names.
	 */
	public static final String FILE_NAME_REG_EXP = ".*\\."; //$NON-NLS-1$

	/**
	 * System EPackage's prefix.
	 */
	public static final String SYSTEM_NS_PREFIX = "system"; //$NON-NLS-1$

	/**
	 * Used in the ARPackage indexing.
	 */
	public static final String ROOT_PACKAGES = "root_packages"; //$NON-NLS-1$

	/**
	 * Project preferences' identifier
	 */
	public static final String PROJECT_SYSTEM_MODEL = "PROJECT_SYSTEM_MODEL"; //$NON-NLS-1$

	/**
	 * Used in the SYSTEM elements' indexing
	 */
	public static final String SYSTEM_ELEMENTS = "SYSTEM_ELEMENTS"; //$NON-NLS-1$

	/**
	 * Used in the ARPackages' indexing
	 */
	public static final String PACKAGE_PREFIX = "PACK_"; //$NON-NLS-1$

	/**
	 * Path separator
	 */
	public static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

	/**
	 * Default ARPackage name
	 */
	public static final String DEFAULT_PACKAGE_NAME = "myPackage"; //$NON-NLS-1$

	/**
	 * Extension of ARXML files.
	 */
	public static final String ARXML_FILE_EXTENSION = "arxml"; //$NON-NLS-1$

	/**
	 * Extension of ARXMLDI files.
	 */
	public static final String ARXMLDI_FILE_EXTENSION = "arxmldi"; //$NON-NLS-1$

	/**
	 * Extension of jar files.
	 */
	public static final String JAR_FILE_EXTENSION = "jar"; //$NON-NLS-1$

	/**
	 * Extension of validator jar files.
	 */
	public static final String VALIDATOR_JAR_EXTENSION = "3c"; //$NON-NLS-1$
	/**
	 * IPath's prefix for platform resources.
	 */
	public static final String PLATFORM_RESOURCE = "platform:/resource"; //$NON-NLS-1$

	/**
	 * Autosar references' identifier.
	 */
	public static final String REFERENCES = "EREF"; //$NON-NLS-1$

	/**
	 * Project's validators' folder name.
	 */
	public static final String VALIDATORS_FOLDER_NAME = "validators"; //$NON-NLS-1$

	/**
	 * Project's validators' classpath variable.
	 */
	public static final String VALIDATORS_CP_VARIABLE = "AUTOSAR_VALIDATORS"; //$NON-NLS-1$

	/**
	 * Project's validators' jar location.
	 */
	public static final String VALIDATORS_JAR_LOCATION = "/validatorsSupport.jar"; //$NON-NLS-1$

	/**
	 * Java nature identifier
	 */
	public static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature"; //$NON-NLS-1$

	/**
	 * Java builder identifier
	 */
	public static final String JAVA_BUILDER = "org.eclipse.jdt.core.javabuilder"; //$NON-NLS-1$

	/**
	 * Path of validators template zip.
	 */
	public static final String VALIDATORS_TEMPLATE_PATH = "/validatorsTemplate.zip"; //$NON-NLS-1$

	/**
	 * Path of BSW Project's template zip,
	 */
	public static final String BSW_TEMPLATE_PATH = "/bswProjectTemplate.zip"; //$NON-NLS-1$

	/**
	 * Custom JET skeleton path.
	 */
	public static final String SKELETON_TEMPLATE = "/resources/JETSkeletonTemplate.java"; //$NON-NLS-1$

	/**
	 * Identifiable shortName feature's name.
	 */
	public static final String SHORTNAME_FEATURE = "shortName"; //$NON-NLS-1$

	/**
	 * ContextReferences eclass
	 */
	public static final String CONTEXT_REFERENCES = "CONTEXTREFSType"; //$NON-NLS-1$

	public static final String HELPER_ANCHOR = "#"; //$NON-NLS-1$

	public static final String HELPER_CONTENT_URL = "html"; //$NON-NLS-1$

	public static final String HELPER_NO_HELP_FOUND = "nohelp.html"; //$NON-NLS-1$

	public static final String HELPER_NSURI_PREFIX = "http:///"; //$NON-NLS-1$

	public static final String HELPER_NSURI_SUFIX = ".ecore"; //$NON-NLS-1$

	/**
	 * Extension of autosar files.
	 */
	public static final String AUTOSAR_FILE_EXTENSION = "autosar"; //$NON-NLS-1$

	/**
	 * Extension of ecuconfig files.
	 */
	public static final String ECUC_FILE_EXTENSION = "ecuconfig"; //$NON-NLS-1$

	/**
	 * Custom preference for custom validators.
	 */
	public static final String CUSTOM_PREFERENCE = "externalCustomValidator"; //$NON-NLS-1$

	/**
	 * Extension of ecucextract files.
	 */
	public static final String ECUEXTRACT_FILE_EXTENSION = "ecuextract"; //$NON-NLS-1$

	/**
	 * package "STANDARD_PARAM_DEF" where is added the Original Module Def (that
	 * must be kept for comparison). So this second package should not be shown
	 * by the editor and the Module Def inside should not be modified by the
	 * tool in any case.
	 */
	public static final String AR_PACKAGE_STANDARD_NAME = "STANDARD_PARAM_DEF"; //$NON-NLS-1$

	/**
	 * Standard ARPackage name.
	 */
	public static final String AR_PACKAGE_NAME = "AUTOSAR"; //$NON-NLS-1$

	/**
	 * URI of a reference namespace.
	 */
	public static final String REF_NsURI = "http://www.autosartools.org/emf/2006/ReferenceType"; //$NON-NLS-1$

	/**
	 * Extended meta-data.
	 */
	public static final String EXTENDED_META_DATA = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
	/**
	 * File addition mode
	 */
	public static final int FILE_ADDITION_MODE = 0;

	/**
	 * File deletion mode
	 */
	public static final int FILE_DELETION_MODE = 1;

	/**
	 * Name of hidden source folder.
	 */
	public static final String HIDDEN_SRC_FOLDER_NAME = ".src"; //$NON-NLS-1$

	/**
	 * Name of hidden bin folder.
	 */
	public static final String HIDDEN_BIN_FOLDER_NAME = ".bin"; //$NON-NLS-1$

	/**
	 * Name of model folder.
	 */
	public static final String DOT_MODEL_FOLDER_NAME = ".model"; //$NON-NLS-1$

	/**
	 * Name of glue layer file.
	 */
	public static final String GLUE_MODEL_NAME = "glue"; //$NON-NLS-1$

	/**
	 * Extension of jet files.
	 */
	public static final String JET_FILE_EXTENSION = "jet"; //$NON-NLS-1$

	/**
	 * Name of hidden jet source folder.
	 */
	public static final String HIDDEN_JET_SRC_FOLDER_NAME = ".src-jet"; //$NON-NLS-1$

	/**
	 * Name of jet attribute from manifest file.
	 */
	public static final String MANIFEST_JET_ATTR = "Jet-class"; //$NON-NLS-1$

	/**
	 * Name of manifest file.
	 */
	public static final String MANIFEST_FILENAME = "MANIFEST.MF"; //$NON-NLS-1$

	/**
	 * Prefix of temporary jars location.
	 */
	public static final String TEMP_JARS_PREFIX = "temp-"; //$NON-NLS-1$

	/**
	 * Name of temporary jars folder.
	 */
	public static final String TEMP_JARS_FOLDER = "temp"; //$NON-NLS-1$

	/**
	 * Path of system project template.
	 */
	public static final String SYS_TEMPLATE_PATH = "/sysProjectTemplate.zip"; //$NON-NLS-1$

	/**
	 * Maximum length of a path.
	 */
	public static final int MAX_WINDOWS_PATH_LENGTH = 248;

	/**
	 * Name of report for long path.
	 */
	public static final String LONG_PATH_REPORT_FILE = "longPathReport.txt"; //$NON-NLS-1$

	/**
	 * Name of report for migration report file.
	 */
	public static final String MIGRATION_REPORT_FILE = "migration.report"; //$NON-NLS-1$

	/**
	 * Sufix for src foulder.
	 */
	public static final String SRC_FOLDER_SUFIXE = "-src"; //$NON-NLS-1$

	/**
	 * Name of base ecuc package.
	 */
	public static final String ECUC_BASE_PACKAGE = "ecuc"; //$NON-NLS-1$

	/**
	 * Extension for java files.
	 */
	public static final String JAVA_FILE_EXTENSION = "java"; //$NON-NLS-1$

	/**
	 * Extension for gen model files.
	 */
	public static final String GEN_MODEL_FILE_EXTENSION = "genModel"; //$NON-NLS-1$

	/**
	 * Extension for ecore files.
	 */
	public static final String ECORE_FILE_EXTENSION = "ecore"; //$NON-NLS-1$

	/**
	 * Path for bws validators' templated.
	 */
	public static final String BSW_VALIDATORS_TEMPLATE_PATH = "/bswValidatorsTemplate.zip"; //$NON-NLS-1$

	/**
	 * Minimum cardinality.
	 */
	public static final int MIN_CARDINALITY = 0;

	/**
	 * Maximum cardinality.
	 */
	public static final int MAX_CARDINALITY = 1;

	/**
	 * Unsigned long min.
	 */
	public static final BigInteger UNSIGNED_LONG_MIN = new BigInteger("0", 16); //$NON-NLS-1$

	/**
	 * Max unsigned long.
	 */
	public static final BigInteger UNSIGNED_LONG_MAX = new BigInteger("FFFFFFFFFFFFFFFF", 16); //$NON-NLS-1$

	/**
	 * Min signed long.
	 */
	public static final BigInteger SIGNED_LONG_MIN = new BigInteger("-9223372036854775808", 10); //$NON-NLS-1$

	/**
	 * Max signed long.
	 */
	public static final BigInteger SIGNED_LONG_MAX = new BigInteger("7FFFFFFFFFFFFFFF", 16); //$NON-NLS-1$

	/**
	 * Extension for OCL files.
	 */
	public static final String OCL_FILE_EXTENSION = "ocl"; //$NON-NLS-1$

	/**
	 * NON-UI Preference pages
	 */
	public static final String PREF_VALIDATE_ON_THE_FLY = "GeneralCorePlugin.PreferenceValidateOnTheFly"; //$NON-NLS-1$
}