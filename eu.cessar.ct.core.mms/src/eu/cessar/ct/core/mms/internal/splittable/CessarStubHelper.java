/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 13.05.2014 16:03:35
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.splittable;

import java.util.ArrayList;
import java.util.HashMap;

import org.artop.aal.gautosar.services.splitting.handler.StubHelper;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * Override ARTOP {@link StubHelper} by passing <code>null</code> as <code>additionalMergeFeatures</code> to super
 * methods
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue May 13 17:48:59 2014 %
 * 
 *         %version: 1 %
 */
// CHECKSTYLE:OFF
public class CessarStubHelper extends StubHelper
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.gautosar.services.splitting.handler.StubHelper#removeStub(java.util.HashMap,
	 * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public EObject removeStub(HashMap<EStructuralFeature, ArrayList<EObject>> additionalMergeFeatures,
		EObject eObject1, EObject eObject2)
	{
		return super.removeStub(null, eObject1, eObject2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.gautosar.services.splitting.handler.StubHelper#removeStub(java.util.HashMap,
	 * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	protected EObject removeStub(HashMap<EStructuralFeature, ArrayList<EObject>> additionalMergeFeatures,
		EObject eObject1, EObject eObject2, EStructuralFeature feature)
	{
		return super.removeStub(null, eObject1, eObject2, feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.gautosar.services.splitting.handler.StubHelper#removeStub(java.util.HashMap,
	 * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected EObject removeStub(HashMap<EStructuralFeature, ArrayList<EObject>> additionalMergeFeatures,
		EObject eObject1, EObject eObject2, EReference reference)
	{
		return super.removeStub(null, eObject1, eObject2, reference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.gautosar.services.splitting.handler.StubHelper#removeStub(java.util.HashMap,
	 * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected EObject removeStub(HashMap<EStructuralFeature, ArrayList<EObject>> additionalMergeFeatures,
		EObject eObject1, EObject eObject2, EAttribute attribute)
	{
		return super.removeStub(null, eObject1, eObject2, attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.gautosar.services.splitting.handler.StubHelper#removeStub(java.util.HashMap,
	 * org.eclipse.emf.ecore.util.FeatureMap, org.eclipse.emf.ecore.util.FeatureMap)
	 */
	@Override
	protected FeatureMap removeStub(HashMap<EStructuralFeature, ArrayList<EObject>> additionalMergeFeatures,
		FeatureMap featureMap1, FeatureMap featureMap2)
	{
		return super.removeStub(null, featureMap1, featureMap2);
	}

}
// CHECKSTYLE:ON
