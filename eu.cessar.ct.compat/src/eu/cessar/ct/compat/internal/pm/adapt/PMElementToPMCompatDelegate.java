/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 23, 2010 11:34:11 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;

/**
 * An implementation of {@link IPMCompatElementInfo} that delegates all calls to
 * an {@link IPMElementInfo}
 */
public class PMElementToPMCompatDelegate<T> extends AdapterImpl implements IPMCompatElementInfo<T>
{

	private final IPMElementInfo<T> wrapped;

	/**
	 * @param wrapped
	 */
	public PMElementToPMCompatDelegate(IPMElementInfo<T> wrapped)
	{
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#initialize(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel, java.lang.Object, eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo)
	 */
	public void initialize(IEcucPresentationModel ecucPM, T element, IPMElementInfo<?> parent)
	{
		wrapped.initialize(ecucPM, element, parent);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getInitialElement()
	 */
	public T getInitialElement()
	{
		return wrapped.getInitialElement();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentInfo()
	 */
	public IPMElementInfo<?> getParentInfo()
	{
		return wrapped.getParentInfo();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentPackage(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	public EPackage getParentPackage(IEcucPresentationModel ecucPM)
	{
		return wrapped.getParentPackage(ecucPM);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getSubPackage(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel, boolean)
	 */
	public EPackage getSubPackage(IEcucPresentationModel ecucPM, boolean create)
	{
		return wrapped.getSubPackage(ecucPM, create);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getClassifier()
	 */
	public EClassifier getClassifier()
	{
		return wrapped.getClassifier();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentClass()
	 */
	public EClass getParentClass()
	{
		return wrapped.getParentClass();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentFeature()
	 */
	public EStructuralFeature getParentFeature()
	{
		return wrapped.getParentFeature();
	}
}
