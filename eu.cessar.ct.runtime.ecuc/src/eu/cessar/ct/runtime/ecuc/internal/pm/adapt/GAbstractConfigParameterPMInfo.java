/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 18, 2010 4:37:18 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypedElement;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.util.BigIntegerUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.DelegatedEPackageImpl;
import eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * Abstract class for handling parameters on normal mode and on compat.
 */
public abstract class GAbstractConfigParameterPMInfo extends AbstractParentedPMInfo<GConfigParameter>
{
	/**
	 * Slash
	 */
	protected static final String SLASH = "/"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pm.AbstractParentedPMInfo#createSubPackage(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel
	 * )
	 */
	@Override
	protected EPackage createSubPackage(IEcucPresentationModel ecucPM)
	{
		EPackage result = new DelegatedEPackageImpl(ecucPM.getProject(), ecucPM.getProxyEngine());
		EPackage parentPack = getParentPackage(ecucPM);
		List<ENamedElement> lists = EMFPMUtils.getPackageNamedElements(parentPack);
		result.setName(EMFPMUtils.genLCaseName(lists, element.gGetShortName()));
		result.setNsPrefix(EMFPMUtils.genName(lists, element.gGetShortName()));
		result.setNsURI(parentPack.getNsURI() + SLASH + result.getNsPrefix());
		parentPack.getESubpackages().add(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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

		EAttribute attribute = ecoreFactory.createEAttribute();
		attribute.setName(EMFPMUtils.genName(getParentClass().getEAllStructuralFeatures(), element.gGetShortName()));
		attribute.setChangeable(true);
		attribute.setUnique(false);
		attribute.setUnsettable(true);
		attribute.setLowerBound(ecucMMService.getLowerMultiplicity(element, BigInteger.ZERO, true).intValue());
		attribute.setEType(classifier);
		BigInteger upper = ecucMMService.getUpperMultiplicity(element, BigInteger.ONE, true);
		int value = BigIntegerUtils.getRestrictedToInt(upper);
		if (value == Integer.MAX_VALUE)
		{
			attribute.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
		}
		else
		{
			attribute.setUpperBound(value);
		}
		EMFPMUtils.setProxyAnnotation(attribute, "TYPE", element.eClass().getName()); //$NON-NLS-1$
		EMFPMUtils.setProxyAnnotation(attribute, "NAME", element.gGetShortName()); //$NON-NLS-1$
		getParentClass().getEStructuralFeatures().add(attribute);
	}

	/**
	 * @param ecucPM
	 */
	protected abstract void createClassifier(IEcucPresentationModel ecucPM);

}
