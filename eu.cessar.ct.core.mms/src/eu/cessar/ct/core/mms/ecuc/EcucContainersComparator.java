/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * 26.10.2012 12:02:43
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms.ecuc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.core.mms.AbstractCessarComparator;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Comparator of {@link GContainer}s based on the parameter/reference values with a specific definition.
 *
 * @author uidl6870
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Thu Apr 11 12:46:15 2013 %
 *
 *         %version: 1 %
 */
public class EcucContainersComparator extends AbstractCessarComparator<GContainer>
{
	private final GCommonConfigurationAttributes definition;

	/**
	 * @param definition
	 *        the definition of the parameter/reference values to be compared
	 */
	public EcucContainersComparator(GCommonConfigurationAttributes definition)
	{
		this.definition = definition;
	}

	/**
	 * Compares the two given {@link GContainer}s, based on whether their parameter values with the
	 * <code>definition</code> specified on the constructor match or not.
	 *
	 * @param container1
	 *        the first container to be compared
	 * @param container2
	 *        the second container to be compared
	 *
	 * @return a negative integer, zero, or a positive integer as the parameter value(s) (having the definition passed
	 *         on the constructor) of first container is less than, equal to, or greater than that of the second.
	 */
	public int compare(GContainer container1, GContainer container2)
	{
		if (definition == null)
		{
			return 0;
		}
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(definition.eClass());
		IEcucMMService ecucMMService = mmService.getEcucMMService();

		List<Object> list1 = getObjectsToBeCompared(ecucMMService, container1, definition);

		List<Object> list2 = getObjectsToBeCompared(ecucMMService, container2, definition);

		return doCompareLists(list1, list2);
	}

	private static List<Object> getObjectsToBeCompared(IEcucMMService ecucMMS, GContainer container,
		GCommonConfigurationAttributes definition)
	{
		List<Object> objects = new ArrayList<Object>();

		if (definition instanceof GConfigReference)
		{
			EList<GConfigReferenceValue> referenceValues = container.gGetReferenceValues();
			for (GConfigReferenceValue referenceValue: referenceValues)
			{
				if (SplitableUtils.INSTANCE.hasDefinition(referenceValue, definition))
				{
					if (referenceValue instanceof GReferenceValue)
					{
						GReferrable value = ((GReferenceValue) referenceValue).gGetValue();
						String qualifiedName = ModelUtils.getAbsoluteQualifiedName(value);

						objects.add(qualifiedName);
					}
					else if (referenceValue instanceof GInstanceReferenceValue)
					{
						GInstanceReferenceValue irefValue = (GInstanceReferenceValue) referenceValue;

						EList<GIdentifiable> contexts = ecucMMS.getInstanceRefContext(irefValue);
						for (GIdentifiable context: contexts)
						{
							String absoluteQualifiedName = ModelUtils.getAbsoluteQualifiedName(context);
							objects.add(absoluteQualifiedName);
						}

						GIdentifiable target = ecucMMS.getInstanceRefTarget(irefValue);
						String absoluteQualifiedName = ModelUtils.getAbsoluteQualifiedName(target);

						objects.add(absoluteQualifiedName);
					}
				}
			}
		}
		else if (definition instanceof GConfigParameter)
		{

			EList<GParameterValue> parameterValues = container.gGetParameterValues();
			for (GParameterValue parameterValue: parameterValues)
			{
				if (SplitableUtils.INSTANCE.hasDefinition(parameterValue, definition))
				{
					objects.add(ecucMMS.getParameterValue(parameterValue));
				}
			}
		}
		return objects;
	}

}
