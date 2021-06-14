/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 16.05.2013 13:49:38
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import eu.cessar.ct.sdk.sea.util.SEAModelUtil;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * Wrapper around a {@link GInstanceReferenceValue} object
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:41 2013 %
 * 
 *         %version: 1 %
 */
@Requirement(
	reqID = "REQ_API#SEA#7")
public interface ISEAInstanceReference extends ISEAObject
{
	/**
	 * Returns the wrapped object
	 * 
	 * @return the wrapped instance reference value
	 */
	public GInstanceReferenceValue getValue();

	/**
	 * Returns the value of this instance reference's target
	 * 
	 * @return the target
	 */
	public GIdentifiable getTarget();

	/**
	 * Returns this instance reference's contexts. <br>
	 * <b>NOTE</b>: altering the obtained list will be done outside a write transaction, regardless of the
	 * {@link SEAModelUtil#READ_ONLY} flag! <br>
	 * So, a proper usage that involves modifying the list, must wrap the write operation in a write transaction, like
	 * shown in the snippet below:
	 * 
	 * 
	 * <pre>
	 * final EList<GIdentifiable> contexts = ...// obtain the list
	 * ExecutionService.getRunningManager(project).updateModel(new Runnable()
	 * {
	 * 	public void run()
	 * 	{
	 * 		contexts.clear(); 
	 * 	}
	 * });
	 * </pre>
	 * 
	 * @return live list of context elements, never <code>null</code>
	 */
	public EList<GIdentifiable> getContexts();

	/**
	 * Sets the target of this instance reference to the provided value.
	 * 
	 * @param target
	 *        the new value for the target
	 * @return a reference to this object
	 */
	public ISEAInstanceReference setTarget(GIdentifiable target);

	/**
	 * Sets the contexts of this instance reference to the <code>contexts</code> list, by replacing the existing ones.
	 * 
	 * @param contexts
	 *        the new contexts to be set
	 * @return a reference to this object
	 */
	public ISEAInstanceReference setContexts(List<GIdentifiable> contexts);

	/**
	 * Returns whether the target feature is set.
	 * 
	 * @return <code>true</code> if this instance reference has the target feature set, <code>false</code> otherwise
	 */
	public boolean isSetTarget();

	/**
	 * Returns whether the contexts feature is set.
	 * 
	 * @return return <code>true</code> if the instance reference has the contexts feature set, <code>false</code>
	 *         otherwise
	 */
	public boolean isSetContexts();

	/**
	 * Unsets the target feature
	 * 
	 * @return a reference to this object
	 */
	public ISEAInstanceReference unSetTarget();

	/**
	 * Unsets the contexts feature
	 * 
	 * @return a reference to this object
	 */
	public ISEAInstanceReference unSetContexts();

}
