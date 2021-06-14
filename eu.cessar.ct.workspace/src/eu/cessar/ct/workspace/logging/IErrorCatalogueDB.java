/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidr0073<br/>
 * 17.11.2015 09:03:41
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.logging;

/**
 * Error catalogue database interface to make queries to the database.
 *
 * @author uidr0073
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public interface IErrorCatalogueDB
{

	/**
	 * Tries to find corresponding message to given id.
	 *
	 * @param id
	 *        of the requested message
	 * @return corresponding message or a default message if no message exists for the given id
	 */
	public String getMessageById(String id);

}
