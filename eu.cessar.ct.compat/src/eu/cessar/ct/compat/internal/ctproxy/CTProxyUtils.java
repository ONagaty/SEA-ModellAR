/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.common.resource.AutosarURIFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.EMFProxyRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * Proxification utilities.
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class CTProxyUtils
{
	private static long calls;

	private static long splitted;

	private static long converted;

	/**
	 * @param eModelElement
	 * @param defaultValue
	 * @return
	 */
	public static String getARFeatureAnnotation(EModelElement eModelElement, String defaultValue)
	{
		EAnnotation eAnnotation = eModelElement.getEAnnotation(ICompatConstants.ANN_EMF_PROXY);
		if (eAnnotation != null)
		{
			EMap<String, String> details = eAnnotation.getDetails();
			if (details != null)
			{
				String eAnnotationValue = details.get(ICompatConstants.ANN_KEY_AR_FEATURE);
				if (eAnnotationValue != null)
				{
					return eAnnotationValue;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * @param eModelElement
	 * @param defaultValue
	 * @return
	 */
	public static String getARFullClassifierAnnotation(EModelElement eModelElement, String defaultValue)
	{
		EAnnotation eAnnotation = eModelElement.getEAnnotation(ICompatConstants.ANN_EMF_PROXY);
		if (eAnnotation != null)
		{
			EMap<String, String> details = eAnnotation.getDetails();
			if (details != null)
			{
				String eAnnotationValue = details.get(ICompatConstants.ANN_KEY_AR_FULL_CLASSIFIER);
				if (eAnnotationValue != null)
				{
					return eAnnotationValue;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * @param eModelElement
	 * @param defaultValue
	 * @return
	 */
	public static String getWrappedUsageAnnotation(EModelElement eModelElement, String defaultValue)
	{
		EAnnotation eAnnotation = eModelElement.getEAnnotation(ICompatConstants.ANN_EMF_PROXY);
		if (eAnnotation != null)
		{
			EMap<String, String> details = eAnnotation.getDetails();
			if (details != null)
			{
				String eAnnotationValue = details.get(ICompatConstants.ANN_KEY_WRAPPED_USAGE);
				if (eAnnotationValue != null)
				{
					return eAnnotationValue;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * @param eModelElement
	 * @param defaultValue
	 * @return
	 */
	public static String getUsageAnnotation(EModelElement eModelElement, String defaultValue)
	{
		EAnnotation eAnnotation = eModelElement.getEAnnotation(ICompatConstants.ANN_EMF_PROXY);
		if (eAnnotation != null)
		{
			EMap<String, String> details = eAnnotation.getDetails();
			if (details != null)
			{
				String eAnnotationValue = details.get(ICompatConstants.ANN_KEY_USAGE);
				if (eAnnotationValue != null)
				{
					return eAnnotationValue;
				}
			}
		}
		return defaultValue;
	}

	/**
	 * @param qName
	 * @param typeSet
	 *        TODO
	 * @param expectedEClass
	 * @return
	 */
	public static EObject getRealOrProxyObject(IProject project, String qName, String typeSet, EClass expectedEClass)
	{
		boolean isProxy = false;
		EObject dest = null;

		List<EObject> objectsWithQName = EObjectLookupUtils.getEObjectsWithQName(project, qName);
		if (objectsWithQName.size() > 0)
		{
			dest = objectsWithQName.get(0);
			if (expectedEClass != null && !expectedEClass.isInstance(dest))
			{
				// the new referred object not of the expected type
				isProxy = true;
			}
		}
		else
		{
			if (expectedEClass != null)
			{
				isProxy = true;

			}
		}

		if (isProxy)
		{
			String destName = ""; //$NON-NLS-1$
			if (typeSet != null && !typeSet.equals("")) //$NON-NLS-1$
			{
				destName = typeSet;
				// BasicExtendedMetaData.INSTANCE.getNamespace(eClassifier);
				AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
				EClassifier type = ExtendedMetaData.INSTANCE.getType(autosarRelease.getNamespace(), destName);
				if (type instanceof EClass)
				{
					dest = EcoreUtil.create((EClass) type);
				}
			}
			else
			{
				dest = EcoreUtil.create(expectedEClass);
				destName = ExtendedMetaData.INSTANCE.getName(dest.eClass());
			}
			URI uri = AutosarURIFactory.createAutosarURI(qName, destName);
			((BasicEObjectImpl) dest).eSetProxyURI(uri);
		}
		return dest;
	}

	/**
	 * 
	 * @param masterObject
	 * @param project
	 * @param slaveURI
	 * @param context
	 * @return
	 */
	public static IEMFProxyObject getSlaveObjectForMaster(EObject masterObject, IProject project, String slaveURI,
		Object context)
	{
		// get the emf proxy engine
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		String masterURI = autosarRelease.getNamespace();
		IEMFProxyEngine emfProxyEngine = EMFProxyRegistry.eINSTANCE.getEMFProxyEngine(masterURI, slaveURI, project);
		if (emfProxyEngine != null)
		{
			// get the slave root
			return emfProxyEngine.getSlaveObject(context, masterObject);

		}
		return null;
	}

	/**
	 * @param slaveObject
	 * @return
	 */
	public static EObject getMasterObjectForSlave(EObject slaveObject)
	{
		if (slaveObject instanceof EMFProxyObjectImpl)
		{
			IEMFProxyEngine proxyEngine = ((EMFProxyObjectImpl) slaveObject).eGetProxyEngine();
			if (proxyEngine != null)
			{
				List<Object> masterObjects = proxyEngine.getMasterObjects(((EMFProxyObjectImpl) slaveObject));
				if (masterObjects.size() > 0)
				{
					return (EObject) masterObjects.get(0);

				}
			}
		}
		return null;
	}

	/**
	 * If the object argument is a splited instance of a single reald object and the execution engine is in read/only
	 * mode return the single instance. Otherwise return the argument itself
	 * 
	 * @param object
	 * @return the unwrapped instance
	 */
	public static EObject unwrapSplitedObject(EObject object)
	{
		// calls++;
		// EObject result = object;
		// SplitableUtils splitUtils = SplitableUtils.INSTANCE;
		// if (splitUtils.isReadOnly() && splitUtils.isSplitable(object))
		// {
		// splitted++;
		// if (splitUtils.getAllFragments((Splitable) object).size() == 1)
		// {
		// converted++;
		// result = splitUtils.getActiveFragment((Splitable) object);
		// }
		// }
		// if (calls % 10000 == 0)
		// {
		// System.err.println("Calls     :" + calls);
		// System.err.println("Splitted  :" + splitted);
		// System.err.println("Converted :" + converted);
		// }
		return object;
	}
}
