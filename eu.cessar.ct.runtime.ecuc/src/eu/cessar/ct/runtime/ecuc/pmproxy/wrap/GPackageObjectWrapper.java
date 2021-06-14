/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 1:59:12 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GinfrastructurePackage;

/**
 * 
 */
public class GPackageObjectWrapper extends MultiObjectWrapper<GARPackage>
{

	/**
	 * @param wrapped
	 */
	public GPackageObjectWrapper(IEMFProxyEngine engine, List<GARPackage> wrapped)
	{
		super(engine, GARPackage.class, wrapped);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#getMasterEClass()
	 */
	@Override
	public EClass getMasterEClass()
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject());
		return factory.getConcreteEClass(GinfrastructurePackage.Literals.GAR_PACKAGE);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		List<GARPackage> list = getMasterObject();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		else
		{
			EObject master = list.get(0).eContainer();
			if (master instanceof GARPackage)
			{
				return master;
			}
			else
			{
				// return the project where the object is locate
				return getEngine().getProject();
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		throw new PMRuntimeException("Cannot alter a package using the presentation model"); //$NON-NLS-1$
	}

}
