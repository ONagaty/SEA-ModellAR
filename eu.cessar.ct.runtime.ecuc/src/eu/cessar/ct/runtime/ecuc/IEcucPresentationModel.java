/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 8, 2009 5:18:07 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.execution.IBinaryClassResolver;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 */
public interface IEcucPresentationModel extends IBinaryClassResolver
{
	/**
	 * The family job for the dumpPresentationModel job
	 */
	public static final Object DUMP_PM_FAMILY_JOB = new Object();

	/**
	 * Clear whatever private cache informations are stored
	 */
	public void modelChanged();

	/**
	 * Return the EMF proxy engine used to delegate calls from PM model to AUTOSAR model
	 * 
	 * @return
	 */
	public IEMFProxyEngine getProxyEngine();

	/**
	 * @param execClassLoader
	 * @return
	 */
	public EObject getPMModelRoot();

	/**
	 * Return the EClass that has been created for the root of the presentation model.
	 * 
	 * @return
	 */
	public EClass getRootPMEClass();

	/**
	 * Return the EClassifier that has been created for an particular GIdentifiable inside the presentation model.
	 * 
	 * @param ident
	 *        a valid identifiable
	 * @return the EClass generated for the identifiable or null if there is no such classifier available
	 * 
	 */
	public EClassifier getPMClassifier(GIdentifiable ident);

	/**
	 * @return
	 */
	public IProject getProject();

	/**
	 * @param uri
	 * @return
	 */
	public EPackage getEPackage(String pmURI);

	/**
	 * Retrieves the status of presentation model preparation.
	 * 
	 * Used to signal errors like multiple definitions of an AUTOSAR module.
	 * 
	 * @return the status of presentation model preparation
	 */
	public IStatus getInitStatus();

	/**
	 * Dumps the Presentation Model to the specified folder ,meaning it will create a ClassFolder containing the
	 * Interfaces from the PresentationModel. It gives the possibility to add the implementation of the Interfaces as
	 * well
	 * 
	 * @see eu.cessar.ct.runtime.CessarRuntime#addClassFolder(IProject, String)
	 * 
	 * 
	 * @param dumpClasses
	 *        this parameter is used in order to specify if we also want to dump the java classes or not
	 * @param dumpFolder
	 *        is used in order to specify the folder in which the PresentationModel will be dumped
	 * @param monitor
	 *        The monitor that checks the action
	 * @return the status of presentation model dumping
	 */
	public IStatus dumpPresentationModel(boolean dumpClasses, String dumpFolder, IProgressMonitor monitor);
}
