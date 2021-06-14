/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 14.05.2013 09:02:45
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GConfigReference;

import java.util.List;

/**
 * Sea handler for manipulating reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb  3 11:04:44 2014 %
 * 
 *         %version: 3 %
 * @param <T>
 */
public interface ISeaReferencesHandler<T> extends ISeaAttributesHandler<GConfigReference>
{

	/**
	 * 
	 * @see ISEAContainer#isSet(String)
	 */
	@SuppressWarnings("javadoc")
	public boolean isSet(ISEAContainer parent, String defName);

	/**
	 * 
	 * @see ISEAContainer#unSet(String)
	 */
	@SuppressWarnings("javadoc")
	public void unSet(ISEAContainer parent, String defName);

	/**
	 * @param parent
	 * @param defName
	 * @return the value of the reference having the definition given by its short name: <code>defName</code>, from
	 *         given <code>parent</code>
	 */
	public T getReference(ISEAContainer parent, String defName);

	/**
	 * @param parent
	 * @param defName
	 * @return list with values for the reference having the definition given by its short name: <code>defName</code>,
	 *         from given <code>parent</code>
	 */
	public ISEAList<T> getReferences(ISEAContainer parent, String defName);

	/**
	 * Sets the value of the reference having the definition given by its short name: <code>defName</code>,with the
	 * provided <code>value</code>, in the given <code>parent</code>, considering the specified active container
	 * 
	 * @param activeContainer
	 * @param parent
	 * @param defName
	 * @param value
	 */
	public void setReference(GContainer activeContainer, ISEAContainer parent, String defName, T value);

	/**
	 * Sets the values of the reference having the definition given by its short name: <code>defName</code>, with the
	 * provided <code>values</code>, in the given <code>parent</code>, considering the specified active container
	 * 
	 * @param activeContainer
	 * 
	 * @param parent
	 * @param defName
	 * @param values
	 */
	public void setReference(GContainer activeContainer, ISEAContainer parent, String defName, List<T> values);

	/**
	 * Checks whether provided <code>values</code> for the reference with the given <code>defName</code> shortName, are
	 * valid according to the destination(s) defined by <code>configReference</code>. <br>
	 * If not, the {@linkplain ISEAErrorHandler#valueNotOfDestinationType(ISEAContainer, String, List, List)} is called.
	 * 
	 * @param parent
	 *        Sea container wrapper to be passed to the error handler if the case
	 * @param defName
	 *        reference shortName that will be passed to the error handler if the case
	 * @param configReference
	 *        definition against which the <code>values</code> are compared
	 * @param values
	 *        list with values to be checked
	 * 
	 * @return <code>true</code> if all the elements from< code>values</code> list match the destination defined by
	 *         <code>configReference</code>
	 */
	public boolean checkDestinationType(ISEAContainer parent, String defName, GConfigReference configReference,
		List<T> values);
}
