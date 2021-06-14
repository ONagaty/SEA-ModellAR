/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 11, 2009 1:15:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pm;

import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;

/**
 * 
 */
public class EMFPMUtils
{

	private EMFPMUtils()
	{
		// avoid instance
	}

	/**
	 * @param elements
	 * @param proposal
	 * @return
	 */
	public static String genTCaseName(List<? extends ENamedElement> elements, String proposal)
	{
		return StringUtils.toTitleCase(proposal);
	}

	/**
	 * @param elements
	 * @param proposal
	 * @return
	 */
	public static String genLCaseName(List<? extends ENamedElement> elements, String proposal)
	{
		return proposal.toLowerCase();
	}

	/**
	 * @param elements
	 * @param proposal
	 * @return
	 */
	public static String genName(List<? extends ENamedElement> elements, String proposal)
	{
		return proposal;
	}

	/**
	 * @param pack
	 * @return
	 */
	public static List<ENamedElement> getPackageNamedElements(EPackage pack)
	{
		return CollectionUtils.joinLists(pack.getEClassifiers(), pack.getESubpackages());
	}

	/**
	 * @param element
	 * @param key
	 * @param value
	 */
	public static void setProxyAnnotation(ENamedElement element, String key, String value)
	{
		EAnnotation eAnn = element.getEAnnotation(IEMFProxyConstants.PROXY_ANN_NAME);
		if (eAnn == null)
		{
			eAnn = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnn.setSource(IEMFProxyConstants.PROXY_ANN_NAME);
			element.getEAnnotations().add(eAnn);
		}
		eAnn.getDetails().put(key, value);
	}

}
