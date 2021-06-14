/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * May 11, 2015 11:09:28 AM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.emf.ecore.EEnumLiteral;

/**
 * Interface for retrieving values.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Mon May 11 15:16:19 2015 %
 *
 *         %version: 1 %
 */
public interface IModelFragmentFeatureEditorValues
{
	/**
	 * @return values
	 */
	public List<String> getValues();

	/**
	 * @return enumLiterals
	 */
	public List<EEnumLiteral> getEnumLiterals();
}
