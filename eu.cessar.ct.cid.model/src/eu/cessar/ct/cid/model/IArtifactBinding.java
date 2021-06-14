/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.02.2014 12:04:22
 * 
 * </copyright>
 */
package eu.cessar.ct.cid.model;

/**
 * Concrete Binding contract for an Artifact
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Mar  5 14:33:39 2014 %
 * 
 *         %version: 3 %
 */
public interface IArtifactBinding extends IConcreteBinding
{

	/**
	 * Initialize the binding for the provided artifact
	 * 
	 * @param artifact
	 */
	public void init(Artifact artifact);

	/**
	 * Return the artifact that this binding is bound to
	 * 
	 * @return the bound artifact
	 */
	public Artifact getArtifact();

}
