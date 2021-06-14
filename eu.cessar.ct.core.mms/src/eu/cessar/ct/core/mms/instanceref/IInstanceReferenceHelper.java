/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 23.04.2012 13:16:29 </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Base interface for a provider of candidates for configuring instance references
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:09 2013 %
 * 
 *         %version: 1 %
 */
public interface IInstanceReferenceHelper
{
	/**
	 * NOTE: the call must be done AFTER setting the proper input
	 * 
	 * @return the list with context types of the instance reference that was previously specified on init
	 */
	List<IContextType> getContextTypes();

	/**
	 * NOTE: the call must be done AFTER setting the proper input
	 * 
	 * @return the type of the target of the instance reference that was previously specified on init
	 */
	EClass getTargetType();

	/**
	 * NOTE: the call must be done AFTER setting the proper input and calling {@link #computeCandidates()}
	 * 
	 * @throws InstanceRefConfigurationException
	 */
	void computeCandidates() throws InstanceRefConfigurationException;

	/**
	 * @deprecated #getCandidates() should be used instead
	 * @return the mapping with candidates, where K: target and V: list with list of context elements towards that
	 *         target
	 */
	@Deprecated
	Map<GIdentifiable, List<List<GIdentifiable>>> getCandidatesMap();

	/**
	 * @return whether at least one candidate that configures partially the instance reference exists
	 */
	boolean hasCandidatesForIncompleteConfig();

	/**
	 * NOTE: the call must be done AFTER setting the proper input and calling {@link #computeCandidates()}
	 * 
	 * @return whether at least one candidate that completely configures the instance reference exists
	 */
	boolean hasCandidatesForCompleteConfig();

	/**
	 * Resets the helper in order to be re-used for another input
	 */
	void reset();

	/**
	 * @return the list with candidates for configuring the instance reference that was previously specified on init
	 */
	List<IInstanceReferenceCandidate> getCandidates();

}
