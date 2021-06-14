/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Oct 25, 2012 7:37:50 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.splittable.ecuc;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Class that provide various utilities around splittable services dealing with elements on the ECUC side
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Mar 15 11:38:00 2013 %
 * 
 *         %version: 3 %
 */
public final class EcucSplitableUtils
{

	private EcucSplitableUtils()
	{
		// hide it
	}

	/**
	 * Gets the list with containers representing fragments for the given splitable's wrapped container, that own at
	 * least a parameter/reference value of the specified <code>definition</code>.
	 * 
	 * @param splitable
	 *        the splittable object
	 * @param definition
	 *        the parameter/reference definition for which the returned fragments own at least one parameter/reference
	 *        value
	 * @return list with the container fragments of interest, never <code>null</code>
	 */
	public static List<GContainer> getContainerFragmentsWithParameters(Splitable splitable,
		GCommonConfigurationAttributes definition)
	{
		EObject obj = SplitableUtils.INSTANCE.getActiveFragment(splitable);
		if (!(obj instanceof GContainer))
		{
			return Collections.EMPTY_LIST;
		}

		if (definition instanceof GConfigParameter)
		{
			return getContainerFragmentsWithParameters(splitable, (GConfigParameter) definition);
		}
		else
		{
			return getContainerFragmentsWithParameters(splitable, (GConfigReference) definition);
		}
	}

	private static List<GContainer> getContainerFragmentsWithParameters(Splitable splitable, GConfigParameter definition)
	{
		List<GContainer> fragments = new ArrayList<GContainer>();

		List<EObject> allFragments = SplitableUtils.INSTANCE.getAllFragments(splitable);
		for (EObject fragment: allFragments)
		{
			GContainer container = (GContainer) fragment;

			EList<GParameterValue> parameterValues = container.gGetParameterValues();
			for (GParameterValue parameterValue: parameterValues)
			{
				if (parameterValue.gGetDefinition() == definition)
				{
					fragments.add(container);
					break;
				}
			}
		}

		return fragments;
	}

	private static List<GContainer> getContainerFragmentsWithParameters(Splitable splitable, GConfigReference definition)
	{
		List<GContainer> fragments = new ArrayList<GContainer>();

		List<EObject> allFragments = SplitableUtils.INSTANCE.getAllFragments(splitable);
		for (EObject fragment: allFragments)
		{
			GContainer container = (GContainer) fragment;

			EList<GConfigReferenceValue> referenceValues = container.gGetReferenceValues();
			for (GConfigReferenceValue referenceValue: referenceValues)
			{
				if (referenceValue.gGetDefinition() == definition)
				{
					fragments.add(container);
					break;
				}
			}

		}

		return fragments;
	}

}
