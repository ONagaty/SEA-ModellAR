/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import java.math.BigInteger;
import java.util.List;

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
import eu.cessar.ct.sdk.pm.pmPackage;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GinfrastructurePackage;

/**
 * 
 */
public class GInstanceReferenceDefPMInfo extends AbstractParentedPMInfo<GInstanceReferenceDef>
{

	@Override
	protected EPackage createSubPackage(IEcucPresentationModel ecucPM)
	{
		EPackage result = new DelegatedEPackageImpl(ecucPM.getProject(), ecucPM.getProxyEngine());
		EPackage parentPack = getParentPackage(ecucPM);
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(parentPack);
		result.setName(EMFPMUtils.genLCaseName(lists, element.gGetShortName()));
		result.setNsPrefix(EMFPMUtils.genName(lists, element.gGetShortName()));
		result.setNsURI(parentPack.getNsURI() + "/" + result.getNsPrefix());
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
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(element).getEcucMMService();

		EReference parentRef = ecoreFactory.createEReference();
		parentRef.setName(EMFPMUtils.genName(getParentClass().getEAllStructuralFeatures(),
			element.gGetShortName()));
		parentRef.setContainment(true);
		parentRef.setChangeable(true);
		parentRef.setUnique(false);
		parentRef.setUnsettable(true);
		parentRef.setLowerBound(ecucMMService.getLowerMultiplicity(element, BigInteger.ZERO, true).intValue());
		parentRef.setEType(classifier);
		EMFPMUtils.setProxyAnnotation(classifier, "eContainingFeature", parentRef.getName());
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
		EMFPMUtils.setProxyAnnotation(parentRef, "TYPE", element.eClass().getName());
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
		iRefClass.getESuperTypes().add(pmPackage.Literals.IPM_INSTANCE_REF);

		EMFPMUtils.setProxyAnnotation(iRefClass, "TYPE", element.eClass().getName());
		EMFPMUtils.setProxyAnnotation(iRefClass, "URI",
			MetaModelUtils.getAbsoluteQualifiedName(element));
		getParentPackage(ecucPM).getEClassifiers().add(iRefClass);

		EReference refAttr = ecoreFactory.createEReference();
		refAttr.setName("target");
		refAttr.setEType(GinfrastructurePackage.Literals.GIDENTIFIABLE);
		refAttr.setChangeable(true);
		refAttr.setUnsettable(true);
		refAttr.setContainment(false);
		refAttr.setLowerBound(1);
		refAttr.setUpperBound(1);
		EMFPMUtils.setProxyAnnotation(refAttr, "TYPE", element.eClass().getName() + ".target");
		iRefClass.getEStructuralFeatures().add(refAttr);

		refAttr = ecoreFactory.createEReference();
		refAttr.setName("contexts");
		refAttr.setEType(GinfrastructurePackage.Literals.GIDENTIFIABLE);
		refAttr.setChangeable(true);
		refAttr.setUnsettable(true);
		refAttr.setContainment(false);
		refAttr.setLowerBound(1);
		refAttr.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		EMFPMUtils.setProxyAnnotation(refAttr, "TYPE", element.eClass().getName() + ".context");
		iRefClass.getEStructuralFeatures().add(refAttr);
	}

}
