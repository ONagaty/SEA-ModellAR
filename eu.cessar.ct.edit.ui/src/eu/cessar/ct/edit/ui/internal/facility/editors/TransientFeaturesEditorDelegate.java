package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.DelegatedEditorProvider;
import eu.cessar.ct.edit.ui.facility.EEditorCategory;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;

public class TransientFeaturesEditorDelegate implements IModelFragmentEditorDelegate
{

	private boolean running = false;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate#getEditors(org.eclipse.emf.ecore.EObject)
	 */
	public List<IModelFragmentEditorProvider> getEditors(EObject object, EClass clz)
	{
		if (!running)
		{
			try
			{
				running = true;
				EList<EStructuralFeature> features = clz.getEAllStructuralFeatures();
				List<EStructuralFeature> transientFeatures = new ArrayList<EStructuralFeature>();
				for (EStructuralFeature feature: features)
				{
					if (feature.isTransient())
					{
						transientFeatures.add(feature);
					}
				}

				List<IModelFragmentEditorProvider> providers;
				if (object != null)
				{
					providers = IEditingFacility.eINSTANCE.getSimpleEditorsProviders(object,
						object.eClass(), transientFeatures, IEditingFacility.EDITOR_FEATURE
							| IEditingFacility.EDITOR_CLASSIFIER);
				}
				else
				{
					providers = IEditingFacility.eINSTANCE.getSimpleEditorProviders(clz,
						transientFeatures, IEditingFacility.EDITOR_FEATURE
							| IEditingFacility.EDITOR_CLASSIFIER);
				}
				List<IModelFragmentEditorProvider> result = new ArrayList<IModelFragmentEditorProvider>();
				for (IModelFragmentEditorProvider provider: providers)
				{
					result.add(new DelegatedEditorProvider(provider,
						new String[] {EEditorCategory.HIDDEN.getName()}));
				}
				return result;
			}
			finally
			{
				running = false;
			}
		}
		else
		{
			return Collections.emptyList();
		}
	}
}
