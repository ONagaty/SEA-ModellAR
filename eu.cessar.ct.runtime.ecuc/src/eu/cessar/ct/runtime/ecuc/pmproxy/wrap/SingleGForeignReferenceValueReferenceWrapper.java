/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 5:51:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Wrapper for a single foreign reference value.
 */
public class SingleGForeignReferenceValueReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private GReferenceValue fRef;
	private final GForeignReferenceDef definition;
	private String definitionName;

	/**
	 *
	 * @param engine
	 * @param references
	 * @param fRef
	 * @param owners
	 * @param definition
	 */
	public SingleGForeignReferenceValueReferenceWrapper(IEMFProxyEngine engine, EList<GReferenceValue> references,
		GReferenceValue fRef, List<GContainer> owners, GForeignReferenceDef definition)
	{
		super(engine);
		this.references = references;
		this.fRef = fRef;
		this.owners = owners;
		this.definition = definition;

		if (definition != null)
		{
			definitionName = definition.gGetShortName();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#3")
	public EObject getValue()
	{
		if (!isSetValue())
		{
			return null;
		}

		GReferrable value = fRef.gGetValue();
		return PMUtils.isUsingMergedReferences() ? SplitUtils.getMergedInstance(value) : value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#3")
	public boolean isSetValue()
	{
		List<GReferrable> referencesValues = getReferencesValues(references);
		String parentInformation = ""; //$NON-NLS-1$
		if (references.size() > 0)
		{
			parentInformation = getParentInformation(references.get(0));
		}
		SplitPMUtils.handleMultipleValues(referencesValues,
			"Reference " + definitionName + parentInformation + " with upper multiplicity=1 has multiple values"); //$NON-NLS-1$ //$NON-NLS-2$

		return fRef != null && fRef.gGetValue() != null;
	}

	private List<GReferrable> getReferencesValues(EList<GReferenceValue> refs)
	{
		List<GReferrable> values = new ArrayList<GReferrable>();
		for (GReferenceValue gReferenceValue: refs)
		{
			GReferrable value = gReferenceValue.gGetValue();
			values.add(value);
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		if (newValue == null)
		{
			unsetValue();
		}
		else
		{
			if (!(newValue instanceof GIdentifiable))
			{
				throw new PMRuntimeException("Internal error, expected GIdentifiable, got " + newValue);
			}
			final GIdentifiable newTarget = (GIdentifiable) newValue;
			updateModel(new Runnable()
			{

				public void run()
				{
					if (fRef == null)
					{
						fRef = (GReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject()).createReferenceValue(
							definition);
					}
					fRef.gSetValue(newTarget);

					if (references instanceof DelegatingWithSourceMultiEList)
					{
						setMultiValues(fRef);
					}
					else
					{
						GContainer owner = owners.get(0);
						owner.gGetReferenceValues().add(fRef);
					}

				}
			});
			getEngine().updateSlaveFeature(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		if (fRef != null)
		{
			updateModel(new Runnable()
			{
				public void run()
				{
					SplitPMUtils.clearValues(references, definition);
					fRef = null;
				}
			});
		}
		getEngine().updateSlaveFeature(this);
	}

}
