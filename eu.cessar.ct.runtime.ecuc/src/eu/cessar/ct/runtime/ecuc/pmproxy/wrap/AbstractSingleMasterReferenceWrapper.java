/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:49:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractSingleMasterReferenceWrapper extends AbstractMasterReferenceWrapper implements
	ISingleMasterFeatureWrapper<EObject>
{

	protected List<GContainer> owners;
	protected EList<GReferenceValue> references;

	/**
	 * @param engine
	 */
	public AbstractSingleMasterReferenceWrapper(IEMFProxyEngine engine)
	{
		super(engine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<EObject> getFeatureClass()
	{
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		EObject value = getValue();
		if (value == null)
		{
			return 0;
		}
		else
		{
			return value.hashCode();
		}
	}

	/**
	 * Handle set multiple values for single references.
	 * 
	 * @param newValue
	 *        the value to be set
	 */
	protected void setMultiValues(final Object newValue)
	{
		DelegatingWithSourceMultiEList splitReferences = (DelegatingWithSourceMultiEList) references;
		EList<GReferenceValue> targetList = SplitPMUtils.findDefaultTargetListForSet(splitReferences,
			((GReferenceValue) newValue).gGetDefinition());
		unsetValue();
		for (GContainer owner: owners)
		{
			if (owner.eResource().equals(splitReferences.getSource(targetList)))
			{
				owner.gGetReferenceValues().add((GReferenceValue) newValue);
			}
		}
	}

	/**
	 * @param reference
	 * @return text information about the parent of the reference
	 */
	protected String getParentInformation(GARObject reference)
	{
		EObject eContainer = reference.eContainer();
		String parentInformation = ""; //$NON-NLS-1$
		if (eContainer instanceof GContainer)
		{
			GContainer container = (GContainer) eContainer;
			String parentShortName = container.gGetShortName();
			parentInformation = " from " + parentShortName; //$NON-NLS-1$
		}
		return parentInformation;
	}
}
