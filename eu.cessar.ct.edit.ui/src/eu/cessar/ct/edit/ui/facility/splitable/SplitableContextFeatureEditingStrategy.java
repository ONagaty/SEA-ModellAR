/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 23.10.2012 16:37:42
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.CessarEObjectComparator;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;

/**
 * Implementation of {@link ISplitableContextFeatureEditingStrategy}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Apr  1 09:11:40 2013 %
 * 
 *         %version: 2 %
 */
public class SplitableContextFeatureEditingStrategy extends AbstractSplitableContextEditingStrategy implements
	ISplitableContextFeatureEditingStrategy
{
	private EStructuralFeature feature;

	@Override
	public boolean isSplittingAllowed()
	{
		boolean hasSplitableSupport = false;

		if (feature instanceof EReference)
		{
			IMetaModelService service = getMetaModelService();
			hasSplitableSupport = service.isSplitableReference((EReference) feature);

		}
		else
		{
			// currently, there is no EAttribute that can be splitted
			hasSplitableSupport = false;
		}

		return hasSplitableSupport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextFeatureEditingStrategy#getFeature()
	 */
	@Override
	public EStructuralFeature getFeature()
	{
		return feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IFeatureStrategy#setFeature(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public void setFeature(EStructuralFeature feature)
	{
		this.feature = feature;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractSplitableContextEditingStrategy#computeFragmentsWithValue()
	 */
	@Override
	protected List<EObject> computeFragmentsWithValue()
	{
		return SplitableUtils.INSTANCE.getFragmentsWithFeature(getSplitableInput(), feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractSplitableContextEditingStrategy#isMultiValue()
	 */
	@Override
	protected boolean isMultiValue()
	{
		return feature.isMany();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractSplitableContextEditingStrategy#haveSameValue(java.util.List)
	 */
	@Override
	protected boolean haveSameValue(List<EObject> relevantFragments)
	{
		return areValuesEqual(relevantFragments, feature);
	}

	/**
	 * Returns whether the values on the specified <code>inputFeature</code> of all the elements from given
	 * <code>eObjectList</code> match.
	 * 
	 * @param eObjectList
	 *        EObjects to be compared
	 * @param inputFeature
	 *        the comparison criteria
	 * @return <code>true</code> if the values on the <code>inputFeature</code> of all the elements from
	 *         <code>eObjectList</code> match, <code>false</code> otherwise
	 */
	private boolean areValuesEqual(List<EObject> eObjectList, EStructuralFeature inputFeature)
	{
		boolean result = true;
		for (EObject fragment: eObjectList)
		{
			int index = eObjectList.indexOf(fragment);
			int size = eObjectList.size();
			if (size > index + 1)
			{
				boolean same = areValuesEqual(fragment, eObjectList.subList(index + 1, size), inputFeature);
				if (!same)
				{
					result = false;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * Returns whether the value on the specified <code>inputFeature</code> of the <code>targetEObject</code> matches
	 * the one of each element from <code>eObjectList</code>.
	 * 
	 * @param targetFragment
	 *        the target EObject that will be compared one by one to the elements from <code>eObjectList</code>
	 * @param eObjectList
	 *        the list of EObject to which the target will be compare to
	 * @param inputFeature
	 * @return <code>true</code> if the elements from the list match the <code>targetEbject</code> based on the value on
	 *         <code>inputFeature</code>, <code>false</code> otherwise
	 */
	private boolean areValuesEqual(EObject targetFragment, List<EObject> eObjectList, EStructuralFeature inputFeature)
	{
		boolean result = true;
		for (EObject currentFragment: eObjectList)
		{
			CessarEObjectComparator comparator = new CessarEObjectComparator(inputFeature);
			int comparisonResult = comparator.compare(targetFragment, currentFragment);
			boolean same = (comparisonResult == 0);
			if (!same)
			{
				result = false;
				break;
			}
		}

		return result;
	}
}
