/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 11:58:21 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pm;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * This class holds PM EMF information for a PM element
 */
public interface IPMElementInfo<T>
{

	/**
	 * Initialize the PM information. This method shall be executed exactly
	 * once. Calling this method a second time will result in an exception
	 * 
	 * @param ecucPM
	 * @param element
	 * @param parent
	 */
	public void initialize(IEcucPresentationModel ecucPM, T element, IPMElementInfo<?> parent);

	/**
	 * @return
	 */
	public T getInitialElement();

	/**
	 * @return
	 */
	public IPMElementInfo<?> getParentInfo();

	/**
	 * Return the package where the classifier resides
	 * 
	 * @return
	 */
	public EPackage getParentPackage(IEcucPresentationModel ecucPM);

	/**
	 * Return the subPackage where sub elements should be created
	 * 
	 * @param create
	 *        if true the subPackage should be created if it has not been
	 *        already
	 * @return
	 */
	public EPackage getSubPackage(IEcucPresentationModel ecucPM, boolean create);

	/**
	 * Get the classifier that has been created for the element if any
	 * 
	 * @return
	 */
	public EClassifier getClassifier();

	/**
	 * Return the owner class
	 * 
	 * @return
	 */
	public EClass getParentClass();

	/**
	 * Return the feature from the owner class
	 * 
	 * @return
	 */
	public EStructuralFeature getParentFeature();

}
