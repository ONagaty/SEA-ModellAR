/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.sdk.pm.pmPackage;

/**
 * 
 */
public class GReferenceDefPMInfo extends GConfigReferenceDefPMInfo
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.pm.adapt.GConfigParameterPMInfo#createClassifier(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	@Override
	protected void createClassifier(IEcucPresentationModel ecucPM)
	{
		classifier = pmPackage.Literals.IPM_CONTAINER;
	}

}
