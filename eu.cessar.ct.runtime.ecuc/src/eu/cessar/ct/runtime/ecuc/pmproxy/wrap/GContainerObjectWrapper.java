/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 2:00:26 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.sdk.pm.PMRuntimeException;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GecucdescriptionPackage;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;

/**
 * 
 */
public class GContainerObjectWrapper extends MultiObjectWrapper<GContainer>
{

	private static final String ATTR_SHORTNAME = "shortName"; //$NON-NLS-1$
	private GContainer cachedMaster;

	/**
	 * @param wrapped
	 */
	public GContainerObjectWrapper(IEMFProxyEngine engine, EList<GContainer> wrapped)
	{
		super(engine, GContainer.class, wrapped);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#getMasterEClass()
	 */
	@Override
	public EClass getMasterEClass()
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(getEngine().getProject());
		return factory.getConcreteEClass(GecucdescriptionPackage.Literals.GCONTAINER);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterContainer()
	 */
	public Object getMasterContainer()
	{
		List<GContainer> list = getMasterObject();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		else
		{
			EObject master = list.get(0).eContainer();
			if (master instanceof GContainer
				&& ((GContainer) master).gGetDefinition() instanceof GChoiceContainerDef)
			{
				master = master.eContainer();
			}
			return master;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#setContainer(eu.cessar.ct.emfproxy.IMasterObjectWrapper, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public void setContainer(IMasterObjectWrapper<?> parentWrapper, EStructuralFeature parentFeature)
	{
		if (getMasterObject().size() == 0)
		{
			// do nothing
			return;
		}
		if (getMasterObject().size() > 1)
		{
			throw new PMRuntimeException("Cannot alter a splited container"); //$NON-NLS-1$
		}
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(getEngine().getProject());
		final GContainer children = getMasterObject().get(0);

		@SuppressWarnings("unchecked")
		final EList<GContainer>[] removeFromList = new EList[1];
		@SuppressWarnings("unchecked")
		final EList<GContainer>[] addToList = new EList[1];

		final GContainerDef[] newTargetDef = new GContainerDef[1];
		Object parent = null;
		if (parentWrapper == null)
		{
			addToList[0] = null;
		}
		else
		{
			List<?> masterObjects = parentWrapper.getAllMasterObjects();
			if (masterObjects.size() > 1)
			{
				throw new PMRuntimeException("Cannot alter a splited container"); //$NON-NLS-1$
			}
			parent = masterObjects.get(0);
			addToList[0] = EcucMetaModelUtils.getSubContainers(parent);
			if (PMProxyUtils.haveRefinementSupport(engine))
			{
				GModuleDef targetModuleDef = null;
				if (parent instanceof GModuleConfiguration)
				{
					targetModuleDef = ((GModuleConfiguration) parent).gGetDefinition();
				}
				else
				{
					targetModuleDef = ecucModel.getModuleDef(((GContainer) parent).gGetDefinition());
				}
				if (targetModuleDef != ecucModel.getModuleDef(children.gGetDefinition()))
				{
					// the definition need to be changed
					newTargetDef[0] = ecucModel.getRefinedContainerDefFamily(targetModuleDef,
						children.gGetDefinition());
				}
			}
		}
		if (children.eContainer() != null)
		{
			removeFromList[0] = EcucMetaModelUtils.getSubContainers(children.eContainer());
		}
		if (addToList[0] == removeFromList[0])
		{
			// nothing to do, both null or represent the same list
			return;
		}
		updateModel(new Runnable()
		{

			public void run()
			{
				if (removeFromList[0] != null)
				{
					// remove children from current parent
					removeFromList[0].remove(children);
				}
				if (addToList[0] != null)
				{
					addToList[0].add(children);
					if (newTargetDef[0] != null)
					{
						children.gSetDefinition(newTargetDef[0]);
						// BUG: we should recurse the children of children to
						// set the definition
					}
				}
			}

		});
		// we have to re-run the query
		// pass null as context because it just might be a new context
		if (parentWrapper != null && parentFeature != null)
		{
			IMasterFeatureWrapper<?> featureWrapper = parentWrapper.getProxyObject().eGetCachedFeatureWrapper(
				parentFeature);
			if (featureWrapper != null)
			{
				getEngine().updateSlaveFeature(featureWrapper);
			}
		}
		getEngine().updateSlave(null, getProxyObject(), children);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#preNotifySingleAttrSet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	@Override
	public void preNotifySingleAttrSet(EAttribute attr, Object newValue)
	{
		if (ATTR_SHORTNAME.equals(attr.getName())
			&& (newValue == null || newValue == EStructuralFeatureImpl.NIL))
		{
			throw new PMRuntimeException("Can not set a null shortName"); //$NON-NLS-1$
		}
		super.preNotifySingleAttrSet(attr, newValue);
		saveCurrentMaster(attr);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#preNotifySingleAttrUnset(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public void preNotifySingleAttrUnset(EAttribute attr)
	{
		if (ATTR_SHORTNAME.equals(attr.getName())
			&& (attr.getDefaultValue() == null || attr.getDefaultValue() == EStructuralFeatureImpl.NIL))
		{
			throw new PMRuntimeException("Can not set a null shortName"); //$NON-NLS-1$
		}
		super.preNotifySingleAttrUnset(attr);
		saveCurrentMaster(attr);
	}

	/**
	 * 
	 */
	private void saveCurrentMaster(EAttribute attr)
	{
		if (ATTR_SHORTNAME.equals(attr.getName()))
		{
			cachedMaster = null;
			if (getMasterObject().size() > 0)
			{
				cachedMaster = getMasterObject().get(0);
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#notifySingleAttrSet(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
	 */
	@Override
	public void postNotifySingleAttrSet(EAttribute attr, Object newValue)
	{
		super.postNotifySingleAttrSet(attr, newValue);
		if (ATTR_SHORTNAME.equals(attr.getName()) && cachedMaster != null)
		{
			updateWrappers();
		}

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiObjectWrapper#postNotifySingleAttrUnset(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public void postNotifySingleAttrUnset(EAttribute attr)
	{
		super.postNotifySingleAttrUnset(attr);
		if (ATTR_SHORTNAME.equals(attr.getName()) && cachedMaster != null)
		{
			updateWrappers();
		}
	}

	/**
	 * 
	 */
	private void updateWrappers()
	{
		EMFProxyObjectImpl pmObject = getProxyObject();
		if (pmObject.eContainer() != null && pmObject.eContainingFeature() != null)
		{
			EMFProxyObjectImpl pmParent = (EMFProxyObjectImpl) pmObject.eContainer();
			IMasterFeatureWrapper<?> wrapper = pmParent.eGetCachedFeatureWrapper(pmObject.eContainingFeature());
			getEngine().updateSlaveFeature(wrapper);
		}
		getEngine().updateSlave(getContext(), getProxyObject(), cachedMaster);
	}
}
