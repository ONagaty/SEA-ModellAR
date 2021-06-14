/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 3, 2009 10:48:18 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MissingContainerObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import eu.cessar.ct.sdk.pm.pmPackage;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.List;
import java.util.Map;

import org.artop.aal.common.resource.AutosarURIFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * 
 */
public class GContainerDefClassResolver extends AbstractECUCClassResolver<GIdentifiable>
{

	public static final String PARAM_MASTER_CLASS = "MASTER_CLASS"; //$NON-NLS-1$

	public static final String PARAM_PM_FEATURE = "PM_FEATURE"; //$NON-NLS-1$

	public static final String PARAM_PM_OWNER = "PM_OWNER"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<GContainer> getMasterClassType(IEMFProxyEngine engine)
	{
		return GContainer.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractClassResolver#isValidMasterObject(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	@Override
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object object,
		Map<String, Object> parameters)
	{
		if (engine == null || engine.getProject() == null)
		{
			return false;
		}
		if (object == null)
		{
			// check the parameters
			if (parameters != null
				&& parameters.get(PARAM_MASTER_CLASS) == getMasterClassType(engine))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		IProject objProject = MetaModelUtils.getProject(object);
		if (objProject == null || objProject == engine.getProject())
		{
			if (getMasterClassType(engine).isInstance(object))
			{
				return true;
			}
			else
			{
				if (parameters != null
					&& parameters.get(PARAM_MASTER_CLASS) == getMasterClassType(engine))
				{
					return true;
				}
				else
				{
					return false;
				}

			}
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractClassResolver#resolveMaster(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object)
	 */
	@Override
	public GIdentifiable resolveMaster(IEMFProxyEngine engine, Object context, GIdentifiable master)
	{
		if (shouldProxify(master))
		{
			GContainer resolved = resolve(engine, master);
			if (resolved != null)
			{
				return resolved;
			}
		}
		return master;
	}

	/**
	 * Return true if the <code>master</code> is not a GContainer or is a not
	 * resolved GContainer
	 * 
	 * @param master
	 * @return
	 */
	protected boolean shouldProxify(EObject master)
	{
		if (!(master instanceof GContainer))
		{
			return true;
		}
		else
		{
			GContainer container = (GContainer) master;
			return container.eIsProxy() || container.gGetDefinition() == null
				|| container.gGetDefinition().eIsProxy();
		}
	}

	/**
	 * @return
	 */
	protected EClass getEClassForProxyContainer(IEMFProxyEngine engine)
	{
		return pmPackage.Literals.MISSING_CONTAINER;
	}

	/**
	 * @return
	 */
	protected EClass getEClassForProxyContainer(IEMFProxyEngine engine, GIdentifiable master,
		Map<String, Object> parameters)
	{
		return getEClassForProxyContainer(engine);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, GIdentifiable master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel model = getEcucPresentationModel(engine);
		if (shouldProxify(master))
		{
			// let's see maybe is related with the problem of the abstract dest
			GContainer resolved = resolve(engine, master);
			if (resolved != null)
			{
				master = resolved;
			}
			else
			{
				return getEClassForProxyContainer(engine, master, parameters);
			}
		}
		if (PMProxyUtils.haveRefinementSupport(engine) && context instanceof GModuleDef)
		{
			GContainerDef realDef = getEcucModel(engine).getRefinedContainerDefFamily(
				(GModuleDef) context, ((GContainer) master).gGetDefinition());
			return (EClass) model.getPMClassifier(realDef);
		}
		else
		{
			return (EClass) model.getPMClassifier(((GContainer) master).gGetDefinition());
		}
	}

	/**
	 * @param engine
	 * @param master
	 * @return
	 */
	private GContainer resolve(IEMFProxyEngine engine, GIdentifiable master)
	{
		ResourceSet resourceSet = MetaModelUtils.getEditingDomain(engine.getProject()).getResourceSet();
		URI proxyURI = ((InternalEObject) master).eProxyURI();
		if (proxyURI != null)
		{
			String fragment = proxyURI.fragment();
			if (fragment != null && fragment.indexOf("?") > 0)
			{
				fragment = fragment.substring(0, fragment.indexOf("?"));
				proxyURI = AutosarURIFactory.createAutosarURI(fragment, "Identifiable");
			}
			EObject resolve = resourceSet.getEObject(proxyURI, true);
			if (resolve instanceof GContainer)
			{
				return (GContainer) resolve;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<? extends GIdentifiable> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, GIdentifiable master, Map<String, Object> parameters)
	{
		if (shouldProxify(master))
		{
			// let's see maybe is related with the problem of the abstract dest
			GContainer resolved = resolve(engine, master);
			if (resolved != null)
			{
				master = resolved;
			}
			else
			{
				// Check to see if the EClass of the slave is a missing
				// container or not
				// if (slave.eClass() == getEClassForProxyContainer())
				{
					return new MissingContainerObjectWrapper(engine, master);
				}
				// else
				// {
				// // a real PM class has been created instead
				// }
			}
		}

		EList<GContainer> containers = getEcucModel(engine).getSplitedSiblingContainers(
			(GContainer) master);
		return new GContainerObjectWrapper(engine, containers);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<GContainer> createWrapper(IEMFProxyEngine engine, Object context,
		EMFProxyObjectImpl emfProxyObjectImpl)
	{
		String uri = engine.getProxyElementAnnotation(emfProxyObjectImpl.eClass(), ATTR_URI);
		List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(getEcucModel(engine).getResourcesWithModuleDefs(), uri);
		InternalProxyConfigurationError.assertTrue(objects.size() == 1);
		// should not be null
		InternalProxyConfigurationError.assertTrue(objects.get(0) instanceof GContainerDef);
		// proxyObject is mapped to GModuleConfiguration or GContainer
		GContainerDef def = (GContainerDef) objects.get(0);

		GContainer container = MMSRegistry.INSTANCE.getGenericFactory(engine.getProject()).createContainer();
		container.gSetDefinition(def);
		EList<GContainer> holder = new BasicEList.UnmodifiableEList<GContainer>(1,
			new GContainer[] {container});
		return new GContainerObjectWrapper(engine, holder);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractClassResolver#postCreateSlave(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	@Override
	public void postCreateSlave(IEMFProxyEngine engine, Object context, GIdentifiable master,
		IEMFProxyObject slave)
	{
		super.postCreateSlave(engine, context, master, slave);
		if (getEClassForProxyContainer(engine).isInstance(slave))
		{
			slave.eSetProxyURI(AutosarURIFactory.getURI(master));
		}
	}
}
