/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 9, 2009 1:16:42 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.runtime.ecuc.internal.EcucCoreImpl;

/**
 * 
 */
public interface IEcucCore
{

	public static IEcucCore INSTANCE = new EcucCoreImpl();

	/**
	 * Returns true if the <code>project</code> is a valid ecuc project:<br/>
	 * <ul>
	 * <li>The project exists</li>
	 * <li>The project is open</li>
	 * <li>The project have Cessar nature, Java nature and Autosar nature</li>
	 * </ul>
	 * 
	 * @param project
	 * @return
	 */
	public boolean isValidEcucProject(IProject project);

	/**
	 * Return the Ecuc model for a particular project. Return null if the
	 * project cannot have such a model. Reasons includes:<br/>
	 * <ul>
	 * <li>The project does not exists</li>
	 * <li>The project is not open</li>
	 * <li>The project does not have Cessar nature</li>
	 * </ul>
	 * 
	 * @param project
	 * @return
	 */
	public IEcucModel getEcucModel(IProject project);

	/**
	 * @param object
	 * @return
	 */
	public IEcucModel getEcucModel(EObject object);

	/**
	 * @param projectName
	 * @return
	 */
	public IEcucModel getEcucModel(String projectName);

	/**
	 * @param listener
	 */
	public void addModelListener(IModelListener listener);

	/**
	 * @param listener
	 */
	public void removeModelListener(IModelListener listener);

	/**
	 * Return the Ecuc Presentation model for a particular project. Return null
	 * if the project cannot have such a model. Reasons includes:<br/>
	 * <ul>
	 * <li>The project does not exists</li>
	 * <li>The project is not open</li>
	 * <li>The project does not have Cessar nature</li>
	 * <li>The project does not have Java nature</li>
	 * </ul>
	 * 
	 * @param project
	 * @return
	 */
	public IEcucPresentationModel getEcucPresentationModel(IProject project);

	/**
	 * @param object
	 * @return
	 */
	public IEcucPresentationModel getEcucPresentationModel(EObject object);

	/**
	 * @param projectName
	 * @return
	 */
	public IEcucPresentationModel getEcucPresentationModel(String projectName);

}
