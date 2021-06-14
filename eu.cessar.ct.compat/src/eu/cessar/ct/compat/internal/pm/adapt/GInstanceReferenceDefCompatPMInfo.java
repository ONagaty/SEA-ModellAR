/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.BigIntegerUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.gecucparameterdef.GInstanceReferenceDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GInstanceReferenceDefCompatPMInfo extends
	AbstractParentedPMInfo<GInstanceReferenceDef> implements
	IPMCompatElementInfo<GInstanceReferenceDef>
{

	/**
	 * 
	 */
	private static final String E_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$

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
		createClassifier(ecucPM);
		createParentReference(ecucPM);
	}

	/**
	 * @param ecucPM
	 */
	private void createParentReference(IEcucPresentationModel ecucPM)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(ecucPM.getProject()).getEcucMMService();

		EReference parentRef = ecoreFactory.createEReference();
		parentRef.setName(EMFPMUtils.genName(getParentClass().getEAllStructuralFeatures(),
			element.gGetShortName()));
		parentRef.setContainment(true);
		parentRef.setChangeable(true);
		parentRef.setUnique(false);
		parentRef.setUnsettable(true);
		parentRef.setLowerBound(ecucMMService.getLowerMultiplicity(element, BigInteger.ZERO, true).intValue());
		parentRef.setEType(classifier);
		EMFPMUtils.setProxyAnnotation(classifier, E_CONTAINING_FEATURE, parentRef.getName());
		BigInteger upper = ecucMMService.getUpperMultiplicity(element, BigInteger.ONE, true);
		int value = BigIntegerUtils.getRestrictedToInt(upper);
		if (value == Integer.MAX_VALUE)
		{
			parentRef.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		}
		else
		{
			parentRef.setUpperBound(value);
		}
		EMFPMUtils.setProxyAnnotation(parentRef, "TYPE", element.eClass().getName()); //$NON-NLS-1$
		getParentClass().getEStructuralFeatures().add(parentRef);
	}

	/**
	 * @param ecucPM
	 */
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		EClass iRefClass = ecoreFactory.createEClass();
		classifier = iRefClass;
		iRefClass.setName(EMFPMUtils.genTCaseName(getParentPackage(ecucPM).getEClassifiers(),
			element.gGetShortName()));
		// iRefClass.getESuperTypes().add(pmPackage.Literals.IPM_INSTANCE_REF);

		EMFPMUtils.setProxyAnnotation(iRefClass, "TYPE", element.eClass().getName()); //$NON-NLS-1$
		EMFPMUtils.setProxyAnnotation(iRefClass, "URI", //$NON-NLS-1$
			MetaModelUtils.getAbsoluteQualifiedName(element));
		getParentPackage(ecucPM).getEClassifiers().add(iRefClass);

		EAttribute refAttr = ecoreFactory.createEAttribute();
		refAttr.setName("ContextReferences"); //$NON-NLS-1$
		refAttr.setEType(ecorePackage.getEString());
		refAttr.setChangeable(true);
		refAttr.setUnsettable(true);
		refAttr.setLowerBound(0);
		refAttr.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		EMFPMUtils.setProxyAnnotation(refAttr, "TYPE", element.eClass().getName() + ".context"); //$NON-NLS-1$ //$NON-NLS-2$
		iRefClass.getEStructuralFeatures().add(refAttr);

		refAttr = ecoreFactory.createEAttribute();
		refAttr.setName("Destination"); //$NON-NLS-1$
		refAttr.setEType(ecorePackage.getEString());
		refAttr.setChangeable(true);
		refAttr.setUnsettable(true);
		refAttr.setLowerBound(1);
		refAttr.setUpperBound(1);
		EMFPMUtils.setProxyAnnotation(refAttr, "TYPE", element.eClass().getName() + ".target"); //$NON-NLS-1$ //$NON-NLS-2$
		iRefClass.getEStructuralFeatures().add(refAttr);
	}

}
