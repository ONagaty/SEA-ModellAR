/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 3, 2010 12:27:19 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.SafeUsageCrossReferencer;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.ct.sdk.pm.IPMInstanceRef;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.pm.IPMPackage;
import eu.cessar.ct.sdk.pm.IPresentationModel;
import eu.cessar.ct.sdk.utils.EcucUtils;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * The implementation of {@link PMUtils} utility class from SDK
 */
public final class PMUtilsService implements PMUtils.Service
{
	/** the singleton */
	public static final PMUtilsService eINSTANCE = new PMUtilsService();

	private static final ThreadLocal<Boolean> USAGE_OF_MERGED_REFERENCES_THREAD_LOCAL = new ThreadLocal<Boolean>()
	{
		@Override
		protected Boolean initialValue()
		{
			// default value
			return false;
		}
	};

	/**
	 * Thread-local controlling whether runtime exceptions should be thrown for invalid split parameter and reference
	 * values.
	 */
	private static final ThreadLocal<Boolean> THROW_EXCEPTIONS_ON_INVALID_SPLIT = new ThreadLocal<Boolean>()
	{
		@Override
		protected Boolean initialValue()
		{
			// default value
			return true;
		}

	};

	private PMUtilsService()
	{
		// avoid instance
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getPMRoot(org.eclipse.core.resources.IProject)
	 */
	public IPresentationModel getPMRoot(IProject project)
	{
		IEcucPresentationModel model = IEcucCore.INSTANCE.getEcucPresentationModel(project);
		if (model == null)
		{
			return null;
		}
		return (IPresentationModel) model.getPMModelRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getPMObject(org.eclipse.emf.ecore.EObject)
	 */
	@Requirement(
		reqID = "REQ_API_PMUTILS_MERGED#2")
	public IPMElement getPMObject(EObject autosarObject)
	{
		GARObject autosarObjectNotMerged = SplitableUtils.INSTANCE.unWrapMergedObject((GARObject) autosarObject);
		IEMFProxyEngine proxyEngine = IEcucCore.INSTANCE.getEcucPresentationModel(autosarObjectNotMerged).getProxyEngine();
		return (IPMElement) proxyEngine.getSlaveObject(null, autosarObjectNotMerged);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getContainers(eu.cessar.ct.sdk.pm.IPMContainer)
	 */
	public List<GContainer> getContainers(IPMContainer pmContainer)
	{
		if (pmContainer instanceof EMFProxyObjectImpl)
		{
			EMFProxyObjectImpl impl = (EMFProxyObjectImpl) pmContainer;
			IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
			return proxyEngine.getMasterObjects(impl);
		}
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getContainers(eu.cessar.ct.sdk.pm.IPMContainer, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_PMUTILS_MERGED#1")
	@Override
	public List<GContainer> getContainers(IPMContainer pmContainer, boolean merged)
	{
		if (merged)
		{
			List<GContainer> containers = getContainers(pmContainer);
			return getMergedList(containers);
		}
		return getContainers(pmContainer);
	}

	/**
	 * @param elements
	 * @return merged list of elements
	 */
	private static <T extends GARObject> List<T> getMergedList(List<T> elements)
	{
		List<T> mergedElements = new ArrayList<>();
		for (T container: elements)
		{
			T mElement = SplitUtils.getMergedInstance(container);
			if (!mergedElements.contains(mElement))
			{
				mergedElements.add(mElement);
			}
		}

		return mergedElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getModuleConfigurations(eu.cessar.ct.sdk.pm.IPMModuleConfiguration)
	 */
	public List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration)
	{
		EMFProxyObjectImpl impl = (EMFProxyObjectImpl) pmModuleConfiguration;
		IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
		return proxyEngine.getMasterObjects(impl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getModuleConfigurations(eu.cessar.ct.sdk.pm.IPMModuleConfiguration,
	 * boolean)
	 */
	@Requirement(
		reqID = "REQ_API_PMUTILS_MERGED#1")
	@Override
	public List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration,
		boolean merged)
	{
		if (merged)
		{
			List<GModuleConfiguration> configurations = getModuleConfigurations(pmModuleConfiguration);
			return getMergedList(configurations);
		}
		return getModuleConfigurations(pmModuleConfiguration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getReferencedBy(eu.cessar.ct.sdk.pm.IPMContainer)
	 */
	public Map<IPMContainer, List<String>> getReferencedBy(IPMContainer pmContainer)
	{
		List<GContainer> containers = getContainers(pmContainer);

		if (containers.size() == 0)
		{
			// this should never happen
			throw new RuntimeException("Cannot locate references to containers not stored in resources"); //$NON-NLS-1$
		}
		IProject project = MetaModelUtils.getProject(containers.get(0));
		if (project == null)
		{
			// this should never happen
			throw new RuntimeException("Cannot locate references to containers not stored in resources"); //$NON-NLS-1$
		}
		List<GModuleConfiguration> target = IEcucCore.INSTANCE.getEcucModel(project).getAllModuleCfgs();
		Map<GContainer, Set<String>> arResult = new HashMap<>();
		for (GContainer cnt: containers)
		{
			Collection<Setting> found = SafeUsageCrossReferencer.find(cnt, target);
			for (Setting s: found)
			{
				EObject reference = s.getEObject();
				// the reference must be a GReferenceValue
				if (reference instanceof GReferenceValue)
				{
					GContainer foundCnt = (GContainer) reference.eContainer();
					String refName = ((GReferenceValue) reference).gGetDefinition().gGetShortName();
					Set<String> refList = null;
					if (arResult.containsKey(foundCnt))
					{
						refList = arResult.get(foundCnt);
					}
					else
					{
						refList = new HashSet<>();
						arResult.put(foundCnt, refList);
					}
					refList.add(refName);
				}
			}
		}
		// convert from Autosar objects to PMObjects
		Map<IPMContainer, List<String>> result = new HashMap<>();
		for (GContainer foundCnt: arResult.keySet())
		{
			IPMContainer pmCnt = (IPMContainer) getPMObject(foundCnt);
			List<String> refList = null;
			if (result.containsKey(pmCnt))
			{
				refList = result.get(pmCnt);
			}
			else
			{
				refList = new ArrayList<>();
				result.put(pmCnt, refList);
			}
			for (String ref: arResult.get(foundCnt))
			{
				if (!refList.contains(ref))
				{
					refList.add(ref);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getInstanceoRef(eu.cessar.ct.sdk.pm.IPMInstanceRef)
	 */
	public List<GInstanceReferenceValue> getInstanceRef(IPMInstanceRef value)
	{
		EMFProxyObjectImpl impl = (EMFProxyObjectImpl) value;
		IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
		return proxyEngine.getMasterObjects(impl);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getInstanceRefObject(eu.cessar.ct.sdk.pm.IPMInstanceRef)
	 */
	public GInstanceReferenceValue getInstanceRefObject(IPMInstanceRef value)
	{
		EMFProxyObjectImpl impl = (EMFProxyObjectImpl) value;
		IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
		// There is always one value maximum
		List<GInstanceReferenceValue> refValues = proxyEngine.getMasterObjects(impl);
		if (refValues.isEmpty())
		{
			return null;
		}
		return refValues.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#getPackages(eu.cessar.ct.sdk.pm.IPMPackage)
	 */
	public List<GARPackage> getPackages(IPMPackage pmPackage)
	{
		EMFProxyObjectImpl impl = (EMFProxyObjectImpl) pmPackage;
		IEMFProxyEngine proxyEngine = impl.eGetProxyEngine();
		return proxyEngine.getMasterObjects(impl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#setUsingMergedReferences(boolean)
	 */
	@Override
	public void setUsingMergedReferences(boolean mergedReferences)
	{
		USAGE_OF_MERGED_REFERENCES_THREAD_LOCAL.set(mergedReferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#isUsingMergedReferences()
	 */
	@Override
	public boolean isUsingMergedReferences()
	{
		return USAGE_OF_MERGED_REFERENCES_THREAD_LOCAL.get();
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#isCreatedAtPostBuild(eu.cessar.ct.sdk.pm.IPMContainer)
 */
	@Requirement(
		reqID = "REQ_API_PMUTILS_POST_BUILD#1")
	@Override
	public boolean isCreatedAtPostBuild(IPMContainer pmContainer)
	{
		List<GContainer> containers = getContainers(pmContainer);
		// MOVE TO EcucUtilsService instead and call that method from here
		for (GContainer container: containers)
		{
			boolean createdAtPostBuild = EcucUtils.isCreatedAtPostBuild(container);
			if (!createdAtPostBuild)
			{
				// return false as soon as we find a DEVELOPMENT container
				return false;
			}
		}
		// all of the GContainers are in PRODUCTION, return true
		return true;
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#setContainerCreationPhase(eu.cessar.ct.sdk.pm.IPMContainer,
 * eu.cessar.ct.core.platform.EProjectVariant)
 */
	@Override
	public void setContainerCreationPhase(IPMContainer pmContainer, EProjectVariant phase)
	{
		List<GContainer> containers = getContainers(pmContainer);
		for (GContainer container: containers)
		{
			EcucMetaModelUtils.setContainerCreationPhase(container, phase);
		}
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#setContainerCreationPhase(eu.cessar.ct.sdk.pm.IPMContainer,
 * java.lang.String)
 */
	@Override
	public void setContainerCreationPhase(IPMContainer pmContainer, String phase)
	{
		if (phase != null)
		{
			setContainerCreationPhase(pmContainer, EProjectVariant.getProjectVariant(phase));
		}
		else
		{
			setContainerCreationPhase(pmContainer, (EProjectVariant) null);
		}
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#setSplitChecking(boolean)
 */
	@Override
	public void setSplitChecking(boolean splitChecking)
	{
		THROW_EXCEPTIONS_ON_INVALID_SPLIT.set(splitChecking);
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.sdk.utils.PMUtils.Service#isSplitChecking()
 */
	@Override
	public boolean isSplitChecking()
	{
		return THROW_EXCEPTIONS_ON_INVALID_SPLIT.get();
	}
}
