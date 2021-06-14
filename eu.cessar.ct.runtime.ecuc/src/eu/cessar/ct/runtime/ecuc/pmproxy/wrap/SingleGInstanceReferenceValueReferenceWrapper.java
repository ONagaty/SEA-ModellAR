/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 5:51:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.SplitPMUtils;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Wrapper for a single instance reference value.
 */
public class SingleGInstanceReferenceValueReferenceWrapper extends AbstractSingleMasterReferenceWrapper
{

	private final ESplitableList<GInstanceReferenceValue> iRefs;
	private final GInstanceReferenceDef definition;

	private String definitionName;
	private IMetaModelService mmService;

	/**
	 * 
	 * @param engine
	 * @param iRefs
	 * @param owners
	 * @param definition
	 */
	public SingleGInstanceReferenceValueReferenceWrapper(IEMFProxyEngine engine,
		ESplitableList<GInstanceReferenceValue> iRefs, List<GContainer> owners, GInstanceReferenceDef definition)
	{
		super(engine);
		this.iRefs = iRefs;
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

		GInstanceReferenceValue masterObj = iRefs.get(0);

		boolean isUsingMergedReferences = PMUtils.isUsingMergedReferences();
		// change the master object to the corresponding merged instance
		if (isUsingMergedReferences)
		{
			masterObj = SplitUtils.getMergedInstance(masterObj);
		}

		IEMFProxyObject slaveObject = getEngine().getSlaveObject(getContext(), masterObj);

		return slaveObject;
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
		List<GIdentifiable> targets = new ArrayList<GIdentifiable>();
		List<EList<GIdentifiable>> contextsList = new ArrayList<EList<GIdentifiable>>();
		for (GInstanceReferenceValue ref: iRefs)
		{
			if (ref != null)
			{
				mmService = MMSRegistry.INSTANCE.getMMService(ref.eClass());
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				GIdentifiable instanceRefTarget = ecucMMService.getInstanceRefTarget(ref);
				targets.add(instanceRefTarget);
				EList<GIdentifiable> instanceRefContext = ecucMMService.getInstanceRefContext(ref);
				contextsList.add(instanceRefContext);

			}
		}
		String parentInformation = ""; //$NON-NLS-1$
		boolean isSetValue = iRefs.size() > 0;
		if (isSetValue)
		{
			parentInformation = getParentInformation(iRefs.get(0));
		}
		SplitPMUtils.handleMultipleInstanceReferencesValues(targets, contextsList,
			"Reference " + definitionName + parentInformation + " with upper multiplicity=1 has multiple values"); //$NON-NLS-1$ //$NON-NLS-2$
		return isSetValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public void setValue(Object newValue)
	{
		if (newValue == null || newValue == EStructuralFeature.Internal.DynamicValueHolder.NIL)
		{
			unsetValue();
		}
		else
		{
			if (!(newValue instanceof EMFProxyObjectImpl))
			{
				throw new PMRuntimeException("Internal error, expected EMFProxyObject, got " + newValue); //$NON-NLS-1$
			}
			EMFProxyObjectImpl childrenProxy = (EMFProxyObjectImpl) newValue;
			IMasterObjectWrapper<?> wrapper = childrenProxy.eGetMasterWrapper();
			List<?> childrenMasters = wrapper.getAllMasterObjects();

			final GInstanceReferenceValue iRefValue = (GInstanceReferenceValue) childrenMasters.get(0);

			final GInstanceReferenceDef[] newTargetDef = new GInstanceReferenceDef[1];
			if (PMProxyUtils.haveRefinementSupport(engine))
			{
				IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
				GModuleDef targetModuleDef = ecucModel.getModuleDef(definition);
				if (targetModuleDef != ecucModel.getModuleDef(iRefValue.gGetDefinition()))
				{
					// the definition need to be changed
					newTargetDef[0] = (GInstanceReferenceDef) ecucModel.getRefinedReferenceDefFamily(targetModuleDef,
						iRefValue.gGetDefinition());
				}
			}
			updateModel(new Runnable()
			{

				public void run()
				{
					if (newTargetDef[0] != null)
					{
						iRefValue.gSetDefinition(newTargetDef[0]);
					}

					if (iRefs instanceof DelegatingWithSourceMultiEList)
					{
						setMultiValues(iRefValue);
					}
					else
					{
						iRefs.clear();
						iRefs.add(iRefValue);
					}
				}
			});
			// lastAddedOrSet = iRefValue;
			// update both objects
			getEngine().updateSlaveFeature(this);
			getEngine().updateSlave(getContext(), childrenProxy, iRefValue);
		}
	}

	/**
	 * Handle set multiple values for single references (Ecuc/Foreign).
	 * 
	 * @param newValue
	 *        the value to be set
	 */
	@Override
	protected void setMultiValues(final Object newValue)
	{
		DelegatingWithSourceMultiEList refs = (DelegatingWithSourceMultiEList) iRefs;
		EList defaultTargetList = SplitPMUtils.findDefaultTargetListForSet(refs, definition);
		unsetValue();
		defaultTargetList.add(newValue);
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
		updateModel(new Runnable()
		{
			public void run()
			{
				SplitPMUtils.clearValues(iRefs, definition);
			}
		});
		getEngine().updateSlaveFeature(this);
	}

}
