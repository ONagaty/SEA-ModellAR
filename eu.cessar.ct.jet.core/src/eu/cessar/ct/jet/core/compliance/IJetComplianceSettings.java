/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 29, 2010 4:17:59 PM </copyright>
 */
package eu.cessar.ct.jet.core.compliance;

/**
 * @author uidl6458
 * 
 */
public interface IJetComplianceSettings
{

	/**
	 * Return the list of imported packages or classes that have to be used when
	 * a jet directive is created
	 * 
	 * @return
	 */
	public String[] getSkeletonImports();

	/**
	 * Return true if an extended JET Skeleton can be used, false otherwise
	 * 
	 * @return
	 */
	public boolean useExtendedSkeleton();

}
