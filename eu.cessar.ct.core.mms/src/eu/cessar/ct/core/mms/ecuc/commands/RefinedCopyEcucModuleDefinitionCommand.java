/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Dec 16, 2010 10:50:26 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.commands.parameters.NewEcucModuleDefinitionCommandParameter;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * @author uidt2045
 * 
 */
public class RefinedCopyEcucModuleDefinitionCommand extends CopyEcucModuleDefinitionCommand
{

	private NewEcucModuleDefinitionCommandParameter cmdParameter;

	/**
	 * Creates an instance of {@link RefinedCopyEcucModuleDefinitionCommand} using the provided parameters.
	 * 
	 * @param parameter
	 *        an instance of {@link NewEcucModuleDefinitionCommandParameter}
	 * @param image
	 *        an image object to be displayed as command icon
	 */
	public RefinedCopyEcucModuleDefinitionCommand(NewEcucModuleDefinitionCommandParameter parameter, Object image)
	{
		super(parameter, image);
		cmdParameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.ecuc.commands.CopyEcucModuleDefinitionCommand#execute()
	 */
	@Override
	public void execute()
	{
		GARPackage owner = (GARPackage) cmdParameter.getOwner();
		// EList<GPackageableElement> childrenList = null;
		EList<GPackageableElement> childrenList = owner.gGetElements();

		GModuleDef gModuleDef = EcoreUtil.copy(cmdParameter.getDefinition());

		// set the new moduleDef as a refinement of the old one
		gModuleDef.gSetRefinedModuleDef(cmdParameter.getDefinition());

		// check to see if the refined MD points to the correct
		// destinations
		makeValidRefinedModuleDef(gModuleDef, cmdParameter.getDefinition());

		if (childrenList != null)
		{
			childrenList.add(gModuleDef);
			result.add(gModuleDef);
		}
	}

	/**
	 * Checks that the given refModuleDef is a valid refinement of the given standardDefinition and does the appropriate
	 * changes to make it correct. <br>
	 * It also sets new UIDs to all elements from the refined module definition
	 * 
	 * @param refModuleDef
	 * @param standardDefinition
	 * 
	 */
	private void makeValidRefinedModuleDef(GIdentifiable refModuleDef, GIdentifiable standardDefinition)
	{
		String identifier = "ECUC:"; //$NON-NLS-1$
		TreeIterator<EObject> eAllContents = refModuleDef.eAllContents();
		while (eAllContents.hasNext())
		{
			EObject eObject = eAllContents.next();
			if (eObject instanceof GIdentifiable)
			{
				GIdentifiable refObj = (GIdentifiable) eObject;
				EList<EReference> allReferences = refObj.eClass().getEAllReferences();
				for (EReference ref: allReferences)
				{
					if (!ref.isContainment() && !ref.isContainer() && !ref.isVolatile() && !ref.isTransient()
						&& !ref.isDerived())
					{
						tryToSetStdRefferedObjRef(standardDefinition, refObj, ref);
					}
				}
				refObj.gSetUuid(identifier + java.util.UUID.randomUUID().toString());
			}
		}
		refModuleDef.gSetUuid(identifier + java.util.UUID.randomUUID().toString());
	}

	private void tryToSetStdRefferedObjRef(GIdentifiable standardDefinition, GIdentifiable refObj, EReference ref)
	{
		if (ref.isMany())
		{
			Object eGet = refObj.eGet(ref, false);
			if (eGet instanceof EList)
			{
				for (int i = 0; i < ((EList<?>) eGet).size(); i++)
				{
					EObject refRefferedObj = (EObject) ((EList<?>) eGet).get(i);

					if (refRefferedObj != null && !(refRefferedObj instanceof GChoiceContainerDef))
					{
						String qualifiedName = ModelUtils.getAbsoluteQualifiedName(refRefferedObj);
						GARPackage stdPackageObj = (GARPackage) standardDefinition.eContainer();
						GIdentifiable stdRefferedObj = ModelUtils.getEObjectWithQualifiedName(standardDefinition,
							ModelUtils.getAbsoluteQualifiedName(stdPackageObj) + MetaModelUtils.QNAME_SEPARATOR_STR
								+ qualifiedName);

						if (stdRefferedObj != refRefferedObj && stdRefferedObj != null)
						{
							((EList) eGet).set(i, stdRefferedObj);
						}
					}
				}
			}
		}
		else
		{
			Object refRefferedObj = refObj.eGet(ref, false);

			if (refRefferedObj instanceof EObject && !(refRefferedObj instanceof GChoiceContainerDef))
			{
				String qualifiedName = ModelUtils.getAbsoluteQualifiedName((EObject) refRefferedObj);
				GARPackage stdPackageObj = (GARPackage) standardDefinition.eContainer();
				GIdentifiable stdRefferedObj = ModelUtils.getEObjectWithQualifiedName(standardDefinition,
					ModelUtils.getAbsoluteQualifiedName(stdPackageObj) + MetaModelUtils.QNAME_SEPARATOR_STR
						+ qualifiedName);

				if (stdRefferedObj != refRefferedObj && stdRefferedObj != null)
				{
					refObj.eSet(ref, stdRefferedObj);
				}
			}
		}
	}
}
