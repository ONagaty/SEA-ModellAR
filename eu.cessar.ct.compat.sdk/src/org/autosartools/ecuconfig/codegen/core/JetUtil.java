/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package org.autosartools.ecuconfig.codegen.core;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import org.autosartools.ecuconfig.codegen.core.delegates.LicenseHandlerDelegate;
import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.platform.util.PlatformUtils;

/**
 * Utilities methods that can be accessed from Jet files.
 * 
 * @Review uidl7321 - Apr 3, 2012
 * 
 */
public class JetUtil
{

	public static interface Service
	{

		/**
		 * <p>
		 * Check the license for the given module of the given product and
		 * display a license dialog if needed.
		 * </p>
		 * 
		 * <p>
		 * Starting from Cessar-CT 4.0.2.1 the license mechanism is not
		 * implemented
		 * </p>
		 * 
		 * @param productName
		 * @param moduleName
		 */
		@Deprecated
		public void checkAndInstallLicense(final String productName, final String moduleName);

		/**
		 * <p>
		 * This method return a list containing all <b>ecuextract</b> files
		 * contained by the project from where this method was called.
		 * </p>
		 * 
		 * @return returns a list with ECU Extract files of current project.
		 */
		public List<?> getCurrentProjectEcuExtracts();

		/**
		 * <p>
		 * Prints the given message to the CESSAR-CT console. Calling this
		 * method is fully equivalent to calling
		 * <code>getConsoleOutput().println( message )</code>.
		 * </p>
		 * 
		 * @param message
		 *        the message to print
		 */
		public void printToConsole(String message);

		/**
		 * <p>
		 * Prints the given message to the CESSAR-CT console and appends a
		 * newline character. Calling this method is fully equivalent to calling
		 * <code>getConsoleOutput().println( message )</code>.
		 * </p>
		 * 
		 * @param message
		 *        the message to print
		 */
		public void printToConsoleLn(String message);

		/**
		 * <p>
		 * Returns a <code>PrintStream</code> which outputs to the CESSAR-CT
		 * console.
		 * </p>
		 * 
		 * @return Returns a <code>PrintStream</code> which outputs to the
		 *         CESSAR-CT console.
		 */
		public PrintStream getConsoleOutput();

		/**
		 * <p>
		 * Returns the folder where the generated file is saved. The folder is
		 * created on method call if it doesn't exist. On error,
		 * <code>null</code> can be returned.
		 * 
		 * <p>
		 * Note: this method can only be called from within a JET template.
		 * Calling this method from outside of a template will result in an
		 * <code>IllegalStateException</code>.
		 * 
		 * @return the output destination
		 */
		public File getJetOutputFolder();

		/**
		 * <p>
		 * Returns a list that contains all files of the given extension. The
		 * result contains only the files belonging to the project from where
		 * this method was called.
		 * </p>
		 * 
		 * @param extension
		 *        A string containing the extension of the files to be returned
		 * @return returns a list with <code>java.io.File</code> objects.
		 */
		public List<?> getProjectFiles(String extension);

		/**
		 * <p>
		 * Returns a sorted list that contains all <code>EObject</code>s of the
		 * given parameter that have a short name. The result is sorted
		 * ascending by short name.
		 * </p>
		 * 
		 * @param list
		 *        A list that should be sorted by the short name of the
		 *        elements.
		 * @return returns a sorted list with the elements of the given
		 *         parameter that contain have the shortName feature.
		 */
		@SuppressWarnings("rawtypes")
		public EList sortByShortName(EList list);

		/**
		 * Returns the LicenseHandlerDelegate instance.
		 * 
		 * @return the LicenseHandlerDelegate instance.
		 * @deprecated use checkAndInstallLicense(String, String) instead
		 */
		@Deprecated
		public LicenseHandlerDelegate getLicenseHandler();

		/**
		 * Return true if the user code is executed inside UI, false if it is
		 * executed from command line
		 * 
		 * @return
		 */
		@Deprecated
		public boolean isUIMode();

	}

