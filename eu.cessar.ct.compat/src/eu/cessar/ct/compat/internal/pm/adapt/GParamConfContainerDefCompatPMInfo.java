/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 14, 2010 6:06:05 PM </copyright>
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

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.BigIntegerUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GParamConfContainerDefCompatPMInfo extends
	AbstractParentedPMInfo<GParamConfContainerDef> implements
	IPMCompatElementInfo<GParamConfContainerDef>
{

	/**
	 * 
	 */
	private static final String TARGET = "TARGET"; //$NON-NLS-1$
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
	private static final String TYPE = "TYPE"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String E_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo#createSubPackage(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
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
		createParentReference(ecucPM);
	}

	private boolean isContainedInChoice()
	{
		if (getParentInfo() != null)
		{
			return getParentInfo().getInitialElement() instanceof GChoiceContainerDef;
		}
		return false;
	}

	/**
	 * @param ecucPM
	 */
	private void createClassifer(IEcucPresentationModel ecucPM)
	{
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(getParentPackage(ecucPM));
		EClass clz = ecoreFactory.createEClass();
		classifier = clz;

		clz.setName(EMFPMUtils.genName(lists, element.gGetShortName()));
		clz.getESuperTypes().add(
			CompatibilitySupport.getModelConstants(ecucPM.getProject()).getEClassIContainerDef());
		// check if the parent element is a chocie container
		if (isContainedInChoice())
		{
			clz.getESuperTypes().add(getParentClass());
			EMFPMUtils.setProxyAnnotation(clz, E_CONTAINING_FEATURE,
				getParentInfo().getParentFeature().getName());
		}
		EMFPMUtils.setProxyAnnotation(clz, TYPE, element.eClass().getName());
		EMFPMUtils.setProxyAnnotation(clz, URI, MetaModelUtils.getAbsoluteQualifiedName(element));

		getParentPackage(ecucPM).getEClassifiers().add(clz);

		EAttribute sNameAttr = ecoreFactory.createEAttribute();
		sNameAttr.setName(SHORT_NAME);
		sNameAttr.setChangeable(true);
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
	private void createParentReference(IEcucPresentationModel ecucPM)
	{
		if (isContainedInChoice())
		{
			// no reference required in parent
			return;
		}
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(ecucPM.getProject()).getEcucMMService();

		EReference parentRef = ecoreFactory.createEReference();
		parentFeature = parentRef;

		parentRef.setName(EMFPMUtils.genName(getParentClass().getEStructuralFeatures(),
			element.gGetShortName()));
		EMFPMUtils.setProxyAnnotation(classifier, E_CONTAINING_FEATURE, parentRef.getName());
		parentRef.setEType(classifier);
		parentRef.setChangeable(true);
		parentRef.setUnsettable(true);
		parentRef.setContainment(true);
		parentRef.setLowerBound(BigIntegerUtils.getRestrictedToInt(ecucMMService.getLowerMultiplicity(
			element, BigInteger.ONE, true)));
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
		EMFPMUtils.setProxyAnnotation(parentRef, TYPE, element.eClass().getName());

		getParentClass().getEStructuralFeatures().add(parentRef);
	}
}
