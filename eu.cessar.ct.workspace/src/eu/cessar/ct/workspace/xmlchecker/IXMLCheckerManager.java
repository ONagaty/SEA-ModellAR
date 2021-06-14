/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 26.07.2012 10:42:38 </copyright>
 */
package eu.cessar.ct.workspace.xmlchecker;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;

import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;

/**
 * Manager responsible with running XML loading/saving consistency check on AUTOSAR files.<br>
 * The main usage is to discover potential issues in the serializing or loading mechanism of the AUTOSAR files, issues
 * which could lead to loss of data or even corrupt the files. <br>
 * 
 * @author uidl6870
 * 
 */
public interface IXMLCheckerManager
{
	/**
	 * Runs the loading-serializing consistency check on the given <code>files</code>
	 * 
	 * @param files
	 *        the input files on which to run the loading-serializing consistency check. The files must have been
	 *        previously loaded
	 * @param monitor
	 *        monitor to which progress will be reported
	 * @return a list containing the results of running the check on each of the passed files
	 */
	public List<IConsistencyCheckResult<IXMLCheckerInconsistency>> performConsistencyCheck(List<IFile> files,
		IProgressMonitor monitor);

	/**
	 * Runs the loading-serializing consistency check on the given <code>files</code>
	 * 
	 * @param files
	 *        the input files on which to run the loading-serializing consistency check.
	 * @param metaModelDescriptor
	 *        the metaModelDescriptor of the file that needs to be checked
	 * @param monitor
	 *        monitor to which progress will be reported
	 * @return a list containing the results of running the check on each of the passed files
	 */
	public List<IConsistencyCheckResult<IXMLCheckerInconsistency>> performConsistencyCheck(List<File> files,
		IMetaModelDescriptor metaModelDescriptor, IProgressMonitor monitor);

}
