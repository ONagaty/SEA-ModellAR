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
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.GContainerDefClassResolver;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Wrapper for a single simple reference value.
 */
public class SingleGReferenceValueReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private GReferenceValue fRef;
	private final GConfigReference definition;
	private String definitionName;

	/**
	 * @param engine
	 * @param references
	 * @param fRef
	 * @param owner
	 * @param definition
	 */
	public SingleGReferenceValueReferenceWrapper(IEMFProxyEngine engine, EList<GReferenceValue> references,
		GReferenceValue fRef, List<GContainer> owner, GConfigReference definition)
	{
		super(engine);
		this.references = references;
		this.fRef = fRef;
		owners = owner;
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

		if (fRef.gGetValue() != null)
		{
			GReferrable value = fRef.gGetValue();
			Map<String, Object> parameters = null;
			Object context = null;
			if (!(value instanceof GContainer) || value.eIsProxy())
			{
				parameters = new HashMap<String, Object>();
				parameters.put(GContainerDefClassResolver.PARAM_MASTER_CLASS, GContainer.class);
				parameters.put(GContainerDefClassResolver.PARAM_PM_FEATURE, getWrappedFeature());
				parameters.put(GContainerDefClassResolver.PARAM_PM_OWNER, getProxyObject());
			}
			else
			{
				// the context is the module def where the container is defined
				GContainer cnt = (GContainer) value;

				IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
				GContainerDef def = ecucModel.getRefinedContainerDef(definition, cnt);
				if (def != null)
				{
					context = ecucModel.getModuleDef(def);
				}
			}
			return getEngine().getSlaveObject(context, value, parameters);

		}
		else
		{
			return null;
		}
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
		SplitPMUtils.handleMultipleValues(referencesValues, "Reference " + definitionName //$NON-NLS-1$
			+ parentInformation + " with upper multiplicity=1 has multiple values"); //$NON-NLS-1$

		return fRef != null && fRef.gGetValue() != null;
	}

	private List<GReferrable> getReferencesValues(EList<GReferenceValue> refs)
	{
		List<GReferrable> values = new ArrayList<GReferrable>();
		for (GReferenceValue ref: refs)
		{
			values.add(ref.gGetValue());
		}
		return values;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public void setValue(final Object newValue)
	{
		if (newValue == null)
		{
			unsetValue();
		}
		else
		{
			if (!(newValue instanceof EMFProxyObjectImpl))
			{
				throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
			}
			List<?> masterObjects = ((EMFProxyObjectImpl) newValue).eGetMasterWrapper().getAllMasterObjects();
			final GContainer container = (GContainer) masterObjects.get(0);
			updateModel(new Runnable()
			{

				public void run()
				{
					if (fRef == null)
					{
						fRef = (GReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject()).createReferenceValue(
							definition);
					}
					fRef.gSetValue(container);

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
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public void unsetValue()
	{
		if (fRef != null)
		{
			final GContainer cnt = (GContainer) fRef.eContainer();
			if (cnt == null)
			{
				// nothing to do, should never happen
				fRef = null;
				return;
			}
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
