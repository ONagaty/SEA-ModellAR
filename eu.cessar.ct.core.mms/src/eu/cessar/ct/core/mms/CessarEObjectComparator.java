/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 26.10.2012 11:32:38
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * Compares EObjects based on the values of a specific feature.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Apr  1 09:10:34 2013 %
 * 
 *         %version: 1 %
 */
public class CessarEObjectComparator extends AbstractCessarComparator<EObject>
{
	private final EStructuralFeature feature;

	/**
	 * @param feature
	 *        the feature that dictates comparison criteria
	 */
	public CessarEObjectComparator(EStructuralFeature feature)
	{
		this.feature = feature;
	}

	/**
	 * Compares the two given {@link EObject}s, based on whether the their values on the <code>feature</code> specified
	 * on the constructor match or not.
	 * 
	 * @param eObject1
	 *        the first EObject to be compared.
	 * @param eObject2
	 *        the second EObject to be compared.
	 * @return a negative integer, zero, or a positive integer as the value of first argument on the provided feature is
	 *         less than, equal to, or greater than that of the second.
	 */

	public int compare(EObject o1, EObject o2)
	{
		int result = 0;

		if (feature instanceof EAttribute)
		{
			if (feature.eClass().getInstanceClass().isPrimitive())
			{
				result = ((Comparable<EObject>) o1).compareTo(o2);
			}
			else
			{
				Object value1 = o1.eGet(feature);
				Object value2 = o2.eGet(feature);
				if (feature.isMany())
				{
					result = doCompareLists((List<Object>) value1, (List<Object>) value2);
				}
				else
				{
					result = doCompareObjects(value1, value2);
				}
			}
		}
		else
		{

			result = doCompareByERefrence(o1, o2, (EReference) feature);
		}
		return result;
	}

	private static int doCompareByERefrence(EObject eObject1, EObject eObject2, EReference feature)
	{
		int result = 0;
		// compares by qualified name
		if (!feature.isContainment())
		{
			Object value1 = eObject1.eGet(feature);
			Object value2 = eObject2.eGet(feature);

			List<Object> listToCompare1 = getQualifiedNamesList(value1);
			List<Object> listToCompare2 = getQualifiedNamesList(value2);

			result = doCompareLists(listToCompare1, listToCompare2);
		}
		else
		{
			// TODO: containment reference

			Object val1 = eObject1.eGet(feature);
			Object val2 = eObject2.eGet(feature);

			// temporary solution
			if (val1 != null && val2 != null)
			{
				if (val1 instanceof EObject)
				{
					EStructuralFeature mixedFeat = ((EObject) val1).eClass().getEStructuralFeature("mixed"); //$NON-NLS-1$
					if (mixedFeat != null)
					{
						Object val11 = ((EObject) val1).eGet(mixedFeat);
						Object vall22 = ((EObject) val2).eGet(mixedFeat);
						result = doCompareObjects(val11, vall22);
					}

				}
			}
			else
			{
				result = 0;
			}
		}

		return result;
	}

	/**
	 * @param value
	 *        an EObject or a list of {@link EObject}s
	 * @return list with the qualified name(s)
	 */
	private static List<Object> getQualifiedNamesList(Object value)
	{
		List<Object> list = new ArrayList<Object>();

		if (value instanceof List<?>)
		{
			List<?> l1 = (List<?>) value;
			for (Object eObj: l1)
			{
				if (eObj instanceof EObject)
				{
					String qualifiedName = ModelUtils.getAbsoluteQualifiedName((EObject) eObj);
					list.add(qualifiedName);
				}
			}
		}
		else if (value instanceof EObject)
		{
			String qualifiedName1 = ""; //$NON-NLS-1$
			qualifiedName1 = ModelUtils.getAbsoluteQualifiedName((EObject) value);

			list.add(qualifiedName1);
		}

		return list;
	}

}
