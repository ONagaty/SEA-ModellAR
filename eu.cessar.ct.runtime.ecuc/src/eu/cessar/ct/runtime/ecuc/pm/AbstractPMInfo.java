/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 2:17:13 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pm;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * 
 */
public abstract class AbstractPMInfo<T> extends AdapterImpl implements IPMElementInfo<T>
{

	protected static final EcoreFactory ecoreFactory = EcoreFactory.eINSTANCE;
	protected static final EcorePackage ecorePackage = EcorePackage.eINSTANCE;

	protected T element;
	protected IPMElementInfo<?> parentInfo;
	protected EClassifier classifier;
	protected EPackage parentPackage;
	protected EClass parentClass;
	protected EStructuralFeature parentFeature;
	protected EPackage subPackage;
	private boolean initialized = false;

	/**
	 * 
	 */
	protected abstract void create(IEcucPresentationModel ecucPM);

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#initialize(java.lang.Object, eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo)
	 */
	public void initialize(IEcucPresentationModel ecucPM, T element, IPMElementInfo<?> parentInfo)
	{
		Assert.isTrue(!initialized);
		initialized = true;
			this.element = element;
			this.parentInfo = parentInfo;
			create(ecucPM);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getInitialElement()
	 */
	public T getInitialElement()
	{
		return element;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentInfo()
	 */
	public IPMElementInfo<?> getParentInfo()
	{
		return parentInfo;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getClassifier()
	 */
	public EClassifier getClassifier()
	{
		return classifier;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentPackage()
	 */
	public EPackage getParentPackage(IEcucPresentationModel ecucPM)
	{
		return parentPackage;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentClass()
	 */
	public EClass getParentClass()
	{
		return parentClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentFeature()
	 */
	public EStructuralFeature getParentFeature()
	{
		return parentFeature;
	}

}
