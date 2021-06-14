/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 13:58:34 </copyright>
 */
package eu.cessar.ct.workspace.xmlchecker;

import java.io.File;

/**
 * Additional data about an inconsistency from the file (either original or the generated one) that has been parsed,
 * related to the schema location, the location (line and column) of the XML item where the inconsistency has been
 * encountered and the actual item.
 * 
 * @author uidl6870
 * 
 */
public interface IInconsistencyDetail
{
	/**
	 * Returns the file that has been parsed
	 * 
	 * @return the parsed file
	 */
	public File getFile();

	/**
	 * Sets the file to be parsed. Must not be called by clients.
	 * 
	 * @param file
	 */
	public void setFile(File file);

	/**
	 * Retrieve the file's schema location
	 * 
	 * @return the schema location
	 */
	public String getSchemaLocation();

	/**
	 * Sets the file's schema location. Must not be called by clients.
	 * 
	 * @param schemaLocation
	 */
	public void setSchemaLocation(String schemaLocation);

	/**
	 * Sets the location of the XML item on which the inconsistency has been. Must not be called by clients.
	 * 
	 * @param location
	 */
	public void setLocation(ILocation location);

	/**
	 * Returns the location of the XML item on which the inconsistency has been found.
	 * 
	 * @return the location of the XML item on which the inconsistency has been found
	 */
	public ILocation getLocation();

	/**
	 * Returns the XML item
	 * 
	 * @return the XML item
	 */
	public String getItem();

	/**
	 * 
	 * @param value
	 */
	public void setItem(String value);

}
