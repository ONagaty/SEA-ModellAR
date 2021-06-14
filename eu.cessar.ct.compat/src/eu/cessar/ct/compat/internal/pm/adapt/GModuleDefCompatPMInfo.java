/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 14, 2010 3:42:19 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.core.resources.IProject;
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
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GModuleDefCompatPMInfo extends AbstractParentedPMInfo<GModuleDef> implements
	IPMCompatElementInfo<GModuleDef>
{

	/**
	 * 
	 */
	private static final String E_CONTAINING_FEATURE = "eContainingFeature"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final String TARGET2 = "TARGET"; //$NON-NLS-1$
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
		clz.getESuperTypes().add(getIModuleClass(ecucPM));
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
		EMFPMUtils.setProxyAnnotation(sNameAttr, TARGET2, SHORT_NAME);
		clz.getEStructuralFeatures().add(sNameAttr);
	}

	/**
	 * @param ecucPM
	 * @return
	 */
	private EClass getIModuleClass(IEcucPresentationModel ecucPM)
	{
		IPMElementInfo<?> info = getParentInfo();
		EClass iModuleClass = null;
		while (info != null)
		{
			if (info.getInitialElement() instanceof IProject)
			{
				iModuleClass = (EClass) info.getParentPackage(ecucPM).getEClassifier("IModule"); //$NON-NLS-1$
				break;
			}
			else
			{
				info = info.getParentInfo();
			}
		}
		return iModuleClass;
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
		parentRef.setChangeable(false);
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
