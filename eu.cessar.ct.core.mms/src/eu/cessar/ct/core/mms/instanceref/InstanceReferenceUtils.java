/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 10, 2010 1:40:48 PM </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sphinx.emf.model.IModelDescriptor;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.instanceref.IEcucInstanceReferenceHelper;
import eu.cessar.ct.core.mms.internal.instanceref.impl.EcucInstanceReferenceHelper;
import eu.cessar.ct.core.mms.internal.instanceref.impl.SystemInstanceReferenceHelper;
import eu.cessar.req.Requirement;

/**
 * Utilities around instance references
 * 
 * 
 * @author uidl6870
 */
@Requirement(
	reqID = "REQ_GST#1")
public final class InstanceReferenceUtils
{
	/**
	 * Annotation value for the stereotype type.
	 */
	public static final String STEREOTYPE_VALUE_STEREOTYPE_TYPE = "isOfType"; //$NON-NLS-1$

	/** Base feature name **/
	public static final String BASE_FEAT_INSTANCEREF = "base"; //$NON-NLS-1$

	// can not instantiate
	private InstanceReferenceUtils()
	{

	}

	/**
	 * Get the proper System instance reference helper.
	 * 
	 * @param modelDescriptor
	 *        model descriptor for which the helper is asked
	 * @return the proper helper to be used for obtaining the valid candidates for setting System instance references
	 */
	public static ISystemInstanceReferenceHelper getInstanceReferenceHelper(IModelDescriptor modelDescriptor)
	{
		return new SystemInstanceReferenceHelper(modelDescriptor);
	}

	/**
	 * Get the proper ECUC instance reference helper
	 * 
	 * @param modelDescriptor
	 * @return the proper helper to be used for obtaining the valid candidates for setting ECUC instance references
	 */
	public static IEcucInstanceReferenceHelper getEcucInstanceReferenceHelper(IModelDescriptor modelDescriptor)
	{
		return new EcucInstanceReferenceHelper(modelDescriptor);
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
			EAnnotation annot = ref.getEAnnotation(MetaModelUtils.ANN_STEREOTYPE);
			if (annot != null)
			{
				String stereo = annot.getDetails().get(MetaModelUtils.ANN_STEREOTYPE);
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
