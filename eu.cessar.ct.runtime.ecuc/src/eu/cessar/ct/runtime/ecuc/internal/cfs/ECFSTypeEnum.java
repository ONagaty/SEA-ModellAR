/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 8, 2009 3:37:35 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs;

/**
 * An enum that list the types of the elements that could exists inside the CFS
 * 
 */
public enum ECFSTypeEnum
{
	UNKNOWN,

	// cfs://PROJECT_NAME/
	ROOT,

	// cfs://PROJECT_NAME/.glue
	WRAPPER_GLUE,

	// cfs://PROJECT_NAME/AUTOSAR
	WRAPPER_ARPACKAGE,

	// cfs://PROJECT_NAME/AUTOSAR/Adc
	WRAPPER_MODULE_DEFINITION,

	// cfs://PROJECT_NAME/.glue/ecuc
	JAVA_GLUE_ELEMENT,

	// cfs://PROJECT_NAME/AUTOSAR/Adc/ecuc
	JAVA_MODULE_ELEMENT

}
