/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 28.05.2013 16:51:06
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import gautosar.gecucdescription.GContainer;

/**
 * Sea handler for manipulating containers
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Aug 5 09:43:49 2013 %
 * 
 *         %version: 2 %
 */
public interface ISeaContainersHandler
{

	/**
	 * 
	 * @see ISEAContainer#setShortName(String)
	 */
	@SuppressWarnings("javadoc")
	public void setShortName(ISEAContainerParent parent, String shortName);

	/**
	 * 
	 * @see ISEAContainer#getContainer(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer getContainer(ISEAContainerParent parent, String defName);

	/**
	 * 
	 * @see ISEAContainer#getContainers(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAList<ISEAContainer> getContainers(ISEAContainerParent parent, String defName);

	/**
	 * 
	 * @see ISEAContainer#searchForContainers(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAList<ISEAContainer> searchForContainers(ISEAContainerParent parent, String pathFragment);

	/**
	 * 
	 * @see ISEAContainer#isSetContainer(String)
	 */
	@SuppressWarnings("javadoc")
	public boolean isSetContainer(ISEAContainerParent parent, String defName);

	/**
	 * 
	 * @see ISEAContainerParent#unSetContainer(String)
	 */
	@SuppressWarnings("javadoc")
	public void unSetContainer(ISEAContainerParent parent, String defName);

	/**
	 * 
	 * @see ISEAModel#getContainer(GContainer)
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer getContainer(ISEAModel seaModel, GContainer container);

	/**
	 * 
	 * @see ISEAModel#getContainer(IPMContainer)
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer getContainer(ISEAModel seaModel, IPMContainer container);

	/**
	 * 
	 * @see ISEAContainerParent#createContainer(String, String, boolean)
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createSEAContainer(ISEAContainerParent parent, String defName, String shortName,
		boolean deriveNameFromSuggestion);

	/**
	 * 
	 * @see ISEAContainer#createChoiceContainer(String, String, String, boolean)
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createSEAChoiceContainer(ISEAContainerParent parent, String defName, String chosenDefName,
		String shortName, boolean deriveNameFromSuggestion);

}
