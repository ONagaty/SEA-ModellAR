/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 18, 2010 4:47:36 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pm.adapt;

import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;

/**
 * Class for handling integer parameters on compat.
 */
public class GIntegerParamDefCompatPMInfo extends GConfigParameterCompatPMInfo
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
		classifier = ecorePackage.getEBigInteger();
	}
}
