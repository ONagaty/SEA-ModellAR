/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 04.03.2014 15:18:14
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model;

import org.eclipse.sphinx.emf.ecore.MinimalEObjectImpl2;

/**
 * Base class for any CID related class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Mar  4 17:07:08 2014 %
 * 
 *         %version: 1 %
 */
public class CIDEObject extends MinimalEObjectImpl2
{

	/**
	 * Flag used to store the binding object
	 */
	protected static final int BINDING_FLAG = 1 << 8;

	/**
	 * The id of the last field flag
	 */
	protected static final int MAX_FIELD_FLAG = BINDING_FLAG;

	/**
	 * A bit mask for all the bit flags representing fields.
	 */
	protected static final int CID_FIELD_MASK = FIELD_MASK | BINDING_FLAG;

	@Override
	protected int getFieldMask()
	{
		return CID_FIELD_MASK;
	}

	@Override
	protected int getMaxField()
	{
		return MAX_FIELD_FLAG;
	}

	@Override
	protected void eBasicSetContainerFeatureID(int newContainerFeatureID)
	{
		eFlags = newContainerFeatureID << 16 | eFlags & 0xFFFF;
	}
}
