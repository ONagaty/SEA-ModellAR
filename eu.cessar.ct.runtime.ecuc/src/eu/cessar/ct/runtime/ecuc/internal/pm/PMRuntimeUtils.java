/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 2:55:19 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IProxifiedEPackage;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.sdk.pm.PMRuntimeException;

/**
 * This class consist only of static methods executed by the generated PM binary
 * code.
 */
public final class PMRuntimeUtils
{
	/*
	 * Note for developers.
	 * Please be very carefully while changing this class.
	 */

	private static Pattern pmUriPattern = Pattern.compile("^http://cessar\\.eu/PM(_compat)?/([^/]+)/([^/]+)([/]?.*)$"); //$NON-NLS-1$

	/**
	 * 
	 */
	private PMRuntimeUtils()
	{
		// do nothing
	}

	/**
	 * Return the presentation model package for a corresponding pmURI.<br/>
	 * 
	 * @param pmURI
	 *        the presentation model uri in the format:
	 *        http://cessar.eu/[PM|PM_compat
	 *        ]/[PROJECT_NAME]/[MM_Version]/[PACKAGE_QNAME]
	 * @return
	 */
	public static EPackage getPMPackage(String pmURI)
	{
		Assert.isNotNull(pmURI, "PM uri should not be null"); //$NON-NLS-1$
		Matcher matcher = pmUriPattern.matcher(URI.decode(pmURI));
		Assert.isLegal(matcher.matches(), "PM uri does not match the pattern :" + pmURI); //$NON-NLS-1$
		// at this point the pmURI matches the pattern
		String projectName = matcher.group(2);
		IEcucPresentationModel model = IEcucCore.INSTANCE.getEcucPresentationModel(projectName);
		Assert.isNotNull(model, "Cannot locate project : " + projectName); //$NON-NLS-1$
		EPackage result = model.getEPackage(pmURI);
		return result;
	}

	/**
	 * This method is called from each PM factory init method and it have the
	 * role to update all instance classes of the enums classifiers and
	 * attributes
	 * 
	 * @param factory
	 */
	@SuppressWarnings("unused")
	public static void updatePMFactory(EFactory factory, String nsURI)
	{
		EPackage pack = getPMPackage(nsURI);
		if (pack instanceof IProxifiedEPackage)
		{
			ClassLoader loader = ((IProxifiedEPackage) pack).getRuntimeClassLoader();
			if (loader != null)
			{

				EList<EClassifier> clsz = pack.getEClassifiers();
				for (EClassifier clz: clsz)
				{
					if (clz instanceof EEnum)
					{
						EEnum eEnum = (EEnum) clz;
						try
						{
							eEnum.setInstanceClass(loader.loadClass(eEnum.getInstanceClassName()));
						}
						catch (Throwable t)
						{
							throw new PMRuntimeException(t);
						}
					}
					else if (clz instanceof EClass)
					{
						EClass eClz = (EClass) clz;
						EList<EStructuralFeature> eFeatures = eClz.getEStructuralFeatures();

						for (EStructuralFeature feat: eFeatures)
						{
							if (feat instanceof EStructuralFeature.Internal)
							{
								// reset the setting delegate
								((EStructuralFeature.Internal) feat).setSettingDelegate(null);
							}
							if (feat.getEType() instanceof EEnum)
							{
								EEnum eEnum = (EEnum) feat.getEType();
								try
								{
									eEnum.setInstanceClass(loader.loadClass(eEnum.getInstanceClassName()));
								}
								catch (Throwable t)
								{
									throw new PMRuntimeException(t);
								}
							}
						}
					}
				}
			}
		}
	}

}
