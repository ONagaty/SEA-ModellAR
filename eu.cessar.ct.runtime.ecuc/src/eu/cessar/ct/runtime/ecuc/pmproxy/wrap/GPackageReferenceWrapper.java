/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 4:24:49 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 */
public class GPackageReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private final List<GARPackage> packages;

	public GPackageReferenceWrapper(IEMFProxyEngine engine, List<GARPackage> packages)
	{
		super(engine);
		this.packages = packages;
		setReadOnly(true);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public EObject getValue()
	{
		if (!isSetValue())
		{
			return null;
		}
		GARPackage pack = packages.get(0);
		return getEngine().getSlaveObject(null, pack);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return packages == null || packages.size() > 0;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		// do nothing, cannot set
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		// do nothing, cannot unset
	}
}
