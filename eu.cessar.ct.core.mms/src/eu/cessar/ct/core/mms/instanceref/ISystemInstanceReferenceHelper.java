/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 11:31:26
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;

/**
 * Provides the candidates for setting System instance reference(s). <br>
 * Usage:
 * 
 * <pre>
 *  <code>
 *  ISystemInstanceReferenceHelper helper = InstanceReferenceUtils.getInstanceReferenceHelper(modelDescriptor) //obtain the helper
 *  helper.init(eClass); //pass the EClass representing the instance reference
 *  
 *  List<ContextTypeWrapper> = helper.getContextTypeWrappers(); // obtain the context types
 *  EClass targetType = helper.getTargetType() //and the target type
 *  
 *  helper.computeCandidates(); // ask the helper to compute the candidates
 *  
 *  List<IInstanceReferenceCandidate> candidates = helper.getCandidates();
 *  
 *  helper.reset(); // in order to re-use the helper for another input
 *   </code>
 * </pre>
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:10 2013 %
 * 
 *         %version: 1 %
 */
public interface ISystemInstanceReferenceHelper extends IInstanceReferenceHelper
{
	/**
	 * 
	 * @param input
	 *        an instance reference EClass
	 * @throws InstanceRefConfigurationException
	 */
	void init(EClass input) throws InstanceRefConfigurationException;
}
