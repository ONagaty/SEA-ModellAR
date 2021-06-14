/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Mar 7, 2014 4:50:32 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.base.Function;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;

/**
 * Utilities for split key processing.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon Mar 10 15:26:16 2014 %
 * 
 *         %version: 2 %
 */
public final class SplitKeyUtils
{
	private SplitKeyUtils()
	{

	}

	/**
	 * Get the split key for {@code eObject} using the supplied split key {@code functions} map.
	 * 
	 * @param eObject
	 *        the {@code EObject} for which to retrieve the split key
	 * @param functions
	 *        the split key map {atp.Splitkey component => getter/setter pair}
	 * @return the split key of {@code eObject}
	 */
	private static Map<String, Object> get(EObject eObject, Map<String, List<Function<Object, Object>>> functions)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		for (String splitKeyComponent: functions.keySet())
		{
			result.put(splitKeyComponent, functions.get(splitKeyComponent).get(0).apply(eObject));
		}
		return result;
	}

	/**
	 * Get the split key of {@code eObject}.
	 * 
	 * @param eObject
	 *        the {@code EObject} for which to retrieve the split key.
	 * @return the split key as a map {atp.Splitkey component => value}
	 */
	public static Map<String, Object> get(EObject eObject)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(eObject.eClass());
		EStructuralFeature feature = eObject.eContainingFeature();
		if (null == feature)
		{
			return Collections.EMPTY_MAP;
		}
		Map<String, List<Function<Object, Object>>> splitKeyFunctions = mmService.getSplitKey(feature);

		return get(eObject, splitKeyFunctions);
	}

	/**
	 * Replicate the split key from {@code source} to {@code dest}
	 * 
	 * @param dest
	 *        the destination {@code EObject} created for split
	 * @param source
	 *        the source {@code EObject} that is split
	 */
	public static void copySplitKey(EObject dest, EObject source)
	{
		EClass sourceEClass = source.eClass();
		if (dest.eClass() != sourceEClass)
		{
			return;
		}
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(sourceEClass);
		Map<String, List<Function<Object, Object>>> splitKeyFunctions = mmService.getSplitKey(source.eContainingFeature());

		setSplitKey(dest, splitKeyFunctions, get(source));
	}

	/**
	 * Set split key of {@code b} to {@code aSplitKey} using the split key functions in {@code splitKeyFunctions}.
	 * 
	 * @param b
	 *        the destination EObject
	 * @param splitKeyFunctions
	 *        the split key component=>getter/setter map
	 * @param aSplitKey
	 *        the split key values
	 */
	public static void setSplitKey(EObject b, Map<String, List<Function<Object, Object>>> splitKeyFunctions,
		Map<String, Object> aSplitKey)
	{
		if (!splitKeyFunctions.keySet().equals(aSplitKey.keySet()))
		{
			return;
		}
		for (String key: aSplitKey.keySet())
		{
			splitKeyFunctions.get(key).get(1).apply(new CessarEObjectObjectPair(b, aSplitKey.get(key)));
		}

	}

	/**
	 * Compare to concrete split keys {@code aSplitKey} and {@code bSplitKey}.
	 * 
	 * @param aSplitKey
	 *        the first non-null split key
	 * @param bSplitKey
	 *        the second non-null split key
	 * @return true <b>iff</b> the split keys are the same
	 */
	public static boolean sameKey(Map<String, Object> aSplitKey, Map<String, Object> bSplitKey)
	{
		for (String key: aSplitKey.keySet())
		{
			Object aValue = aSplitKey.get(key);
			Object bValue = bSplitKey.get(key);
			if (aValue == null && bValue == null)
			{
				continue;
			}
			if ((null == aValue && null != bValue) || (null != aValue && null == bValue))
			{
				return false;
			}
			if (!aValue.equals(bValue))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Compare the split key of {@code a} to that of {@code b}.
	 * 
	 * @param a
	 *        the first {@code EObject}
	 * @param b
	 *        the second {@code EObject}
	 * @return true <b>iff</b> split keys are the same
	 */
	public static boolean sameKey(EObject a, EObject b)
	{
		if (!a.eClass().equals(b.eClass()))
		{
			return false;
		}
		Map<String, Object> aSplitKey = get(a);
		Map<String, Object> bSplitKey = get(b);
		if ((aSplitKey == null && bSplitKey != null) || (aSplitKey != null && bSplitKey == null))
		{
			return false;
		}
		if (aSplitKey == null && bSplitKey == null)
		{
			return true;
		}
		Set<String> aKeySet = aSplitKey.keySet();
		Set<String> bKeySet = bSplitKey.keySet();
		if (!aKeySet.equals(bKeySet))
		{
			return false;
		}
		return sameKey(aSplitKey, bSplitKey);
	}
}
