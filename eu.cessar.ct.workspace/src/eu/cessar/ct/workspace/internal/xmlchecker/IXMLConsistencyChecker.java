/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 04.09.2012 14:53:43 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Entity responsible with identifying inconsistencies between 2 XML files. <br>
 * 
 * <i>NOTE</i>: in the moment an inconsistency is found at element level, the parsing of both files is ended and the
 * consistency check is terminated for the respective files.
 * 
 * @author uidl6870
 * 
 */
public interface IXMLConsistencyChecker
{

	/**
	 * Returns the result of the comparison between the 2 given XML files: <code>expectedFile</code> and
	 * <code>obtainedFile</code>.
	 * 
	 * @param expectedFile
	 *        initial file
	 * @param obtainedFile
	 *        generated file
	 * @param monitor
	 *        progress monitor
	 * @return an {@link IConsistencyCheckResult}
	 */

	public IConsistencyCheckResult<IXMLCheckerInconsistency> compare(File expectedFile, File obtainedFile,
		IProgressMonitor monitor);
}
