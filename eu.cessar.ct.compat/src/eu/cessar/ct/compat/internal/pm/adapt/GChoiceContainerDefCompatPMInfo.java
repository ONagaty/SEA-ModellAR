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
import gautosar.gecucparameterdef.GChoiceContainerDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GChoiceContainerDefCompatPMInfo extends AbstractParentedPMInfo<GChoiceContainerDef>
	implements IPMCompatElementInfo<GChoiceContainerDef>
{

	/**
	 * 
	 */
	private static final String E_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String URI = "URI"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String TYPE = "TYPE"; //$NON-NLS-1$

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

	/**
	 * @param ecucPM
	 */
	private void createClassifer(IEcucPresentationModel ecucPM)
	{
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(getParentPackage(ecucPM));
		EClass clz = ecoreFactory.createEClass();
		classifier = clz;

		clz.setName(EMFPMUtils.genTCaseName(lists, element.gGetShortName()));
		// clz.getESuperTypes().add(pmPackage.Literals.IPM_CHOICE_CONTAINER);
		clz.setAbstract(true);
		clz.setInterface(true);
		EMFPMUtils.setProxyAnnotation(clz, TYPE, element.eClass().getName());
		EMFPMUtils.setProxyAnnotation(clz, URI, MetaModelUtils.getAbsoluteQualifiedName(element));

		getParentPackage(ecucPM).getEClassifiers().add(clz);

	}

	/**
	 * @param ecucPM
	 */
	private void createParentReference(IEcucPresentationModel ecucPM)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(ecucPM.getProject()).getEcucMMService();

		EReference parentRef = ecoreFactory.createEReference();
		parentFeature = parentRef;

		parentRef.setName(EMFPMUtils.genName(getParentClass().getEStructuralFeatures(),
			element.gGetShortName()));
		parentRef.setEType(classifier);
		EMFPMUtils.setProxyAnnotation(classifier, E_CONTAINING_FEATURE, parentRef.getName());
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
