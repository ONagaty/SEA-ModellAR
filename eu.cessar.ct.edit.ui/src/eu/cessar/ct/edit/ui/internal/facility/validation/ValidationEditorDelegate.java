package eu.cessar.ct.edit.ui.internal.facility.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.AbstractEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorDelegate;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 * 
 * Validation editor delegate
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Sep 17 14:46:14 2014 %
 * 
 *         %version: 1 %
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#VALIDATION#1")
public class ValidationEditorDelegate implements IModelFragmentEditorDelegate
{
	private static class ValidationEditorProvider extends AbstractEditorProvider
	{

		private static final IModelFragmentEditorProvider eINSTANCE = new ValidationEditorProvider();

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#createEditor()
		 */
		public IModelFragmentEditor createEditor()
		{
			IModelFragmentEditor result = new ValidationEditor();
			result.setEditorProvider(this);
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getCategories()
		 */
		public String[] getCategories()
		{
			return new String[] {"validation"}; //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getEditedFeatures()
		 */
		public List<EStructuralFeature> getEditedFeatures()
		{
			return Collections.emptyList();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getId()
		 */
		public String getId()
		{
			return "GARObject.validation"; //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getPriority()
		 */
		public int getPriority()
		{
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#isMetaEditor()
		 */
		public boolean isMetaEditor()
		{
			return true;
		}

	}

	public List<IModelFragmentEditorProvider> getEditors(EObject object, EClass clz)
	{
		if (!(object instanceof GAUTOSAR) && !(object instanceof GARPackage))
		{
			return Collections.singletonList(ValidationEditorProvider.eINSTANCE);
		}
		else
		{
			return Collections.emptyList();
		}
	}
}
