/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 11:32:40
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.instanceref;

import eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import gautosar.gecucparameterdef.GInstanceReferenceDef;

/**
 * Provides the candidates for setting ECUC instance reference(s). <br>
 * Usage:
 * 
 * <pre>
 *  <code>
 *  IEcucInstanceReferenceHelper helper = InstanceReferenceUtils.getEcucInstanceReferenceHelper(modelDescriptor) //obtain the helper
 *  GInstanceReferenceDef input =...;
 *  helper.init(input); //pass the definition of the instance reference 
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
 *         %date_created: Mon Jul 15 18:12:01 2013 %
 * 
 *         %version: 1 %
 */
public interface IEcucInstanceReferenceHelper extends IInstanceReferenceHelper
{

	/**
	 * @param input
	 * @throws InstanceRefConfigurationException
	 */
	void init(GInstanceReferenceDef input) throws InstanceRefConfigurationException;
}
