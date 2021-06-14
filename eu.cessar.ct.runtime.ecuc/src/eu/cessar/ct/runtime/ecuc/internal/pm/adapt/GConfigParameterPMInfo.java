/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 18, 2010 4:37:18 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.adapt;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.util.BigIntegerUtils;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pm.EMFPMUtils;

/**
 * Abstract class for handling parameters on normal mode.
 */
public abstract class GConfigParameterPMInfo extends GAbstractConfigParameterPMInfo
{

	private static final String INSTANCE_CLASS_NAME_IFILE = "org.eclipse.core.resources.IFile"; //$NON-NLS-1$
	private static final String E_DATA_TYPE_NAME_IFILE = "IFile"; //$NON-NLS-1$
	private EClassifier iFileClassifier;

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.pm.AbstractPMInfo#create(eu.cessar.ct.runtime.ecuc.IEcucPresentationModel)
	 */
	@Override
	protected void create(IEcucPresentationModel ecucPM)
	{
		super.create(ecucPM);

		EPackage parentPack = getParentPackage(ecucPM);
		// create IFile classifier

		iFileClassifier = createIFileClassifier(parentPack);
		createSetIFileOperation(ecucPM);
	}

	/**
	 * Creates an {@code EOperation} corresponding to file-based "set" APIs for writing in split configurations.
	 *
	 * @param ecucPM
	 *        the current {@linkplain IEcucPresentationModel}
	 */
	private void createSetIFileOperation(IEcucPresentationModel ecucPM)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(ecucPM.getProject()).getEcucMMService();
		BigInteger upper = ecucMMService.getUpperMultiplicity(element, BigInteger.ONE, true);
		int value = BigIntegerUtils.getRestrictedToInt(upper);
		// Consider only single parameters
		if (value == 1)
		{
			EOperation operation = ecoreFactory.createEOperation();
			String attribute = EMFPMUtils.genName(getParentClass().getEAllStructuralFeatures(), element.gGetShortName());
			operation.setName("set" + StringUtils.toTitleCase(attribute)); //$NON-NLS-1$
			operation.setUnique(false);
			operation.setLowerBound(ecucMMService.getLowerMultiplicity(element, BigInteger.ZERO, true).intValue());
			EParameter param = ecoreFactory.createEParameter();
			param.setEType(classifier);
			param.setName(attribute);
			operation.getEParameters().add(param);
			param = ecoreFactory.createEParameter();
			param.setEType(iFileClassifier);
			param.setName("file"); //$NON-NLS-1$
			operation.getEParameters().add(param);
			// operation.setEType(classifier);
			EMFPMUtils.setProxyAnnotation(operation, "TYPE", "OP_setWithFile"); //$NON-NLS-1$ //$NON-NLS-2$
			getParentClass().getEOperations().add(operation);
		}
	}

	/**
	 * Creates the classifiers corresponding to the class described in {@code EcoreClassifier}. The classifier is
	 * created in the root package of the supplied {@code ecucPM}.
	 *
	 * @param ecucPM
	 *        the current {@linkplain IEcucPresentationModel}
	 * @param parentPack
	 * @param ecoreFactory
	 *        factory to create the EClass
	 * @param ecoreClassifierDescriptor
	 *        contains basic informations of the classifier
	 * @return the existing or created classifiers list.
	 */
	public static EClassifier createIFileClassifier(EObject parentPack)
	{
		EPackage rootPack = (EPackage) getPMEcoreRoot(parentPack);

		// check if the IFile classifier is already created
		EClassifier eClassifier = rootPack.getEClassifier(E_DATA_TYPE_NAME_IFILE);
		if (null != eClassifier)
		{
			return eClassifier;
		}
		// otherwise create it
		EDataType iFileEDataType = ecoreFactory.createEDataType();
		iFileEDataType.setInstanceClassName(INSTANCE_CLASS_NAME_IFILE);
		iFileEDataType.setName(E_DATA_TYPE_NAME_IFILE);

		rootPack.getEClassifiers().add(iFileEDataType);

		return iFileEDataType;
	}

	/**
	 *
	 * @param ecucPM
	 * @param parentPack
	 *        the package where the classifier resides
	 * @return the ecore root
	 */
	private static EObject getPMEcoreRoot(EObject parentPack)
	{
		EObject root = parentPack;
		while (null != root.eContainer())
		{
			root = root.eContainer();

		}

		return root;
	}
}
