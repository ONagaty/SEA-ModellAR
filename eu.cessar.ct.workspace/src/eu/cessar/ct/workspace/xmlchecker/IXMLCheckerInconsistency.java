/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 13:18:34 </copyright>
 */
package eu.cessar.ct.workspace.xmlchecker;

import eu.cessar.ct.workspace.consistencycheck.IInconsistency;

/**
 * The interface encapsulates the data describing an XML inconsistency that has been identified during the parsing and
 * comparison of two files: initial (original) file and a generated one (copy).
 * 
 * @author uidl6870
 */
public interface IXMLCheckerInconsistency extends IInconsistency
{
	/**
	 * Returns the type of the inconsistency, whether at element, element text or attribute level
	 * 
	 * @return the inconsistency type
	 */
	public EXMLInconsistencyType getInconsistencyType();

	/**
	 * Sets the type of inconsistency. Must not be called by clients
	 * 
	 * @param inconsistencyType
	 */
	public void setInconsistencyType(EXMLInconsistencyType inconsistencyType);

	/**
	 * Returns additional information from the original file regarding the inconsistency, such as: file's schema
	 * location, location of the problem and the actual value
	 * 
	 * @return the {@link IInconsistencyDetail} from the original file
	 */
	public IInconsistencyDetail getDetailFromOriginalFile();

	/**
	 * Returns additional information from the generated file regarding the inconsistency, such as: file's schema
	 * location, location of the problem and the actual value
	 * 
	 * @return the {@link IInconsistencyDetail} from the generated file
	 */
	public IInconsistencyDetail getDetailFromGeneratedFile();

	/**
	 * Sets additional data from the original file regarding the inconsistency. Must not be called by clients
	 * 
	 * @param detail
	 */
	public void setDetailFromOriginalFile(IInconsistencyDetail detail);

	/**
	 * Sets additional data from the generated file regarding the inconsistency. Must not be called by clients
	 * 
	 * @param detail
	 */
	public void setDetailFromGeneratedFile(IInconsistencyDetail detail);

}
