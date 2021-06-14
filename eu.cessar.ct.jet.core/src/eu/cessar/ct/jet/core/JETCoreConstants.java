package eu.cessar.ct.jet.core;

/**
 * Constants used by JET Core classes
 */
@SuppressWarnings("nls")
public final class JETCoreConstants
{
	/**
	 * Jet
	 */
	public static final String JET = "jet";

	/**
	 * Jar
	 */
	public static final String JAR = "jar";

	/**
	 * Java
	 */
	public static final String JAVA = ".java";

	/**
	 * LNS
	 */
	public static final String LNS = System.getProperty("line.separator");

	/**
	 * DQUOTE
	 */
	public static final String DQUOTE = "\"";

	/**
	 * Class
	 */
	public static final String CCLASS = "CLASS";

	/**
	 * Skeleton template
	 */
	public static final String SKELETON_TEMPLATE = "/resources/JETSkeletonTemplate.java";

	/**
	 * Default output file name
	 */
	public static final String DEFAULT_OUTPUT_FILE_NAME = "DEFAULT_OUTPUT_FILE_NAME";

	/**
	 * Default jet package
	 */
	public static final String DEFAULT_JET_PACKAGE = "generation.jets";

	/**
	 * Manifest attribute jet class
	 */
	public static final String MANIFEST_ATTR_JET_CLASS = "Jet-class";

	/**
	 * Manifest attribute tool version
	 */
	public static final String MANIFEST_ATTR_TOOL_VERSION = "CessarAR-Version";
	/**
	 * Manifest attribute meta-model version
	 */
	public static final String MANIFEST_ATTR_METAMODEL_VERSION = "Metamodel-Version";
	/**
	 * Manifest attribute execution mode
	 */
	public static final String MANIFEST_ATTR_EXECUTION_MODE = "ExecutionMode";
	/**
	 * Manifest attribute compliance
	 */
	public static final String MANIFEST_ATTR_COMPLIANCE = "Compliance";

	/**
	 * Execution mode parallel
	 */
	public static final String EXECUTION_MODE_PARALEL = "paralel";
	/**
	 * Execution mode sequential
	 */
	public static final String EXECUTION_MODE_SEQUENTIAL = "sequential";

	/**
	 * The JET content type id; must be the same with the one defined in "plugin.xml"
	 */
	public static final String JET_CONTENT_TYPE_ID = "eu.cessar.ct.jet.core.contenttype";

	/**
	 *
	 */
	public static final String JET_RUNNER_APPLICATION = "eu.cessar.ct.jetRunner";

	/**
	 * Preference dump java source
	 */
	public static final String PREF_DUMP_JAVA_SOURCE = "dump.source";
	/**
	 * Preference dump java source folder
	 */
	public static final String PREF_DUMP_JAVA_SOURCE_FOLDER = "dump.source.folder";
	/**
	 * Preference dump java source folder value
	 */
	public static final String PREF_DUMP_JAVA_SOURCE_FOLDER_VALUE = ".src";

	/**
	 * Debug model id constant
	 */
	// debug constants
	public static final String JET_DEBUG_MODEL_ID = "eu.cessar.ct.jet.core.model";

	/**
	 * Debug marker type constant
	 */
	public static final String JET_DEBUG_MARKER_TYPE = "eu.cessar.ct.jet.core.jetTemplateBreakpointMarker";

	/**
	 * Indicator for the access to a file. Used to create or to detect an read-only status by the {@link JETCoreUtils}
	 * and {@link JETPackerJob}.
	 */
	public static final int READ_ONLY = 13;

	/**
	 * Empty string
	 */
	public static final String EMPTY = "";

	/**
	 * Folder/path delimiter
	 */
	public static final String DOT = ".";

	private JETCoreConstants()
	{
	}
}