	private final static Service service = PlatformUtils.getService(Service.class);

	/**
	 * <p>
	 * Prints the given message to the CESSAR-CT console and appends a newline
	 * character. Calling this method is fully equivalent to calling
	 * <code>getConsoleOutput().println( message )</code>.
	 * </p>
	 * 
	 * @param message
	 *        the message to print
	 */
	public static void printToConsoleLn(String message)
	{
		service.printToConsoleLn(message);
	}

	/**
	 * <p>
	 * Prints the given message to the CESSAR-CT console. Calling this method is
	 * fully equivalent to calling
	 * <code>getConsoleOutput().println( message )</code>.
	 * </p>
	 * 
	 * @param message
	 *        the message to print
	 */

	public static void printToConsole(String message)
	{
		service.printToConsole(message);
	}

	/**
	 * <p>
	 * Returns a <code>PrintStream</code> which outputs to the CESSAR-CT
	 * console.
	 * </p>
	 * 
	 * @return Returns a <code>PrintStream</code> which outputs to the CESSAR-CT
	 *         console.
	 */
	public static PrintStream getConsoleOutput()
	{
		return service.getConsoleOutput();
	}

	/**
	 * <p>
	 * Returns the folder where the generated file is saved. The folder is
	 * created on method call if it doesn't exist. On error, <code>null</code>
	 * can be returned.
	 * 
	 * <p>
	 * Note: this method can only be called from within a JET template. Calling
	 * this method from outside of a template will result in an
	 * <code>IllegalStateException</code>.
	 * 
	 * @return the output destination
	 */
	public static File getJetOutputFolder()
	{
		return service.getJetOutputFolder();
	}

	/**
	 * <p>
	 * Returns a list that contains all files of the given extension. The result
	 * contains only the files belonging to the project from where this method
	 * was called.
	 * </p>
	 * 
	 * @param extension
	 *        A string containing the extension of the files to be returned
	 * @return returns a list with <code>java.io.File</code> objects.
	 */
	@SuppressWarnings("rawtypes")
	public static List getProjectFiles(String extension)
	{
		return service.getProjectFiles(extension);
	}

	/**
	 * <p>
	 * Check the license for the given module of the given product and display a
	 * license dialog if needed.
	 * </p>
	 * 
	 * <p>
	 * Starting from Cessar-CT 4.0.2.1 the license mechanism is not implemented
	 * </p>
	 * 
	 * @param productName
	 * @param moduleName
	 */
	@Deprecated
	public static void checkAndInstallLicense(final String productName, final String moduleName)
	{
		service.checkAndInstallLicense(productName, moduleName);
	}

	/**
	 * Return true if the user code is executed inside UI, false if it is
	 * executed from command line
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean isUIMode()
	{
		return false;
	}

	/**
	 * <p>
	 * Returns a sorted list that contains all <code>EObject</code>s of the
	 * given parameter that have a short name. The result is sorted ascending by
	 * short name.
	 * </p>
	 * 
	 * @param list
	 *        A list that should be sorted by the short name of the elements.
	 * @return returns a sorted list with the elements of the given parameter
	 *         that contain the shortName feature.
	 */
	@SuppressWarnings("rawtypes")
	public static EList sortByShortName(final EList list)
	{
		return service.sortByShortName(list);
	}

	/**
	 * <p>
	 * This method return a list containing all <b>ecuextract</b> files
	 * contained by the project from where this method was called.
	 * </p>
	 * 
	 * @return returns a list with ECU Extract files of current project, or an
	 *         empty list.
	 */
	public static List<?> getCurrentProjectEcuExtracts()
	{
		return service.getCurrentProjectEcuExtracts();
	}

	/**
	 * Returns the LicenseHandlerDelegate instance.
	 * 
	 * @return the LicenseHandlerDelegate instance.
	 * @deprecated use checkAndInstallLicense(String, String) instead
	 */
	@Deprecated
	public static LicenseHandlerDelegate getLicenseHandler()
	{
		return service.getLicenseHandler();
	}
}
