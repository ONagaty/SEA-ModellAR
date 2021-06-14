/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 10, 2010 1:40:48 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.instanceref;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * @author uidl6870
 * 
 */
public final class InstanceRefsUtils
{
	/**
	 * Annotation source for the stereotype annotation.
	 */
	public static final String ANNOTATION_SOURCE_STEREOTYPE = "Stereotype"; //$NON-NLS-1$

	/**
	 * Annotation key for the stereotype type.
	 */
	public static final String STEREOTYPE_KEY_STEREOTYPE = "Stereotype"; //$NON-NLS-1$

	/**
	 * Annotation value for the stereotype type.
	 */
	public static final String STEREOTYPE_VALUE_STEREOTYPE_TYPE = "isOfType"; //$NON-NLS-1$

	private InstanceRefsUtils()
	{
		// hide
	}

	/**
	 * Checks if the given <code>prototype</code> has a reference of <code>isOfType</code> stereotype and returns the
	 * value of that reference. If no reference of this kind is found or if the reference is not set, <code>null</code>
	 * is returned
	 * 
	 * @param prototype
	 *        the prototype to examine
	 * @return the referenced type
	 */
	public static EObject getTypeOfPrototype(EObject prototype)
	{
		EObject type = null;
		if (prototype == null)
		{
			throw new IllegalArgumentException("Prototype is null"); //$NON-NLS-1$
		}

		EList<EReference> allReferences = prototype.eClass().getEAllReferences();
		for (EReference ref: allReferences)
		{
			EAnnotation annot = ref.getEAnnotation(ANNOTATION_SOURCE_STEREOTYPE);
			if (annot != null)
			{
				String stereo = annot.getDetails().get(STEREOTYPE_KEY_STEREOTYPE);
				if (STEREOTYPE_VALUE_STEREOTYPE_TYPE.equals(stereo))
				{
					type = (EObject) prototype.eGet(ref);
					break;
				}
			}
		}
		return type;
	}

}
