/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 3:56:35 PM </copyright>
 */
package eu.cessar.ct.workspace.internal.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.DelegatingSubEList;
import eu.cessar.ct.workspace.sort.AbstractSortTarget;
import eu.cessar.ct.workspace.sort.IFeatureBasedSortTarget;
import eu.cessar.ct.workspace.sort.ISortCriterion;
import eu.cessar.ct.workspace.sort.ISortProvider;
import eu.cessar.ct.workspace.sort.SimpleAttributeSortCriterion;
import eu.cessar.ct.workspace.sort.SortUtils;
import gautosar.util.GAutosarPackage;

/**
 * @author uidt2045
 * 
 */
public class FeatureBasedSortTarget extends AbstractSortTarget implements IFeatureBasedSortTarget
{
	protected final EClass type;
	protected final EReference reference;
	protected boolean useFeatureName;
	protected ISortProvider sortProvider;

	public FeatureBasedSortTarget(ISortProvider sortProvider, EClass type, EReference reference)
	{
		this.sortProvider = sortProvider;
		this.type = type;
		this.reference = reference;
		updateLabelAndImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.ISortTarget#getObjectsToSort(org.eclipse.emf.ecore.EObject)
	 */
	public EList<EObject> getObjectsToSort(EObject parent)
	{
		Class<EObject> instanceClass = (Class<EObject>) type.getInstanceClass();
		DelegatingSubEList<EObject> result = new DelegatingSubEList<EObject>(instanceClass,
			getAllSortableObjects(parent), IModelChangeMonitor.INSTANCE.getChangeStampProvider(parent));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.sort.ISortTarget#getAllSortableObjects(org.eclipse.emf.ecore.EObject)
	 */
	public EList<EObject> getAllSortableObjects(EObject parent)
	{
		return (EList<EObject>) parent.eGet(reference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.internal.sort.AbstractSortTarget#doGetSortCriteria()
	 */
	@Override
	protected List<ISortCriterion> doGetSortCriteria()
	{
		EClass typesToSort = getType();
		Collection<EStructuralFeature> ignorableFeatures = SortUtils.getIgnorableFeatures(typesToSort);

		List<ISortCriterion> criteria = new ArrayList<ISortCriterion>();
		EList<EAttribute> attributes = typesToSort.getEAllAttributes();
		for (EAttribute attr: attributes)
		{
			boolean shallProcess = !ignorableFeatures.contains(attr);
			shallProcess &= !attr.isMany() && !attr.isDerived() && !attr.isTransient();
			if (shallProcess && MetaModelUtils.isModelClass(attr.getEContainingClass()))
			{
				ISortCriterion criterion = new SimpleAttributeSortCriterion(this, attr);
				criteria.add(criterion);
			}
		}
		EList<EReference> references = typesToSort.getEAllReferences();
		for (EReference ref: references)
		{
			boolean shallProcess = !ignorableFeatures.contains(ref);
			shallProcess &= !ref.isMany() && !ref.isDerived() && !ref.isTransient();

			if (shallProcess && MetaModelUtils.isModelClass(ref.getEContainingClass()))
			{
				EClass refType = SortUtils.getReferedEClass(GAutosarPackage.eINSTANCE, ref);
				if (refType != null)
				{
					for (EAttribute attr: refType.getEAllAttributes())
					{
						shallProcess = !ignorableFeatures.contains(attr);
						shallProcess &= !attr.isMany() && !attr.isDerived() && !attr.isTransient();
						if (shallProcess)
						{
							ISortCriterion criterion = new ChildrenAttributeSortCriterion(this, ref, attr);
							criteria.add(criterion);
						}
					}

				}
			}
		}
		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.IFeatureBasedSortTarget#getFeature()
	 */
	public EReference getFeature()
	{
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.IFeatureBasedSortTarget#getType()
	 */
	public EClass getType()
	{
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.IFeatureBasedSortTarget#isUsingFeatureName()
	 */
	public boolean isUsingFeatureName()
	{
		return useFeatureName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.autosartools.general.core.sort.IFeatureBasedSortTarget#setUsingFeatureName(boolean)
	 */
	public void setUsingFeatureName(boolean usage)
	{
		if (usage != useFeatureName)
		{
			useFeatureName = usage;
			updateLabelAndImage();
		}
	}

	/**
	 * 
	 */
	protected void updateLabelAndImage()
	{
		updateLabel();
		updateImage();
	}

	/**
	 * 
	 */
	protected void updateImage()
	{
		EObject obj = EcoreUtil.create(type);
		IItemLabelProvider provider = (IItemLabelProvider) sortProvider.getEditingDomain().getAdapterFactory().adapt(
			obj, IItemLabelProvider.class);
		if (provider != null)
		{
			setImage(provider.getImage(obj));
		}
	}

	/**
	 * 
	 */
	protected void updateLabel()
	{
		String label = ""; //$NON-NLS-1$
		if (useFeatureName && reference != null)
		{
			label = reference.getName() + "->"; //$NON-NLS-1$
		}
		label = label.concat(type.getName());
		setLabel(label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.sort.ISortTarget#getGroupName(org.eclipse.emf.ecore.EObject)
	 */
	public String getGroupName(EObject candidate)
	{
		return candidate == null ? "null" : candidate.eClass().getName(); //$NON-NLS-1$
	}

}
