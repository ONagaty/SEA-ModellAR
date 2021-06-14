/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigReferenceDefPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import eu.cessar.ct.runtime.ecuc.pm.IPMElementInfo;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GForeignReferenceDefCompatPMInfo extends GConfigReferenceDefPMInfo implements
	IPMCompatElementInfo<GConfigReference>
{

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

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigParameterPMInfo#createClassifier(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	@Override
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		EPackage targetPack = getTargetPackage(ecucPM);
		IMetaModelService service = MMSRegistry.INSTANCE.getMMService(ecucPM.getProject());
		String destClass = ((GForeignReferenceDef) element).gGetDestinationType();
		EClass eClass = service.findEClass(destClass);
		if (eClass != null)
		{
			String refName = eClass.getName() + "Ref"; //$NON-NLS-1$

			classifier = targetPack.getEClassifier(refName);

			if (classifier == null)
			{
				classifier = ecoreFactory.createEClass();
				classifier.setName(EMFPMUtils.genTCaseName(targetPack.getEClassifiers(),
					eClass.getName() + "Ref")); //$NON-NLS-1$
				EMFPMUtils.setProxyAnnotation(classifier, TYPE, element.eClass().getName());
				EMFPMUtils.setProxyAnnotation(classifier, URI,
					MetaModelUtils.getAbsoluteQualifiedName(element));

				EAttribute refAttr = ecoreFactory.createEAttribute();
				refAttr.setName(SHORT_NAME);
				refAttr.setEType(ecorePackage.getEString());
				refAttr.setChangeable(true);
				refAttr.setUnsettable(true);
				refAttr.setLowerBound(1);
				refAttr.setUpperBound(1);
				EMFPMUtils.setProxyAnnotation(refAttr, TYPE, element.eClass().getName()
					+ ".shortName"); //$NON-NLS-1$
				((EClass) classifier).getEStructuralFeatures().add(refAttr);

				refAttr = ecoreFactory.createEAttribute();
				refAttr.setName("Path"); //$NON-NLS-1$
				refAttr.setEType(ecorePackage.getEString());
				refAttr.setChangeable(true);
				refAttr.setUnsettable(true);
				refAttr.setLowerBound(1);
				refAttr.setUpperBound(1);
				EMFPMUtils.setProxyAnnotation(refAttr, TYPE, element.eClass().getName() + ".Path"); //$NON-NLS-1$
				((EClass) classifier).getEStructuralFeatures().add(refAttr);

				targetPack.getEClassifiers().add(classifier);
			}

		}
		else
		{
			// invalid foreign reference destination, cannot create anything
		}
	}

	/**
	 * @return
	 */
	protected EPackage getTargetPackage(IEcucPresentationModel ecucPM)
	{
		IPMElementInfo<?> info = getParentInfo();
		while (info != null && !(info.getInitialElement() instanceof GModuleDef))
		{
			info = info.getParentInfo();
		}
		if (info != null && info.getInitialElement() instanceof GModuleDef)
		{
			return info.getSubPackage(ecucPM, true);
		}
		else
		{
			return null;
		}
	}
}
