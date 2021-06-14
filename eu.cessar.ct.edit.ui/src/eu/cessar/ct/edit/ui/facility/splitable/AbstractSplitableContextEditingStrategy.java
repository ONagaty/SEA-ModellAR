/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 23.10.2012 16:33:47
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import java.util.ArrayList;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;

/**
 * Basic implementation of {@link ISplitableContextEditingStrategy}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Mar 15 11:38:01 2013 %
 * 
 *         %version: 3 %
 */
public abstract class AbstractSplitableContextEditingStrategy implements ISplitableContextEditingStrategy
{
	private Splitable splitableInput;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#setInput(org.artop.aal.gautosar.services.splitting
	 * .Splitable)
	 */
	@Override
	public void setInput(Splitable input)
	{
		splitableInput = input;
	}

	/**
	 * @return the splittable input
	 */
	protected Splitable getSplitableInput()
	{
		return splitableInput;
	}

	/**
	 * @param relevantFragments
	 * @return whether the piece of model has the same value in all the given fragments
	 */
	protected abstract boolean haveSameValue(List<EObject> relevantFragments);

	/**
	 * @return whether the piece of model allows more than one value
	 */
	protected abstract boolean isMultiValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#isEditingAllowed()
	 */
	@Override
	public boolean isEditingAllowed()
	{
		boolean allowEdit = false;
		List<EObject> relevantFragments = getFragmentsWithValue();
		int size = relevantFragments.size();
		// no value in any fragment
		if (size == 0)
		{
			allowEdit = true;
		}
		// one value in one fragment
		else if (size == 1)
		{
			allowEdit = true;

		}// same or different values in two or more fragments
		else
		{
			boolean sameValues = areValuesConsistent();
			if (isSplittingAllowed())
			{
				if (isMultiValue())
				{
					allowEdit = true;
				}
				else
				{
					allowEdit = sameValues;
				}
			}
			else
			{
				allowEdit = sameValues;
			}

		}

		return allowEdit;
	}

	/**
	 * @return
	 */
	protected IMetaModelService getMetaModelService()
	{
		EObject activeFragment = SplitableUtils.INSTANCE.getActiveFragment(splitableInput);
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(activeFragment.eClass());

		return mmService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#getResourcesWithValue()
	 */
	@Override
	public List<Resource> getResourcesWithValue()
	{
		List<Resource> resources = new ArrayList<Resource>();
		for (EObject obj: getFragmentsWithValue())
		{
			resources.add(obj.eResource());
		}

		return resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#getAllFragments()
	 */
	@Override
	public List<EObject> getAllFragments()
	{
		List<EObject> allFragments = SplitableUtils.INSTANCE.getAllFragments(splitableInput);
		return allFragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#getFragmentsInScope()
	 */
	@Override
	public List<EObject> getFragmentsInScope()
	{
		List<EObject> fragmentsInScope = new ArrayList<EObject>();
		if (!isSplittingAllowed())
		{
			fragmentsInScope.addAll(getAllFragments());
		}
		else
		{
			EObject activeFragment = SplitableUtils.INSTANCE.getActiveFragment(getSplitableInput());
			fragmentsInScope.add(activeFragment);
		}
		return fragmentsInScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.ISplitableContextEditingStrategy#areValuesConsistent()
	 */
	@Override
	public boolean areValuesConsistent()
	{
		boolean consistent = true;

		List<EObject> relevantFragments = getFragmentsWithValue();
		int size = relevantFragments.size();

		boolean moreThanOneResource = (size > 1);
		if (moreThanOneResource)
		{
			consistent = haveSameValue(relevantFragments);
		}

		return consistent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.IStrategy#getRelevantFragments()
	 */
	@Override
	public List<EObject> getFragmentsWithValue()
	{
		return computeFragmentsWithValue();
	}

	/**
	 * 
	 * @return the fragments in which the edited piece of model is set
	 */
	protected abstract List<EObject> computeFragmentsWithValue();
}
