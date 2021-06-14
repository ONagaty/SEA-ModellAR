/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 13, 2010 4:24:17 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pm;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * 
 */
public abstract class AbstractParentedPMInfo<T> extends AbstractPMInfo<T>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getPackage()
	 */
	@Override
	public EPackage getParentPackage(IEcucPresentationModel ecucPM)
	{
		return getParentInfo().getSubPackage(ecucPM, true);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getParentClass()
	 */
	@Override
	public EClass getParentClass()
	{
		return (EClass) getParentInfo().getClassifier();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo#getSubPackage(boolean)
	 */
	public EPackage getSubPackage(IEcucPresentationModel ecucPM, boolean create)
	{
		if (subPackage != null)
		{
			return subPackage;
		}
		else
		{
			if (create)
			{
				subPackage = createSubPackage(ecucPM);
			}
			return subPackage;
		}
	}

	/**
	 * Create the sub package for the element. This method will be executed only
	 * once and only if needed
	 * 
	 * @return
	 */
	protected abstract EPackage createSubPackage(IEcucPresentationModel ecucPM);

}
