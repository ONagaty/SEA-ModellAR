/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 29, 2011 1:55:58 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.IMetaModelService.IgnorableFeaturesAplication;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.EEditorCategory;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.composition.IEditorComposition;
import eu.cessar.ct.edit.ui.facility.composition.SystemCategory;
import gautosar.gecucdescription.GecucdescriptionPackage;

/**
 * @author uidt2045
 * 
 */
public class SYSTEMCompositionProvider extends AbstractCompositionProvider implements
	IEditorCompositionProvider
{

	public static List<EClass> ingnorableEClass;

	public static List<EClass> getIgnorableReferences(IMetaModelService service)
	{
		ingnorableEClass = new ArrayList<EClass>();
		Collection<EStructuralFeature> ignorableFeatures = service.getIgnorableFeatures(IgnorableFeaturesAplication.SysUI);
		for (EStructuralFeature feature: ignorableFeatures)
		{
			EReference ref = (EReference) feature;

			ingnorableEClass.add(ref.getEReferenceType());
		}

		Collection<EStructuralFeature> ignorableFeaturesForTableUI = service.getIgnorableFeatures(IgnorableFeaturesAplication.TableUI);
		for (EStructuralFeature feature: ignorableFeaturesForTableUI)
		{
			if (feature instanceof EReference)
			{
				EReference ref = (EReference) feature;
				ingnorableEClass.add(ref.getEReferenceType());
			}

		}

		return ingnorableEClass;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#getEditorCompositions(org.eclipse.emf.ecore.EObject)
	 */
	public List<SystemComposition> getEditorCompositions(EObject object)
	{
		return getEditorCompositions(object.eClass());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorCompositionProvider#getEditorCompositions(org.eclipse.emf.ecore.EClass)
	 */
	public List<SystemComposition> getEditorCompositions(EClass clz)
	{
		List<SystemComposition> result = new ArrayList<SystemComposition>();

		IMetaModelService service = MMSRegistry.INSTANCE.getMMService(clz);

		EClass gContainerEclz = GecucdescriptionPackage.eINSTANCE.getGContainer();
		EClass gModuleConfigurationEclz = GecucdescriptionPackage.eINSTANCE.getGModuleConfiguration();
		boolean ecucInput = gContainerEclz.isSuperTypeOf(clz)
			|| gModuleConfigurationEclz.isSuperTypeOf(clz);

		if (ecucInput)
		{
			Collection<EReference> acceptedFeatures = service.getAcceptedFeaturesForCompositionProvider();

			for (EReference ref: acceptedFeatures)
			{
				collectSYSCompositions(result, service, ref, new ArrayList<EClass>());
			}

		}
		// system input
		else
		{
			EList<EReference> refernces = clz.getEAllReferences();

			List<EClass> ignorableTypes = getIgnorableReferences(service);

			for (EReference ref: refernces)
			{
				if (!ref.isContainment() || ref.isTransient())
				{
					continue;
				}

				collectSYSCompositions(result, service, ref, ignorableTypes);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param compositions
	 *        list where to collect the compositions
	 * @param service
	 * @param reference
	 * @param ignorableTypes
	 */
	private void collectSYSCompositions(List<SystemComposition> compositions,
		IMetaModelService service, EReference reference, List<EClass> ignorableTypes)
	{

		EClass referenceType = reference.getEReferenceType();
		SystemComposition sysCmp;
		if (!referenceType.isAbstract() && !referenceType.isInterface())
		{
			sysCmp = instantiateSYSComposition(referenceType, ignorableTypes, reference);
			if (sysCmp != null)
			{
				compositions.add(sysCmp);
			}
		}
		else
		{
			Collection<EClass> allSubClasses = service.getEAllSubClasses(referenceType, true);
			Iterator<EClass> iterator = allSubClasses.iterator();
			while (iterator.hasNext())
			{
				sysCmp = instantiateSYSComposition(iterator.next(), ignorableTypes, reference);
				if (sysCmp != null)
				{
					compositions.add(sysCmp);
				}
			}
		}
	}

	/**
	 * The given referenceType must not be an abstract EClass
	 * 
	 * @param referenceType
	 */
	private SystemComposition instantiateSYSComposition(EClass referenceType,
		List<EClass> ignorableReferences, EReference ref)
	{
		if (ignorableReferences.contains(referenceType))
		{
			return null;
		}

		SystemComposition sysCmp = new SystemComposition(new SystemCategory(referenceType, ref),
			this);
		return sysCmp;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.internal.facility.composition.IEditorCompositionProvider#doComputeEditors(eu.cessar.ct.edit.ui.facility.composition.IEditorComposition)
	 */
	public void doComputeEditors(IEditorComposition<?> composition)
	{
		if (composition != null)
		{
			SystemComposition systemComposition = (SystemComposition) composition;
			EClass input = ((SystemCategory) composition.getCategory()).getInput();

			List<IModelFragmentEditorProvider> editorsProviders = IEditingFacility.eINSTANCE.getSimpleEditorsProviders(input);
			for (IModelFragmentEditorProvider prov: editorsProviders)
			{
				if (isAnnotationsProvider(prov))
				{
					continue;
				}
				String[] categories = prov.getCategories();
				boolean isMain = Arrays.asList(categories).contains(EEditorCategory.MAIN.getName());
				boolean isConfigClass = Arrays.asList(categories).contains(
					EEditorCategory.CONFIGCLASS.getName());
				if (isMain || isConfigClass)
				{
					systemComposition.addEditorProvider(prov);
				}
			}
		}

	}

	/**
	 * @param prov
	 * @return
	 */
	private boolean isAnnotationsProvider(IModelFragmentEditorProvider prov)
	{
		for (EStructuralFeature feature: prov.getEditedFeatures())
		{
			if ("annotations".equals(feature.getName())) //$NON-NLS-1$
			{
				return true;
			}
		}
		return false;
	}
}
