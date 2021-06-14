/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 26, 2010 3:22:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GinfrastructurePackage;

import org.artop.aal.common.resource.AutosarURIFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;

/**
 * 
 */
public class MissingContainerObjectWrapper extends PlainMasterObjectWrapper<GIdentifiable>
	implements INonsplitedSingleEAttributeAccessorProvider
{

	/**
	 * @param engine
	 * @param masterClass
	 * @param masterObject
	 */
	public MissingContainerObjectWrapper(IEMFProxyEngine engine, GIdentifiable masterObject)
	{
		super(engine, GIdentifiable.class, masterObject);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		// if (parentWrapper != null)
		// {
		throw new PMRuntimeException("Cannot move a missing container in another location"); //$NON-NLS-1$
		// }
		// else
		// {
		// // nothing to do??
		// }
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessorProvider#getAccessor(java.lang.String)
	 */
	public INonsplitedSingleEAttributeAccessor getAccessor(String featureName)
	{
		// we accept only shortName as featureName
		if (featureName.equalsIgnoreCase("shortName"))
		{
			URI uri = ((BasicEObjectImpl) getMasterObject()).eProxyURI();
			String path = null;
			if (getMasterObject().eIsProxy() && uri != null)
			{
				// return the last segment from the fragment
				path = uri.fragment();
				if (path != null)
				{
					path = AutosarURIFactory.getTrailingAbsoluteQualifiedNameSegment(path);
					if (path != null && path.length() == 0)
					{
						path = null;
					}
				}
			}
			else
			{
				path = getMasterObject().gGetShortName();
			}
			return new ConstantAttributeAccessor(path);
		}
		else
		{
			return new NonsplitedSingleEAttributeAccessor(getAllMasterObjects(),
				(EAttribute) getMasterEClass().getEStructuralFeature(featureName), null);
		}
	}

	/**
	 * @return
	 */
	private EClass getMasterEClass()
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject());
		return factory.getConcreteEClass(GinfrastructurePackage.Literals.GIDENTIFIABLE);
	}
}
