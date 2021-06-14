/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 28.08.2013 14:45:30
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.List;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.sdk.sea.ISEAObject;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * Exception indicating that multiple definitions were found.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:43 2013 %
 * 
 *         %version: 1 %
 */
public class SEAMultipleDefinitionsFoundException extends AbstractSEARuntimeException
{
	private static final String MSG = "Multiple definitions ({0}) found for {1} in {2}!"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;

	/**
	 * @param parent
	 *        the object on which the method was executed
	 * @param defName
	 *        the used definition name
	 * @param result
	 *        list with found definitions, never <code>null</code> or empty
	 */
	public SEAMultipleDefinitionsFoundException(ISEAObject parent, String defName, List<? extends GARObject> result)
	{
		super(NLS.bind(MSG, new Object[] {result.size(), defName, parent.toString()}));
	}
}
