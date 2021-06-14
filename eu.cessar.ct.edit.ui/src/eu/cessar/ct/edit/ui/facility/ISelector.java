package eu.cessar.ct.edit.ui.facility;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;

public interface ISelector
{
	public boolean isSelected(IMetaModelService service, EObject object, EClass clz,
		EStructuralFeature feature);
}
