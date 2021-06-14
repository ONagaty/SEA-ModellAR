package eu.cessar.ct.jet.core.internal;

import java.text.MessageFormat;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class JETCoreMessages extends NLS
{
	/**
	 * Task Compiling Jet
	 */
	public static String task_CompilingJet;
	/**
	 * Task Search Jet Files
	 */
	public static String task_SearchJetFiles;
	/**
	 * Task Found Jet Files
	 */
	public static String task_FoundJetFiles;
	/**
	 * Task Completed
	 */
	public static String task_Completed;
	/**
	 * Task Write Jar
	 */
	public static String task_WriteJar;

	/**
	 * Error Jet Parsing Problem
	 */
	public static String error_JetParsingProblem;
	/**
	 * Error No Jet Files
	 */
	public static String error_NoJetFiles;
	/**
	 * Error Read Only JAR
	 */
	public static String error_ReadOnlyJAR;
	/**
	 * Error JET Compilation Problems
	 */
	public static String error_JETCompilationProblems;
	/**
	 * Error Reserved Word In Import
	 */
	public static String error_ReservedWordInImport;
	/**
	 * Error Invalid Identifier Start In Import
	 */
	public static String error_invalidIdentifierStartInImport;
	/**
	 * Error Invalid Identifier End In Import
	 */
	public static String error_invalidIdentifierEndInImport;
	/**
	 * Error Invalid Identifier Part In Import
	 */
	public static String error_invalidIdentifierPartInImport;
	private final static String BUNDLE_NAME = JETCoreMessages.class.getCanonicalName();
	static
	{
		NLS.initializeMessages(BUNDLE_NAME, JETCoreMessages.class);
	}

	/**
	 * @param key
	 * @param arguments
	 * @return formatted message
	 */
	public static String format(final String key, final Object... arguments)
	{
		return MessageFormat.format(key, arguments);
	}
}
