/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 14, 2010 10:03:30 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * @Review uidl7321 - Apr 11, 2012
 */
public class GARPackageCompatPMInfo extends AbstractParentedPMInfo<GARPackage> implements
	IPMCompatElementInfo<GARPackage>
{

	/**
	 * 
	 */
	private static final String E_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String DIRECT_MULTI_ATTRIBUTE = "DirectMultiAttribute"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String SHORT_NAME = "shortName"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String URI = "URI"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String TARGET = "TARGET"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String TYPE = "TYPE"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo#createSubPackage()
	 */
	@Override
	protected EPackage createSubPackage(IEcucPresentationModel ecucPM)
	{
		EPackage result = new DelegatedEPackageImpl(ecucPM.getProject(), ecucPM.getProxyEngine());
		EPackage parentPack = getParentPackage(ecucPM);
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(parentPack);
		result.setName(EMFPMUtils.genLCaseName(lists, element.gGetShortName()));
		result.setNsPrefix(EMFPMUtils.genName(lists, element.gGetShortName()));
		result.setNsURI(parentPack.getNsURI() + "/" + result.getNsPrefix()); //$NON-NLS-1$
		parentPack.getESubpackages().add(result);
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.AbstractPMInfo#create(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	@Override
	protected void create(IEcucPresentationModel ecucPM)
	{
		createClassifer(ecucPM);
		createParentReference(/*ecucPM*/);
	}

	/**
	 * @param ecucPM
	 */
	private void createClassifer(IEcucPresentationModel ecucPM)
	{
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(getParentPackage(ecucPM));
		EClass clz = ecoreFactory.createEClass();
		classifier = clz;

		clz.setName(EMFPMUtils.genTCaseName(lists, element.gGetShortName()));
		// clz.getESuperTypes().add(pmPackage.Literals.IPM_PACKAGE);
		EMFPMUtils.setProxyAnnotation(clz, TYPE, element.eClass().getName());
		EMFPMUtils.setProxyAnnotation(clz, URI, MetaModelUtils.getAbsoluteQualifiedName(element));

		getParentPackage(ecucPM).getEClassifiers().add(clz);

		EAttribute sNameAttr = ecoreFactory.createEAttribute();
		sNameAttr.setName(SHORT_NAME);
		sNameAttr.setChangeable(false);
		sNameAttr.setUnsettable(true);
		sNameAttr.setLowerBound(1);
		sNameAttr.setEType(ecorePackage.getEString());
		EMFPMUtils.setProxyAnnotation(sNameAttr, TYPE, DIRECT_MULTI_ATTRIBUTE);
		EMFPMUtils.setProxyAnnotation(sNameAttr, TARGET, SHORT_NAME);
		clz.getEStructuralFeatures().add(sNameAttr);
	}

	/**
	 * @param ecucPM
	 */
	private void createParentReference(/*IEcucPresentationModel ecucPM*/)
	{
		// TODO Auto-generated method stub
		EReference parentRef = ecoreFactory.createEReference();
		parentFeature = parentRef;

		parentRef.setName(EMFPMUtils.genName(getParentClass().getEStructuralFeatures(),
			element.gGetShortName()));
		EMFPMUtils.setProxyAnnotation(classifier, E_CONTAINING_FEATURE, parentRef.getName());
		parentRef.setEType(classifier);
		parentRef.setChangeable(false);
		parentRef.setContainment(true);
		parentRef.setUnsettable(true);
		parentRef.setLowerBound(1);
		EMFPMUtils.setProxyAnnotation(parentRef, TYPE, element.eClass().getName());

		getParentClass().getEStructuralFeatures().add(parentRef);
	}

}
