/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Sep 17, 2013 10:08:48 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.test.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Method order annotation used as the first sorting criteria in {@code CessarTestSuite}.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed May 21 18:47:34 2014 %
 * 
 *         %version: 1 %
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodOrder
{
	public static final int DEFAULT_ORDER = Integer.MAX_VALUE / 4; /*
																	 * maxint/4, to allow for plenty of room before and
																	 * after
																	 */

	int order() default DEFAULT_ORDER;
}
