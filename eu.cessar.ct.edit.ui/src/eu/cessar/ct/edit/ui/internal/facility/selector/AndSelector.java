package eu.cessar.ct.edit.ui.internal.facility.selector;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.edit.ui.facility.ISelector;

/**
 * Class that make AND bettwen Selectors
 * 
 * @author uid94858
 * 
 */
public class AndSelector extends CollectionBasedSelector
{

	/**
	 * Return false if one of the Selectors method isSelected()} is false
	 * otherwise true
	 * 
	 * @param EClass
	 *        {@link EClass}
	 * @param EStructuralFeature
	 *        feature {@link EStructuralFeature}
	 */
	public boolean isSelected(IMetaModelService mmService, EObject object, EClass clz,
		EStructuralFeature feature)
	{
		if (clz == null && feature == null)
		{
			return false;
		}

		List<ISelector> selectors = getSelectors();
		if (selectors.size() == 0)
		{
			return false;
		}

		boolean isSelected = true;
		for (int i = 0; i < selectors.size(); i++)
		{
			if (!selectors.get(i).isSelected(mmService, object, clz, feature))
			{
				isSelected = false;
				break;
			}
		}
		return isSelected;
	}
}
