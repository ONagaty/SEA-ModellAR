/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Jun 11, 2013 12:37:09 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.mdl;

/**
 * Represents the possible eligibility settings used in {@code ModelDependencyLookup} and {@code ModelDependencyMatrix}.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Tue Jul  2 12:50:47 2013 %
 * 
 *         %version: 1 %
 */
public enum MDLEligibility
{
	/**
	 * Only containment references are considered.
	 */
	CONTAINMENT,
	/**
	 * Only non-containment references are considered.
	 */
	NON_CONTAINMENT
}
