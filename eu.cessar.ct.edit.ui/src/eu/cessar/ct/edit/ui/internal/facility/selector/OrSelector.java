package eu.cessar.ct.edit.ui.internal.facility.selector;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;

public class OrSelector extends CollectionBasedSelector
{

	/**
	 * Return the true if one Selection method isSelected() of Selectors is true
	 * false otherwise
	 * 
	 * @param EClass
	 *        {@link EClass}
	 * @param EStructuralFeature
	 *        feature {@link EStructuralFeature}
	 */
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature)
	{
		List<ISelector> selectors = getSelectors();
		if (selectors.size() == 0)
		{
			return false;
		}
		for (int i = 0; i < selectors.size(); i++)
		{
			if (selectors.get(i).isSelected(mmService, object, clz, feature))
			{
				return true;
			}
		}
		return false;
	}
}
