/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 26, 2010 12:00:36 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import org.eclipse.emf.ecore.EDataType;

/**
 * 
 */
public interface IPMBinaryGeneratorSettings
{

	/**
	 * @return
	 */
	public boolean isUsingGenerics();

	/**
	 * @return
	 */
	public boolean isUsingNewEnum();

	/**
	 * @return
	 */
	public boolean isUsingTitleCaseFeatureNames();

	/**
	 * Return the binary name of the datatype class using dot as segment
	 * separator
	 * 
	 * @param dataType
	 * @return
	 */
	public String getEDataTypeClass(EDataType dataType);

}
