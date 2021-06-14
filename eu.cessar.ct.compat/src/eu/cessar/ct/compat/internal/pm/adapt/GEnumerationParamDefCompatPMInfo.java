/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.ASMNames;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.Constants;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;
import gautosar.gecucparameterdef.GEnumerationParamDef;

/**
 * Class for handling enumeration parameters on compat.
 */
public class GEnumerationParamDefCompatPMInfo extends GConfigParameterCompatPMInfo
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigParameterCompatPMInfo#createClassifier(eu.cessar.ct.runtime
	 * .ecuc .IEcucPresentationModel)
	 */
	@Override
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		GEnumerationParamDef enumDef = (GEnumerationParamDef) element;
		EEnum eEnum = ecoreFactory.createEEnum();
		classifier = eEnum;
		eEnum.setName(EMFPMUtils.genTCaseName(getParentPackage(ecucPM).getEClassifiers(), enumDef.gGetShortName()));
		getParentPackage(ecucPM).getEClassifiers().add(eEnum);
		eEnum.setInstanceClassName(ASMNames.getEEnumCName(Constants.DOT, eEnum));
		for (int i = 0; i < enumDef.gGetLiterals().size(); i++)
		{
			EEnumLiteral enumLiteral = ecoreFactory.createEEnumLiteral();
			enumLiteral.setName(EMFPMUtils.genName(eEnum.getELiterals(), enumDef.gGetLiterals().get(i).gGetShortName()));
			enumLiteral.setValue(i);
			eEnum.getELiterals().add(enumLiteral);
		}
	}
}
