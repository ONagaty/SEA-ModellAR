/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Aug 2, 2010 11:37:35 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc;

import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Interface that encapsulate a BSW Module Description. This is necessary
 * because of differences between metamodels.
 * 
 * @Review uidl6458 - 30.03.2012
 * 
 */
public interface IBSWModuleDescription
{

	/**
	 * Return the object from the model that really holds the BSW module
	 * description. This is metamodel specific.
	 * 
	 * @return
	 */
	public GIdentifiable getWrappedDescription();

	/**
	 * Return the shortname of the BSW module description
	 * 
	 * @return
	 */
	public String getShortName();

	/**
	 * Return the module definition that is referred by this BSW Module
	 * Description. Could be null.
	 * 
	 * @return
	 */
	public GModuleDef getModuleDefinition();

	/**
	 * Get the preconfigured configuration. Could return null;
	 * 
	 * @return
	 */
	public GModuleConfiguration getPreconfiguredConfiguration();

	/**
	 * Return the recommended configuration or null if there is no such
	 * configuration available
	 * 
	 * @return
	 */
	public GModuleConfiguration getRecommendedConfiguration();

}
