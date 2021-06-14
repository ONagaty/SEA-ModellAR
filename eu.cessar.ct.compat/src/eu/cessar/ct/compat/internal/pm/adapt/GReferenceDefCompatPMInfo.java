/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigReferenceDefPMInfo;
import eu.cessar.ct.runtime.ecuc.pm.IPMCompatElementInfo;
import gautosar.gecucparameterdef.GConfigReference;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class GReferenceDefCompatPMInfo extends GConfigReferenceDefPMInfo implements
	IPMCompatElementInfo<GConfigReference>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigParameterPMInfo#createClassifier(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	@Override
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		classifier = CompatibilitySupport.getModelConstants(ecucPM.getProject()).getEClassIContainerDef();
	}

}
